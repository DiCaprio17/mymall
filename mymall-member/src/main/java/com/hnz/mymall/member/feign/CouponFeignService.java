package com.hnz.mymall.member.feign;

import com.hnz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huangnuozhong
 * @create 2020-04-12-21:37
 */

/**
 * 这是一个声明式的远程调用
 */
@FeignClient("mymall-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    R membercoupons();
}
