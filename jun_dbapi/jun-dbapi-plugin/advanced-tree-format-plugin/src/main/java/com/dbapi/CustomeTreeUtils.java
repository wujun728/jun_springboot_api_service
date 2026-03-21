package com.dbapi;

import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class CustomeTreeUtils {

    @SuppressWarnings("unchecked")
    public static List<JSONObject> groupData(List<JSONObject> source, List<GroupingParam> groupingParams, List<JSONObject> result) {
        if (groupingParams == null || groupingParams.isEmpty()) {
            return source;
        }

        GroupingParam currentParam = groupingParams.get(0);
        String groupBy = currentParam.getGroupBy();
        String childrenName = currentParam.getChildrenName();
        List<String> fields = currentParam.getFields();
        List<String> orderBy = currentParam.getOrderBy();

        Map<Object, List<JSONObject>> grouped = new LinkedHashMap<>();
        for (JSONObject item : source) {
            Object key = item.get(groupBy);
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }

        for (Map.Entry<Object, List<JSONObject>> entry : grouped.entrySet()) {
            JSONObject node = new JSONObject(true);
            node.put(groupBy, entry.getKey());

            if (fields != null && !fields.isEmpty()) {
                JSONObject first = entry.getValue().get(0);
                for (String field : fields) {
                    node.put(field, first.get(field));
                }
            }

            List<GroupingParam> remainingParams = groupingParams.subList(1, groupingParams.size());
            if (!remainingParams.isEmpty()) {
                List<JSONObject> children = groupData(entry.getValue(), remainingParams, new ArrayList<>());
                node.put(childrenName != null ? childrenName : "children", children);
            } else {
                node.put(childrenName != null ? childrenName : "children", entry.getValue());
            }

            result.add(node);
        }

        if (orderBy != null && !orderBy.isEmpty()) {
            result.sort((a, b) -> {
                for (String field : orderBy) {
                    Object va = a.get(field);
                    Object vb = b.get(field);
                    if (va instanceof Comparable && vb instanceof Comparable) {
                        int cmp = ((Comparable) va).compareTo(vb);
                        if (cmp != 0) return cmp;
                    }
                }
                return 0;
            });
        }

        return result;
    }
}
