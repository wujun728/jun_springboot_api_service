package com.itboy.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Transient;

/**
 * @ClassName ExecuteSql
 * @Description 执行器BEAN
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/21 0021 20:33
 **/
@Data
public class ExecuteSql {

    @Transient
    private String dataType;

    @Transient
    private String sqlText;

    @Transient
    private String code;

    @Transient
    private String msg;

    @Transient
    private JSONArray dataJson;

    @Transient
    private JSONObject dataObj;

}
