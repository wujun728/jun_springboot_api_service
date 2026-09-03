package com.zapi.workflow.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapi.workflow.service.mapper.WfApprovalRecordMapper;
import com.zapi.workflow.service.model.entity.WfApprovalRecord;
import com.zapi.workflow.service.service.WfApprovalRecordService;
import com.zapi.workflow.service.service.WfCcRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程审批节点记录服务实现
 */
@Slf4j
@Service
public class WfApprovalRecordServiceImpl extends ServiceImpl<WfApprovalRecordMapper, WfApprovalRecord> 
        implements WfApprovalRecordService {

    private final WfCcRecordService ccRecordService;
    private final ObjectMapper objectMapper;
    
    public WfApprovalRecordServiceImpl(WfCcRecordService ccRecordService, ObjectMapper objectMapper) {
        this.ccRecordService = ccRecordService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public WfApprovalRecord createApprovalRecord(
            String processInstanceId,
            String processDefinitionId,
            String nodeId,
            String nodeName,
            List<WfCcRecordService.CcTarget> approvalTargets,
            String approvalType) {
        
        if (approvalTargets == null || approvalTargets.isEmpty()) {
            return null;
        }
        
        // 展开获取所有实际用户ID（复用抄送的展开逻辑）
        List<Long> userIds = ccRecordService.expandCcTargetsToUserIds(approvalTargets);
        if (userIds.isEmpty()) {
            log.warn("审批目标展开后无有效用户, nodeId: {}", nodeId);
            return null;
        }
        
        // 获取用户姓名
        String userNames = ccRecordService.getUserNamesString(userIds);
        
        WfApprovalRecord record = new WfApprovalRecord();
        record.setProcessInstanceId(processInstanceId);
        record.setProcessDefinitionId(processDefinitionId);
        record.setNodeId(nodeId);
        record.setNodeName(nodeName);
        record.setApprovalType(approvalType != null ? approvalType : "ANY");
        record.setStatus(1); // 进行中
        record.setTotalCount(userIds.size()); // 总审批人数
        record.setApprovedCount(0); // 已通过人数
        record.setRejectedCount(0); // 已驳回人数
        record.setCreateTime(LocalDateTime.now());
        
        // JSON存储 - 注意：userIds 需要转为字符串数组以便 JSON_CONTAINS 查询
        try {
            record.setApprovalTargets(objectMapper.writeValueAsString(approvalTargets));
            // 转为字符串数组 ["1", "2", "3"]
            List<String> userIdStrings = userIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            record.setApprovalUserIds(objectMapper.writeValueAsString(userIdStrings));
        } catch (JsonProcessingException e) {
            log.error("序列化审批目标失败", e);
        }
        record.setApprovalUserNames(userNames);
        
        save(record);
        log.info("创建审批记录成功, 流程实例: {}, 节点: {}, 审批人数: {}, 审批方式: {}", 
            processInstanceId, nodeName, userIds.size(), approvalType);
        
        return record;
    }

    @Override
    @Transactional
    public void updateApprovalStatus(String processInstanceId, String nodeId, Integer status) {
        LambdaUpdateWrapper<WfApprovalRecord> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WfApprovalRecord::getProcessInstanceId, processInstanceId)
                    .eq(WfApprovalRecord::getNodeId, nodeId)
                    .set(WfApprovalRecord::getStatus, status)
                    .set(WfApprovalRecord::getUpdateTime, LocalDateTime.now());
        
        update(updateWrapper);
        log.info("更新审批记录状态: processInstanceId={}, nodeId={}, status={}", 
            processInstanceId, nodeId, status);
    }
    @Override
    public List<WfApprovalRecord> getApprovalRecordsByInstanceId(String processInstanceId) {
        LambdaQueryWrapper<WfApprovalRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WfApprovalRecord::getProcessInstanceId, processInstanceId)
                   .orderByAsc(WfApprovalRecord::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public List<WfApprovalRecord> getUserPendingApprovals(Long userId) {
        LambdaQueryWrapper<WfApprovalRecord> queryWrapper = new LambdaQueryWrapper<>();
        // 查询状态为进行中且包含该用户的审批记录
        queryWrapper.eq(WfApprovalRecord::getStatus, 1)
                   .apply("JSON_CONTAINS(approval_user_ids, {0})", "\"" + userId + "\"")
                   .orderByDesc(WfApprovalRecord::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public WfApprovalRecord getApprovalRecord(String processInstanceId, String nodeId) {
        LambdaQueryWrapper<WfApprovalRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WfApprovalRecord::getProcessInstanceId, processInstanceId)
                   .eq(WfApprovalRecord::getNodeId, nodeId)
                   .last("LIMIT 1");
        return getOne(queryWrapper);
    }

    @Override
    @Transactional
    public Boolean recordApprovalResult(String processInstanceId, String nodeId, boolean approved) {
        WfApprovalRecord record = getApprovalRecord(processInstanceId, nodeId);
        if (record == null) {
            log.warn("审批记录不存在: processInstanceId={}, nodeId={}", processInstanceId, nodeId);
            return null;
        }

        // 更新审批计数
        if (approved) {
            record.setApprovedCount(record.getApprovedCount() + 1);
        } else {
            record.setRejectedCount(record.getRejectedCount() + 1);
        }
        record.setUpdateTime(LocalDateTime.now());

        // 判断审批结果
        String approvalType = record.getApprovalType();
        Integer totalCount = record.getTotalCount();
        Integer approvedCount = record.getApprovedCount();
        Integer rejectedCount = record.getRejectedCount();

        Boolean result = null;

        if ("ALL".equals(approvalType)) {
            // 会签：任一人驳回则节点驳回，所有人通过则节点通过
            if (rejectedCount > 0) {
                record.setStatus(3); // 已驳回
                result = false;
                log.info("会签审批驳回: processInstanceId={}, nodeId={}, 已驳回人数={}", 
                    processInstanceId, nodeId, rejectedCount);
            } else if (approvedCount.equals(totalCount)) {
                record.setStatus(2); // 已通过
                result = true;
                log.info("会签审批通过: processInstanceId={}, nodeId={}, 已通过人数={}/{}", 
                    processInstanceId, nodeId, approvedCount, totalCount);
            }
        } else {
            // 或签（ANY）：任一人通过则节点通过，任一人驳回则节点驳回
            if (approved) {
                record.setStatus(2); // 已通过
                result = true;
                log.info("或签审批通过: processInstanceId={}, nodeId={}, 审批人通过", 
                    processInstanceId, nodeId);
            } else {
                record.setStatus(3); // 已驳回
                result = false;
                log.info("或签审批驳回: processInstanceId={}, nodeId={}, 审批人驳回", 
                    processInstanceId, nodeId);
            }
        }

        updateById(record);
        return result;
    }
    
    @Override
    public boolean isMultiApproval(String processInstanceId, String nodeId) {
        WfApprovalRecord record = getApprovalRecord(processInstanceId, nodeId);
        if (record == null) {
            return false;
        }
        // 如果总审批人数大于1，则是多人审批
        return record.getTotalCount() != null && record.getTotalCount() > 1;
    }
}
