package com.dbapi;

import com.alibaba.fastjson.JSON;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;

import java.util.ArrayList;
import java.util.List;

public class AdvancedTreeFormatPlugin extends TransformPlugin {

    @Override
    public void init() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object transform(Object data, String param) {
        if (!(data instanceof List)) {
            throw new IllegalArgumentException("AdvancedTreeFormatPlugin expects List data, but got: " + (data == null ? "null" : data.getClass().getName()));
        }
        List source = (List) data;
        List<GroupingParam> groupingParams = JSON.parseArray(param, GroupingParam.class);
        List list = CustomeTreeUtils.groupData(source, groupingParams, new ArrayList());
        return list;
    }

    @Override
    public String getName() {
        return "高级树状数据结构插件";
    }

    @Override
    public String getDescription() {
        return "将SQL查询结果转换成树状结构，可设置每一层级的分组字段和取值字段";
    }

    @Override
    public String getParamDescription() {
        return "[{\"groupBy\":\"xxx\",\"childrenName\":\"yyy\",\"fields\":[\"nn\",\"mmm\",...]},...] ";
    }
}
