package com.hnz.mymall.member.dao;

import com.hnz.mymall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-12 17:07:45
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
