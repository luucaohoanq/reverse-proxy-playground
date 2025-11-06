package com.fpt.mss.configs;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignGatewayHeaderConfig {

    @Bean
    public RequestInterceptor gatewayHeaders() {
        return template -> {
            template.header("X-From-Gateway", "true");
        };
    }
}
