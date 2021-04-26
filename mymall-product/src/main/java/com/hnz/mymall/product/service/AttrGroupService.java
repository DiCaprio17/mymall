package com.hnz.mymall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hnz.common.utils.PageUtils;
import com.hnz.mymall.product.entity.AttrGroupEntity;
import com.hnz.mymall.product.vo.AttrGroupWithAttrsVo;
import com.hnz.mymall.product.vo.SpuItemAttrGroupVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-11 23:12:04
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);

    List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId);
}

