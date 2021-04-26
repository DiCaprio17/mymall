package com.hnz.mymall.auth.feign;

import com.hnz.common.utils.R;
import com.hnz.mymall.auth.vo.SocialUser;
import com.hnz.mymall.auth.vo.UserLoginVo;
import com.hnz.mymall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author:
 * @createTime: 2020-06-27 17:10
 **/

@FeignClient("mymall-member")
public interface MemberFeignService {

    @PostMapping(value = "/member/member/register")
    R register(@RequestBody UserRegisterVo vo);


    @PostMapping(value = "/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping(value = "/member/member/oauth2/login")
    R oauthLogin(@RequestBody SocialUser socialUser) throws Exception;

    @PostMapping(value = "/member/member/weixin/login")
    R weixinLogin(@RequestParam("accessTokenInfo") String accessTokenInfo);
}
