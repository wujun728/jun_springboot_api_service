package com.dbapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class TreePlugin extends TransformPlugin {

    @Override
    public void init() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object transform(Object data, String param) {
        TreeUtil treeUtil = new TreeUtil();
        if (StringUtils.isNotBlank(param)) {
            JSONObject jsonObject = JSON.parseObject(param);
            if (StringUtils.isNotBlank(jsonObject.getString("idField"))) {
                treeUtil.setIdField(jsonObject.getString("idField"));
            }
            if (StringUtils.isNotBlank(jsonObject.getString("parentIdField"))) {
                treeUtil.setParentIdField(jsonObject.getString("parentIdField"));
            }
        }
        List<JSONObject> list = (List<JSONObject>) data;
        List<JSONObject> jsonObjects = treeUtil.listToTree(list);
        return jsonObjects;
    }

    @Override
    public String getName() {
        return "树状结构数据插件";
    }

    @Override
    public String getDescription() {
        return "将sql查询结果转换成树状结构（父子结构），可以递归转换任意多级";
    }

    @Override
    public String getParamDescription() {
        return "参数格式是{\"idField\":\"xxx\",\"parentIdField\":\"xxx\"}，指定数据的id字段名和父级id字段名，如果不传参数，将默认使用\"id\" \"parent_id\"作为id字段名和父级id字段名";
    }
}
