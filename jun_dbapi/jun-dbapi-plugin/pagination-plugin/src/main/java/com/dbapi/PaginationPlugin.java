package com.dbapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.ParamProcessPlugin;

import java.util.Map;

public class PaginationPlugin extends ParamProcessPlugin {

    @Override
    public String getName() {
        return "分页插件";
    }

    @Override
    public String getDescription() {
        return "根据分页参数pageNo、pageSize，自动生成新的参数 offset = (pageNo - 1) * pageSize";
    }

    @Override
    public String getParamDescription() {
        return "参数格式： {\"pageNoField\":\"pageNo\",\"pageSizeField\":\"pageSize\",\"offsetField\":\"offset\"}，非必填，有默认值";
    }

    @Override
    public void init() {
        this.logger.info("分页插件初始化完成");
    }

    @Override
    public void process(Map<String, Object> requestParam, ApiConfig apiConfig, String localPluginParam) {
        String pageNofield = "pageNo";
        String pageSizeField = "pageSize";
        String offsetField = "offset";

        if (localPluginParam != null && !localPluginParam.trim().isEmpty()) {
            JSONObject jsonObject = JSON.parseObject(localPluginParam);
            if (jsonObject.containsKey("pageNoField")) {
                pageNofield = jsonObject.getString("pageNoField");
            }
            if (jsonObject.containsKey("pageSizeField")) {
                pageSizeField = jsonObject.getString("pageSizeField");
            }
            if (jsonObject.containsKey("offsetField")) {
                offsetField = jsonObject.getString("offsetField");
            }
        }

        Object pageNoObj = requestParam.get(pageNofield);
        if (pageNoObj == null) {
            throw new RuntimeException("分页插件：参数" + pageNofield + "不能为空");
        }
        Integer pageNo;
        if (pageNoObj instanceof Long) {
            pageNo = ((Long) pageNoObj).intValue();
        } else if (pageNoObj instanceof Integer) {
            pageNo = (Integer) pageNoObj;
        } else {
            pageNo = Integer.parseInt(pageNoObj.toString());
        }

        Object pageSizeObj = requestParam.get(pageSizeField);
        if (pageSizeObj == null) {
            throw new RuntimeException("分页插件：参数" + pageSizeField + "不能为空");
        }
        Integer pageSize;
        if (pageSizeObj instanceof Long) {
            pageSize = ((Long) pageSizeObj).intValue();
        } else if (pageSizeObj instanceof Integer) {
            pageSize = (Integer) pageSizeObj;
        } else {
            pageSize = Integer.parseInt(pageSizeObj.toString());
        }

        Integer offset = (pageNo - 1) * pageSize;
        requestParam.put(offsetField, offset);
    }
}
