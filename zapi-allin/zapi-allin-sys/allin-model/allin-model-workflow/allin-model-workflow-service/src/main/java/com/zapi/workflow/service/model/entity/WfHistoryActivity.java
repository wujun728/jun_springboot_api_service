package com.zapi.workflow.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流程活动历史实体类
 * 对应数据库表：wf_history_activity
 * 记录流程流转轨迹和每个节点的执行情况
 */
@Data
@TableName("wf_history_activity")
public class WfHistoryActivity {
    
    /**
     * 历史记录ID
     * 主键，UUID格式
     * 每条历史记录的唯一标识
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**
     * 流程实例ID
     * 外键，指向wf_process_instance.id
     * 标识此历史记录属于哪个流程实例
     */
    private String processInstanceId;
    
    /**
     * 节点ID
     * 对应流程定义中的节点ID
     * 记录流程经过的每个节点
     */
    private String nodeId;
    
    /**
     * 节点名称
     * 冗余字段，来自流程定义中的节点名称
     * 便于历史查询和报表展示，例如：部门经理审批
     */
    private String nodeName;
    
    /**
     * 节点类型
     * 记录节点的具体类型，支持以下值：
     * START：开始节点 - 流程的起始点
     * APPROVAL：审批节点 - 需要人工审批的节点
     * CONDITIONAL_BRANCH：条件分支节点 - 根据条件选择不同路径
     * END：结束节点 - 流程的终止点
     */
    private String nodeType;
    
    /**
     * 节点开始时间
     * 流程进入此节点的时间戳
     * 必填字段，用于追踪流程流转时间线
     */
    private LocalDateTime startTime;
    
    /**
     * 节点结束时间
     * 流程离开此节点的时间戳
     * 对于审批节点，表示任务完成时间
     * 对于自动节点，表示处理完成时间
     */
    private LocalDateTime endTime;
    
    /**
     * 节点执行耗时
     * 单位：毫秒
     * 计算公式：endTime - startTime
     * 用于性能分析和流程优化
     */
    private Long duration;
    
    /**
     * 操作人
     * 对于审批节点：实际处理任务的用户
     * 对于自动节点：系统用户或流程发起人
     * 用于审计追踪和责任定位
     */
    private String operator;
    
    /**
     * 操作备注
     * 节点处理时的说明信息
     * 对于审批节点：审批意见
     * 对于系统节点：处理结果描述
     * 最大长度512字符
     */
    private String comment;
}
    