package com.zapi.form.model.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 表单更新请求对象
 *
 * @author zapi
 * @since 2024-09-24
 */
@Data
public class FormUpdateRequest {

    /**
     * 表单ID
     */
    @NotBlank(message = "表单ID不能为空")
    private String id;

    /**
     * 表单名称
     */
    @NotBlank(message = "表单名称不能为空")
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
    @NotNull(message = "表单状态不能为空")
    private Integer status;
}
