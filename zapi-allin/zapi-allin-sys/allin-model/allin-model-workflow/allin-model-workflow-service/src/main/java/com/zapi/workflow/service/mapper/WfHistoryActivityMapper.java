package com.zapi.workflow.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.workflow.service.model.dto.CcDisplayDTO;
import com.zapi.workflow.service.model.dto.CompletedTaskDisplayDTO;
import com.zapi.workflow.service.model.entity.WfHistoryActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程活动历史 Mapper 接口
 * 提供流程历史记录的数据访问功能
 */
@Mapper
public interface WfHistoryActivityMapper extends BaseMapper<WfHistoryActivity> {
    
    /**
     * 根据流程实例ID查询历史活动
     * 按开始时间正序排列，用于追踪流程流转轨迹
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史活动列表
     */
    List<WfHistoryActivity> selectByProcessInstanceId(@Param("processInstanceId") String processInstanceId);
    
    /**
     * 根据流程实例ID和节点ID查询历史活动
     * 用于检查特定节点的执行历史
     * 
     * @param processInstanceId 流程实例ID
     * @param nodeId 节点ID
     * @return 历史活动列表
     */
    List<WfHistoryActivity> selectByProcessInstanceIdAndNodeId(
            @Param("processInstanceId") String processInstanceId, 
            @Param("nodeId") String nodeId);
    
    /**
     * 查询流程实例的最新活动节点
     * 用于获取流程当前所在的节点
     * 
     * @param processInstanceId 流程实例ID
     * @return 最新的历史活动记录
     */
    WfHistoryActivity selectLatestByProcessInstanceId(@Param("processInstanceId") String processInstanceId);
    
    /**
     * 分页查询用户抄送记录（增强显示信息）
     * 查询抄送给指定用户的CC节点记录，并关联流程实例和流程定义信息
     * 
     * @param page 分页参数
     * @param ccUser 抄送接收人（用户ID或用户名）
     * @return 分页的抄送记录显示DTO列表
     */
    IPage<CcDisplayDTO> selectUserCcRecordsWithDisplayInfo(Page<CcDisplayDTO> page, @Param("ccUser") String ccUser);
    
    /**
     * 分页查询用户抄送记录（按应用ID过滤）
     * 查询抄送给指定用户的CC节点记录，并关联流程实例和流程定义信息，只返回指定应用的记录
     * 
     * @param page 分页参数
     * @param ccUser 抄送接收人（用户ID或用户名）
     * @param appId 应用ID
     * @return 分页的抄送记录显示DTO列表
     */
    IPage<CcDisplayDTO> selectUserCcRecordsWithDisplayInfoByAppId(Page<CcDisplayDTO> page, @Param("ccUser") String ccUser, @Param("appId") String appId);
    
    /**
     * 分页查询用户抄送记录（只查询app_id为NULL的记录）
     * 查询抄送给指定用户的CC节点记录，并关联流程实例和流程定义信息，只返回通用流程的记录
     * 
     * @param page 分页参数
     * @param ccUser 抄送接收人（用户ID或用户名）
     * @return 分页的抄送记录显示DTO列表
     */
    IPage<CcDisplayDTO> selectUserCcRecordsWithDisplayInfoWithNullAppId(Page<CcDisplayDTO> page, @Param("ccUser") String ccUser);
    
    /**
     * 分页查询用户已办任务（增强显示信息）
     * 查询指定用户已完成的审批节点记录，并关联流程实例和流程定义信息
     * 
     * @param page 分页参数
     * @param operator 操作人（用户ID或用户名）
     * @return 分页的已办任务显示DTO列表
     */
    IPage<CompletedTaskDisplayDTO> selectUserCompletedTasksWithDisplayInfo(Page<CompletedTaskDisplayDTO> page, @Param("operator") String operator);
    
    /**
     * 分页查询用户已办任务（按应用ID过滤）
     * 查询指定用户已完成的审批节点记录，并关联流程实例和流程定义信息，只返回指定应用的记录
     * 
     * @param page 分页参数
     * @param operator 操作人（用户ID或用户名）
     * @param appId 应用ID
     * @return 分页的已办任务显示DTO列表
     */
    IPage<CompletedTaskDisplayDTO> selectUserCompletedTasksWithDisplayInfoByAppId(Page<CompletedTaskDisplayDTO> page, @Param("operator") String operator, @Param("appId") String appId);
    
    /**
     * 分页查询用户已办任务（只查询app_id为NULL的记录）
     * 查询指定用户已完成的审批节点记录，并关联流程实例和流程定义信息，只返回通用流程的记录
     * 
     * @param page 分页参数
     * @param operator 操作人（用户ID或用户名）
     * @return 分页的已办任务显示DTO列表
     */
    IPage<CompletedTaskDisplayDTO> selectUserCompletedTasksWithDisplayInfoWithNullAppId(Page<CompletedTaskDisplayDTO> page, @Param("operator") String operator);
}