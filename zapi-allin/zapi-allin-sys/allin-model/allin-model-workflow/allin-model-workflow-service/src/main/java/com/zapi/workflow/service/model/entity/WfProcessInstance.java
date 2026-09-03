package com.zapi.workflow.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流程实例实体类
 * 对应数据库表：wf_process_instance
 * 记录流程运行时状态和流转信息
 */
@Data
@TableName("wf_process_instance")
public class WfProcessInstance {
    
    /**
     * 流程实例ID
     * 主键，UUID格式
     * 每个流程实例的唯一标识
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**
     * 关联的流程定义ID
     * 外键，指向wf_process_definition.id
     * 标识此实例基于哪个流程定义启动
     */
    private String processDefId;
    
    /**
     * 关联的流程定义版本记录ID
     * 外键，指向wf_process_definition_version.id
     * 标识此实例基于哪个版本的流程定义启动
     * 确保流程实例在运行过程中不受流程定义修改影响
     */
    private String processDefVersionId;
    
    /**
     * 流程标识
     * 冗余字段，来自流程定义的key
     * 便于查询和统计，例如：LEAVE_APPROVAL
     */
    private String processKey;
    
    /**
     * 业务关联ID
     * 可选字段，关联具体的业务数据
     * 例如：订单ID、请假申请ID等
     * 便于在业务系统中查找对应的流程实例
     */
    private String businessKey;
    
    /**
     * 流程实例状态
     * 1：运行中 - 流程正在执行，存在待办任务或等待条件
     * 2：已结束 - 流程正常完成或被终止
     * 3：已暂停 - 流程被暂停，可以恢复执行
     */
    private Integer status;
    
    /**
     * 流程变量
     * JSON格式存储，包含流程执行过程中的所有变量
     * 例如：{"amount": 5000, "approver": "张三", "reason": "项目采购"}
     * 用于条件判断、任务分配等业务逻辑
     */
    private String variables;
    
    /**
     * 当前节点ID
     * 指向流程定义中的节点ID
     * 表示流程当前执行到哪个节点
     * 用于流程跟踪和状态查询
     */
    private String currentNodeId;
    
    /**
     * 流程发起人
     * 启动此流程实例的用户ID或用户名
     * 用于权限控制和流程追溯
     */
    private String startUser;
    
    /**
     * 应用ID
     * 关联的应用标识
     * 用于按应用过滤流程实例，实现多应用隔离
     * 如果为空，表示通用流程实例
     */
    private String appId;
    
    /**
     * 流程启动时间
     * 流程实例创建的时间戳
     * 默认为当前时间
     */
    private LocalDateTime startTime;
    
    /**
     * 流程结束时间
     * 流程完成或终止的时间戳
     * 只有当status=2（已结束）时才有值
     */
    private LocalDateTime endTime;
    
    /**
     * 最后更新时间
     * 流程实例数据最后修改的时间戳
     * 自动更新，用于跟踪流程状态变化
     */
    private LocalDateTime updateTime;
}
    