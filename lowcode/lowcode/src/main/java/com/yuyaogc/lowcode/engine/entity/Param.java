package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.entity.datatype.DataType;

public class Param extends Entity {

    private DataType paramType;

    public DataType getParamType() {
        return paramType;
    }

    public void setParamType(DataType paramType) {
        this.paramType = paramType;
    }
}
