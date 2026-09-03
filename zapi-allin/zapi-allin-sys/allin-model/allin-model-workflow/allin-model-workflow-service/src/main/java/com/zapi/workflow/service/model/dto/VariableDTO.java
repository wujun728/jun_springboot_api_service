package com.zapi.workflow.service.model.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 流程变量定义传输对象
 * 用于定义流程中使用的变量信息
 */
@Data
public class VariableDTO {
    
    /**
     * 变量名称
     * 必填字段，变量的标识符
     * 用于在表达式和代码中引用，例如：amount、applicant、department
     * 命名规则：字母开头，可包含字母、数字、下划线
     */
    @NotBlank(message = "变量名称不能为空")
    private String name;
    
    /**
     * 变量类型
     * 必填字段，定义变量的数据类型
     * 支持的类型：
     * - string：字符串类型
     * - number：数值类型（整数或小数）
     * - boolean：布尔类型（true/false）
     * - date：日期类型
     * - object：对象类型（JSON格式）
     */
    @NotBlank(message = "变量类型不能为空")
    private String type;
    
    /**
     * 变量显示标签
     * 可选字段，变量的中文名称或描述
     * 用于前端表单显示，例如：申请金额、申请人、所属部门
     */
    private String label;
    
    /**
     * 是否必填
     * 默认值：false（非必填）
     * true：此变量在流程启动时必须提供值
     * false：此变量可以为空或使用默认值
     */
    private boolean required = false;
    
    /**
     * 默认值
     * 可选字段，变量的初始值
     * 类型必须与type字段定义的类型一致
     * 当流程启动时未提供此变量值时使用
     */
    private Object defaultValue;
}
    