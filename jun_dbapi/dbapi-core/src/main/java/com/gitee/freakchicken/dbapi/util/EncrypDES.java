package com.gitee.freakchicken.dbapi.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES-256-GCM 加密工具类（替代原 DES 实现）。
 * <p>
 * 密钥从配置属性 dbapi.encrypt.key 加载（Base64 编码的 32 字节密钥）。
 * 若未配置，使用内置默认密钥（仅限开发环境）。
 * <p>
 * GCM 模式自带认证，无需额外 HMAC。每次加密生成随机 IV 并前置于密文。
 */
public class EncrypDES {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    private static volatile byte[] configuredKey = null;

    // 默认密钥（仅开发环境，生产必须通过配置覆盖）
    private static final String DEFAULT_KEY_BASE64 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=";

    private final SecretKey secretKey;

    /**
     * 设置全局密钥（由 Spring 配置注入调用）。
     * @param base64Key Base64 编码的 32 字节 AES-256 密钥
     */
    public static void setKey(String base64Key) {
        if (base64Key == null || base64Key.trim().isEmpty()) {
            throw new IllegalArgumentException("AES 加密密钥不能为空");
        }
        byte[] keyBytes = Base64.getDecoder().decode(base64Key.trim());
        if (keyBytes.length != 32) {
            throw new IllegalArgumentException("AES-256 密钥必须为 32 字节，当前: " + keyBytes.length + " 字节");
        }
        configuredKey = keyBytes;
    }

    public EncrypDES() {
        byte[] keyBytes = configuredKey;
        if (keyBytes == null) {
            keyBytes = Base64.getDecoder().decode(DEFAULT_KEY_BASE64);
        }
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * 加密字符串，返回密文字节（IV + 密文 + GCM tag）。
     */
    public byte[] Encrytor(String str) throws Exception {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

        byte[] encrypted = cipher.doFinal(str.getBytes("UTF-8"));

        ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);
        buffer.put(iv);
        buffer.put(encrypted);
        return buffer.array();
    }

    /**
     * 解密字节数组（前 12 字节为 IV）。
     */
    public byte[] Decryptor(byte[] buff) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(buff);

        byte[] iv = new byte[GCM_IV_LENGTH];
        buffer.get(iv);

        byte[] encrypted = new byte[buffer.remaining()];
        buffer.get(encrypted);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

        return cipher.doFinal(encrypted);
    }

    /**
     * 便捷方法：加密为 Base64 字符串。
     */
    public String encryptToBase64(String plaintext) throws Exception {
        return Base64.getEncoder().encodeToString(Encrytor(plaintext));
    }

    /**
     * 便捷方法：从 Base64 字符串解密。
     */
    public String decryptFromBase64(String base64Ciphertext) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(base64Ciphertext);
        return new String(Decryptor(decoded), "UTF-8");
    }

    public static void main(String[] args) throws Exception {
        EncrypDES aes = new EncrypDES();
        String msg = "asdfasdfasd";
        String encrypted = aes.encryptToBase64(msg);
        String decrypted = aes.decryptFromBase64(encrypted);
        System.out.println("明文是:" + msg);
        System.out.println("加密后:" + encrypted);
        System.out.println("解密后:" + decrypted);
        System.out.println("加解密一致:" + msg.equals(decrypted));
    }
}
