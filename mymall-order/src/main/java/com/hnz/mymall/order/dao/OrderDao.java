package com.hnz.mymall.order.dao;

import com.hnz.mymall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-12 17:52:01
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
