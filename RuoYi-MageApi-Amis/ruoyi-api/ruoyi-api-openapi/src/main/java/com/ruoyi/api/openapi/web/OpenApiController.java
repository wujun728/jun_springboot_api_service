package com.ruoyi.api.openapi.web;

import com.ruoyi.api.openapi.auth.AppKeyManager;
import com.ruoyi.api.openapi.auth.SignatureValidator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/openapi")
public class OpenApiController {

    private final AppKeyManager appKeyManager;

    public OpenApiController(AppKeyManager appKeyManager) {
        this.appKeyManager = appKeyManager;
    }

    @PostMapping("/execute")
    public Map<String, Object> execute(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        String appKey = request.getHeader("AppKey");
        String appSecret = request.getHeader("AppSecret");
        String timestamp = request.getHeader("Timestamp");
        String sign = request.getHeader("Sign");

        if (appKey == null || appSecret == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("code", 401);
            error.put("message", "Missing AppKey or AppSecret");
            return error;
        }

        if (!appKeyManager.authenticate(appKey, appSecret)) {
            Map<String, Object> error = new HashMap<>();
            error.put("code", 401);
            error.put("message", "Invalid AppKey or AppSecret");
            return error;
        }

        if (timestamp != null && sign != null) {
            if (!SignatureValidator.validate(appSecret, timestamp,
                    body != null ? body.toString() : "", sign)) {
                Map<String, Object> error = new HashMap<>();
                error.put("code", 401);
                error.put("message", "Invalid signature");
                return error;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", body);
        return result;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestParam String appKey,
                                         @RequestParam String appSecret,
                                         @RequestParam String name) {
        appKeyManager.register(appKey, appSecret, name);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "registered");
        return result;
    }
}