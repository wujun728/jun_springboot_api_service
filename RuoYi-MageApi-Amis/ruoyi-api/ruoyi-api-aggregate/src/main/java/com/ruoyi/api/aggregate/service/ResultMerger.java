package com.ruoyi.api.aggregate.service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ResultMerger {

    private static final int MAX_MERGE_COUNT = 10;

    public Map<String, Object> merge(List<Supplier<Object>> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return Collections.emptyMap();
        }
        if (tasks.size() > MAX_MERGE_COUNT) {
            throw new IllegalArgumentException("Merge count exceeds maximum: " + MAX_MERGE_COUNT);
        }

        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            int index = i;
            futures.add(CompletableFuture.runAsync(() -> {
                Object result = tasks.get(index).get();
                resultMap.put("result" + (index + 1), result);
            }));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return resultMap;
    }
}