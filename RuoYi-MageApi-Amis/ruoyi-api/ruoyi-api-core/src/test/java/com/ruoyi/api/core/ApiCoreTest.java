package com.ruoyi.api.core;

import com.ruoyi.api.core.constant.ApiConstant;
import com.ruoyi.api.core.model.ApiRequest;
import com.ruoyi.api.core.model.ApiResponse;
import com.ruoyi.api.core.spi.*;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class ApiCoreTest {

    @Test
    void testApiRequest() {
        ApiRequest req = new ApiRequest();
        req.setPath("/test");
        req.setMethod("GET");
        req.getQueryParams().put("name", "test");
        assertEquals("/test", req.getPath());
        assertEquals("GET", req.getMethod());
        assertEquals("test", req.getParam("name"));
    }

    @Test
    void testApiResponse() {
        ApiResponse resp = ApiResponse.success("ok");
        assertEquals(200, resp.getCode());
        assertEquals("success", resp.getMessage());
        assertEquals("ok", resp.getData());

        ApiResponse err = ApiResponse.error(500, "error");
        assertEquals(500, err.getCode());
    }

    @Test
    void testConstants() {
        assertEquals("/api", ApiConstant.PREFIX_API);
        assertEquals("/reverse", ApiConstant.PREFIX_REVERSE);
        assertEquals(10, ApiConstant.MAX_API_MERGE_COUNT);
    }

    @Test
    void testAuthProvider() {
        AuthProvider provider = new AuthProvider() {
            public boolean authenticate(String token, String path, String method) { return true; }
            public String getType() { return "test"; }
        };
        assertTrue(provider.authenticate(null, null, null));
        assertEquals("test", provider.getType());
    }

    @Test
    void testCacheProvider() {
        CacheProvider cache = new CacheProvider() {
            private String key; private Object val;
            public void put(String k, Object v, long e) { this.key = k; this.val = v; }
            public Object get(String k) { return k.equals(key) ? val : null; }
            public void invalid(String k) { if (k.equals(key)) { key = null; val = null; } }
            public boolean exists(String k) { return k.equals(key); }
        };
        cache.put("k1", "v1", 300);
        assertEquals("v1", cache.get("k1"));
        assertTrue(cache.exists("k1"));
        cache.invalid("k1");
        assertNull(cache.get("k1"));
    }
}