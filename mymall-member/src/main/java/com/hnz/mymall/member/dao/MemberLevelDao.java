package com.hnz.mymall.member.dao;

import com.hnz.mymall.member.entity.MemberLevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级
 *
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-12 17:07:45
 */
@Mapper
public interface MemberLevelDao extends BaseMapper<MemberLevelEntity> {
    MemberLevelEntity getDefaultLevel();
}
