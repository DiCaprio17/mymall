package com.hnz.mymall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hnz.common.to.SkuReductionTo;
import com.hnz.common.utils.PageUtils;
import com.hnz.mymall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-12 16:56:14
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo reductionTo);
}

