package com.dbapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

public class EncryptPlugin extends TransformPlugin {

    @Override
    public void init() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object transform(Object data, String localPluginParam) {
        if (!(data instanceof List)) {
            throw new IllegalArgumentException("EncryptPlugin expects List data, but got: " + (data == null ? "null" : data.getClass().getName()));
        }
        List<JSONObject> list = (List<JSONObject>) data;

        String algorithm = "md5";
        JSONArray fields = null;

        if (localPluginParam != null && !localPluginParam.trim().isEmpty()) {
            JSONObject param = JSON.parseObject(localPluginParam);
            if (param.containsKey("algorithm")) {
                algorithm = param.getString("algorithm");
            }
            if (param.containsKey("fields")) {
                fields = param.getJSONArray("fields");
            }
        }

        if (fields == null || fields.isEmpty()) {
            return list;
        }

        List<String> fieldList = fields.toJavaList(String.class);

        for (JSONObject row : list) {
            for (String field : fieldList) {
                Object value = row.get(field);
                if (value != null) {
                    String strValue = value.toString();
                    switch (algorithm.toLowerCase()) {
                        case "md5":
                            row.put(field, DigestUtils.md5Hex(strValue));
                            break;
                        case "sha1":
                            row.put(field, DigestUtils.sha1Hex(strValue));
                            break;
                        case "sha256":
                            row.put(field, DigestUtils.sha256Hex(strValue));
                            break;
                        default:
                            row.put(field, DigestUtils.md5Hex(strValue));
                            break;
                    }
                }
            }
        }
        return list;
    }

    @Override
    public String getName() {
        return "数据脱敏加密插件";
    }

    @Override
    public String getDescription() {
        return "对查询结果中指定字段的值进行MD5/SHA哈希加密，实现数据脱敏";
    }

    @Override
    public String getParamDescription() {
        return "参数格式：{\"algorithm\":\"md5\",\"fields\":[\"phone\",\"id_card\"]}。algorithm可选md5/sha1/sha256，默认md5；fields为要加密的字段名列表，必填";
    }
}
