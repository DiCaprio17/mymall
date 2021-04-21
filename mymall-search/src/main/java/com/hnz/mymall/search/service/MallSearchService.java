package com.hnz.mymall.search.service;

import com.hnz.mymall.search.vo.SearchParam;
import com.hnz.mymall.search.vo.SearchReult;

public interface MallSearchService {
    /**
     * 完整查询参数 keyword=小米&sort=saleCount_desc/asc&hasStock=0/1&skuPrice=400_1900&brandId=1&catalog3Id=1&at trs=1_3G:4G:5G&attrs=2_骁龙845&attrs=4_高清屏
     * @param param 检索的所有参数
     * @return  检索的结果，里面包含页面所需要的所有信息
     */
    SearchReult search(SearchParam param);
}
