package com.dbapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.ParamProcessPlugin;

import java.util.List;
import java.util.Map;

public class StringConcatPlugin extends ParamProcessPlugin {

    @Override
    public void init() {
    }

    @Override
    public String getName() {
        return "字符串拼接插件";
    }

    @Override
    public String getDescription() {
        return "给参数的值首尾拼接字符串，默认首尾拼接字符%";
    }

    @Override
    public String getParamDescription() {
        return "参数格式是{\"fields\":[\"xx\",\"yy\"], \"position\":\"all\", \"content\":\"%\"}。fields代表要进行字符串拼接的参数名，是数组格式；position可选值是all/start/end，分别代表首尾拼接、开头拼接、结尾拼接，可以不指定，默认是all；content表示要拼接的字符串内容，可以不指定，默认是%。";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process(Map<String, Object> map, ApiConfig apiConfig, String param) {
        JSONObject p = new JSONObject();
        if (param != null && !param.isEmpty()) {
            p = JSON.parseObject(param);
        }

        String position = p.getOrDefault("position", "all").toString();
        String content = p.getOrDefault("content", "%").toString();
        List<String> fields = p.getJSONArray("fields").toJavaList(String.class);

        for (String field : fields) {
            Object o = map.get(field);
            if (o != null) {
                String value = map.get(field).toString();
                if (position.equals("all")) {
                    map.put(field, content + value + content);
                } else if (position.equals("start")) {
                    map.put(field, content + value);
                } else if (position.equals("end")) {
                    map.put(field, value + content);
                }
            }
        }
    }
}
