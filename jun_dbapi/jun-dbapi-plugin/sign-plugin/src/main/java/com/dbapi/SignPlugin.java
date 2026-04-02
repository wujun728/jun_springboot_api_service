package com.dbapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.ParameterValidator;
import com.gitee.freakchicken.dbapi.plugin.PluginConf;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.TreeMap;

public class SignPlugin extends ParameterValidator {

    @Override
    public void init() {
        this.logger.info("签名校验插件初始化完成");
    }

    @Override
    public boolean validate(Map<String, Object> requestParameter, ApiConfig apiConfig, String clientId, String localPluginParam) {
        String secret = null;
        String signField = "sign";
        String timestampField = "timestamp";
        long expireMs = 300000;

        if (localPluginParam != null && !localPluginParam.trim().isEmpty()) {
            JSONObject param = JSON.parseObject(localPluginParam);
            secret = param.getString("secret");
            if (param.containsKey("signField")) {
                signField = param.getString("signField");
            }
            if (param.containsKey("timestampField")) {
                timestampField = param.getString("timestampField");
            }
            if (param.containsKey("expireMs")) {
                expireMs = param.getLongValue("expireMs");
            }
        }

        if (secret == null || secret.isEmpty()) {
            secret = PluginConf.getKey("SignPlugin.secret");
        }
        if (secret == null || secret.isEmpty()) {
            throw new RuntimeException("签名插件：缺少secret配置");
        }

        Object signObj = requestParameter.get(signField);
        if (signObj == null) {
            throw new RuntimeException("签名插件：缺少签名参数" + signField);
        }
        String sign = signObj.toString();

        Object tsObj = requestParameter.get(timestampField);
        if (tsObj != null) {
            long timestamp;
            if (tsObj instanceof Long) {
                timestamp = (Long) tsObj;
            } else {
                try {
                    timestamp = Long.parseLong(tsObj.toString());
                } catch (NumberFormatException e) {
                    throw new RuntimeException("签名插件：时间戳参数'" + timestampField + "'格式非法，期望数字，实际值：" + tsObj);
                }
            }
            if (Math.abs(System.currentTimeMillis() - timestamp) > expireMs) {
                throw new RuntimeException("签名插件：请求已过期");
            }
        }

        TreeMap<String, Object> sortedParams = new TreeMap<>(requestParameter);
        sortedParams.remove(signField);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
            if (entry.getValue() != null) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        sb.append("secret=").append(secret);

        String expectedSign = DigestUtils.md5Hex(sb.toString());
        if (!expectedSign.equals(sign)) {
            throw new RuntimeException("签名插件：签名校验失败");
        }

        requestParameter.remove(signField);
        return true;
    }

    @Override
    public String getName() {
        return "请求签名校验插件";
    }

    @Override
    public String getDescription() {
        return "对请求参数进行签名校验，防止参数被篡改。签名算法：将参数按key排序后拼接，加上secret，进行MD5签名";
    }

    @Override
    public String getParamDescription() {
        return "参数格式：{\"secret\":\"xxx\",\"signField\":\"sign\",\"timestampField\":\"timestamp\",\"expireMs\":300000}。secret为签名密钥（必填，也可在plugin.properties配置SignPlugin.secret）；signField为签名参数名，默认sign；timestampField为时间戳参数名，默认timestamp；expireMs为请求过期毫秒数，默认300000";
    }
}
