package com.zapi.workflow.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工作流任务实体类
 * 对应数据库表：wf_task
 * 审批节点产生的待办任务，需要人工处理
 */
@Data
@TableName("wf_task")
public class WfTask {
    
    /**
     * 任务ID
     * 主键，UUID格式
     * 每个任务的唯一标识
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**
     * 关联的流程实例ID
     * 外键，指向wf_process_instance.id
     * 标识此任务属于哪个流程实例
     */
    private String processInstanceId;
    
    /**
     * 关联的流程定义版本记录ID
     * 外键，指向wf_process_definition_version.id
     * 标识此任务基于哪个版本的流程定义创建
     * 确保任务在执行过程中不受流程定义修改影响
     */
    private String processDefVersionId;
    
    /**
     * 关联的节点ID
     * 对应流程定义中的节点ID
     * 标识此任务由哪个审批节点产生
     */
    private String nodeId;
    
    /**
     * 节点名称
     * 冗余字段，来自流程定义中的节点名称
     * 便于任务列表显示，例如：部门经理审批
     */
    private String nodeName;
    
    /**
     * 任务处理人
     * 分配给此任务的用户ID或用户名
     * 只有此人员可以处理该任务
     * 支持用户、角色、部门等分配方式
     */
    private String assignee;
    
    /**
     * 任务状态
     * 1：待处理 - 任务已创建，等待处理人操作
     * 2：已完成 - 任务已被处理人审批通过
     * 3：已驳回 - 任务被处理人驳回
     */
    private Integer status;
    
    /**
     * 任务优先级
     * 1：高优先级 - 紧急任务，需要优先处理
     * 2：中优先级 - 普通任务
     * 3：低优先级 - 不紧急任务，默认值
     */
    private Integer priority;
    
    /**
     * 任务变量
     * JSON格式存储，包含任务相关的变量信息
     * 例如：{"approvalAmount": 5000, "description": "设备采购"}
     * 用于任务处理时的业务判断和数据传递
     */
    private String variables;
    
    /**
     * 任务创建时间
     * 任务生成的时间戳
     * 默认为当前时间，用于任务排序和统计
     */
    private LocalDateTime createTime;
    
    /**
     * 任务完成时间
     * 任务被处理的时间戳
     * 只有当status=2或3时才有值
     * 用于计算任务处理时长
     */
    private LocalDateTime completeTime;
    
    /**
     * 任务完成人
     * 实际处理此任务的用户ID或用户名
     * 可能与assignee不同（代理处理等情况）
     * 用于审计和责任追溯
     */
    private String completeUser;
    
    /**
     * 处理意见
     * 任务处理时填写的审批意见或备注
     * 最大长度512字符
     * 记录审批人的决策依据和说明
     */
    private String comment;
}
    