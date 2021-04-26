package com.hnz.mymall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hnz.common.utils.PageUtils;
import com.hnz.mymall.product.entity.SkuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * sku图片
 *
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-11 23:12:04
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuImagesEntity> getImagesBySkuId(Long skuId);
}

