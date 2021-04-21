package com.hnz.mymall.coupon.dao;

import com.hnz.mymall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-12 16:56:14
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
