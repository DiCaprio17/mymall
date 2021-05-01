package com.hnz.mymall.order.vo;

import lombok.Data;

/**
 * @Description: 库存vo
 *
 * @createTime: 2020-07-03 18:13
 **/

@Data
public class SkuStockVo {


    private Long skuId;

    private Boolean hasStock;

}
