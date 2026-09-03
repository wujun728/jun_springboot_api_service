package com.zapi.form.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/***
 * 表单数据表
 * @author zhonglz
 * @date 2025年08月21日14:58:34
 */
@Data
@TableName("form_table_info")
public class FormTableInfo {

    /****
     * 唯一标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /****
     * 表单模板ID
     */
    @TableField("form_id")
    private String formId;

    /**
     * 数据表名
     */
    @TableField("table_name")
    private String tableName;

    /**
     * 数据表注释
     */
    @TableField("table_comment")
    private String tableComment;
}
