package com.hnz.mymall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 封装页面可能传递过来的查询条件
 */
@Data
public class SearchParam {

    //检索关键字
    private String keyword;
    //三级分类ID
    private Long catalog3Id;

    //排序条件(saleCount（销量）、hotScore（热度分）、skuPrice（价格）)
    private String sort;

    //过滤条件
    //是否有库存(0表示无库存，1表示有库存)
    private Integer hasStock;

    //价格区间查询 1-500/-500/500-
    private String skuPrice;

    //商品ID
    private List<Long> brandId;

    //按照属性进行筛选
    private List<String> attrs;

    //页码
    private Integer pageNum = 1;

    //原生的所有查询条件
    private String _queryString;
}

