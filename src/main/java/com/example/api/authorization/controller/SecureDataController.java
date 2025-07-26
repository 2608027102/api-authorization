package com.example.api.authorization.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SecureDataController {

    @PostMapping("/secure/data/post")
    public ResponseEntity<Map<String, Object>> handleSecureDataPost(@RequestBody Map<String, Object> requestData) {
        // 处理安全数据请求
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Secure data received and processed successfully");
        response.put("receivedData", requestData);
        response.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}