package com.zapi.workflow.service.model.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 流程连线传输对象
 * 表示流程中节点之间的连接线，定义流程的执行路径
 */
@Data
public class FlowDTO {
    
    /**
     * 连线ID
     * 必填字段，连线的唯一标识符
     * 在整个流程定义中必须唯一
     */
    @NotBlank(message = "连线ID不能为空")
    private String id;
    
    /**
     * 连线名称
     * 可选字段，连线的显示名称
     * 例如：同意、驳回、大于1万、小于1万等
     * 用于流程图的可视化显示
     */
    private String name;
    
    /**
     * 源节点ID
     * 必填字段，连线的起始节点
     * 必须对应NodeDTO中的某个节点ID
     */
    @NotBlank(message = "源节点ID不能为空")
    private String from;
    
    /**
     * 目标节点ID
     * 必填字段，连线的目标节点
     * 必须对应NodeDTO中的某个节点ID
     */
    @NotBlank(message = "目标节点ID不能为空")
    private String to;
    
    /**
     * 执行条件
     * 可选字段，决定是否执行此连线的条件表达式
     * 支持SpEL表达式，例如：
     * - ${amount > 10000}：金额大于1万
     * - ${department == 'finance'}：部门是财务部
     * - ${approved == true}：审批通过
     * 空值表示无条件执行
     */
    private String condition;
}
    