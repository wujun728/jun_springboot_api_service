package com.zapi.workflow.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.workflow.service.model.dto.CcDisplayDTO;
import com.zapi.workflow.service.model.entity.WfCcRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 流程抄送记录服务接口
 */
public interface WfCcRecordService extends IService<WfCcRecord> {
    
    /**
     * 抄送目标（用于接收前端传入的抄送配置）
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CcTarget {
        private Long id;
        private String type;  // user/dept/role
        private String name;  // 名称（可选，用于显示）
    }
    
    /**
     * 创建抄送记录（一个节点一条记录）
     * 
     * @param processInstanceId 流程实例ID
     * @param processDefinitionId 流程定义ID
     * @param nodeId 节点ID
     * @param nodeName 节点名称
     * @param ccTargets 抄送目标列表
     * @param notifyType 通知方式
     * @param requireRead 是否需要确认阅读
     * @return 抄送记录
     */
    WfCcRecord createCcRecord(
            String processInstanceId,
            String processDefinitionId,
            String nodeId,
            String nodeName,
            List<CcTarget> ccTargets,
            String notifyType,
            Boolean requireRead);
    
    /**
     * 查询用户收到的抄送记录（分页）
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param appId 应用ID（可选，如果传递则查询该应用的记录，否则查询app_id为NULL的通用记录）
     * @return 分页的抄送记录显示DTO列表
     */
    IPage<CcDisplayDTO> getUserCcRecordsForDisplay(
            Page<CcDisplayDTO> page, Long userId, String appId);
    
    /**
     * 查询流程实例的抄送记录
     */
    List<WfCcRecord> getCcRecordsByInstanceId(String processInstanceId);
    
    /**
     * 获取用户未读抄送数量
     */
    long getUnreadCount(Long userId);
    
    /**
     * 展开抄送目标，获取所有实际用户ID
     */
    List<Long> expandCcTargetsToUserIds(List<CcTarget> ccTargets);
    
    /**
     * 获取用户姓名列表（用于显示）
     */
    String getUserNamesString(List<Long> userIds);
}
