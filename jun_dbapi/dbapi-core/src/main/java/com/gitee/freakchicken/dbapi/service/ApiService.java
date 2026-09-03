package com.gitee.freakchicken.dbapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-20 15:36
 **/
@Service
@Slf4j
public class ApiService {

    public Map<String, Object> getSqlParam(HttpServletRequest request, ApiConfig config) {
        Map<String, Object> map = new HashMap<>();

        JSONArray requestParams = JSON.parseArray(config.getParams());
        for (int i = 0; i < requestParams.size(); i++) {
            JSONObject jo = requestParams.getJSONObject(i);
            String name = jo.getString("name");
            String type = jo.getString("type");

            //数组类型参数
            if (type.startsWith("Array")) {
                String[] values = request.getParameterValues(name);
                if (values != null) {
                    List<String> list = Arrays.asList(values);
                    if (values.length > 0) {
                        switch (type) {
                            case "Array<double>":
                                try {
                                    List<Double> collect = list.stream().map(value -> Double.valueOf(value)).collect(Collectors.toList());
                                    map.put(name, collect);
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("参数 '" + name + "' 包含非法的浮点数值", e);
                                }
                                break;
                            case "Array<bigint>":
                                try {
                                    List<Long> longs = list.stream().map(value -> Long.valueOf(value)).collect(Collectors.toList());
                                    map.put(name, longs);
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("参数 '" + name + "' 包含非法的整数值", e);
                                }
                                break;
                            case "Array<string>":
                            case "Array<date>":
                                map.put(name, list);
                                break;
                        }
                    } else {
                        map.put(name, list);
                    }
                } else {
                    map.put(name, null);
                }
            } else {

                String value = request.getParameter(name);
                if (StringUtils.isNotBlank(value)) {

                    switch (type) {
                        case "double":
                            try {
                                Double v = Double.valueOf(value);
                                map.put(name, v);
                            } catch (NumberFormatException e) {
                                throw new IllegalArgumentException("参数 '" + name + "' 的值 '" + value + "' 不是合法的浮点数", e);
                            }
                            break;
                        case "bigint":
                            try {
                                Long longV = Long.valueOf(value);
                                map.put(name, longV);
                            } catch (NumberFormatException e) {
                                throw new IllegalArgumentException("参数 '" + name + "' 的值 '" + value + "' 不是合法的整数", e);
                            }
                            break;
                        case "string":
                        case "date":
                            map.put(name, value);
                            break;
                    }
                } else {
                    map.put(name, value);
                }
            }
        }
        return map;
    }

}
