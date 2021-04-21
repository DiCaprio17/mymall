package com.hnz.mymall.search.controller;

import com.hnz.mymall.search.service.MallSearchService;
import com.hnz.mymall.search.vo.SearchParam;
import com.hnz.mymall.search.vo.SearchReult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    /**
     * 利用springMVC自动将页面提交过来的所有请求查询参数封装成指定的对象
     * @param param
     * @param model
     * @return
     */
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request){
        param.set_queryString(request.getQueryString());
        //1. 根据页面传递过来的查询参数，到ES中检索商品
        SearchReult result=mallSearchService.search(param);
        model.addAttribute("result",result);
        return "list";
    }
}
