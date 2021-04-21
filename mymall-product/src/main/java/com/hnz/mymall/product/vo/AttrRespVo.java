package com.hnz.mymall.product.vo;

import lombok.Data;

/**
 * @author huangnuozhong
 * @create 2020-05-15-18:08
 */
@Data
public class AttrRespVo extends AttrVo {
    /**
     * 			"catelogName": "手机/数码/手机", //所属分类名字
     * 			"groupName": "主体", //所属分组名字
     */
    private String catelogName;
    private String groupName;

    private Long[] catelogPath;
}
