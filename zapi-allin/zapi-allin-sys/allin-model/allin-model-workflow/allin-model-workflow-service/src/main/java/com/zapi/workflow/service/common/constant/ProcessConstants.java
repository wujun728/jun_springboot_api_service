package com.zapi.workflow.service.common.constant;

/**
 * 工作流常量定义类
 * 定义工作流系统中使用的所有常量值
 * 基于workflow_db.sql中的表结构和业务规则
 */
public class ProcessConstants {

    /**
     * 节点类型常量
     * 对应NodeDTO.type字段和wf_history_activity.node_type字段
     */
    public static class NodeType {
        /**
         * 开始节点
         * 流程的起始点，每个流程定义只能有一个开始节点
         */
        public static final String START = "START";
        
        /**
         * 审批节点
         * 需要人工审批的节点，会生成待办任务
         */
        public static final String APPROVAL = "APPROVAL";
        
        /**
         * 条件分支节点
         * 根据流程变量的值选择不同的执行路径
         */
        public static final String CONDITIONAL_BRANCH = "CONDITIONAL_BRANCH";
        
        /**
         * 抄送节点
         * 仅做通知，不生成审批任务
         */
        public static final String CC = "CC";
        
        /**
         * 并行网关节点
         * 同时将流程路由到多个分支
         */
        public static final String PARALLEL = "PARALLEL";
        
        /**
         * 结束节点
         * 流程的终止点，可以有多个结束节点
         */
        public static final String END = "END";
    }

    /**
     * 流程实例状态常量
     * 对应wf_process_instance.status字段
     */
    public static class InstanceStatus {
        /**
         * 运行中状态
         * 流程正在执行，存在待办任务或等待条件
         */
        public static final Integer RUNNING = 1;
        
        /**
         * 已结束状态
         * 流程正常完成或被终止
         */
        public static final Integer ENDED = 2;
        
        /**
         * 已暂停状态
         * 流程被暂停，可以恢复执行
         */
        public static final Integer SUSPENDED = 3;
        
        /**
         * 已撤回状态
         * 流程被发起人主动撤回
         */
        public static final Integer REVOKED = 4;
    }

    /**
     * 任务状态常量
     * 对应wf_task.status字段
     */
    public static class TaskStatus {
        /**
         * 待处理状态
         * 任务已创建，等待处理人操作
         */
        public static final Integer PENDING = 1;
        
        /**
         * 已完成状态
         * 任务已被处理人审批通过
         */
        public static final Integer COMPLETED = 2;
        
        /**
         * 已驳回状态
         * 任务被处理人驳回
         */
        public static final Integer REJECTED = 3;
    }

    /**
     * 流程定义状态常量
     * 对应wf_process_definition.status字段
     */
    public static class DefinitionStatus {
        /**
         * 禁用状态
         * 不允许启动新的流程实例
         */
        public static final Integer DISABLED = 0;
        
        /**
         * 激活状态
         * 可以启动新的流程实例
         */
        public static final Integer ACTIVE = 1;
    }

    /**
     * 任务优先级常量
     * 对应wf_task.priority字段
     */
    public static class TaskPriority {
        /**
         * 高优先级
         * 紧急任务，需要优先处理
         */
        public static final Integer HIGH = 1;
        
        /**
         * 中优先级
         * 普通任务，正常处理
         */
        public static final Integer MEDIUM = 2;
        
        /**
         * 低优先级
         * 不紧急任务，可以延后处理
         */
        public static final Integer LOW = 3;
    }

    /**
     * 审批人分配类型常量
     * 对应NodeDTO.assigneeType字段
     */
    public static class AssigneeType {
        /**
         * 固定审批人
         * assignee字段直接指定用户ID或用户名
         */
        public static final String FIXED = "FIXED";
        
        /**
         * 动态人员
         * assignee字段指定流程变量名，运行时从变量中取值
         */
        public static final String DYNAMIC = "DYNAMIC";
        
        /**
         * 角色审批
         * assignee字段指定角色，系统自动分配给该角色的用户
         */
        public static final String ROLE = "ROLE";
        
        /**
         * 表达式分配
         * assignee字段为SpEL表达式，动态计算审批人
         */
        public static final String EXPRESSION = "EXPRESSION";
    }
}
