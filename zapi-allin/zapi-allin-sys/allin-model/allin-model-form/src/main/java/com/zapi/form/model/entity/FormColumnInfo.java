package com.zapi.form.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 表单数据表字段
 * @author zhonglz
 * @date 2025年08月21日14:58:34
 */
@Data
@TableName("form_column_info")
public class FormColumnInfo {

    /****
     * 唯一标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 字段名
     */
    private String columnName;
    
    /**
     * 字段类型(DB)
     */
    private String columnType;

    /****
     * 表单字段类型
     */
    private String formType;

    /****
     * 是否系统字段 0不是，1是的
     */
    private Integer isSysField;

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
     * 表单ID（关联表单模板）
     */
    private String formId;

} 