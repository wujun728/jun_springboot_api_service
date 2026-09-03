package com.zapi.form.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表字段信息数据传输对象
 * 
 * @author zhonglz
 * @date 2025年08月21日14:58:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormColumnInfoDTO {
    
    /**
     * 字段名
     */
    private String columnName;
    
    /**
     * 字段类型
     */
    private String columnType;
    
    /**
     * 表单字段类型
     */
    private String formType;

    /**
     * 字段注释
     */
    private String columnComment;
    
    /**
     * 字段长度/精度
     */
    private Integer columnLength;
    
    /**
     * 小数位数（用于decimal类型）
     */
    private Integer columnScale;
    
    /**
     * 是否可为空
     */
    private Boolean isNullable;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 是否为主键
     */
    private Boolean isPrimaryKey;
    
    /**
     * 是否为自增字段
     */
    private Boolean isAutoIncrement;
    
    /**
     * 字符集
     */
    private String characterSet;
    
    /**
     * 排序规则
     */
    private String collation;
    
    /**
     * 字段额外属性（如：on update current_timestamp）
     */
    private String extra;
    

    /**
     * 转换为 Map 格式（用于兼容现有接口）
     * @return Map 格式的字段信息
     */
    public java.util.Map<String, Object> toMap() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("columnName", columnName);
        map.put("columnType", columnType);
        map.put("formType", formType);
        map.put("columnLength", columnLength);
        map.put("columnScale", columnScale);
        map.put("isNullable", isNullable);
        map.put("defaultValue", defaultValue);
        map.put("columnComment", columnComment);
        map.put("isPrimaryKey", isPrimaryKey);
        map.put("isAutoIncrement", isAutoIncrement);
        map.put("characterSet", characterSet);
        map.put("collation", collation);
        map.put("extra", extra);
        return map;
    }

} 