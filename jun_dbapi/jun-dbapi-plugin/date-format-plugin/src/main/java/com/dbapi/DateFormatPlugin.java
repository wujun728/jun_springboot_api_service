package com.dbapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateFormatPlugin extends TransformPlugin {

    @Override
    public void init() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object transform(Object data, String localPluginParam) {
        if (!(data instanceof List)) {
            throw new IllegalArgumentException("DateFormatPlugin expects List data, but got: " + (data == null ? "null" : data.getClass().getName()));
        }
        List<JSONObject> list = (List<JSONObject>) data;

        String format = "yyyy-MM-dd HH:mm:ss";
        JSONArray fields = null;

        if (localPluginParam != null && !localPluginParam.trim().isEmpty()) {
            JSONObject param = JSON.parseObject(localPluginParam);
            if (param.containsKey("format")) {
                format = param.getString("format");
            }
            if (param.containsKey("fields")) {
                fields = param.getJSONArray("fields");
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        for (JSONObject row : list) {
            for (String key : row.keySet()) {
                if (fields != null && !fields.contains(key)) {
                    continue;
                }
                Object value = row.get(key);
                if (value instanceof Date) {
                    row.put(key, sdf.format((Date) value));
                } else if (value instanceof Long) {
                    row.put(key, sdf.format(new Date((Long) value)));
                }
            }
        }
        return list;
    }

    @Override
    public String getName() {
        return "日期格式化插件";
    }

    @Override
    public String getDescription() {
        return "将查询结果中的日期字段格式化为指定格式的字符串";
    }

    @Override
    public String getParamDescription() {
        return "参数格式：{\"format\":\"yyyy-MM-dd HH:mm:ss\",\"fields\":[\"create_time\",\"update_time\"]}。format为日期格式，默认yyyy-MM-dd HH:mm:ss；fields为要格式化的字段名列表，不填则对所有日期类型字段进行格式化";
    }
}
