package com.ruoyi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.config.MagicAPIProperties;
import org.ssssssss.magicapi.core.context.RequestEntity;
import org.ssssssss.magicapi.core.interceptor.ResultProvider;
import org.ssssssss.magicapi.core.model.JsonBean;
import org.ssssssss.magicapi.modules.db.model.Page;
import org.ssssssss.magicapi.utils.ScriptManager;
import org.ssssssss.script.MagicScriptContext;

import java.util.*;

@Component
public class CustomJsonValueProvider implements ResultProvider
{


    @Autowired
    MagicAPIProperties magicAPIProperties;

    /**
     * 定义返回结果，默认返回JsonBean
     */
    @Override
    public Object buildResult(RequestEntity requestEntity, int code, String message, Object data) {
        // 如果对分页格式有要求的话，可以对data的类型进行判断，进而返回不同的格式
        long timestamp = System.currentTimeMillis();
        LinkedHashMap lk=new LinkedHashMap();
        lk.get("id");
        System.out.println();
        if (magicAPIProperties.getResponse() != null) {
            MagicScriptContext context = new MagicScriptContext();
            context.setScriptName(requestEntity.getMagicScriptContext().getScriptName());
            context.set("code", code);
            context.set("message", message);
            if (data == null) {
                data = new ArrayList<>();
            }
            context.set("data", data);
            context.set("apiInfo", requestEntity.getApiInfo());
            context.set("request", requestEntity.getRequest());
            context.set("response", requestEntity.getResponse());
            context.set("timestamp", timestamp);
            context.set("requestTime", requestEntity.getRequestTime());
            context.set("executeTime", timestamp - requestEntity.getRequestTime());
            return ScriptManager.executeExpression(magicAPIProperties.getResponse(), context);
        } else {
            return new JsonBean(code, message, data, (int) (timestamp - requestEntity.getRequestTime()));
        }
    }

    /**
     * 定义分页返回结果，该项会被封装在Json结果内，
     * 此方法可以不覆盖，默认返回PageResult
     */
    @Override
    public Object buildPageResult(RequestEntity requestEntity, Page page, long total, List<Map<String, Object>> data) {
        return new HashMap<String, Object>()
        {
            {
                put("total", total);
                if (data == null) {
                    put("rows", new ArrayList<>());
                } else {
                    put("rows", data);
                }
            }
        };
    }
}
