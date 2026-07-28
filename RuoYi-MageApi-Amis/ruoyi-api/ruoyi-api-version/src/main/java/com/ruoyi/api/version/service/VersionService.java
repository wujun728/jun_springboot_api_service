package com.ruoyi.api.version.service;

import com.ruoyi.api.version.entity.ApiVersionRecord;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class VersionService {

    private final Map<String, List<ApiVersionRecord>> store = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public ApiVersionRecord saveVersion(String apiId, String apiPath, String apiName,
                                         String content, String operateType, String operateBy) {
        List<ApiVersionRecord> records = store.computeIfAbsent(apiId, k -> new ArrayList<>());
        AtomicInteger counter = counters.computeIfAbsent(apiId, k -> new AtomicInteger(0));

        ApiVersionRecord record = new ApiVersionRecord();
        record.setId((long) (records.size() + 1));
        record.setApiId(apiId);
        record.setApiPath(apiPath);
        record.setApiName(apiName);
        record.setContent(content);

        if (!records.isEmpty()) {
            ApiVersionRecord last = records.get(records.size() - 1);
            record.setDiffContent(computeDiff(last.getContent(), content));
        }

        record.setVersionNo(counter.incrementAndGet());
        record.setOperateType(operateType);
        record.setOperateBy(operateBy);
        record.setOperateTime(new Date());

        records.add(record);
        return record;
    }

    public List<ApiVersionRecord> getHistory(String apiId) {
        return store.getOrDefault(apiId, Collections.emptyList());
    }

    public ApiVersionRecord getVersion(String apiId, int versionNo) {
        List<ApiVersionRecord> records = store.get(apiId);
        if (records == null) return null;
        return records.stream()
                .filter(r -> r.getVersionNo() == versionNo)
                .findFirst().orElse(null);
    }

    public ApiVersionRecord rollback(String apiId, int versionNo) {
        ApiVersionRecord target = getVersion(apiId, versionNo);
        if (target == null) return null;
        return saveVersion(apiId, target.getApiPath(), target.getApiName(),
                target.getContent(), "rollback", "system");
    }

    private String computeDiff(String oldContent, String newContent) {
        if (oldContent == null || oldContent.isEmpty()) return "[new]";
        if (oldContent.equals(newContent)) return "[no change]";
        return "[modified]";
    }
}