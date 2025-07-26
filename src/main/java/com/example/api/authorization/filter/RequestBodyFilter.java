package com.example.api.authorization.filter;

import com.example.api.authorization.security.RequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestBodyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 仅包装HTTP请求
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            // 使用自定义RequestWrapper包装请求
            RequestWrapper requestWrapper = new RequestWrapper(httpRequest);
            chain.doFilter(requestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}