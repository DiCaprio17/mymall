package com.hnz.mymall.auth.config;

import com.alibaba.fastjson.JSON;
import com.hnz.common.utils.R;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 自定义阻塞返回方法
 * @Created: with IntelliJ IDEA.
 * @author:
 * @createTime: 2020-07-13 11:30
 **/

@Configuration
public class MymallAuthServerSentinelConfig {

    public MymallAuthServerSentinelConfig() {

        // WebCallbackManager.setUrlBlockHandler(new UrlBlockHandler() {
        //     @Override
        //     public void blocked(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws IOException {
        //         R error = R.error(BizCodeEnum.TO_MANY_REQUEST.getCode(), BizCodeEnum.TO_MANY_REQUEST.getMessage());
        //         response.setCharacterEncoding("UTF-8");
        //         response.setContentType("application/json");
        //         response.getWriter().write(JSON.toJSONString(error));
        //
        //     }
        // });

    }

}