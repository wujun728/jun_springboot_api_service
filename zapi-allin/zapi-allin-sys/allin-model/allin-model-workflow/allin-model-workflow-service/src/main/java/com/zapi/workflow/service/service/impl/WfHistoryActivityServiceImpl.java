package com.zapi.workflow.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.User;
import com.zapi.service.UserService;
import com.zapi.workflow.service.mapper.WfHistoryActivityMapper;
import com.zapi.workflow.service.model.dto.CcDisplayDTO;
import com.zapi.workflow.service.model.dto.CompletedTaskDisplayDTO;
import com.zapi.workflow.service.model.entity.WfHistoryActivity;
import com.zapi.workflow.service.service.WfCcRecordService;
import com.zapi.workflow.service.service.WfHistoryActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程活动历史服务实现类
 * 实现流程历史记录的具体业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WfHistoryActivityServiceImpl extends ServiceImpl<WfHistoryActivityMapper, WfHistoryActivity> 
        implements WfHistoryActivityService {

    private final UserService userService;
    
    @Autowired
    @Lazy
    private WfCcRecordService ccRecordService;

    @Override
    public WfHistoryActivity recordActivityStart(String processInstanceId, String nodeId, 
                                               String nodeName, String nodeType, String operator) {
        log.info("记录流程活动开始: processInstanceId={}, nodeId={}, nodeType={}", 
                processInstanceId, nodeId, nodeType);
        
        WfHistoryActivity activity = new WfHistoryActivity();
        activity.setProcessInstanceId(processInstanceId);
        activity.setNodeId(nodeId);
        activity.setNodeName(nodeName);
        activity.setNodeType(nodeType);
        activity.setStartTime(LocalDateTime.now());
        activity.setOperator(operator);
        
        baseMapper.insert(activity);
        
        log.info("流程活动开始记录完成: activityId={}", activity.getId());
        return activity;
    }

    @Override
    public void recordActivityEnd(String processInstanceId, String nodeId, String operator, String comment) {
        log.info("记录流程活动结束: processInstanceId={}, nodeId={}", processInstanceId, nodeId);
        
        // 查找对应的开始记录
        LambdaQueryWrapper<WfHistoryActivity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WfHistoryActivity::getProcessInstanceId, processInstanceId)
                   .eq(WfHistoryActivity::getNodeId, nodeId)
                   .isNull(WfHistoryActivity::getEndTime)
                   .orderByDesc(WfHistoryActivity::getStartTime)
                   .last("LIMIT 1");
        
        WfHistoryActivity activity = baseMapper.selectOne(queryWrapper);
        
        if (activity != null) {
            LocalDateTime endTime = LocalDateTime.now();
            activity.setEndTime(endTime);
            activity.setOperator(operator);
            activity.setComment(comment);
            
            // 计算耗时（毫秒）
            if (activity.getStartTime() != null) {
                long duration = java.time.Duration.between(activity.getStartTime(), endTime).toMillis();
                activity.setDuration(duration);
            }
            
            baseMapper.updateById(activity);
            log.info("流程活动结束记录完成: activityId={}, duration={}ms", activity.getId(), activity.getDuration());
        } else {
            log.warn("未找到对应的活动开始记录: processInstanceId={}, nodeId={}", processInstanceId, nodeId);
        }
    }

    @Override
    public WfHistoryActivity recordCompleteActivity(String processInstanceId, String nodeId, 
                                                  String nodeName, String nodeType, String operator, String comment) {
        log.info("记录完整流程活动: processInstanceId={}, nodeId={}, nodeType={}", 
                processInstanceId, nodeId, nodeType);
        
        LocalDateTime now = LocalDateTime.now();
        
        WfHistoryActivity activity = new WfHistoryActivity();
        activity.setProcessInstanceId(processInstanceId);
        activity.setNodeId(nodeId);
        activity.setNodeName(nodeName);
        activity.setNodeType(nodeType);
        activity.setStartTime(now);
        activity.setEndTime(now);
        activity.setDuration(0L); // 瞬时完成
        activity.setOperator(operator);
        activity.setComment(comment);
        
        baseMapper.insert(activity);
        
        log.info("完整流程活动记录完成: activityId={}", activity.getId());
        return activity;
    }

    @Override
    public List<WfHistoryActivity> getProcessInstanceHistory(String processInstanceId) {
        log.debug("获取流程实例历史: processInstanceId={}", processInstanceId);
        return baseMapper.selectByProcessInstanceId(processInstanceId);
    }

    @Override
    public WfHistoryActivity getCurrentActivity(String processInstanceId) {
        log.debug("获取流程实例当前活动: processInstanceId={}", processInstanceId);
        return baseMapper.selectLatestByProcessInstanceId(processInstanceId);
    }

    @Override
    public boolean isNodeCompleted(String processInstanceId, String nodeId) {
        LambdaQueryWrapper<WfHistoryActivity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WfHistoryActivity::getProcessInstanceId, processInstanceId)
                   .eq(WfHistoryActivity::getNodeId, nodeId)
                   .isNotNull(WfHistoryActivity::getEndTime);
        
        List<WfHistoryActivity> activities = baseMapper.selectList(queryWrapper);
        boolean completed = !activities.isEmpty();
        
        log.debug("检查节点完成状态: processInstanceId={}, nodeId={}, completed={}", 
                processInstanceId, nodeId, completed);
        
        return completed;
    }

    @Override
    public IPage<CcDisplayDTO> getUserCcRecordsWithDisplayInfo(Page<CcDisplayDTO> page, String ccUser, String appId) {
        log.info("查询用户抄送记录: ccUser={}, appId={}, pageNum={}, pageSize={}", 
                ccUser, appId, page.getCurrent(), page.getSize());
        
        // 优先从新的wf_cc_record表查询
        try {
            Long userId = Long.parseLong(ccUser);
            log.info("开始从wf_cc_record表查询, userId={}, appId={}", userId, appId);
            IPage<CcDisplayDTO> newResult = ccRecordService.getUserCcRecordsForDisplay(page, userId, appId);
            log.info("wf_cc_record表查询结果: total={}", newResult.getTotal());
            
            // 如果新表有数据，直接返回
            if (newResult.getTotal() > 0) {
                log.info("从wf_cc_record表查询到抄送记录: ccUser={}, appId={}, total={}", ccUser, appId, newResult.getTotal());
                return newResult;
            }
            log.info("wf_cc_record表无数据，回退到旧表查询");
        } catch (NumberFormatException e) {
            log.warn("用户ID格式错误: ccUser={}", ccUser);
        } catch (Exception e) {
            log.error("从wf_cc_record表查询失败，尝试从历史表查询: {}", e.getMessage(), e);
        }
        
        // 兼容旧数据：从wf_history_activity表查询（通过comment字段匹配）
        log.debug("从wf_history_activity表查询旧的抄送记录");
        
        // 获取用户姓名用于匹配（因为comment中存的是姓名而不是ID）
        String userNameForMatch = getUserRealName(ccUser);
        IPage<CcDisplayDTO> result;
        
        if (org.springframework.util.StringUtils.hasText(appId)) {
            result = baseMapper.selectUserCcRecordsWithDisplayInfoByAppId(page, userNameForMatch, appId);
        } else {
            result = baseMapper.selectUserCcRecordsWithDisplayInfoWithNullAppId(page, userNameForMatch);
        }
        
        // 填充用户名称和表单名称
        List<CcDisplayDTO> displayList = result.getRecords().stream()
                .map(this::enrichCcDisplayDTO)
                .collect(Collectors.toList());
        
        result.setRecords(displayList);
        
        log.info("查询用户抄送记录完成: ccUser={}, appId={}, total={}", ccUser, appId, result.getTotal());
        return result;
    }
    
    /**
     * 丰富抄送显示DTO信息（兼容旧数据）
     */
    private CcDisplayDTO enrichCcDisplayDTO(CcDisplayDTO dto) {
        try {
            // 设置抄送接收人（从comment中提取）
            String ccUserFromComment = extractCcUserFromComment(dto.getComment());
            if (ccUserFromComment != null) {
                dto.setCcUser(ccUserFromComment);
                // 如果提取的是姓名（不是纯数字），直接使用
                try {
                    Long.parseLong(ccUserFromComment);
                    dto.setCcUserName(getUserRealName(ccUserFromComment));
                } catch (NumberFormatException e) {
                    // 已经是姓名了
                    dto.setCcUserName(ccUserFromComment);
                }
            }
            
            // 获取发起人真实姓名
            dto.setStartUserName(getUserRealName(dto.getStartUser()));
            
            // 设置表单名称
            if (dto.getBusinessType() != null) {
                if (dto.getBusinessType() == 1) {
                    String formName = dto.getSystemFormKey() != null ? 
                        "系统表单(" + dto.getSystemFormKey() + ")" : "系统表单(未配置)";
                    dto.setFormName(formName);
                } else if (dto.getBusinessType() == 2) {
                    String formName = dto.getBusinessFormId() != null ? 
                        "自定义表单(" + dto.getBusinessFormId() + ")" : "自定义表单(未配置)";
                    dto.setFormName(formName);
                } else {
                    dto.setFormName("未配置表单");
                }
            } else {
                dto.setFormName("未配置表单");
            }
            
            // 从comment中提取通知方式
            extractNotifyInfoFromComment(dto);
            
        } catch (Exception e) {
            log.warn("丰富抄送显示DTO信息失败: ccRecordId={}, error={}", dto.getId(), e.getMessage());
            dto.setStartUserName("未知");
            dto.setCcUserName("未知");
            dto.setFormName("加载失败");
        }
        
        return dto;
    }
    
    /**
     * 从comment中提取抄送接收人
     */
    private String extractCcUserFromComment(String comment) {
        if (comment == null || comment.isEmpty()) {
            return null;
        }
        
        // 匹配 "接收人: xxx" 格式，xxx可能是姓名列表如"张三、李四、王五"
        int startIndex = comment.indexOf("接收人: ");
        if (startIndex != -1) {
            startIndex += "接收人: ".length();
            int endIndex = comment.indexOf("，", startIndex);
            if (endIndex == -1) {
                endIndex = comment.indexOf(",", startIndex);
            }
            if (endIndex == -1) {
                endIndex = comment.length();
            }
            return comment.substring(startIndex, endIndex).trim();
        }
        
        return null;
    }
    
    /**
     * 从comment中提取通知方式和是否需要确认阅读
     */
    private void extractNotifyInfoFromComment(CcDisplayDTO dto) {
        String comment = dto.getComment();
        if (comment == null || comment.isEmpty()) {
            return;
        }
        
        // 提取通知方式
        int notifyIndex = comment.indexOf("方式: ");
        if (notifyIndex != -1) {
            notifyIndex += "方式: ".length();
            int endIndex = comment.indexOf("，", notifyIndex);
            if (endIndex == -1) {
                endIndex = comment.indexOf(",", notifyIndex);
            }
            if (endIndex == -1) {
                endIndex = comment.length();
            }
            dto.setNotifyType(comment.substring(notifyIndex, endIndex).trim());
        }
        
        // 检查是否需要确认阅读
        dto.setRequireRead(comment.contains("需确认阅读"));
    }
    
    /**
     * 根据用户ID获取真实姓名
     */
    private String getUserRealName(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return "未知用户";
        }
        
        try {
            // 尝试将userId转换为Long类型
            Long userIdLong = Long.parseLong(userId);
            User user = userService.getById(userIdLong);
            if (user != null && user.getRealName() != null && !user.getRealName().trim().isEmpty()) {
                return user.getRealName();
            }
        } catch (NumberFormatException e) {
            // 如果userId不是数字，尝试通过用户名查询
            User user = userService.findByUsername(userId);
            if (user != null && user.getRealName() != null && !user.getRealName().trim().isEmpty()) {
                return user.getRealName();
            }
        } catch (Exception e) {
            // 查询失败，返回用户ID
            log.debug("查询用户真实姓名失败: userId={}, error={}", userId, e.getMessage());
        }
        
        // 如果查询失败或没有真实姓名，返回用户ID
        return userId;
    }
    
    @Override
    public IPage<CompletedTaskDisplayDTO> getUserCompletedTasksWithDisplayInfo(Page<CompletedTaskDisplayDTO> page, String operator, String appId) {
        log.debug("查询用户已办任务: operator={}, appId={}, pageNum={}, pageSize={}", 
                operator, appId, page.getCurrent(), page.getSize());
        
        // 1. 查询已办任务
        IPage<CompletedTaskDisplayDTO> result;
        if (org.springframework.util.StringUtils.hasText(appId)) {
            result = baseMapper.selectUserCompletedTasksWithDisplayInfoByAppId(page, operator, appId);
        } else {
            result = baseMapper.selectUserCompletedTasksWithDisplayInfoWithNullAppId(page, operator);
        }
        
        // 2. 填充用户名称和表单名称
        List<CompletedTaskDisplayDTO> displayList = result.getRecords().stream()
                .map(this::enrichCompletedTaskDisplayDTO)
                .collect(Collectors.toList());
        
        result.setRecords(displayList);
        
        log.info("查询用户已办任务完成: operator={}, appId={}, total={}", operator, appId, result.getTotal());
        return result;
    }
    
    /**
     * 丰富已办任务显示DTO信息
     */
    private CompletedTaskDisplayDTO enrichCompletedTaskDisplayDTO(CompletedTaskDisplayDTO dto) {
        try {
            // 获取发起人真实姓名
            dto.setStartUserName(getUserRealName(dto.getStartUser()));
            
            // 获取操作人真实姓名
            dto.setOperatorName(getUserRealName(dto.getOperator()));
            
            // 设置表单名称
            if (dto.getBusinessType() != null) {
                if (dto.getBusinessType() == 1) {
                    // 系统表单
                    String formName = dto.getSystemFormKey() != null ? 
                        "系统表单(" + dto.getSystemFormKey() + ")" : "系统表单(未配置)";
                    dto.setFormName(formName);
                } else if (dto.getBusinessType() == 2) {
                    // 自定义表单
                    String formName = dto.getBusinessFormId() != null ? 
                        "自定义表单(" + dto.getBusinessFormId() + ")" : "自定义表单(未配置)";
                    dto.setFormName(formName);
                } else {
                    dto.setFormName("未配置表单");
                }
            } else {
                dto.setFormName("未配置表单");
            }
            
        } catch (Exception e) {
            log.warn("丰富已办任务显示DTO信息失败: taskId={}, error={}", dto.getId(), e.getMessage());
            dto.setStartUserName("未知");
            dto.setOperatorName("未知");
            dto.setFormName("加载失败");
        }
        
        return dto;
    }
}