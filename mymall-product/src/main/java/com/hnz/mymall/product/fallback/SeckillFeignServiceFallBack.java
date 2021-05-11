package com.hnz.mymall.product.fallback;

import com.hnz.common.exception.BizCodeEnume;
import com.hnz.common.utils.R;
import com.hnz.mymall.product.feign.SeckillFeignService;
import org.springframework.stereotype.Component;

/**
 * @Description:
 *
 * @createTime: 2020-07-13 14:45
 **/

@Component
public class SeckillFeignServiceFallBack implements SeckillFeignService {
    @Override
    public R getSkuSeckilInfo(Long skuId) {
        return R.error(BizCodeEnume.TO_MANY_REQUEST.getCode(),BizCodeEnume.TO_MANY_REQUEST.getMsg());
    }
}
