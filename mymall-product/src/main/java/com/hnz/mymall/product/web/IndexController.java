package com.hnz.mymall.product.web;

import com.hnz.mymall.product.entity.CategoryEntity;
import com.hnz.mymall.product.service.CategoryService;
import com.hnz.mymall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient redisson;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping(value = {"/", "/index.html"})
    public String indexPage(Model model) {
        //1. 查询出所有的一级分类
        List<CategoryEntity> categoryEntityList = categoryService.getLevel1Categories();
        model.addAttribute("categories", categoryEntityList);
        return "index";
    }


    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatelogJson() {

        Map<String, List<Catelog2Vo>> map = categoryService.getCatelogJson();
        return map;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        //1.获取一把锁，只要名字一样，就是同一把锁
        RLock lock = redisson.getLock("my-lock");
        //2.加锁和解锁
        try {
            // lock.lock();
            lock.lock(10, TimeUnit.SECONDS);
            System.out.println("加锁成功，执行业务方法..." + Thread.currentThread().getId());
            Thread.sleep(30000);
        } catch (Exception e) {

        } finally {
            lock.unlock();
            System.out.println("释放锁..." + Thread.currentThread().getId());
        }
        return "hello";
    }

    //保证一定能读到最新数据，修改期间，写锁是一个排它锁（互斥），读锁是一个共享锁
    //写锁没释放读就必须等待
    //读写锁测试，改数据加写锁，读数据加读锁
    @GetMapping("/write")
    @ResponseBody
    public String writeValue() {
        RReadWriteLock writeLock = redisson.getReadWriteLock("rw-loc");
        String uuid = null;
        RLock lock = writeLock.writeLock();
        lock.lock();
        try {
            log.info("写锁加锁成功");
            uuid = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set("writeValue", uuid);
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            log.info("写锁释放");

        }
        return uuid;
    }

    @GetMapping("/read")
    @ResponseBody
    public String readValue() {
        RReadWriteLock readLock = redisson.getReadWriteLock("rw-loc");
        String uuid = null;
        RLock lock = readLock.readLock();
        lock.lock();
        try {
            log.info("读锁加锁成功");
            uuid = redisTemplate.opsForValue().get("writeValue");
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            log.info("读锁释放");
        }
        return uuid;
    }

    //闭锁

    /**
     * 放假，锁门
     * 1班没人，2班没人..全部没人，锁门
     */
    @GetMapping("/lockDoor")
    @ResponseBody
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();//等待闭锁都完成

        return "放假了";
    }

    //班级id
    @GetMapping("/gogogo/{id}")
    @ResponseBody
    public String gogogo(@PathVariable("id") Long id) {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.countDown();//技术减一

        return id + "班的人都走了";
    }

    /**
     * 信号量测试
     * 车库停车
     * 3车位
     * 信号量也可以做分布式限流
     */
    @GetMapping("/park")
    @ResponseBody
    public String park() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        park.acquire();//获取一个信号，获取一个值，阻塞（获取失败则会一直等），占一个车位
        // //分布式限流
        // boolean b = park.tryAcquire();//试着获取，返回布尔，非阻塞
        // if (b) {
        //     //执行业务
        // } else {
        //     return "error";
        // }

        return "ok";
    }

    @GetMapping("/go")
    @ResponseBody
    public String go() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        park.release();//释放一个信号，释放一个车位

        return "ok";
    }
}
