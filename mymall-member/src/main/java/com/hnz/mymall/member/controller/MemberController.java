package com.hnz.mymall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.hnz.common.exception.BizCodeEnume;
import com.hnz.mymall.member.exception.PhoneException;
import com.hnz.mymall.member.exception.UsernameException;
import com.hnz.mymall.member.feign.CouponFeignService;
import com.hnz.mymall.member.vo.MemberUserLoginVo;
import com.hnz.mymall.member.vo.MemberUserRegisterVo;
import com.hnz.mymall.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hnz.mymall.member.entity.MemberEntity;
import com.hnz.mymall.member.service.MemberService;
import com.hnz.common.utils.PageUtils;
import com.hnz.common.utils.R;



/**
 * 会员
 *
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-12 17:07:45
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    CouponFeignService couponFeignService;

    @PostMapping(value = "/register")
    public R register(@RequestBody MemberUserRegisterVo vo) {

        try {
            memberService.register(vo);
        } catch (PhoneException e) {
            return R.error(BizCodeEnume.PHONE_EXIST_EXCEPTION.getCode(),BizCodeEnume.PHONE_EXIST_EXCEPTION.getMsg());
        } catch (UsernameException e) {
            return R.error(BizCodeEnume.USER_EXIST_EXCEPTION.getCode(),BizCodeEnume.USER_EXIST_EXCEPTION.getMsg());
        }

        return R.ok();
    }


    @PostMapping(value = "/login")
    public R login(@RequestBody MemberUserLoginVo vo) {

        MemberEntity memberEntity = memberService.login(vo);

        if (memberEntity != null) {
            return R.ok().setData(memberEntity);
        } else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION.getCode(),BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION.getMsg());
        }
    }


    @PostMapping(value = "/oauth2/login")
    public R oauthLogin(@RequestBody SocialUser socialUser) throws Exception {

        MemberEntity memberEntity = memberService.login(socialUser);

        if (memberEntity != null) {
            return R.ok().setData(memberEntity);
        } else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION.getCode(),BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION.getMsg());
        }
    }

    @PostMapping(value = "/weixin/login")
    public R weixinLogin(@RequestParam("accessTokenInfo") String accessTokenInfo) {

        MemberEntity memberEntity = memberService.login(accessTokenInfo);
        if (memberEntity != null) {
            return R.ok().setData(memberEntity);
        } else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION.getCode(),BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION.getMsg());
        }
    }

    @RequestMapping("/coupons")
    public R test(){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");

        R membercoupons = couponFeignService.membercoupons();
        return R.ok().put("member",memberEntity).put("coupons",membercoupons.get("coupons"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
