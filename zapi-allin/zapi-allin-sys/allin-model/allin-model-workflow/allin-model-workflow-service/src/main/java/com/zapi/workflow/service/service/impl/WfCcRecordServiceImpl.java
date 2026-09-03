package com.zapi.workflow.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapi.entity.User;
import com.zapi.service.UserService;
import com.zapi.service.UserOrgService;
import com.zapi.service.UserRoleService;
import com.zapi.workflow.service.mapper.WfCcRecordMapper;
import com.zapi.workflow.service.mapper.WfProcessInstanceMapper;
import com.zapi.workflow.service.model.dto.CcDisplayDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.entity.WfCcRecord;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import com.zapi.workflow.service.service.WfCcRecordService;
import com.zapi.workflow.service.service.WfProcessDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WfCcRecordServiceImpl extends ServiceImpl<WfCcRecordMapper, WfCcRecord> implements WfCcRecordService {

    private final UserService userService;
    private final UserOrgService userOrgService;
    private final UserRoleService userRoleService;
    private final WfProcessInstanceMapper processInstanceMapper;
    private final ObjectMapper objectMapper;
    
    @Autowired
    @Lazy
    private WfProcessDefinitionService processDefinitionService;
    
    public WfCcRecordServiceImpl(UserService userService, 
                                  UserOrgService userOrgService, 
                                  UserRoleService userRoleService,
                                  WfProcessInstanceMapper processInstanceMapper,
                                  ObjectMapper objectMapper) {
        this.userService = userService;
        this.userOrgService = userOrgService;
        this.userRoleService = userRoleService;
        this.processInstanceMapper = processInstanceMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public WfCcRecord createCcRecord(
            String processInstanceId,
            String processDefinitionId,
            String nodeId,
            String nodeName,
            List<CcTarget> ccTargets,
            String notifyType,
            Boolean requireRead) {
        
        if (ccTargets == null || ccTargets.isEmpty()) {
            return null;
        }
        
        // 展开获取所有实际用户ID
        List<Long> userIds = expandCcTargetsToUserIds(ccTargets);
        if (userIds.isEmpty()) {
            log.warn("抄送目标展开后无有效用户, nodeId: {}", nodeId);
            return null;
        }
        
        // 获取用户姓名
        String userNames = getUserNamesString(userIds);
        
        // 为ccTargets补充name字段
        enrichCcTargetsWithNames(ccTargets);
        
        WfCcRecord record = new WfCcRecord();
        record.setProcessInstanceId(processInstanceId);
        record.setProcessDefinitionId(processDefinitionId);
        record.setNodeId(nodeId);
        record.setNodeName(nodeName);
        record.setNotifyType(notifyType != null ? notifyType : "SYSTEM");
        record.setRequireRead(requireRead);
        record.setCcTime(LocalDateTime.now());
        
        // JSON存储
        try {
            record.setCcTargets(objectMapper.writeValueAsString(ccTargets));
            record.setCcUserIds(objectMapper.writeValueAsString(userIds));
        } catch (JsonProcessingException e) {
            log.error("序列化抄送目标失败", e);
        }
        record.setCcUserNames(userNames);
        
        save(record);
        log.info("创建抄送记录成功, 流程实例: {}, 节点: {}, 抄送人数: {}", 
            processInstanceId, nodeName, userIds.size());
        
        return record;
    }
    
    /**
     * 为抄送目标补充名称
     */
    private void enrichCcTargetsWithNames(List<CcTarget> ccTargets) {
        for (CcTarget target : ccTargets) {
            if (target.getName() != null && !target.getName().isEmpty()) {
                continue;
            }
            try {
                switch (target.getType().toLowerCase()) {
                    case "user":
                        User user = userService.getById(target.getId());
                        if (user != null) {
                            target.setName(user.getRealName() != null ? user.getRealName() : user.getUsername());
                        }
                        break;
                    case "dept":
                        // 获取部门名称（根据你的实际接口调整）
                        target.setName("部门" + target.getId());
                        break;
                    case "role":
                        // 获取角色名称（根据你的实际接口调整）
                        target.setName("角色" + target.getId());
                        break;
                }
            } catch (Exception e) {
                log.warn("获取抄送目标名称失败, type: {}, id: {}", target.getType(), target.getId());
            }
        }
    }

    @Override
    public List<Long> expandCcTargetsToUserIds(List<CcTarget> ccTargets) {
        Set<Long> userIds = new HashSet<>();
        
        for (CcTarget target : ccTargets) {
            if (target.getId() == null || target.getType() == null) {
                continue;
            }
            
            try {
                switch (target.getType().toLowerCase()) {
                    case "user":
                        userIds.add(target.getId());
                        break;
                    case "dept":
                        List<Long> deptUserIds = userOrgService.getUserIdsByOrgId(target.getId());
                        if (deptUserIds != null) {
                            userIds.addAll(deptUserIds);
                        }
                        break;
                    case "role":
                        List<Long> roleUserIds = userRoleService.getUserIdsByRoleId(target.getId());
                        if (roleUserIds != null) {
                            userIds.addAll(roleUserIds);
                        }
                        break;
                    default:
                        log.warn("未知的抄送目标类型: {}", target.getType());
                }
            } catch (Exception e) {
                log.error("展开抄送目标失败, type: {}, id: {}", target.getType(), target.getId(), e);
            }
        }
        
        return new ArrayList<>(userIds);
    }

    @Override
    public String getUserNamesString(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return "";
        }
        
        List<String> names = new ArrayList<>();
        for (Long userId : userIds) {
            try {
                User user = userService.getById(userId);
                if (user != null) {
                    names.add(user.getRealName() != null ? user.getRealName() : user.getUsername());
                }
            } catch (Exception e) {
                names.add(String.valueOf(userId));
            }
        }
        
        return String.join("、", names);
    }

    
    private String getUserRealName(Long userId) {
        try {
            User user = userService.getById(userId);
            if (user != null && user.getRealName() != null) {
                return user.getRealName();
            }
        } catch (Exception e) {
            log.warn("获取用户姓名失败, userId: {}", userId);
        }
        return String.valueOf(userId);
    }

    @Override
    public List<WfCcRecord> getCcRecordsByInstanceId(String processInstanceId) {
        LambdaQueryWrapper<WfCcRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WfCcRecord::getProcessInstanceId, processInstanceId)
                   .orderByAsc(WfCcRecord::getCcTime);
        return list(queryWrapper);
    }

    @Override
    public long getUnreadCount(Long userId) {
        LambdaQueryWrapper<WfCcRecord> queryWrapper = new LambdaQueryWrapper<>();
        // cc_user_ids 存储的是字符串数组 ["1", "2", "3"]，需要用 '"2"' 格式查询
        queryWrapper.apply("JSON_CONTAINS(cc_user_ids, {0})", "\"" + userId + "\"");
        return count(queryWrapper);
    }

    @Override
    public IPage<CcDisplayDTO> getUserCcRecordsForDisplay(Page<CcDisplayDTO> page, Long userId, String appId) {
        // 查询包含该用户的抄送记录
        Page<WfCcRecord> recordPage = new Page<>(page.getCurrent(), page.getSize());
        
        log.info("查询抄送记录, userId={}, appId={}", userId, appId);
        
        IPage<WfCcRecord> recordResult;
        if (org.springframework.util.StringUtils.hasText(appId)) {
            // 查询指定应用的抄送记录（数据库层面过滤）
            recordResult = baseMapper.selectUserCcRecordsByAppId(recordPage, userId, appId);
        } else {
            // 查询app_id为NULL的通用抄送记录（数据库层面过滤）
            recordResult = baseMapper.selectUserCcRecordsWithNullAppId(recordPage, userId);
        }
        
        log.info("查询到抄送记录数: {}", recordResult.getTotal());
        
        // 转换为CcDisplayDTO
        List<CcDisplayDTO> displayList = recordResult.getRecords().stream()
                .map(this::convertToCcDisplayDTO)
                .collect(Collectors.toList());
        
        Page<CcDisplayDTO> resultPage = new Page<>(page.getCurrent(), page.getSize(), recordResult.getTotal());
        resultPage.setRecords(displayList);
        
        return resultPage;
    }
    
    /**
     * 转换为CcDisplayDTO（用于WorkflowController接口）
     */
    private CcDisplayDTO convertToCcDisplayDTO(WfCcRecord record) {
        CcDisplayDTO dto = new CcDisplayDTO();
        
        dto.setId(record.getId());
        dto.setProcessInstanceId(record.getProcessInstanceId());
        dto.setProcessDefId(record.getProcessDefinitionId());
        dto.setNodeId(record.getNodeId());
        dto.setNodeName(record.getNodeName());
        dto.setCcUserName(record.getCcUserNames()); // 抄送人姓名列表
        dto.setNotifyType(record.getNotifyType());
        dto.setRequireRead(record.getRequireRead());
        dto.setCcTime(record.getCcTime());
        
        try {
            // 获取流程实例信息
            WfProcessInstance instance = processInstanceMapper.selectById(record.getProcessInstanceId());
            if (instance != null) {
                dto.setStartUser(instance.getStartUser());
                dto.setStartUserName(getUserRealName(Long.parseLong(instance.getStartUser())));
                dto.setProcessStatus(instance.getStatus());
                dto.setBusinessKey(instance.getBusinessKey());
                dto.setProcessStartTime(instance.getStartTime());
                dto.setProcessKey(instance.getProcessKey());
                dto.setAppId(instance.getAppId()); // 设置应用ID
                
                // 获取流程定义信息
                ProcessDefinitionDTO definition = processDefinitionService.getDefinitionDTOById(instance.getProcessDefId());
                if (definition != null) {
                    dto.setProcessName(definition.getName());
                    dto.setBusinessType(definition.getBusinessType());
                    dto.setSystemFormKey(definition.getSystemFormKey());
                    dto.setBusinessFormId(definition.getBusinessFormId());
                    
                    // 设置表单名称
                    if (definition.getBusinessType() != null) {
                        if (definition.getBusinessType() == 1) {
                            dto.setFormName(definition.getSystemFormKey() != null ? 
                                "系统表单(" + definition.getSystemFormKey() + ")" : "系统表单(未配置)");
                        } else if (definition.getBusinessType() == 2) {
                            dto.setFormName(definition.getBusinessFormId() != null ? 
                                "自定义表单(" + definition.getBusinessFormId() + ")" : "自定义表单(未配置)");
                        } else {
                            dto.setFormName("未配置表单");
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("获取抄送记录关联信息失败, recordId: {}", record.getId(), e);
            dto.setStartUserName("未知");
            dto.setFormName("加载失败");
        }
        
        return dto;
    }
}
