package com.zapi.enums;

import lombok.Getter;

/**
 * 表单字段类型枚举
 * 用于定义表单字段类型到数据库字段类型的映射
 *
 * @author zhonglz
 * @date 2025年08月21日14:58:34
 */
@Getter
public enum FormFieldTypeEnum {
    
    // 文本类型
    INPUT("input", "varchar", "单行文本", 255, null),
    TEXTAREA("textarea", "text", "多行文本", 1024, null),
    
    // 数字类型
    NUMBER("number", "int", "数字", null, 0),
    SLIDER("slider", "int", "滑块", null, 0),
    RATE("rate", "decimal", "评分", null, 2),
    
    // 日期时间类型
    DATE("date", "date", "日期", null, null),
    DATETIME("datetime", "datetime", "日期时间", null, null),
    TIME("time", "time", "时间", null, null),
    
    // 选择类型
    SELECT("select", "varchar", "下拉选择", 1024, null),
    RADIO("radio", "varchar", "单选框", 255, null),
    CHECKBOX("checkbox", "varchar", "复选框", 255, null),
    
    // 开关类型
    SWITCH("switch", "tinyint", "开关", 1, null),
    
    // 文件类型
    UPLOAD("upload", "text", "文件上传", null, null),
    // 富文本
    RICHTEXT("richtext", "longtext", "富文本", null, null),

    HTML("html", "longtext", "富文本", null, null),
    
    // 业务组件类型（存储ID，使用varchar）
    USER_SELECT("user-select", "varchar", "人员选择", 1024, null),
    DEPARTMENT_SELECT("department-select", "varchar", "部门选择", 1024, null),
    ROLE_SELECT("role-select", "varchar", "角色选择", 1024, null),

    // 默认类型
    DEFAULT("default", "varchar", "默认类型", 255, null);
    
    /**
     * 表单字段类型
     */
    private final String formType;
    
    /**
     * 数据库字段类型
     */
    private final String dbType;
    
    /**
     * 字段类型描述
     */
    private final String description;
    
    /**
     * 默认字段长度
     */
    private final Integer defaultLength;
    
    /**
     * 默认字段精度（小数位数）
     */
    private final Integer defaultScale;
    
    FormFieldTypeEnum(String formType, String dbType, String description, Integer defaultLength, Integer defaultScale) {
        this.formType = formType;
        this.dbType = dbType;
        this.description = description;
        this.defaultLength = defaultLength;
        this.defaultScale = defaultScale;
    }
    
    /**
     * 根据表单字段类型获取枚举
     * @param formType 表单字段类型
     * @return 对应的枚举，如果没找到返回DEFAULT
     */
    public static FormFieldTypeEnum getByFormType(String formType) {
        if (formType == null) {
            return DEFAULT;
        }
        
        for (FormFieldTypeEnum fieldType : values()) {
            if (fieldType.getFormType().equals(formType)) {
                return fieldType;
            }
        }
        
        return DEFAULT;
    }
    
    /**
     * 获取数据库字段类型
     * @param formType 表单字段类型
     * @return 数据库字段类型
     */
    public static String getDbType(String formType) {
        return getByFormType(formType).getDbType();
    }
    
    /**
     * 获取字段类型描述
     * @param formType 表单字段类型
     * @return 字段类型描述
     */
    public static String getDescription(String formType) {
        return getByFormType(formType).getDescription();
    }
    
    /**
     * 获取字段长度（使用默认长度）
     * @param formType 表单字段类型
     * @return 字段长度
     */
    public static Integer getColumnLength(String formType) {
        return getByFormType(formType).getDefaultLength();
    }
    
    /**
     * 获取字段精度
     * @param formType 表单字段类型
     * @return 字段精度
     */
    public static Integer getColumnScale(String formType) {
        return getByFormType(formType).getDefaultScale();
    }
} 