package com.zapi.workflow.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.workflow.service.model.dto.CcDisplayDTO;
import com.zapi.workflow.service.model.dto.CompletedTaskDisplayDTO;
import com.zapi.workflow.service.model.entity.WfHistoryActivity;

import java.util.List;

/**
 * 流程活动历史服务接口
 * 提供流程历史记录的业务操作功能
 */
public interface WfHistoryActivityService extends IService<WfHistoryActivity> {
    
    /**
     * 记录流程活动开始
     * 当流程进入某个节点时调用
     * 
     * @param processInstanceId 流程实例ID
     * @param nodeId 节点ID
     * @param nodeName 节点名称
     * @param nodeType 节点类型
     * @param operator 操作人
     * @return 创建的历史活动记录
     */
    WfHistoryActivity recordActivityStart(String processInstanceId, String nodeId, 
                                        String nodeName, String nodeType, String operator);
    
    /**
     * 记录流程活动结束
     * 当流程离开某个节点时调用
     * 
     * @param processInstanceId 流程实例ID
     * @param nodeId 节点ID
     * @param operator 操作人
     * @param comment 操作备注
     */
    void recordActivityEnd(String processInstanceId, String nodeId, String operator, String comment);
    
    /**
     * 记录完整的流程活动（开始和结束同时发生）
     * 用于系统自动处理的节点
     * 
     * @param processInstanceId 流程实例ID
     * @param nodeId 节点ID
     * @param nodeName 节点名称
     * @param nodeType 节点类型
     * @param operator 操作人
     * @param comment 操作备注
     * @return 创建的历史活动记录
     */
    WfHistoryActivity recordCompleteActivity(String processInstanceId, String nodeId, 
                                           String nodeName, String nodeType, String operator, String comment);
    
    /**
     * 获取流程实例的所有历史活动
     * 按时间顺序排列
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史活动列表
     */
    List<WfHistoryActivity> getProcessInstanceHistory(String processInstanceId);
    
    /**
     * 获取流程实例的当前活动节点
     * 
     * @param processInstanceId 流程实例ID
     * @return 当前活动的历史记录
     */
    WfHistoryActivity getCurrentActivity(String processInstanceId);
    
    /**
     * 检查节点是否已完成
     * 
     * @param processInstanceId 流程实例ID
     * @param nodeId 节点ID
     * @return 是否已完成
     */
    boolean isNodeCompleted(String processInstanceId, String nodeId);
    
    /**
     * 查询用户抄送记录（增强显示信息）
     * 返回包含流程定义名称、表单信息、发起人名称等完整显示信息的DTO
     * 用于抄送列表展示，减少前端多次接口调用
     * 
     * @param page 分页参数
     * @param ccUser 抄送接收人（用户ID或用户名）
     * @param appId 应用ID（可选，如果传递则查询该应用的记录，否则查询app_id为NULL的通用记录）
     * @return 分页的抄送记录显示DTO列表
     */
    IPage<CcDisplayDTO> getUserCcRecordsWithDisplayInfo(Page<CcDisplayDTO> page, String ccUser, String appId);
    
    /**
     * 查询用户已办任务（增强显示信息）
     * 返回包含流程定义名称、表单信息、发起人名称等完整显示信息的DTO
     * 用于已办任务列表展示，减少前端多次接口调用
     * 
     * @param page 分页参数
     * @param operator 操作人（用户ID或用户名）
     * @param appId 应用ID（可选，如果传递则查询该应用的记录，否则查询app_id为NULL的通用记录）
     * @return 分页的已办任务显示DTO列表
     */
    IPage<CompletedTaskDisplayDTO> getUserCompletedTasksWithDisplayInfo(Page<CompletedTaskDisplayDTO> page, String operator, String appId);
}