package com.hnz.mymall.product.dao;

import com.hnz.mymall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-11 23:12:04
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
