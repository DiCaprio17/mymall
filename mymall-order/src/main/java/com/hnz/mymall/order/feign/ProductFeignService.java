package com.hnz.mymall.order.feign;

import com.hnz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description:
 *
 * @createTime: 2020-07-05 09:29
 **/

@FeignClient("mymall-product")
public interface ProductFeignService {

    /**
     * 根据skuId查询spu的信息
     * @param skuId
     * @return
     */
    @GetMapping(value = "/product/spuinfo/skuId/{skuId}")
    R getSpuInfoBySkuId(@PathVariable("skuId") Long skuId);

}
