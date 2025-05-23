package com.example.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/videoFile/**").addResourceLocations("file:D:/code2025/file/video/");
        registry.addResourceHandler("/file/imageFile/**").addResourceLocations("file:D:/code2025/file/image/");

    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/register","/files/download/**","/delete","/update","/registers",
                        "/file/imageFile/**",
                        "/file/videoFile/**",
                        "/SelectVideo/policemen",
                        "/error"
                        );
    }
    @Bean
    public JWTInterceptor jwtInterceptor(){
        return new JWTInterceptor();
    }
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**").allowedOriginPatterns("*") // 允许所有来源（使用通配符*）
                .allowCredentials(true) // 允许凭据（如Cookies、认证头等）
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方法
                .exposedHeaders("Content-Length", "Content-Range")
                .allowedHeaders("*"); // 允许的请求头，可以自行添加需要的请求头
    }
}
