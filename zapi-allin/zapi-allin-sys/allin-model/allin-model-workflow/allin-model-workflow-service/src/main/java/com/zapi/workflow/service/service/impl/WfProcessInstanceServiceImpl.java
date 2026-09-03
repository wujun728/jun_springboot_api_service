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
import com.zapi.workflow.service.mapper.WfProcessInstanceMapper;
import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.dto.ProcessInstanceQueryDTO;
import com.zapi.workflow.service.model.dto.ProcessInstanceDisplayDTO;
import com.zapi.workflow.service.model.dto.ProcessInstanceDetailDTO;
import com.zapi.workflow.service.model.dto.StartProcessDTO;
import com.zapi.workflow.service.model.dto.RevokeProcessDTO;
import com.zapi.workflow.service.model.dto.VariableDTO;
import com.zapi.workflow.service.model.entity.*;
import com.zapi.workflow.service.service.WfProcessDefinitionService;
import com.zapi.workflow.service.service.WfProcessDefinitionVersionService;
import com.zapi.workflow.service.service.WfProcessInstanceService;
import com.zapi.workflow.service.service.WfHistoryActivityService;
import com.zapi.workflow.service.service.WfApprovalRecordService;
import com.zapi.workflow.service.common.constant.ProcessConstants;
import com.zapi.workflow.service.mapper.WfTaskMapper;
import com.zapi.workflow.service.support.AssigneeResolver;
import com.zapi.workflow.service.support.NodeHandlerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WfProcessInstanceServiceImpl extends ServiceImpl<WfProcessInstanceMapper, WfProcessInstance>
        implements WfProcessInstanceService {

    private final WfProcessDefinitionService processDefinitionService;
    private final WfProcessDefinitionVersionService processDefinitionVersionService;
    private final ObjectMapper objectMapper;
    private final WfHistoryActivityService historyActivityService;
    private final WfApprovalRecordService approvalRecordService;
    private final WfTaskMapper taskMapper;
    private final UserService userService;
    private final NodeHandlerRegistry nodeHandlerRegistry;
    private final AssigneeResolver assigneeResolver;

    @Override
    @Transactional
    public WfProcessInstance startProcess(StartProcessDTO startProcessDTO) {
        // 1. 获取流程定义
        ProcessDefinitionDTO definitionDTO = processDefinitionService.getDefinitionDTOById(
                startProcessDTO.getProcessDefinitionId());

        // 2. 创建流程定义版本记录（基于当前流程定义创建快照）
        // 构建完整的版本内容，包括 nodes、flows、variables、businessView
        Map<String, Object> versionContent = new HashMap<>();
        versionContent.put("nodes", definitionDTO.getNodes());
        versionContent.put("flows", definitionDTO.getFlows());
        versionContent.put("variables", definitionDTO.getVariables());
        versionContent.put("businessView", definitionDTO.getBusinessView());
        
        String currentJsonDef;
        try {
            currentJsonDef = objectMapper.writeValueAsString(versionContent);
        } catch (JsonProcessingException e) {
            log.error("序列化流程定义JSON失败", e);
            throw new RuntimeException("序列化流程定义失败", e);
        }

        // 检查是否存在相同内容的版本记录（基于processDefId和完整的jsonDef）
        WfProcessDefinitionVersion existingVersion = processDefinitionVersionService
            .getExistingVersionByContent(definitionDTO.getId(), currentJsonDef);

        WfProcessDefinitionVersion versionRecord;
        if (existingVersion != null) {
            // 复用已存在的版本记录
            versionRecord = existingVersion;
        } else {
            // 创建新的版本记录
            versionRecord = new WfProcessDefinitionVersion();
            versionRecord.setProcessDefId(definitionDTO.getId());
            versionRecord.setName(definitionDTO.getName());
            versionRecord.setKey(definitionDTO.getKey());
            versionRecord.setJsonDef(currentJsonDef);
            versionRecord.setStatus(definitionDTO.getBusinessType() != null ? definitionDTO.getBusinessType() : 0);
            versionRecord.setDescription(definitionDTO.getDescription());
            versionRecord.setBusinessType(definitionDTO.getBusinessType());
            versionRecord.setBusinessFormId(definitionDTO.getBusinessFormId());
            versionRecord.setBusinessView(definitionDTO.getBusinessView());
            versionRecord.setSystemFormKey(definitionDTO.getSystemFormKey());
            versionRecord.setPriority(definitionDTO.getPriority());

            // 设置版本号：从当前流程定义获取版本号
            WfProcessDefinition currentProcessDef = processDefinitionService.getById(definitionDTO.getId());
            Integer currentVersion = (currentProcessDef != null && currentProcessDef.getVersion() != null)
                ? currentProcessDef.getVersion() : 1;
            versionRecord.setVersion(currentVersion);

            // 保存版本记录
            processDefinitionVersionService.save(versionRecord);
        }

        // 3. 创建流程实例
        WfProcessInstance instance = new WfProcessInstance();
        instance.setProcessDefId(startProcessDTO.getProcessDefinitionId());
        // 关联流程定义版本记录ID，确保流程实例使用启动时的版本定义
        instance.setProcessDefVersionId(versionRecord.getId());
        instance.setProcessKey(definitionDTO.getKey());
        instance.setBusinessKey(startProcessDTO.getBusinessKey());
        instance.setStatus(1); // 运行中
        instance.setStartUser(startProcessDTO.getStartUser());
        instance.setStartTime(LocalDateTime.now());
        instance.setUpdateTime(LocalDateTime.now());
        
        // 从流程定义中获取应用ID并设置到流程实例
        // 这样流程实例会继承流程定义的应用归属
        WfProcessDefinition processDefinition = processDefinitionService.getById(startProcessDTO.getProcessDefinitionId());
        if (processDefinition != null && processDefinition.getAppId() != null) {
            instance.setAppId(processDefinition.getAppId());
        }

        // 4. 处理流程变量
        try {
            Map<String, Object> variables = startProcessDTO.getVariables();
            instance.setVariables(variables != null ? objectMapper.writeValueAsString(variables) : null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("流程变量JSON序列化失败", e);
        }

        // 5. 找到开始节点
        NodeDTO startNode = definitionDTO.getNodes().stream()
                .filter(node -> "START".equals(node.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("未找到开始节点"));

        // 6. 找到开始节点的流出连线
        String nextNodeId = definitionDTO.getFlows().stream()
                .filter(flow -> startNode.getId().equals(flow.getFrom()))
                .map(flow -> flow.getTo())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("开始节点没有流出连线"));

        // 7. 设置当前节点ID（在插入之前设置，避免数据库NOT NULL约束错误）
        instance.setCurrentNodeId(nextNodeId);

        // 8. 保存流程实例
        baseMapper.insert(instance);

        // 9. 记录开始节点历史
        historyActivityService.recordCompleteActivity(
            instance.getId(),
            startNode.getId(),
            startNode.getName(),
            startNode.getType(),
            startProcessDTO.getStartUser(),
            "流程启动"
        );

        // 10. 处理下一个节点（使用策略模式）
        nodeHandlerRegistry.processNextNode(instance, definitionDTO, nextNodeId);

        return instance;
    }

    /**
     * 解析审批人
     * 委托给 AssigneeResolver 处理
     */
    private String resolveAssignee(NodeDTO node, WfProcessInstance instance) {
        return assigneeResolver.resolve(node, instance);
    }

    @Override
    @Transactional
    public void endProcess(String instanceId, String endNodeId, String operator) {
        WfProcessInstance instance = getById(instanceId);
        if (instance == null) {
            throw new RuntimeException("流程实例不存在: " + instanceId);
        }

        instance.setStatus(2); // 已结束
        instance.setCurrentNodeId(endNodeId);
        instance.setEndTime(LocalDateTime.now());
        instance.setUpdateTime(LocalDateTime.now());
        updateById(instance);
    }

    @Override
    @Transactional
    public void suspendProcess(String instanceId) {
        WfProcessInstance instance = getById(instanceId);
        if (instance == null) {
            throw new RuntimeException("流程实例不存在: " + instanceId);
        }

        instance.setStatus(3); // 已暂停
        instance.setUpdateTime(LocalDateTime.now());
        updateById(instance);
    }

    @Override
    @Transactional
    public void activateProcess(String instanceId) {
        WfProcessInstance instance = getById(instanceId);
        if (instance == null) {
            throw new RuntimeException("流程实例不存在: " + instanceId);
        }

        instance.setStatus(1); // 运行中
        instance.setUpdateTime(LocalDateTime.now());
        updateById(instance);
    }

    @Override
    @Transactional
    public void updateVariables(String instanceId, Map<String, Object> variables) {
        WfProcessInstance instance = getById(instanceId);
        if (instance == null) {
            throw new RuntimeException("流程实例不存在: " + instanceId);
        }

        try {
            // 合并现有变量和新变量
            Map<String, Object> existingVariables = null;
            if (instance.getVariables() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> parsedVariables = objectMapper.readValue(instance.getVariables(), Map.class);
                existingVariables = parsedVariables;
            }

            if (existingVariables == null) {
                existingVariables = new HashMap<>();
            }

            if (variables != null) {
                existingVariables.putAll(variables);
            }

            instance.setVariables(objectMapper.writeValueAsString(existingVariables));
            instance.setUpdateTime(LocalDateTime.now());
            updateById(instance);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("流程变量更新失败", e);
        }
    }

    @Override
    public IPage<WfProcessInstance> pageQuery(Page<WfProcessInstance> page, ProcessInstanceQueryDTO queryDTO) {
        LambdaQueryWrapper<WfProcessInstance> queryWrapper = new LambdaQueryWrapper<>();

        // 按流程定义ID查询
        if (StringUtils.hasText(queryDTO.getProcessDefId())) {
            queryWrapper.eq(WfProcessInstance::getProcessDefId, queryDTO.getProcessDefId());
        }

        // 按流程标识模糊查询
        if (StringUtils.hasText(queryDTO.getProcessKey())) {
            queryWrapper.like(WfProcessInstance::getProcessKey, queryDTO.getProcessKey());
        }

        // 按业务关联ID查询
        if (StringUtils.hasText(queryDTO.getBusinessKey())) {
            queryWrapper.eq(WfProcessInstance::getBusinessKey, queryDTO.getBusinessKey());
        }

        // 按状态查询
        if (queryDTO.getStatus() != null) {
            queryWrapper.eq(WfProcessInstance::getStatus, queryDTO.getStatus());
        }

        // 按发起人查询
        if (StringUtils.hasText(queryDTO.getStartUser())) {
            queryWrapper.eq(WfProcessInstance::getStartUser, queryDTO.getStartUser());
        }

        // 按应用ID查询
        // 如果传递了 appId，则查询该应用的流程实例
        // 如果没有传递 appId，则只查询 app_id 为 NULL 的通用流程实例
        if (StringUtils.hasText(queryDTO.getAppId())) {
            queryWrapper.eq(WfProcessInstance::getAppId, queryDTO.getAppId());
        } else {
            queryWrapper.isNull(WfProcessInstance::getAppId);
        }

        // 按启动时间倒序排列
        queryWrapper.orderByDesc(WfProcessInstance::getStartTime);

        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public IPage<ProcessInstanceDisplayDTO> pageQueryWithDisplayInfo(Page<ProcessInstanceDisplayDTO> page, ProcessInstanceQueryDTO queryDTO) {
        // 1. 先查询流程实例基础信息
        Page<WfProcessInstance> instancePage = new Page<>(page.getCurrent(), page.getSize());
        IPage<WfProcessInstance> instanceResult = pageQuery(instancePage, queryDTO);

        // 2. 转换为显示DTO并填充关联信息
        List<ProcessInstanceDisplayDTO> displayList = instanceResult.getRecords().stream()
                .map(this::convertToDisplayDTO)
                .collect(Collectors.toList());

        // 3. 构建分页结果
        Page<ProcessInstanceDisplayDTO> resultPage = new Page<>(page.getCurrent(), page.getSize(), instanceResult.getTotal());
        resultPage.setRecords(displayList);

        return resultPage;
    }

    /**
     * 转换流程实例为显示DTO
     */
    private ProcessInstanceDisplayDTO convertToDisplayDTO(WfProcessInstance instance) {
        ProcessInstanceDisplayDTO dto = new ProcessInstanceDisplayDTO();

        // 基础信息
        dto.setId(instance.getId());
        dto.setProcessDefId(instance.getProcessDefId());
        dto.setProcessDefVersionId(instance.getProcessDefVersionId());
        dto.setProcessKey(instance.getProcessKey());
        dto.setBusinessKey(instance.getBusinessKey());
        dto.setStartUser(instance.getStartUser());
        dto.setStatus(instance.getStatus());
        dto.setCurrentNodeId(instance.getCurrentNodeId());
        dto.setStartTime(instance.getStartTime());
        dto.setEndTime(instance.getEndTime());
        dto.setVariables(instance.getVariables());

        try {
            // 获取流程定义信息
            ProcessDefinitionDTO definition = processDefinitionService.getDefinitionDTOById(instance.getProcessDefId());
            if (definition != null) {
                // 任务名称：使用流程定义的名称
                dto.setTaskName(definition.getName());

                // 表单名称：根据业务类型判断
                dto.setBusinessType(definition.getBusinessType());
                dto.setSystemFormKey(definition.getSystemFormKey());
                dto.setBusinessFormId(definition.getBusinessFormId());

                if (definition.getBusinessType() == 1) {
                    // 系统表单
                    String formName = definition.getSystemFormKey() != null ?
                        "系统表单(" + definition.getSystemFormKey() + ")" : "系统表单(未配置)";
                    dto.setFormName(formName);
                } else if (definition.getBusinessType() == 2) {
                    // 自定义表单
                    String formName = definition.getBusinessFormId() != null ?
                        "自定义表单(" + definition.getBusinessFormId() + ")" : "自定义表单(未配置)";
                    dto.setFormName(formName);
                } else {
                    dto.setFormName("未配置表单");
                }

                // 优先级：从流程定义获取，默认为普通
                dto.setPriority(definition.getPriority() != null ? definition.getPriority() : 0);

                // 当前节点名称
                if (instance.getCurrentNodeId() != null) {
                    NodeDTO currentNode = definition.getNodes().stream()
                            .filter(node -> instance.getCurrentNodeId().equals(node.getId()))
                            .findFirst()
                            .orElse(null);
                    if (currentNode != null) {
                        dto.setCurrentNodeName(currentNode.getName());
                    }
                }
            } else {
                // 流程定义不存在时的默认值
                dto.setTaskName(instance.getProcessKey());
                dto.setFormName("未配置表单");
                dto.setPriority(0);
            }

            // 获取发起人真实姓名
            dto.setStartUserName(getUserRealName(instance.getStartUser()));

        } catch (Exception e) {
            // 异常时使用默认值
            dto.setTaskName(instance.getProcessKey());
            dto.setFormName("加载失败");
            dto.setStartUserName(getUserRealName(instance.getStartUser()));
            dto.setPriority(0);
        }

        return dto;
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
        }

        // 如果查询失败或没有真实姓名，返回用户ID
        return userId;
    }

    /**
     * 获取流程实例完整详情
     * 一次性获取详情页面需要的所有数据，避免前端多次接口调用
     */
    @Override
    public ProcessInstanceDetailDTO getCompleteDetail(String instanceId) {
        // 1. 获取流程实例基本信息
        WfProcessInstance instance = this.getById(instanceId);
        if (instance == null) {
            throw new RuntimeException("流程实例不存在: " + instanceId);
        }

        ProcessInstanceDetailDTO detailDTO = new ProcessInstanceDetailDTO();

        // 设置基本实例信息
        detailDTO.setId(instance.getId());
        detailDTO.setProcessDefId(instance.getProcessDefId());
        detailDTO.setProcessDefVersionId(instance.getProcessDefVersionId());
        detailDTO.setProcessKey(instance.getProcessKey());
        detailDTO.setBusinessKey(instance.getBusinessKey());
        detailDTO.setStatus(instance.getStatus());
        detailDTO.setStartUser(instance.getStartUser());
        detailDTO.setStartTime(instance.getStartTime());
        detailDTO.setEndTime(instance.getEndTime());
        // 暂不设置variables，等获取流程定义后再过滤

        // 2. 获取流程定义信息
        ProcessDefinitionDTO definition = null;

        // 优先使用版本记录中的定义（确保显示的是实例启动时的版本）
        if (instance.getProcessDefVersionId() != null) {
            WfProcessDefinitionVersion versionRecord = processDefinitionVersionService.getById(instance.getProcessDefVersionId());
            if (versionRecord != null) {
                // 从版本记录构建流程定义DTO
                definition = new ProcessDefinitionDTO();
                definition.setName(versionRecord.getName());
                definition.setKey(versionRecord.getKey());
                definition.setDescription(versionRecord.getDescription());
                definition.setBusinessType(versionRecord.getBusinessType());
                definition.setBusinessFormId(versionRecord.getBusinessFormId());
                definition.setBusinessView(versionRecord.getBusinessView());
                definition.setSystemFormKey(versionRecord.getSystemFormKey());
                definition.setPriority(versionRecord.getPriority());

                // 解析JSON定义
                try {
                    Map<String, Object> jsonMap = objectMapper.readValue(versionRecord.getJsonDef(), Map.class);
                    Object nodesObj = jsonMap.get("nodes");
                    Object flowsObj = jsonMap.get("flows");

                    if (nodesObj instanceof List) {
                        List<NodeDTO> nodes = objectMapper.convertValue(nodesObj,
                            objectMapper.getTypeFactory().constructCollectionType(List.class, NodeDTO.class));
                        definition.setNodes(nodes);
                    }
                    if (flowsObj instanceof List) {
                        List<com.zapi.workflow.service.model.dto.FlowDTO> flows = objectMapper.convertValue(flowsObj,
                            objectMapper.getTypeFactory().constructCollectionType(List.class, com.zapi.workflow.service.model.dto.FlowDTO.class));
                        definition.setFlows(flows);
                    }
                } catch (Exception e) {
                    log.error("解析版本记录中的流程定义JSON失败", e);
                }
            }
        }

        // 如果没有版本记录，回退到当前流程定义
        if (definition == null) {
            definition = processDefinitionService.getDefinitionDTOById(instance.getProcessDefId());
        }

        if (definition != null) {
            ProcessInstanceDetailDTO.ProcessDefinitionInfo defInfo = new ProcessInstanceDetailDTO.ProcessDefinitionInfo();
            defInfo.setId(definition.getId());
            defInfo.setName(definition.getName());
            defInfo.setKey(definition.getKey());
            defInfo.setDescription(definition.getDescription());
            defInfo.setBusinessType(definition.getBusinessType());
            defInfo.setSystemFormKey(definition.getSystemFormKey());
            defInfo.setBusinessFormId(definition.getBusinessFormId());
            defInfo.setBusinessView(definition.getBusinessView());
            defInfo.setPriority(definition.getPriority());
            // 从nodes和flows构建jsonDef，并替换动态人员节点的assignee为实际值
            try {
                if (definition.getNodes() != null && definition.getFlows() != null) {
                    // 复制节点列表，避免修改原对象
                    List<NodeDTO> enrichedNodes = definition.getNodes().stream()
                        .map(node -> {
                            NodeDTO enrichedNode = new NodeDTO();
                            // 复制所有字段
                            enrichedNode.setId(node.getId());
                            enrichedNode.setName(node.getName());
                            enrichedNode.setType(node.getType());
                            enrichedNode.setPosition(node.getPosition());
                            enrichedNode.setPriority(node.getPriority());
                            enrichedNode.setAssigneeType(node.getAssigneeType());
                            enrichedNode.setFormKey(node.getFormKey());
                            enrichedNode.setAllowReject(node.isAllowReject());
                            enrichedNode.setDefaultFlow(node.getDefaultFlow());
                            enrichedNode.setDescription(node.getDescription());
                            enrichedNode.setNotifyType(node.getNotifyType());
                            enrichedNode.setRequireRead(node.getRequireRead());
                            enrichedNode.setStrategy(node.getStrategy());
                            enrichedNode.setBranches(node.getBranches());
                            enrichedNode.setMessageConfig(node.getMessageConfig());

                            // 复制抄送目标和审批目标
                            enrichedNode.setCcTargets(node.getCcTargets());
                            enrichedNode.setApprovalTargets(node.getApprovalTargets());
                            enrichedNode.setApprovalMode(node.getApprovalMode());

                            // 特殊处理：对于动态人员类型的审批节点，解析实际的审批人ID
                            if ("APPROVAL".equals(node.getType()) && "DYNAMIC".equals(node.getAssigneeType())) {
                                // 从流程变量中解析实际的审批人ID
                                String actualAssignee = resolveAssignee(node, instance);
                                enrichedNode.setAssignee(actualAssignee);
                            } else {
                                enrichedNode.setAssignee(node.getAssignee());
                            }

                            return enrichedNode;
                        })
                        .collect(Collectors.toList());

                    Map<String, Object> jsonMap = new HashMap<>();
                    jsonMap.put("nodes", enrichedNodes);
                    jsonMap.put("flows", definition.getFlows());
                    String jsonDef = objectMapper.writeValueAsString(jsonMap);
                    defInfo.setJsonDef(jsonDef);
                } else {
                    // 如果 nodes 或 flows 为空，尝试从原始定义中获取 jsonDef
                    log.warn("流程定义缺少 nodes 或 flows，尝试使用原始 jsonDef");
                    WfProcessDefinition rawDef = processDefinitionService.getById(instance.getProcessDefId());
                    if (rawDef != null && rawDef.getJsonDef() != null) {
                        defInfo.setJsonDef(rawDef.getJsonDef());
                    } else {
                        defInfo.setJsonDef(null);
                    }
                }
            } catch (JsonProcessingException e) {
                // 如果序列化失败，尝试使用原始 jsonDef
                log.error("序列化流程定义失败，尝试使用原始 jsonDef", e);
                WfProcessDefinition rawDef = processDefinitionService.getById(instance.getProcessDefId());
                if (rawDef != null && rawDef.getJsonDef() != null) {
                    defInfo.setJsonDef(rawDef.getJsonDef());
                } else {
                    defInfo.setJsonDef(null);
                }
            }

            // 转换变量定义
            if (definition.getVariables() != null) {
                List<ProcessInstanceDetailDTO.VariableInfo> variables = definition.getVariables().stream()
                    .map(v -> {
                        ProcessInstanceDetailDTO.VariableInfo varInfo = new ProcessInstanceDetailDTO.VariableInfo();
                        varInfo.setName(v.getName());
                        varInfo.setType(v.getType());
                        varInfo.setLabel(v.getLabel());
                        // 将Object类型的defaultValue转换为String
                        varInfo.setDefaultValue(v.getDefaultValue() != null ? v.getDefaultValue().toString() : null);
                        varInfo.setRequired(v.isRequired()); // 注意这里是isRequired()方法
                        return varInfo;
                    })
                    .collect(Collectors.toList());
                defInfo.setVariables(variables);
            }

            detailDTO.setProcessDefinition(defInfo);

            // 设置发起人真实姓名
            detailDTO.setStartUserName(getUserRealName(instance.getStartUser()));

            // 处理流程实例变量
            // 注意：对于业务表单类型（businessType=2），表单配置（formConfig）和表单数据（formData）应该在启动流程时已经保存到 variables 中
            if (instance.getVariables() != null) {
                try {
                    // 解析实例变量JSON
                    Map<String, Object> instanceVars = objectMapper.readValue(instance.getVariables(), new TypeReference<Map<String, Object>>() {});

                    // 创建过滤后的变量Map
                    Map<String, Object> filteredVars = new HashMap<>();

                    // 如果流程定义中有声明的变量，只保留这些变量
                    if (definition.getVariables() != null && !definition.getVariables().isEmpty()) {
                        for (VariableDTO varDef : definition.getVariables()) {
                            String varName = varDef.getName();
                            if (instanceVars.containsKey(varName)) {
                                filteredVars.put(varName, instanceVars.get(varName));
                            }
                        }
                    } else {
                        // 如果流程定义没有声明变量，直接使用所有实例变量
                        filteredVars.putAll(instanceVars);
                    }

                    // 如果是业务表单类型，保留 formConfig 和 formData 字段（即使它们不在流程定义的 variables 中）
                    if (definition.getBusinessType() != null && definition.getBusinessType() == 2) {
                        if (instanceVars.containsKey("formConfig")) {
                            filteredVars.put("formConfig", instanceVars.get("formConfig"));
                        }
                        if (instanceVars.containsKey("formData")) {
                            filteredVars.put("formData", instanceVars.get("formData"));
                        }
                    }

                    // 将过滤后的变量转换为JSON字符串
                    String filteredVarsJson = objectMapper.writeValueAsString(filteredVars);
                    detailDTO.setVariables(filteredVarsJson);
                } catch (JsonProcessingException e) {
                    // 如果解析失败，设置为原始变量或空JSON对象
                    detailDTO.setVariables(instance.getVariables() != null ? instance.getVariables() : "{}");
                }
            } else {
                // 如果实例变量为空，设置为空JSON对象
                detailDTO.setVariables("{}");
            }
        } else {
            // 如果流程定义不存在，设置为实例的原始变量
            detailDTO.setVariables(instance.getVariables() != null ? instance.getVariables() : "{}");
        }

        // 3. 获取任务历史记录
        // 先获取审批记录，用于填充审批人名称和审批方式
        List<WfApprovalRecord> approvalRecords = approvalRecordService.getApprovalRecordsByInstanceId(instanceId);
        Map<String, String> nodeApprovalUserNamesMap = new HashMap<>();
        Map<String, String> nodeApprovalModeMap = new HashMap<>();
        for (WfApprovalRecord record : approvalRecords) {
            nodeApprovalUserNamesMap.put(record.getNodeId(), record.getApprovalUserNames());
            nodeApprovalModeMap.put(record.getNodeId(), record.getApprovalType());
        }

        List<ProcessInstanceDetailDTO.TaskHistoryInfo> taskHistory = historyActivityService.getProcessInstanceHistory(instanceId)
            .stream()
            .map(activity -> {
                ProcessInstanceDetailDTO.TaskHistoryInfo historyInfo = new ProcessInstanceDetailDTO.TaskHistoryInfo();
                historyInfo.setId(activity.getId());
                historyInfo.setTaskId(null); // WfHistoryActivity没有taskId字段，设置为null
                historyInfo.setNodeId(activity.getNodeId());
                historyInfo.setNodeName(activity.getNodeName());
                historyInfo.setNodeType(activity.getNodeType());
                historyInfo.setAssignee(null); // WfHistoryActivity没有assignee字段，设置为null
                historyInfo.setAssigneeName(null); // 对应字段不存在，设置为null
                historyInfo.setOperator(activity.getOperator());
                historyInfo.setOperatorName(getUserRealName(activity.getOperator())); // 获取操作人真实姓名
                historyInfo.setStartTime(activity.getStartTime());
                historyInfo.setEndTime(activity.getEndTime());
                historyInfo.setDuration(activity.getDuration());
                historyInfo.setComment(activity.getComment());
                // 根据endTime是否为空判断状态：已完成(1) 或 进行中(0)
                historyInfo.setStatus(activity.getEndTime() != null ? 1 : 0);

                // 对于审批节点，从审批记录中获取审批人名称和审批方式
                if ("APPROVAL".equals(activity.getNodeType())) {
                    String approvalUserNames = nodeApprovalUserNamesMap.get(activity.getNodeId());
                    historyInfo.setApprovalUserNames(approvalUserNames);
                    String approvalMode = nodeApprovalModeMap.get(activity.getNodeId());
                    historyInfo.setApprovalMode(approvalMode);
                }

                return historyInfo;
            })
            .collect(Collectors.toList());
        detailDTO.setTaskHistory(taskHistory);

        // 4. 构建流程图数据
        ProcessInstanceDetailDTO.ProcessDiagramInfo diagramInfo = new ProcessInstanceDetailDTO.ProcessDiagramInfo();
        diagramInfo.setProcessDefId(instance.getProcessDefId());
        diagramInfo.setProcessDefName(definition != null ? definition.getName() : instance.getProcessKey());
        // 设置流程图的jsonDef
        if (definition != null && definition.getNodes() != null && definition.getFlows() != null) {
            try {
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("nodes", definition.getNodes());
                jsonMap.put("flows", definition.getFlows());
                String jsonDef = objectMapper.writeValueAsString(jsonMap);
                diagramInfo.setJsonDef(jsonDef);
            } catch (JsonProcessingException e) {
                diagramInfo.setJsonDef(null);
            }
        } else {
            diagramInfo.setJsonDef(null);
        }
        diagramInfo.setStatus(instance.getStatus());
        diagramInfo.setCurrentNodeId(instance.getCurrentNodeId());
        diagramInfo.setTaskHistory(taskHistory); // 复用任务历史记录
        diagramInfo.setProcessDefinition(detailDTO.getProcessDefinition()); // 复用流程定义信息
        detailDTO.setDiagramData(diagramInfo);

        return detailDTO;
    }

    @Override
    @Transactional
    public void revokeProcess(RevokeProcessDTO revokeProcessDTO) {
        // 1. 获取流程实例
        WfProcessInstance instance = getById(revokeProcessDTO.getInstanceId());
        if (instance == null) {
            throw new RuntimeException("流程实例不存在: " + revokeProcessDTO.getInstanceId());
        }

        // 2. 检查流程状态，只有运行中的流程才能撤回
        if (!ProcessConstants.InstanceStatus.RUNNING.equals(instance.getStatus())) {
            String statusText = getStatusText(instance.getStatus());
            throw new RuntimeException("流程已" + statusText + "，无法撤回");
        }

        // 3. 检查操作人是否是流程发起人
        if (!instance.getStartUser().equals(revokeProcessDTO.getOperator())) {
            throw new RuntimeException("只有流程发起人才能撤回流程");
        }

        // 4. 更新流程实例状态为已撤回
        instance.setStatus(ProcessConstants.InstanceStatus.REVOKED);
        instance.setEndTime(LocalDateTime.now());
        instance.setUpdateTime(LocalDateTime.now());
        updateById(instance);

        // 5. 取消当前待办任务
        cancelPendingTasks(revokeProcessDTO.getInstanceId());

        // 6. 记录撤回历史
        historyActivityService.recordCompleteActivity(
            instance.getId(),
            instance.getCurrentNodeId(),
            "流程撤回",
            "REVOKE",
            revokeProcessDTO.getOperator(),
            "撤回原因：" + revokeProcessDTO.getReason()
        );

        log.info("流程实例 [{}] 已被用户 [{}] 撤回，原因: {}",
            instance.getId(), revokeProcessDTO.getOperator(), revokeProcessDTO.getReason());
    }

    /**
     * 获取流程状态文本
     */
    private String getStatusText(Integer status) {
        if (status == null) return "未知状态";
        switch (status) {
            case 1: return "运行中";
            case 2: return "结束";
            case 3: return "暂停";
            case 4: return "撤回";
            default: return "未知状态";
        }
    }

    /**
     * 取消流程实例的所有待办任务
     * 直接使用Mapper避免循环依赖
     */
    private void cancelPendingTasks(String instanceId) {
        LambdaQueryWrapper<WfTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WfTask::getProcessInstanceId, instanceId)
                   .eq(WfTask::getStatus, 1); // 1-待处理

        List<WfTask> pendingTasks = taskMapper.selectList(queryWrapper);

        if (pendingTasks != null && !pendingTasks.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            for (WfTask task : pendingTasks) {
                task.setStatus(4); // 4-已取消
                task.setCompleteTime(now);
                task.setComment("流程已被发起人撤回");
                taskMapper.updateById(task);
            }
            log.info("已取消流程实例 [{}] 的 {} 个待办任务", instanceId, pendingTasks.size());
        }
    }

    @Override
    public String getProcessDefVersionId(String instanceId) {
        WfProcessInstance instance = this.getById(instanceId);
        if (instance != null) {
            return instance.getProcessDefVersionId();
        }
        return null;
    }
}
