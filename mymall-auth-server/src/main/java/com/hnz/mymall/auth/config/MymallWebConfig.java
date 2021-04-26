package com.hnz.mymall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author:
 * @createTime: 2020-06-27 08:52
 **/
@Configuration
public class MymallWebConfig implements WebMvcConfigurer {

    /**
     * 用springmvc做视图映射，不用再controller里写这两个空方法了
     * 视图映射:发送一个请求，直接跳转到一个页面
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");
    }
}
