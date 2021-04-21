package com.hnz.mymall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hnz.mymall.product.service.CategoryBrandRelationService;
import com.hnz.mymall.product.vo.Catalog3List;
import com.hnz.mymall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnz.common.utils.PageUtils;
import com.hnz.common.utils.Query;

import com.hnz.mymall.product.dao.CategoryDao;
import com.hnz.mymall.product.entity.CategoryEntity;
import com.hnz.mymall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1.查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //2.组装成父子结构
        //2.1）、找到所有的一级分类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 检查当前删除的菜单是否被别的地方引用
        //
        baseMapper.deleteBatchIds(asList);
    }

    //[2,25,225]
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);

        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * （1）更新分类数据
     * （2）采用失效模式更新缓存
     * （3）可以有两种方式来实现同时更新缓存到redis的一级或三级分类数据
     * key = "'level1Categorys'"要加单引号：SpEL表达式；level1Categorys必须与其getLevel1Categories()方法使用的缓存名一致，否则找不到
     * 级联更新所有关联的数据
     * @CacheEvict:失效模式
     * @param category
     */
    @CacheEvict(value = {"category"}, allEntries = true)
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());


    }

    @Cacheable(value = {"category"}, key = "#root.methodName")
    @Override
    public List<CategoryEntity> getLevel1Categories() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    /**
     * （1）根据一级分类，找到对应的二级分类
     * （2）将得到的二级分类，封装到Catelog2Vo中
     * （3）根据二级分类，得到对应的三级分类
     * （3）将三级分类封装到Catalog3List
     * (4) 将执行结果放入到缓存中
     *
     * @return
     */

    @Cacheable(value = {"category"}, key = "#root.methodName")
    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        log.info("查询数据库");
        //一次性查询出所有的分类数据，减少对于数据库的访问次数，后面的数据操作并不是到数据库中查询，而是直接从这个集合中获取，
        // 由于分类信息的数据量并不大，所以这种方式是可行的
        List<CategoryEntity> categoryEntities = this.baseMapper.selectList(null);

        //1.查出所有一级分类
        List<CategoryEntity> level1Categories = getParentCid(categoryEntities, 0L);

        Map<String, List<Catelog2Vo>> parent_cid = level1Categories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), level1 -> {
            //2. 根据一级分类的id查找到对应的二级分类
            List<CategoryEntity> level2Categories = getParentCid(categoryEntities, level1.getCatId());

            //3. 根据二级分类，查找到对应的三级分类
            List<Catelog2Vo> catelog2Vos = null;

            if (null != level2Categories || level2Categories.size() > 0) {
                catelog2Vos = level2Categories.stream().map(level2 -> {
                    //得到对应的三级分类
                    List<CategoryEntity> level3Categories = getParentCid(categoryEntities, level2.getCatId());
                    //封装到Catalog3List
                    List<Catalog3List> catalog3Lists = null;
                    if (null != level3Categories) {
                        catalog3Lists = level3Categories.stream().map(level3 -> {
                            Catalog3List catalog3List = new Catalog3List(level2.getCatId().toString(), level3.getCatId().toString(), level3.getName());
                            return catalog3List;
                        }).collect(Collectors.toList());
                    }
                    return new Catelog2Vo(level1.getCatId().toString(), catalog3Lists, level2.getCatId().toString(), level2.getName());
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));
        return parent_cid;
    }

    @Deprecated
    public Map<String, List<Catelog2Vo>> getCatelogJsons() {
        //先从缓存中获取分类数据，如果没有再从数据库中查询，并且分类数据是以JSON的形式存放到Reids中的
        String catelogJSON = redisTemplate.opsForValue().get("catelogJSON");

        //1. 空结果缓存：解决缓存穿透
        //2. 设置过期时间(加随机值)：解决缓存雪崩
        //3. 加锁：解决缓存击穿（使用分布式锁）
        if (StringUtils.isEmpty(catelogJSON)) {
            //缓存中没有查数据库
            Map<String, List<Catelog2Vo>> catelogJsonFromDb = getCatelogJsonFromDbWithRedissonLock();

            return catelogJsonFromDb;

        }

        Map<String, List<Catelog2Vo>> stringListMap = JSON.parseObject(catelogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        log.warn("缓存命中");

        return stringListMap;
    }

    /**
     * 使用Redisson分布式锁来实现多个服务共享同一缓存中的数据
     * 缓存数据一致性
     * 1）、双写模式
     * 2）、失效模式
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithRedissonLock() {

        //1.锁的名字。锁的粒度越细越快
        //锁的粒度：具体缓存的是某个数据，11号商品，product-11-lock
        RLock lock = redissonClient.getLock("CatelogJson-lock");
        //该方法会阻塞其他线程向下执行，只有释放锁之后才会接着向下执行
        lock.lock();
        Map<String, List<Catelog2Vo>> catelogJsonFromDb;
        try {
            //从数据库中查询分类数据
            catelogJsonFromDb = getCatelogJsonFromDb();
        } finally {
            lock.unlock();
        }

        return catelogJsonFromDb;

    }


    /**
     * 使用分布式锁来实现多个服务共享同一缓存中的数据
     * （1）设置读写锁，失败则表明其他线程先于该线程获取到了锁，则执行自旋，成功则表明获取到了锁
     * （2）获取锁成功，查询数据库，获取分类数据
     * （3）释放锁
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithRedisLock() {
        String uuid = UUID.randomUUID().toString();
        //设置redis分布式锁，成功则返回true，否则返回false，该操作是原子性的
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lock == null || !lock) {
            //获取锁失败，重试：自旋
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            log.warn("获取锁失败，重新获取...");
            return getCatelogJsonFromDbWithRedisLock();
        } else {
            //获取锁成功
            log.warn("获取锁成功:)");
            Map<String, List<Catelog2Vo>> catelogJsonFromDb;
            try {
                //从数据库中查询分类数据
                catelogJsonFromDb = getCatelogJsonFromDb();
            } finally {
                //确保一定会释放锁
                //获取值对比+对比成功删除=原子操作，使用Lua脚本解锁，来自redis官网文档
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                redisTemplate.execute(new DefaultRedisScript(script, Long.class), Arrays.asList("lock"), uuid);
                log.warn("释放锁成功:)");
            }
            return catelogJsonFromDb;
        }

    }

    /**
     * 逻辑是
     * （0）首先查询Redis缓存中是否有分类数据信息，有则返回，否则继续执行
     * （1）根据一级分类，找到对应的二级分类
     * （2）将得到的二级分类，封装到Catelog2Vo中
     * （3）根据二级分类，得到对应的三级分类
     * （3）将三级分类封装到Catalog3List
     * （4）将查询结果放入到Redis中
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDb() {

        //先从redis缓存中查询，如果有数据，则返回查询结果
        String catelogJSON = redisTemplate.opsForValue().get("catelogJSON");
        if (!StringUtils.isEmpty(catelogJSON)) {
            log.warn("从缓存中获取数据");
            return JSON.parseObject(catelogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
        }
        log.warn("查询数据库");
        //一次性查询出所有的分类数据，减少对于数据库的访问次数，后面的数据操作并不是到数据库中查询，而是直接从这个集合中获取，
        // 由于分类信息的数据量并不大，所以这种方式是可行的
        List<CategoryEntity> categoryEntities = this.baseMapper.selectList(null);

        //1.查出所有一级分类
        List<CategoryEntity> level1Categories = getParentCid(categoryEntities, 0L);

        Map<String, List<Catelog2Vo>> parent_cid = level1Categories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), level1 -> {
            //2. 根据一级分类的id查找到对应的二级分类
            List<CategoryEntity> level2Categories = getParentCid(categoryEntities, level1.getCatId());

            //3. 根据二级分类，查找到对应的三级分类
            List<Catelog2Vo> catelog2Vos = null;

            if (null != level2Categories || level2Categories.size() > 0) {
                catelog2Vos = level2Categories.stream().map(level2 -> {
                    //得到对应的三级分类
                    List<CategoryEntity> level3Categories = getParentCid(categoryEntities, level2.getCatId());
                    //封装到Catalog3List
                    List<Catalog3List> catalog3Lists = null;
                    if (null != level3Categories) {
                        catalog3Lists = level3Categories.stream().map(level3 -> {
                            Catalog3List catalog3List = new Catalog3List(level2.getCatId().toString(), level3.getCatId().toString(), level3.getName());
                            return catalog3List;
                        }).collect(Collectors.toList());
                    }
                    return new Catelog2Vo(level1.getCatId().toString(), catalog3Lists, level2.getCatId().toString(), level2.getName());
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));

        //将查询结果放入到redis中
        redisTemplate.opsForValue().set("catelogJson", JSON.toJSONString(parent_cid), 1, TimeUnit.DAYS);
        return parent_cid;
    }

    //225,25,2
    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1.手机当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map(categoryEntity -> {
            //1、找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //2、菜单的排序，sort越大排在越后面
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }

    /**
     * 在selectList中找到parentId等于传入的parentCid的所有分类数据
     *
     * @param selectList
     * @param parentCid
     * @return
     */
    private List<CategoryEntity> getParentCid(List<CategoryEntity> selectList, Long parentCid) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid().equals(parentCid)).collect(Collectors.toList());
        return collect;
    }
}