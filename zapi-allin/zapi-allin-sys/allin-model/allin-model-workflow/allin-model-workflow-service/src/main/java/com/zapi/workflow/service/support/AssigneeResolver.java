package com.zapi.workflow.service.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 审批人解析器
 * 负责根据节点配置和流程实例解析实际的审批人
 * 
 * 支持的审批人类型：
 * - FIXED: 固定用户，直接返回配置的用户ID
 * - DYNAMIC: 动态人员，从流程变量中获取审批人ID
 * 
 * 后续可扩展的类型：
 * - ROLE: 角色，根据角色获取用户
 * - DEPT_LEADER: 部门领导
 * - INITIATOR: 流程发起人
 * - FORM_USER: 表单中指定的用户字段
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AssigneeResolver {
    
    private final ObjectMapper objectMapper;
    
    /**
     * 审批人类型常量
     */
    public static class AssigneeType {
        /** 固定用户 */
        public static final String FIXED = "FIXED";
        /** 动态人员（从流程变量获取） - 旧版兼容 */
        public static final String DYNAMIC = "DYNAMIC";
        /** 动态人员（从人员选择组件获取） */
        public static final String DYNAMIC_USER = "DYNAMIC_USER";
        /** 动态部门（从部门选择组件获取） */
        public static final String DYNAMIC_DEPT = "DYNAMIC_DEPT";
        /** 动态角色（从角色选择组件获取） */
        public static final String DYNAMIC_ROLE = "DYNAMIC_ROLE";
        /** 角色（预留扩展） */
        public static final String ROLE = "ROLE";
        /** 部门领导（预留扩展） */
        public static final String DEPT_LEADER = "DEPT_LEADER";
        /** 流程发起人（预留扩展） */
        public static final String INITIATOR = "INITIATOR";
        /** 表单用户字段（预留扩展） */
        public static final String FORM_USER = "FORM_USER";
    }
    
    /**
     * 解析审批人
     * 根据节点配置和流程实例解析实际的审批人ID
     * 
     * @param node 节点配置
     * @param instance 流程实例
     * @return 审批人ID
     */
    public String resolve(NodeDTO node, WfProcessInstance instance) {
        if (node == null) {
            log.warn("节点配置为空，无法解析审批人");
            return null;
        }
        
        String assigneeType = node.getAssigneeType();
        String assigneeConfig = node.getAssignee();
        
        log.debug("解析审批人: 节点={}, 类型={}, 配置={}", node.getName(), assigneeType, assigneeConfig);
        
        // 根据审批人类型进行解析
        String assignee = doResolve(assigneeType, assigneeConfig, instance);
        
        log.debug("审批人解析结果: {}", assignee);
        return assignee;
    }
    
    /**
     * 执行审批人解析
     * 
     * @param assigneeType 审批人类型
     * @param assigneeConfig 审批人配置
     * @param instance 流程实例
     * @return 解析后的审批人ID
     */
    private String doResolve(String assigneeType, String assigneeConfig, WfProcessInstance instance) {
        // 如果没有配置处理人类型，默认为固定用户
        if (assigneeType == null || AssigneeType.FIXED.equals(assigneeType)) {
            return resolveFixed(assigneeConfig);
        }
        
        switch (assigneeType) {
            case AssigneeType.DYNAMIC:
            case AssigneeType.DYNAMIC_USER:
                return resolveDynamicUser(assigneeConfig, instance);
            case AssigneeType.DYNAMIC_DEPT:
                return resolveDynamicDept(assigneeConfig, instance);
            case AssigneeType.DYNAMIC_ROLE:
                return resolveDynamicRole(assigneeConfig, instance);
            case AssigneeType.ROLE:
                return resolveRole(assigneeConfig, instance);
            case AssigneeType.DEPT_LEADER:
                return resolveDeptLeader(assigneeConfig, instance);
            case AssigneeType.INITIATOR:
                return resolveInitiator(instance);
            case AssigneeType.FORM_USER:
                return resolveFormUser(assigneeConfig, instance);
            default:
                log.warn("未知的审批人类型: {}，使用配置值作为默认", assigneeType);
                return assigneeConfig;
        }
    }
    
    /**
     * 解析固定用户
     * 直接返回配置的用户ID
     */
    private String resolveFixed(String assigneeConfig) {
        return assigneeConfig;
    }
    
    /**
     * 解析动态人员（从人员选择组件获取）
     * 从流程变量中获取人员选择组件的值，格式为 [{id, name}]
     */
    private String resolveDynamicUser(String assigneeConfig, WfProcessInstance instance) {
        try {
            if (instance.getVariables() != null && assigneeConfig != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> variables = objectMapper.readValue(instance.getVariables(), Map.class);
                Object assigneeValue = variables.get(assigneeConfig);
                if (assigneeValue != null) {
                    // 解析 [{id, name}] 格式，提取所有用户ID
                    return extractIdsFromJsonArray(assigneeValue);
                }
                log.warn("流程变量中未找到人员选择组件配置: {}", assigneeConfig);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析流程变量获取审批人失败", e);
        }
        return assigneeConfig;
    }
    
    /**
     * 解析动态部门（从部门选择组件获取）
     * 从流程变量中获取部门选择组件的值，格式为 [{id, name}]
     * 返回格式：DEPT:id1,id2,id3（用于后续展开为部门下的用户）
     */
    private String resolveDynamicDept(String assigneeConfig, WfProcessInstance instance) {
        try {
            if (instance.getVariables() != null && assigneeConfig != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> variables = objectMapper.readValue(instance.getVariables(), Map.class);
                Object deptValue = variables.get(assigneeConfig);
                if (deptValue != null) {
                    // 解析 [{id, name}] 格式，提取所有部门ID
                    String deptIds = extractIdsFromJsonArray(deptValue);
                    if (deptIds != null && !deptIds.isEmpty()) {
                        return "DEPT:" + deptIds;
                    }
                }
                log.warn("流程变量中未找到部门选择组件配置: {}", assigneeConfig);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析流程变量获取部门失败", e);
        }
        return assigneeConfig;
    }
    
    /**
     * 解析动态角色（从角色选择组件获取）
     * 从流程变量中获取角色选择组件的值，格式为 [{id, name}]
     * 返回格式：ROLE:id1,id2,id3（用于后续展开为角色下的用户）
     */
    private String resolveDynamicRole(String assigneeConfig, WfProcessInstance instance) {
        try {
            if (instance.getVariables() != null && assigneeConfig != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> variables = objectMapper.readValue(instance.getVariables(), Map.class);
                Object roleValue = variables.get(assigneeConfig);
                if (roleValue != null) {
                    // 解析 [{id, name}] 格式，提取所有角色ID
                    String roleIds = extractIdsFromJsonArray(roleValue);
                    if (roleIds != null && !roleIds.isEmpty()) {
                        return "ROLE:" + roleIds;
                    }
                }
                log.warn("流程变量中未找到角色选择组件配置: {}", assigneeConfig);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析流程变量获取角色失败", e);
        }
        return assigneeConfig;
    }
    
    /**
     * 从JSON数组中提取ID列表
     * 支持格式：
     * - [{id: "1", name: "xxx"}, {id: "2", name: "yyy"}] -> "1,2"
     * - ["1", "2", "3"] -> "1,2,3"
     * - "1,2,3" -> "1,2,3"
     */
    @SuppressWarnings("unchecked")
    private String extractIdsFromJsonArray(Object value) {
        if (value == null) {
            return null;
        }
        
        try {
            // 如果是字符串，尝试解析为JSON
            if (value instanceof String) {
                String strValue = (String) value;
                if (strValue.startsWith("[")) {
                    value = objectMapper.readValue(strValue, java.util.List.class);
                } else {
                    // 可能已经是逗号分隔的ID字符串
                    return strValue;
                }
            }
            
            // 如果是列表
            if (value instanceof java.util.List) {
                java.util.List<?> list = (java.util.List<?>) value;
                if (list.isEmpty()) {
                    return null;
                }
                
                java.util.List<String> ids = new java.util.ArrayList<>();
                for (Object item : list) {
                    if (item instanceof Map) {
                        // {id: "1", name: "xxx"} 格式
                        Map<String, Object> map = (Map<String, Object>) item;
                        Object id = map.get("id");
                        if (id != null) {
                            ids.add(id.toString());
                        }
                    } else {
                        // 直接是ID
                        ids.add(item.toString());
                    }
                }
                return String.join(",", ids);
            }
            
            // 其他情况直接转字符串
            return value.toString();
        } catch (Exception e) {
            log.warn("解析JSON数组失败: {}", e.getMessage());
            return value.toString();
        }
    }
    
    /**
     * 解析动态人员 - 旧版兼容方法
     * 从流程变量中获取审批人ID
     */
    private String resolveDynamic(String assigneeConfig, WfProcessInstance instance) {
        return resolveDynamicUser(assigneeConfig, instance);
    }
    
    /**
     * 解析角色
     * TODO: 根据角色获取用户，需要集成角色服务
     */
    private String resolveRole(String assigneeConfig, WfProcessInstance instance) {
        log.warn("角色类型审批人暂未实现，使用配置值作为默认: {}", assigneeConfig);
        // TODO: 实现角色解析逻辑
        // 1. 根据 assigneeConfig 获取角色ID
        // 2. 查询该角色下的用户
        // 3. 返回用户ID（可能需要处理多用户情况）
        return assigneeConfig;
    }
    
    /**
     * 解析部门领导
     * TODO: 根据发起人所在部门获取部门领导
     */
    private String resolveDeptLeader(String assigneeConfig, WfProcessInstance instance) {
        log.warn("部门领导类型审批人暂未实现，使用配置值作为默认: {}", assigneeConfig);
        // TODO: 实现部门领导解析逻辑
        // 1. 获取流程发起人
        // 2. 查询发起人所在部门
        // 3. 根据 assigneeConfig 确定是直接领导还是上N级领导
        // 4. 返回领导用户ID
        return assigneeConfig;
    }
    
    /**
     * 解析流程发起人
     * 直接返回流程实例的发起人
     */
    private String resolveInitiator(WfProcessInstance instance) {
        if (instance != null && instance.getStartUser() != null) {
            return instance.getStartUser();
        }
        log.warn("无法获取流程发起人");
        return null;
    }
    
    /**
     * 解析表单用户字段
     * TODO: 从表单数据中获取指定字段的用户值
     */
    private String resolveFormUser(String assigneeConfig, WfProcessInstance instance) {
        log.warn("表单用户字段类型审批人暂未实现，使用配置值作为默认: {}", assigneeConfig);
        // TODO: 实现表单用户字段解析逻辑
        // 1. 从流程变量中获取表单数据
        // 2. 根据 assigneeConfig 指定的字段名获取用户ID
        return assigneeConfig;
    }
    
    /**
     * 解析流程变量为Map
     * 工具方法，供外部使用
     */
    public Map<String, Object> parseVariables(WfProcessInstance instance) {
        try {
            if (instance.getVariables() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> variables = objectMapper.readValue(instance.getVariables(), Map.class);
                return variables;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析流程变量失败", e);
        }
        return null;
    }
}
