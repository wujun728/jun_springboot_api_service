package com.zapi.workflow.service.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 流程节点传输对象
 * 表示流程中的一个节点，包含节点的类型、配置和位置信息
 */
@Data
public class NodeDTO {
    
    /**
     * 节点ID
     * 必填字段，节点的唯一标识符
     * 在整个流程定义中必须唯一
     */
    @NotBlank(message = "节点ID不能为空")
    private String id;
    
    /**
     * 节点名称
     * 必填字段，节点的显示名称
     * 例如：开始、部门经理审批、财务审批、结束
     */
    @NotBlank(message = "节点名称不能为空")
    private String name;
    
    /**
     * 节点描述
     * 可选字段，用于补充节点目的、处理说明等
     */
    private String description;
    
    /**
     * 节点类型
     * 必填字段，定义节点的行为类型
     * START：开始节点 - 流程的起始点，只能有一个
     * APPROVAL：审批节点 - 需要人工审批的节点
     * CONDITIONAL_BRANCH：条件分支节点 - 根据条件选择不同路径
     * CC：抄送节点 - 仅做通知
     * PARALLEL：并行网关节点 - 同时路由到多个分支
     * END：结束节点 - 流程的终止点，可以有多个
     */
    @NotBlank(message = "节点类型不能为空")
    private String type;
    
    /**
     * 节点位置信息
     * 必填字段，用于流程图的可视化显示
     * 包含x、y坐标，便于前端渲染流程图
     */
    @NotNull(message = "节点位置不能为空")
    private PositionDTO position;
    
    // ========== 审批节点特有属性 ==========
    
    /**
     * 审批人分配类型
     * 仅审批节点有效，定义如何确定审批人
     * FIXED：固定审批人 - assignee字段直接指定用户
     * ROLE：角色审批 - assignee字段指定角色，系统自动分配
     * EXPRESSION：表达式 - assignee字段为表达式，动态计算审批人
     */
    private String assigneeType;
    
    /**
     * 审批人配置
     * 仅审批节点有效，根据assigneeType类型有不同含义：
     * FIXED：用户ID或用户名
     * ROLE：角色ID或角色名
     * EXPRESSION：SpEL表达式，例如：${applicant.manager}
     */
    private String assignee;
    
    /**
     * 审批目标列表（支持多人/部门/角色）
     * 仅审批节点有效，当assigneeType为FIXED时使用
     * 格式: [{"id":1,"type":"user","name":"张三"},{"id":2,"type":"dept","name":"研发部"}]
     */
    private List<CcTargetDTO> approvalTargets;
    
    /**
     * 多人审批方式
     * 仅审批节点有效，当有多个审批人时生效
     * ANY：或签 - 任意一人审批通过即可进入下一步
     * ALL：会签 - 所有人都审批通过才能进入下一步
     * 默认为 ANY（或签）
     */
    private String approvalMode = "ANY";
    
    /**
     * 抄送/通知方式
     * 仅抄送节点有效，SYSTEM/EMAIL/SMS/ALL
     */
    private String notifyType;
    
    /**
     * 是否要求抄送对象确认阅读
     * 仅抄送节点有效
     */
    private Boolean requireRead = Boolean.FALSE;
    
    /**
     * 抄送目标列表（支持多人/部门/角色）
     * 仅抄送节点有效
     * 格式: [{"id":1,"type":"user","name":"张三"},{"id":2,"type":"dept","name":"研发部"}]
     */
    private List<CcTargetDTO> ccTargets;
    
    /**
     * 抄送目标DTO
     */
    @Data
    public static class CcTargetDTO {
        /**
         * 目标ID（用户ID/部门ID/角色ID）
         */
        private Long id;
        
        /**
         * 目标类型：user/dept/role
         */
        private String type;
        
        /**
         * 目标名称（用于显示）
         */
        private String name;
    }
    
    /**
     * 表单Key
     * 仅审批节点有效，指定审批时使用的表单
     * 可以是表单ID或表单路径
     */
    private String formKey;
    
    /**
     * 是否允许驳回
     * 仅审批节点有效，默认允许驳回
     * true：审批人可以选择通过或驳回
     * false：审批人只能选择通过
     */
    private boolean allowReject = true;
    
    /**
     * 消息发送配置
     * 仅审批节点有效，配置审批过程中的消息通知
     * 支持待审批、通过、驳回、完成四种时机的独立配置
     */
    private MessageConfigDTO messageConfig;
    
    /**
     * 任务优先级
     * 仅审批节点有效，生成任务时的默认优先级
     * 1：高优先级，2：中优先级，3：低优先级（默认）
     */
    private Integer priority = 2;
    
    // ========== 条件分支节点特有属性 ==========
    
    /**
     * 默认流向
     * 仅条件分支节点有效，当所有条件都不满足时的默认路径
     * 指向某个FlowDTO的ID
     * 确保流程在条件不满足时仍能继续执行
     */
    private String defaultFlow;
    
    // ========== 并行节点特有属性 ==========
    
    /**
     * 并行执行策略
     * ALL：全部分支完成后继续；ANY：任意分支完成即继续
     */
    private String strategy;
    
    /**
     * 并行分支配置
     */
    private List<ParallelBranchDTO> branches;
    
    /**
     * 并行分支定义
     */
    @Data
    public static class ParallelBranchDTO {
        /**
         * 分支ID
         */
        @NotBlank(message = "并行分支ID不能为空")
        private String id;
        
        /**
         * 分支名称
         */
        @NotBlank(message = "并行分支名称不能为空")
        private String name;
        
        /**
         * 分支描述
         */
        private String description;
        
        /**
         * 分支条件
         */
        private String condition;
    }
}
    