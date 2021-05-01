package com.hnz.mymall.order.to;

import com.hnz.mymall.order.entity.OrderEntity;
import com.hnz.mymall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description:
 *
 * @createTime: 2020-07-04 23:04
 **/

@Data
public class OrderCreateTo {

    private OrderEntity order;

    private List<OrderItemEntity> orderItems;

    /** 订单计算的应付价格 **/
    private BigDecimal payPrice;

    /** 运费 **/
    private BigDecimal fare;

}
