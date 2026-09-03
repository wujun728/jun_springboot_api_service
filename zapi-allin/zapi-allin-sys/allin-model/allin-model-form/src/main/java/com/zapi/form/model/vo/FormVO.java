package com.zapi.form.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表单显示对象
 *
 * @author zapi
 * @since 2024-09-24
 */
@Data
public class FormVO {

    /**
     * 表单ID
     */
    private String id;

    /**
     * 表单名称
     */
    private String formName;

    /**
     * 表单描述
     */
    private String formDescription;

    /**
     * 表单配置JSON
     */
    private String formConfig;

    /**
     * 表单状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
