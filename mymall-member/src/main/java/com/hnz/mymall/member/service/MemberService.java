package com.hnz.mymall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hnz.common.utils.PageUtils;
import com.hnz.mymall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-12 17:07:45
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

