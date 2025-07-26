package com.example.api.authorization.security;

import org.springframework.web.servlet.HandlerInterceptor;
import com.example.api.authorization.config.KeyConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Enumeration;
import java.io.IOException;

/**
 * API请求签名验证拦截器
 */
public class SignatureVerificationInterceptor implements HandlerInterceptor {
    // 公钥(实际应用中应从配置或密钥管理服务获取)
    private static final String PUBLIC_KEY = KeyConfig.PUBLIC_KEY;
    // 签名请求头名
    private static final String SIGN_HEADER_NAME = "X-API-Signature";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 读取请求体内容
        RequestWrapper requestWrapper;
        if (request instanceof RequestWrapper) {
            requestWrapper = (RequestWrapper) request;
        } else {
            requestWrapper = new RequestWrapper(request);
        }
        String requestBody = requestWrapper.getBody();

        // 2. 获取所有请求参数
        Map<String, String> params = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            params.put(paramName, request.getParameter(paramName));
        }

        // 3. 如果有请求体，添加到签名参数中
        if (requestBody != null && !requestBody.isEmpty()) {
            params.put("requestBody", requestBody);
        }

        // 4. 获取签名
        String sign = request.getHeader(SIGN_HEADER_NAME);
        if (sign == null || sign.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing signature header: " + SIGN_HEADER_NAME);
            return false;
        }

        // 5. 验证签名
        boolean verifyResult = SignatureUtil.verifySignature(params, PUBLIC_KEY, sign);
        if (!verifyResult) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid signature");
            return false;
        }

        // 6. 签名验证通过，继续处理请求
        return true;
    }

    // 移除嵌套的RequestWrapper类定义
}