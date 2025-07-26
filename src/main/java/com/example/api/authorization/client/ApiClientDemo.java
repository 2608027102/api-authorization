package com.example.api.authorization.client;

import com.example.api.authorization.security.SignatureUtil;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.example.api.authorization.config.KeyConfig;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

/**
 * API客户端示例，演示如何使用私钥对请求参数进行签名并发送请求
 */
public class ApiClientDemo {
    // 私钥 (实际应用中应安全存储，不要硬编码)
    private static final String PRIVATE_KEY = KeyConfig.PRIVATE_KEY;
    // API基础URL
    private static final String API_BASE_URL = "http://localhost:8081/api";

    public static void main(String[] args) throws Exception {
        // 创建HTTP客户端
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 1. 调用公开接口 (不需要签名)
            System.out.println("==== 调用公开接口 ====");
            String publicUrl = API_BASE_URL + "/public/info";
            HttpGet publicGet = new HttpGet(publicUrl);
            String publicResponse = EntityUtils.toString(httpClient.execute(publicGet).getEntity());
            System.out.println("公开接口响应: " + publicResponse);

            // 2. 调用安全接口 (需要签名)
            System.out.println("\n==== 调用安全接口 ====");
            String secureUrl = API_BASE_URL + "/secure/data";
            
            // 创建请求参数
            Map<String, String> params = new HashMap<>();
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("nonce", String.valueOf((int)(Math.random() * 1000000)));
            params.put("userId", "demo_user_123");
            
            // 生成签名
            String sign = SignatureUtil.generateSignature(params, PRIVATE_KEY);
            System.out.println("生成的签名: " + sign);

            // 构建带签名的请求URL
            URIBuilder uriBuilder = new URIBuilder(secureUrl);
            // 添加参数到URL
            params.forEach(uriBuilder::addParameter);
            HttpGet secureGet = new HttpGet(uriBuilder.build());
            // 添加签名到请求头
            secureGet.setHeader("X-API-Signature", sign);
            String secureResponse = EntityUtils.toString(httpClient.execute(secureGet).getEntity());
            System.out.println("安全接口响应: " + secureResponse);

            // 3. 调用带请求体的安全接口 (需要签名包含请求体)
            System.out.println("\n==== 调用带请求体的安全接口 ====");
            String postUrl = API_BASE_URL + "/secure/data/post";
            HttpPost postRequest = new HttpPost(postUrl);
            postRequest.setHeader("Content-Type", "application/json");

            // 创建请求体
            String requestBody = "{\"userId\":\"demo_user_123\",\"action\":\"create\",\"data\":\"test_content\"}";
            postRequest.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));

            // 创建请求参数(包含请求体)
            Map<String, String> postParams = new HashMap<>();
            postParams.put("timestamp", String.valueOf(System.currentTimeMillis()));
            postParams.put("nonce", String.valueOf((int)(Math.random() * 1000000)));
            postParams.put("requestBody", requestBody); // 添加请求体到签名参数

            // 生成签名
            String postSign = SignatureUtil.generateSignature(postParams, PRIVATE_KEY);
            System.out.println("带请求体的签名: " + postSign);

            // 添加参数到URL
            URIBuilder postUriBuilder = new URIBuilder(postUrl);
            postUriBuilder.addParameter("timestamp", postParams.get("timestamp"));
            postUriBuilder.addParameter("nonce", postParams.get("nonce"));
            postRequest.setURI(postUriBuilder.build());

            // 添加签名到请求头
            postRequest.setHeader("X-API-Signature", postSign);
            postRequest.setHeader("X-API-Timestamp", postParams.get("timestamp"));
            postRequest.setHeader("X-API-Nonce", postParams.get("nonce"));

            // 发送请求
            String postResponse = EntityUtils.toString(httpClient.execute(postRequest).getEntity());
            System.out.println("带请求体的接口响应: " + postResponse);
        }
    }
}