package com.hnz.mymall.search.feign;

import com.hnz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mymall-product")
public interface ProductFeignService {
    /**
     * 信息
     * 功能：查询属性详情
     * API：https://easydoc.xyz/doc/75716633/ZUqEdvA4/7C3tMIuF
     */
    @GetMapping("/product/attr/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId);

}
