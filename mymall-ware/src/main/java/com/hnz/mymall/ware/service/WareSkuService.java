package com.hnz.mymall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hnz.common.to.OrderTo;
import com.hnz.common.to.mq.StockLockedTo;
import com.hnz.common.utils.PageUtils;
import com.hnz.mymall.ware.entity.WareSkuEntity;
import com.hnz.mymall.ware.vo.SkuHasStockVo;
import com.hnz.mymall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-12 17:58:32
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    /**
     * 锁定库存
     * @param vo
     * @return
     */
    boolean orderLockStock(WareSkuLockVo vo);


    /**
     * 解锁库存
     * @param to
     */
    void unlockStock(StockLockedTo to);

    /**
     * 解锁订单
     * @param orderTo
     */
    void unlockStock(OrderTo orderTo);
}

