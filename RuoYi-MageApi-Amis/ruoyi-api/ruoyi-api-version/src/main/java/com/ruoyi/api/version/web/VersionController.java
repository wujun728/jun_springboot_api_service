package com.ruoyi.api.version.web;

import com.ruoyi.api.version.entity.ApiVersionRecord;
import com.ruoyi.api.version.service.VersionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/version")
public class VersionController {

    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    @PostMapping("/save")
    public ApiVersionRecord save(@RequestBody Map<String, String> params) {
        return versionService.saveVersion(
                params.get("apiId"),
                params.get("apiPath"),
                params.get("apiName"),
                params.get("content"),
                params.getOrDefault("operateType", "update"),
                params.getOrDefault("operateBy", "system")
        );
    }

    @GetMapping("/history/{apiId}")
    public List<ApiVersionRecord> history(@PathVariable String apiId) {
        return versionService.getHistory(apiId);
    }

    @PostMapping("/rollback/{apiId}/{versionNo}")
    public ApiVersionRecord rollback(@PathVariable String apiId, @PathVariable int versionNo) {
        return versionService.rollback(apiId, versionNo);
    }
}