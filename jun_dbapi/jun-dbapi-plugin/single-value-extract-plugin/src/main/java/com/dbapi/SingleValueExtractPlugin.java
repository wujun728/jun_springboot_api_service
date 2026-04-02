package com.dbapi;

import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SingleValueExtractPlugin extends TransformPlugin {

    @Override
    public void init() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object transform(Object data, String localPluginParam) {
        if (!(data instanceof List)) {
            throw new IllegalArgumentException("SingleValueExtractPlugin expects List data, but got: " + (data == null ? "null" : data.getClass().getName()));
        }
        List<JSONObject> list = (List<JSONObject>) data;
        if (list.size() == 1) {
            JSONObject rowData = list.get(0);
            if (StringUtils.isNotBlank(localPluginParam)) {
                Object value = rowData.get(localPluginParam);
                return value;
            }
            return rowData;
        }
        if (list.size() > 1) {
            throw new RuntimeException("单条查询结果集大于1");
        }
        return null;
    }

    @Override
    public String getName() {
        return "单条数据取值插件";
    }

    @Override
    public String getDescription() {
        return "针对SQL单条数据查询，转换结果格式为单条数据，即单个对象，或者单个值（字符串、数字）。如果SQL查询结果为空，结果为null";
    }

    @Override
    public String getParamDescription() {
        return "参数非必填，如果不填参数，格式为单个对象；如果填参数，会将参数作为字段将对应的值取出来";
    }
}
