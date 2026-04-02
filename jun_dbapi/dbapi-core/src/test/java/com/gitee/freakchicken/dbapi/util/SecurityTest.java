package com.gitee.freakchicken.dbapi.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;

/**
 * T032: 安全相关修复的测试套件。
 * 覆盖 JWT、密码哈希（BCrypt）、AES-256-GCM 加密、UUID 生成等安全修复。
 */
public class SecurityTest {

    private static final String VALID_SECRET = "ThisIsAVeryLongSecretKeyForTesting1234567890!";

    @BeforeClass
    public static void setup() throws Exception {
        // 重置 JwtUtils.secret 以确保测试隔离
        resetJwtSecret();
        JwtUtils.setSecret(VALID_SECRET);
    }

    private static void resetJwtSecret() throws Exception {
        Field secretField = JwtUtils.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(null, null);
    }

    // ===== JWT 测试 =====

    @Test
    public void testJwtCreateAndVerify() {
        String token = JwtUtils.createToken("123");
        assertNotNull(token);
        assertTrue(JwtUtils.verifyToken(token));
        assertEquals("123", JwtUtils.getAudience(token));
    }

    @Test
    public void testJwtRejectsTokenSignedWithDifferentKey() {
        // 用不同密钥伪造 token
        String forgedToken = JWT.create()
                .withAudience("admin")
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256("AttackerKeyThatIsDifferentFromReal!!"));

        assertFalse("伪造的 token 应被拒绝", JwtUtils.verifyToken(forgedToken));
    }

    @Test
    public void testJwtRejectsTamperedToken() {
        String token = JwtUtils.createToken("123");
        // 篡改 payload 部分
        String[] parts = token.split("\\.");
        String tamperedPayload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"aud\":[\"admin\"],\"iat\":1}".getBytes());
        String tamperedToken = parts[0] + "." + tamperedPayload + "." + parts[2];

        assertFalse("篡改的 token 应被拒绝", JwtUtils.verifyToken(tamperedToken));
    }

    @Test
    public void testJwtRejectsGarbageToken() {
        assertFalse(JwtUtils.verifyToken("not.a.jwt"));
        assertFalse(JwtUtils.verifyToken(""));
        assertNull(JwtUtils.getAudience("garbage"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJwtRejectsShortSecret() throws Exception {
        resetJwtSecret();
        try {
            JwtUtils.setSecret("short");
        } finally {
            // 恢复有效密钥
            JwtUtils.setSecret(VALID_SECRET);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJwtRejectsNullSecret() throws Exception {
        resetJwtSecret();
        try {
            JwtUtils.setSecret(null);
        } finally {
            JwtUtils.setSecret(VALID_SECRET);
        }
    }

    @Test
    public void testJwtSecretMinLength32() throws Exception {
        resetJwtSecret();
        // 恰好 32 字符应该通过
        String exact32 = "12345678901234567890123456789012";
        JwtUtils.setSecret(exact32);
        assertNotNull(JwtUtils.createToken("1"));
        // 恢复
        JwtUtils.setSecret(VALID_SECRET);
    }

    @Test(expected = IllegalStateException.class)
    public void testJwtCreateTokenWithoutSecretThrows() throws Exception {
        resetJwtSecret();
        try {
            JwtUtils.createToken("1");
        } finally {
            JwtUtils.setSecret(VALID_SECRET);
        }
    }

    // ===== BCrypt 密码哈希测试 =====

    @Test
    public void testPasswordHashAndCheck() {
        String hash = PasswordUtil.hashPassword("MyPassword123");
        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$"));
        assertTrue(PasswordUtil.checkPassword("MyPassword123", hash));
        assertFalse(PasswordUtil.checkPassword("WrongPassword", hash));
    }

    @Test
    public void testPasswordHashUniqueSalts() {
        String hash1 = PasswordUtil.hashPassword("same");
        String hash2 = PasswordUtil.hashPassword("same");
        assertNotEquals("相同密码应产生不同哈希（不同盐值）", hash1, hash2);
        // 但两者都应验证通过
        assertTrue(PasswordUtil.checkPassword("same", hash1));
        assertTrue(PasswordUtil.checkPassword("same", hash2));
    }

    @Test
    public void testPasswordLegacyPlaintextCompatibility() {
        // 旧版明文密码兼容
        assertTrue(PasswordUtil.checkPassword("admin", "admin"));
        assertFalse(PasswordUtil.checkPassword("wrong", "admin"));
    }

    @Test
    public void testPasswordNullStoredPassword() {
        assertFalse(PasswordUtil.checkPassword("any", null));
    }

    @Test
    public void testIsBCryptHash() {
        assertTrue(PasswordUtil.isBCryptHash("$2a$10$abcdefghijklmnopqrstuvwxyz"));
        assertFalse(PasswordUtil.isBCryptHash("plaintext"));
        assertFalse(PasswordUtil.isBCryptHash(null));
        assertFalse(PasswordUtil.isBCryptHash(""));
    }

    @Test
    public void testWeakPasswordStillHashed() {
        // 即使弱密码也应被哈希保护
        String hash = PasswordUtil.hashPassword("1");
        assertTrue(hash.startsWith("$2a$"));
        assertTrue(PasswordUtil.checkPassword("1", hash));
    }

    // ===== AES-256-GCM 加密测试 =====

    @Test
    public void testAesEncryptDecryptRoundTrip() throws Exception {
        EncrypDES aes = new EncrypDES();
        String plaintext = "Hello, 安全测试!";
        String encrypted = aes.encryptToBase64(plaintext);
        String decrypted = aes.decryptFromBase64(encrypted);
        assertEquals(plaintext, decrypted);
    }

    @Test
    public void testAesEncryptionProducesDifferentCiphertextEachTime() throws Exception {
        // 每次加密应产生不同密文（随机 IV）
        EncrypDES aes = new EncrypDES();
        String plaintext = "same input";
        String enc1 = aes.encryptToBase64(plaintext);
        String enc2 = aes.encryptToBase64(plaintext);
        assertNotEquals("相同明文应产生不同密文（随机 IV）", enc1, enc2);
        // 两者都应正确解密
        assertEquals(plaintext, aes.decryptFromBase64(enc1));
        assertEquals(plaintext, aes.decryptFromBase64(enc2));
    }

    @Test(expected = Exception.class)
    public void testAesTamperedCiphertextFailsDecryption() throws Exception {
        EncrypDES aes = new EncrypDES();
        String encrypted = aes.encryptToBase64("secret data");
        // 篡改密文
        byte[] bytes = Base64.getDecoder().decode(encrypted);
        bytes[bytes.length - 1] ^= 0xFF; // 翻转最后一个字节（GCM tag 部分）
        String tampered = Base64.getEncoder().encodeToString(bytes);
        aes.decryptFromBase64(tampered); // 应抛出 AEADBadTagException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesRejectsNullKey() {
        EncrypDES.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesRejectsEmptyKey() {
        EncrypDES.setKey("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesRejectsWrongLengthKey() {
        // 16 字节密钥（AES-128）应被拒绝
        String shortKey = Base64.getEncoder().encodeToString(new byte[16]);
        EncrypDES.setKey(shortKey);
    }

    @Test
    public void testAesAcceptsValid32ByteKey() throws Exception {
        byte[] keyBytes = new byte[32];
        new java.security.SecureRandom().nextBytes(keyBytes);
        String key = Base64.getEncoder().encodeToString(keyBytes);
        EncrypDES.setKey(key);
        EncrypDES aes = new EncrypDES();
        String result = aes.decryptFromBase64(aes.encryptToBase64("test"));
        assertEquals("test", result);
        // 恢复默认
        resetEncryptKey();
    }

    @Test
    public void testAesEmptyStringEncryption() throws Exception {
        resetEncryptKey();
        EncrypDES aes = new EncrypDES();
        String encrypted = aes.encryptToBase64("");
        assertEquals("", aes.decryptFromBase64(encrypted));
    }

    private void resetEncryptKey() throws Exception {
        Field keyField = EncrypDES.class.getDeclaredField("configuredKey");
        keyField.setAccessible(true);
        keyField.set(null, null);
    }

    // ===== UUID 测试 =====

    @Test
    public void testUuidFormat() {
        String id = UUIDUtil.id();
        assertNotNull(id);
        assertEquals("UUID 应为32位十六进制", 32, id.length());
        assertTrue("应全为十六进制字符", id.matches("[0-9a-f]{32}"));
    }

    @Test
    public void testUuidUniqueness() {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            assertTrue("UUID 应唯一", ids.add(UUIDUtil.id()));
        }
    }

    @Test
    public void testUuidNoHyphens() {
        String id = UUIDUtil.id();
        assertFalse(id.contains("-"));
    }
}
