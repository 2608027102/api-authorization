package com.example.api.authorization.config;

import com.example.api.authorization.security.SignatureVerificationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC配置类，注册拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册签名验证拦截器，拦截/api/secure/**路径下的请求
        registry.addInterceptor(new SignatureVerificationInterceptor())
                .addPathPatterns("/api/secure/**")
                .excludePathPatterns("/api/public/**");
    }
}