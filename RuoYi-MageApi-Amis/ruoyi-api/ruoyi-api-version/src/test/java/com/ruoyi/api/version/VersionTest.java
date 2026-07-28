package com.ruoyi.api.version;

import com.ruoyi.api.version.entity.ApiVersionRecord;
import com.ruoyi.api.version.service.VersionService;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class VersionTest {

    @Test
    void testSaveAndGetHistory() {
        VersionService vs = new VersionService();

        vs.saveVersion("api1", "/test", "Test API", "content_v1", "create", "admin");
        vs.saveVersion("api1", "/test", "Test API", "content_v2", "update", "admin");

        List<ApiVersionRecord> history = vs.getHistory("api1");
        assertEquals(2, history.size());
        assertEquals(1, history.get(0).getVersionNo().intValue());
        assertEquals(2, history.get(1).getVersionNo().intValue());
    }

    @Test
    void testRollback() {
        VersionService vs = new VersionService();

        vs.saveVersion("api2", "/test2", "API2", "v1", "create", "admin");
        vs.saveVersion("api2", "/test2", "API2", "v2", "update", "admin");

        vs.rollback("api2", 1);
        List<ApiVersionRecord> history = vs.getHistory("api2");
        assertEquals(3, history.size());
        assertEquals("rollback", history.get(2).getOperateType());
    }

    @Test
    void testEmptyHistory() {
        VersionService vs = new VersionService();
        assertTrue(vs.getHistory("nonexistent").isEmpty());
    }
}