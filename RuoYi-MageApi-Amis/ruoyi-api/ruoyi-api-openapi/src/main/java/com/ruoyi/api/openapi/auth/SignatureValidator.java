package com.ruoyi.api.openapi.auth;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SignatureValidator {

    private static final String HMAC_SHA256 = "HmacSHA256";

    public static String generateSign(String appSecret, String timestamp, String body) {
        try {
            String data = timestamp + body;
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(keySpec);
            byte[] signBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signBytes);
        } catch (Exception e) {
            throw new RuntimeException("Sign generation failed", e);
        }
    }

    public static boolean validate(String appSecret, String timestamp, String body, String sign) {
        String expected = generateSign(appSecret, timestamp, body);
        return expected.equals(sign);
    }
}