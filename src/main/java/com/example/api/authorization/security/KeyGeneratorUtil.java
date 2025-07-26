package com.example.api.authorization.security;

import java.security.*;
import java.util.Base64;

/**
 * RSA密钥对生成工具类
 */
public class KeyGeneratorUtil {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 初始化RSA密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(3072);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        
        // 获取私钥和公钥
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        
        // 输出Base64编码的密钥对（实际应用中应安全存储）
        System.out.println("==== 生成的RSA密钥对 ====");
        System.out.println("私钥 (Private Key): " + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        System.out.println("公钥 (Public Key): " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
    }
}