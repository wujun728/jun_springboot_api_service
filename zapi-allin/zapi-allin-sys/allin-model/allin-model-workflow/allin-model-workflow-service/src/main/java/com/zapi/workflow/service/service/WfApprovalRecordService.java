package com.zapi.workflow.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.workflow.service.model.entity.WfApprovalRecord;

import java.util.List;

/**
 * 流程审批节点记录服务接口
 */
public interface WfApprovalRecordService extends IService<WfApprovalRecord> {
    
    /**
     * 创建审批记录（一个节点一条记录）
     * 
     * @param processInstanceId 流程实例ID
     * @param processDefinitionId 流程定义ID
     * @param nodeId 节点ID
     * @param nodeName 节点名称
     * @param approvalTargets 审批目标列表（复用CcTarget）
     * @param approvalType 审批方式
     * @return 审批记录
     */
    WfApprovalRecord createApprovalRecord(
            String processInstanceId,
            String processDefinitionId,
            String nodeId,
            String nodeName,
            List<WfCcRecordService.CcTarget> approvalTargets,
            String approvalType);
    
    /**
     * 更新审批记录状态
     * 
     * @param processInstanceId 流程实例ID
     * @param nodeId 节点ID
     * @param status 状态：2-已通过 3-已驳回
     */
    void updateApprovalStatus(String processInstanceId, String nodeId, Integer status);
    
    /**
     * 记录单个审批人的审批结果
     * 
     * @param processInstanceId 流程实例ID
     * @param nodeId 节点ID
     * @param approved 是否通过
     * @return 审批结果：null-继续等待, true-节点通过, false-节点驳回
     */
    Boolean recordApprovalResult(String processInstanceId, String nodeId, boolean approved);
    
    /**
     * 获取审批记录
     */
    WfApprovalRecord getApprovalRecord(String processInstanceId, String nodeId);
    
    /**
     * 查询流程实例的审批记录
     */
    List<WfApprovalRecord> getApprovalRecordsByInstanceId(String processInstanceId);
    
    /**
     * 查询用户待审批的记录
     */
    List<WfApprovalRecord> getUserPendingApprovals(Long userId);
    
    /**
     * 判断是否是多人审批（会签场景）
     * 
     * @param processInstanceId 流程实例ID
     * @param nodeId 节点ID
     * @return true-多人审批, false-单人审批
     */
    boolean isMultiApproval(String processInstanceId, String nodeId);
}
