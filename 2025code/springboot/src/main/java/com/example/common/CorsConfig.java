package com.example.common;

import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 支持域
        corsConfiguration.addAllowedOrigin("*");//设置访问源地址
        // 允许的原始请求头部信息
        corsConfiguration.addAllowedHeader("*");//设置访问源请求头
        // 支持请求方式
        corsConfiguration.addAllowedMethod("*");//设置访问源请求方法

        source.registerCorsConfiguration("/**", corsConfiguration);//对接口配置跨域设置
        // 3.返回 CorsFilter 对象
        return new CorsFilter(source);
    }
}
