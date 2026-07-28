package com.ruoyi.api.aggregate;

import com.ruoyi.api.aggregate.service.DataTransformer;
import com.ruoyi.api.aggregate.service.ResultMerger;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.function.Supplier;
import static org.junit.jupiter.api.Assertions.*;

class AggregateTest {

    @Test
    void testResultMerger() {
        ResultMerger merger = new ResultMerger();

        Map<String, Object> result = merger.merge(Arrays.asList(
                (Supplier<Object>) () -> "result1",
                (Supplier<Object>) () -> "result2"
        ));
        assertEquals(2, result.size());
        assertEquals("result1", result.get("result1"));
        assertEquals("result2", result.get("result2"));
    }

    @Test
    void testResultMergerEmpty() {
        ResultMerger merger = new ResultMerger();
        assertTrue(merger.merge(null).isEmpty());
        assertTrue(merger.merge(Collections.emptyList()).isEmpty());
    }

    @Test
    void testKvConver() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1);
        data.put("name", "test");

        Map<String, Object> result = DataTransformer.kvConver(data, "{\"name\":\"id\",\"value\":\"name\"}");
        assertEquals(1, result.get("name"));
        assertEquals("test", result.get("value"));
    }

    @Test
    void testRcConver() {
        Map<String, Object> data = new HashMap<>();
        data.put("key", "k1");
        data.put("value", "v1");

        Map<String, Object> result = DataTransformer.rcConver(data, "{\"name\":\"key\",\"value\":\"value\"}");
        assertEquals("v1", result.get("k1"));
    }

    @Test
    void testKeyExtract() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 100);
        data.put("name", "test");

        Map<String, Object> result = DataTransformer.keyExtract(data, "{\"key\":\"id\"}");
        assertTrue(result.containsKey("100"));
        assertEquals(data, result.get("100"));
    }
}