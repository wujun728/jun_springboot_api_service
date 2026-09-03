package com.zapi.enums;

/***
 * 表单操作枚举
 * @author zhonglz
 * @date 2025年08月21日14:58:34
 */
public enum FormOperationEnum {

    ADDTABLE("ADDTABLE", "新增数据表"),
    UPDATETABLE("UPDATETABLE", "修改数据表");

    private final String code;
    private final String label;

    FormOperationEnum(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
