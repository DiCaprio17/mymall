package com.hnz.mymall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hnz.common.utils.PageUtils;
import com.hnz.mymall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-12 17:52:01
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

