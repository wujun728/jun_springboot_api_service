package com.dbapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TreeUtil {

    private String idField = "id";
    private String parentIdField = "parent_id";

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public void setParentIdField(String parentIdField) {
        this.parentIdField = parentIdField;
    }

    public List<JSONObject> listToTree(List<JSONObject> list) {
        List<JSONObject> trees = new ArrayList<>();
        for (JSONObject entity : list) {
            String parentId = entity.getString(this.parentIdField);
            if (parentId == null) {
                trees.add(findChildren(entity, list));
            }
        }
        return trees;
    }

    private JSONObject findChildren(JSONObject entity, List<JSONObject> list) {
        for (JSONObject item : list) {
            if (entity.containsKey(this.idField)
                    && entity.getString(this.idField).equals(item.getString(this.parentIdField))) {
                if (entity.getJSONArray("children") == null) {
                    entity.put("children", new JSONArray());
                }
                entity.getJSONArray("children").add(findChildren(item, list));
            }
        }
        return entity;
    }
}
