package com.yuyaogc.lowcode.engine.util;

import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelUtil {

    public static  <T extends Model> Map<String, List<Object>> convertListToMap(List<T> list) {
        Map<String, List<Object>> resultMap = new HashMap<>();

        for (Map<String,Object> map : list) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                // 如果键还没有出现在 resultMap 中，则初始化一个新的 List
                resultMap.putIfAbsent(key, new ArrayList<>());

                // 将值添加到相应的 List 中
                resultMap.get(key).add(value);
            }
        }

        return resultMap;
    }
}
