package com.hnz.mymall.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.fastjson.JSON;
import com.hnz.common.exception.BizCodeEnume;
import com.hnz.common.utils.R;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description:

 * @createTime: 2020-07-13 17:18
 **/

@Configuration
public class MymallGatewaySentinelConfig {

    public MymallGatewaySentinelConfig() {
        GatewayCallbackManager.setBlockHandler(new BlockRequestHandler() {
            //网关限流了请求，就会调用此回调
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable t) {

                R error = R.error(BizCodeEnume.TO_MANY_REQUEST.getCode(), BizCodeEnume.TO_MANY_REQUEST.getMsg());
                String errorJson = JSON.toJSONString(error);

                Mono<ServerResponse> body = ServerResponse.ok().body(Mono.just(errorJson), String.class);
                return body;
            }
        });
    }

}
