package com.hnz.mymall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "mymall.thread")
// @Component
@Data
public class ThreadPoolConfigProperties {

    private Integer coreSize;

    private Integer maxSize;

    private Integer keepAliveTime;


}
