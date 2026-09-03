package com.zy.utils;

import com.zy.common.utils.ParamUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecuteSqlUtils {

    public static String getExecuteSql(Map<String, Object> allArgsParamMap, Map<String, Object> inputParamMap, String sql) {
        //判断sql语句是否为空
        if (StringUtils.isNotEmpty(sql)) {
            //查询出占位符 [`and role_id={roleId}`]
            List<String> sqlPlaceHolderList = ParamUtils.getRegexSpList(sql);

            //如果没有输入参数,将所有占位符清空
            if (inputParamMap.size() == 0) {
                for (int j = 0; j < sqlPlaceHolderList.size(); j++) {
                    String sqlPlaceHolder = sqlPlaceHolderList.get(j);
                    sql = sql.replaceAll( ParamUtils.escapeQueryChars(sqlPlaceHolder), "");
                }
            }

            //有输入参数,占位符处理
            if (inputParamMap.size() > 0) {

                Map<String, Object> allParamMap = new HashMap<>();
                allParamMap.putAll(allArgsParamMap);
                allParamMap.putAll(inputParamMap);

                for (String paramMapKey : allParamMap.keySet()) {
                    for (int i = 0; i < sqlPlaceHolderList.size(); i++) {
                        Object paramMapValue = allParamMap.get(paramMapKey);
                        String sqlPlaceHolder = sqlPlaceHolderList.get(i);

                        if (sqlPlaceHolder.contains("{" + paramMapKey + "}")) {

                            if (EmptyUtils.isEmpty(paramMapValue)) {
                                sql = ParamUtils.replaceParam(sql, sqlPlaceHolder, paramMapKey, "");
                            }

                            if (EmptyUtils.isNotEmpty(paramMapValue)) {
                                sql = ParamUtils.replaceParam(sql, sqlPlaceHolder, paramMapKey, paramMapValue);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("ExecuteSql==>" + sql);
        return sql;
    }



}
