package com.example.api.authorization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 包含需要签名验证的API端点
 */
@RestController
@RequestMapping("/api")
public class SecureApiController {
    
    /**
     * 公开接口 - 不需要签名验证
     */
    @GetMapping("/public/info")
    public Map<String, Object> publicInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "这是一个公开接口，不需要签名验证");
        result.put("data", System.currentTimeMillis());
        return result;
    }
    
    /**
     * 安全接口 - 需要签名验证
     * 请求参数需要包含: timestamp(时间戳), nonce(随机数), sign(签名)
     */
    @GetMapping("/secure/data")
    public Map<String, Object> secureData(
            @RequestParam String timestamp,
            @RequestParam String nonce,
            @RequestParam String userId) {
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "签名验证通过，这是受保护的数据");
        result.put("data", Map.of(
            "userId", userId,
            "timestamp", timestamp,
            "nonce", nonce,
            "secretInfo", "这是需要保护的敏感数据"
        ));
        return result;
    }
}