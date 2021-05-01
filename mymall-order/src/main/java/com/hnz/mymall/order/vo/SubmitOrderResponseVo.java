package com.hnz.mymall.order.vo;

import com.hnz.mymall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @Description:
 *
 * @createTime: 2020-07-04 22:34
 **/

@Data
public class SubmitOrderResponseVo {

    private OrderEntity order;

    /** 错误状态码 **/
    private Integer code;


}
