package com.example.api.authorization.config;

import com.example.api.authorization.filter.RequestBodyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<RequestBodyFilter> requestBodyFilter() {
        FilterRegistrationBean<RequestBodyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestBodyFilter());
        registrationBean.addUrlPatterns("/*"); // 应用于所有请求
        registrationBean.setName("requestBodyFilter");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 确保在其他过滤器之前执行
        return registrationBean;
    }
}