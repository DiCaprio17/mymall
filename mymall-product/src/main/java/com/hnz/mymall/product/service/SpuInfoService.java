package com.hnz.mymall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hnz.common.utils.PageUtils;
import com.hnz.mymall.product.entity.SpuInfoEntity;
import com.hnz.mymall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-11 23:12:04
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfoEntity infoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void up(Long spuId);

    SpuInfoEntity getSpuInfoBySkuId(Long skuId);
}

