package com.hnz.mymall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 *
 * @createTime: 2020-07-04 23:19
 **/

@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;

}
