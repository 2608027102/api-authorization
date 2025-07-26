package com.example.api.authorization.security;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * RSA签名工具类，提供签名生成和验证功能
 */
public class SignatureUtil {
    // 签名算法
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    // 加密算法
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 生成签名
     * @param params 请求参数
     * @param privateKey 私钥(Base64编码)
     * @return 签名结果(Base64编码)
     */
    public static String generateSignature(Map<String, String> params, String privateKey) throws Exception {
        // 1. 参数排序
        String sortedParams = sortParams(params);
        // 2. 使用私钥签名
        PrivateKey priKey = getPrivateKey(privateKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(sortedParams.getBytes("UTF-8"));
        byte[] signed = signature.sign();
        return Base64.encodeBase64String(signed);
    }

    /**
     * 验证签名
     * @param params 请求参数
     * @param publicKey 公钥(Base64编码)
     * @param sign 签名结果(Base64编码)
     * @return 验证结果
     */
    public static boolean verifySignature(Map<String, String> params, String publicKey, String sign) throws Exception {
        // 1. 参数排序
        String sortedParams = sortParams(params);
        // 2. 使用公钥验签
        PublicKey pubKey = getPublicKey(publicKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(sortedParams.getBytes("UTF-8"));
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * 参数排序并拼接成字符串
     */
    private static String sortParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        // 排序参数名
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        // 拼接参数
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i > 0) {
                sb.append("&");
            }
            sb.append(key).append("=");
            if (value != null) {
                sb.append(value);
            }
        }
        return sb.toString();
    }

    /**
     * 获取私钥对象
     */
    private static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥对象
     */
    private static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }
}