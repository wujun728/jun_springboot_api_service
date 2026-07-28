package com.ruoyi.api.plugin;

import com.ruoyi.api.plugin.impl.MemoryCachePlugin;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class PluginTest {

    @Test
    void testMemoryCachePlugin() {
        MemoryCachePlugin cache = new MemoryCachePlugin();
        cache.init();

        assertEquals("Memory Cache", cache.getName());
        assertNotNull(cache.getDescription());

        cache.set("api1", new HashMap<>(), "data1", null);
        assertEquals("data1", cache.get("api1", new HashMap<>(), null));

        cache.clean("api1", null);
        assertNull(cache.get("api1", new HashMap<>(), null));
    }

    @Test
    void testPluginManager() {
        assertDoesNotThrow(() -> PluginManager.loadPlugins());
        assertNotNull(PluginManager.getAllPlugins("cache"));
    }

    @Test
    void testBasePluginContract() {
        BasePlugin p = new BasePlugin() {
            public String getName() { return "test"; }
            public String getDescription() { return "desc"; }
            public String getParamDescription() { return "param"; }
        };
        assertEquals("test", p.getName());
        assertEquals("desc", p.getDescription());
    }
}