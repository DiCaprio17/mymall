package com.hnz.mymall.auth.feign;

import com.hnz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author:
 * @createTime: 2020-06-27 10:10
 **/

@FeignClient("mymall-third-party")
public interface ThirdPartFeignService {

    @GetMapping(value = "/sms/sendCode")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);

}
