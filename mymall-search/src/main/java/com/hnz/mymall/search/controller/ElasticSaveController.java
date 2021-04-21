package com.hnz.mymall.search.controller;

import com.hnz.common.exception.BizCodeEnume;
import com.hnz.common.to.es.SkuEsModel;
import com.hnz.common.utils.R;
import com.hnz.mymall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("search/save")
@Slf4j
public class ElasticSaveController {

    @Resource
    ProductSaveService productSaveService;

    //上架商品
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {

        boolean status = false;
        try {
            status = productSaveService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            log.error("ElasticSaveController商品上架错误{}", e);
            return R.error(BizCodeEnume.PRODUCT_EXCEPTION.getCode(), BizCodeEnume.PRODUCT_EXCEPTION.getMsg());
        }

        if (status) {
            return R.error(BizCodeEnume.PRODUCT_EXCEPTION.getCode(), BizCodeEnume.PRODUCT_EXCEPTION.getMsg());
        } else {
            return R.ok();
        }

    }
}
