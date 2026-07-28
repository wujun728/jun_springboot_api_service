package com.ruoyi.api.openapi;

import com.ruoyi.api.openapi.auth.AppKeyManager;
import com.ruoyi.api.openapi.auth.SignatureValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OpenApiTest {

    @Test
    void testAppKeyManager() {
        AppKeyManager mgr = new AppKeyManager();
        mgr.register("key1", "secret1", "app1");

        assertTrue(mgr.authenticate("key1", "secret1"));
        assertFalse(mgr.authenticate("key1", "wrong"));
        assertFalse(mgr.authenticate("key2", "secret1"));

        mgr.remove("key1");
        assertFalse(mgr.authenticate("key1", "secret1"));
    }

    @Test
    void testSignature() {
        String secret = "test_secret";
        String timestamp = "1700000000000";
        String body = "test_body";

        String sign = SignatureValidator.generateSign(secret, timestamp, body);
        assertNotNull(sign);
        assertTrue(SignatureValidator.validate(secret, timestamp, body, sign));
        assertFalse(SignatureValidator.validate(secret, timestamp, body, "wrong_sign"));
    }
}