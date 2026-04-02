package com.dbapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUtil {

    private String idField = "id";
    private String parentIdField = "parent_id";

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public void setParentIdField(String parentIdField) {
        this.parentIdField = parentIdField;
    }

    /**
     * 使用 Map 索引将平铺列表转换为树结构，时间复杂度 O(n)
     */
    public List<JSONObject> listToTree(List<JSONObject> list) {
        // 第一遍：建立 id -> node 的索引
        Map<String, JSONObject> idMap = new HashMap<>(list.size());
        for (JSONObject node : list) {
            String id = node.getString(this.idField);
            if (id != null) {
                idMap.put(id, node);
            }
        }

        // 第二遍：遍历所有节点，通过 Map 查找父节点并挂载
        List<JSONObject> roots = new ArrayList<>();
        for (JSONObject node : list) {
            String parentId = node.getString(this.parentIdField);
            if (parentId == null) {
                roots.add(node);
            } else {
                JSONObject parent = idMap.get(parentId);
                if (parent != null) {
                    JSONArray children = parent.getJSONArray("children");
                    if (children == null) {
                        children = new JSONArray();
                        parent.put("children", children);
                    }
                    children.add(node);
                }
            }
        }
        return roots;
    }
}
