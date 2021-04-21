package com.hnz.mymall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//无参构造器（lombk）
@NoArgsConstructor
//全参构造器（lombk）
@AllArgsConstructor
@Data
/**
 * 二级分类VO
 */
public class Catelog2Vo {

    private String catalog1Id;//一级父分类id
    private List<Catalog3List> catalog3List;//三级子分类
    private String id;
    private String name;
}