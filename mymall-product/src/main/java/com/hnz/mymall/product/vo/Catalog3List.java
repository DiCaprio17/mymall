package com.hnz.mymall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
/**
 * 三级分类VO
 */
public class Catalog3List {

    private String catalog2Id;//父分类，二级分类id
    private String id;
    private String name;
}