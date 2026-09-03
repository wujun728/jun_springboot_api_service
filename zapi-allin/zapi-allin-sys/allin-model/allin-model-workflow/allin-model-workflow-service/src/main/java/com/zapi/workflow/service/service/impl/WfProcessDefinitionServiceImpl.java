package com.zapi.workflow.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapi.workflow.service.common.constant.ProcessConstants;
import com.zapi.workflow.service.mapper.WfProcessDefinitionMapper;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionQueryDTO;
import com.zapi.workflow.service.model.entity.WfProcessDefinition;
import com.zapi.workflow.service.model.entity.WfProcessDefinitionVersion;
import com.zapi.workflow.service.service.WfProcessDefinitionService;
import com.zapi.workflow.service.service.WfProcessDefinitionVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WfProcessDefinitionServiceImpl extends ServiceImpl<WfProcessDefinitionMapper, WfProcessDefinition>
        implements WfProcessDefinitionService {

    private final ObjectMapper objectMapper;
    private final WfProcessDefinitionVersionService processDefinitionVersionService;

    @Override
    @Transactional
    public WfProcessDefinition deploy(ProcessDefinitionDTO definitionDTO) {
        // 1. 校验流程定义合法性（宽松验证，允许空流程用于初始化）
        validateProcessDefinitionLoose(definitionDTO);

        // 2. 计算版本号（同一key自动升级版本）
        Integer maxVersion = baseMapper.selectMaxVersionByKey(definitionDTO.getKey());
        int version = maxVersion == null ? 1 : maxVersion + 1;

        // 3. 转换为实体类并保存
        WfProcessDefinition definition = new WfProcessDefinition();
        definition.setName(definitionDTO.getName());
        definition.setKey(definitionDTO.getKey());
        definition.setVersion(version);
        definition.setDescription(definitionDTO.getDescription());
        definition.setAppId(definitionDTO.getAppId());
        definition.setStatus(1); // 默认激活状态
        definition.setCreateTime(LocalDateTime.now());
        definition.setUpdateTime(LocalDateTime.now());

        // 设置业务类型相关字段
        definition.setBusinessType(definitionDTO.getBusinessType() != null ? definitionDTO.getBusinessType() : 1);
        definition.setBusinessFormId(definitionDTO.getBusinessFormId());
        definition.setSystemFormKey(definitionDTO.getSystemFormKey());
        definition.setBusinessView(definitionDTO.getBusinessView());

        try {
            // 4. 将DTO转换为JSON字符串存储
            definition.setJsonDef(objectMapper.writeValueAsString(definitionDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("流程定义JSON序列化失败", e);
        }

        // 5. 保存到数据库
        baseMapper.insert(definition);
        return definition;
    }

    @Override
    public ProcessDefinitionDTO getDefinitionDTOById(String id) {
        WfProcessDefinition definition = baseMapper.selectById(id);
        if (definition == null) {
            throw new RuntimeException("流程定义不存在：" + id);
        }

        try {
            // 从jsonDef反序列化ProcessDefinitionDTO
            ProcessDefinitionDTO dto = objectMapper.readValue(definition.getJsonDef(), ProcessDefinitionDTO.class);

            // 确保基本信息正确设置（从数据库实体中获取最新值）
            dto.setId(definition.getId());
            dto.setName(definition.getName());
            dto.setKey(definition.getKey());
            dto.setDescription(definition.getDescription());
            dto.setAppId(definition.getAppId());

            // 设置业务类型相关字段
            dto.setBusinessType(definition.getBusinessType());
            dto.setBusinessFormId(definition.getBusinessFormId());
            dto.setSystemFormKey(definition.getSystemFormKey());
            dto.setBusinessView(definition.getBusinessView());

            return dto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("流程定义JSON反序列化失败", e);
        }
    }

    @Override
    @Transactional
    public WfProcessDefinition updateDefinition(ProcessDefinitionDTO definitionDTO) {
        // 1. 校验流程定义是否存在
        WfProcessDefinition existingDefinition = baseMapper.selectById(definitionDTO.getId());
        if (existingDefinition == null) {
            throw new RuntimeException("流程定义不存在：" + definitionDTO.getId());
        }

        // 2. 保存当前版本为历史记录
        WfProcessDefinitionVersion versionRecord = new WfProcessDefinitionVersion();
        versionRecord.setProcessDefId(existingDefinition.getId());
        versionRecord.setName(existingDefinition.getName());
        versionRecord.setKey(existingDefinition.getKey());
        versionRecord.setVersion(existingDefinition.getVersion());
        versionRecord.setJsonDef(existingDefinition.getJsonDef());
        versionRecord.setStatus(existingDefinition.getStatus());
        versionRecord.setDescription(existingDefinition.getDescription());
        versionRecord.setBusinessType(existingDefinition.getBusinessType());
        versionRecord.setBusinessFormId(existingDefinition.getBusinessFormId());
        versionRecord.setBusinessView(existingDefinition.getBusinessView());
        versionRecord.setSystemFormKey(existingDefinition.getSystemFormKey());
        versionRecord.setPriority(existingDefinition.getPriority());
        versionRecord.setCreateTime(existingDefinition.getCreateTime());
        versionRecord.setUpdateTime(existingDefinition.getUpdateTime());

        // 保存版本记录
        processDefinitionVersionService.saveVersionRecord(versionRecord);

        // 3. 校验流程定义合法性（宽松验证，允许保存不完整的流程）
        validateProcessDefinitionLoose(definitionDTO);

        // 4. 更新实体信息
        existingDefinition.setName(definitionDTO.getName());
        existingDefinition.setKey(definitionDTO.getKey());
        existingDefinition.setDescription(definitionDTO.getDescription());
        existingDefinition.setAppId(definitionDTO.getAppId());
        existingDefinition.setUpdateTime(LocalDateTime.now());

        // 更新业务类型相关字段
        if (definitionDTO.getBusinessType() != null) {
            existingDefinition.setBusinessType(definitionDTO.getBusinessType());
        }
        existingDefinition.setBusinessFormId(definitionDTO.getBusinessFormId());
        existingDefinition.setSystemFormKey(definitionDTO.getSystemFormKey());
        existingDefinition.setBusinessView(definitionDTO.getBusinessView());

        try {
            // 5. 将DTO转换为JSON字符串存储
            existingDefinition.setJsonDef(objectMapper.writeValueAsString(definitionDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("流程定义JSON序列化失败", e);
        }

        // 6. 更新到数据库
        baseMapper.updateById(existingDefinition);
        return existingDefinition;
    }

    @Override
    @Transactional
    public void updateStatus(String id, Integer status) {
        // 如果是激活操作（status = 1），需要进行严格验证
        if (status != null && status == 1) {
            // 获取流程定义
            ProcessDefinitionDTO definitionDTO = getDefinitionDTOById(id);
            // 进行严格验证，确保流程完整可用
            validateProcessDefinitionStrict(definitionDTO);
        }

        WfProcessDefinition definition = new WfProcessDefinition();
        definition.setId(id);
        definition.setStatus(status);
        definition.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(definition);
    }

    @Override
    public IPage<WfProcessDefinition> pageQuery(Page<WfProcessDefinition> page, ProcessDefinitionQueryDTO queryDTO) {
        LambdaQueryWrapper<WfProcessDefinition> queryWrapper = new LambdaQueryWrapper<>();

        // 按名称模糊查询
        if (StringUtils.hasText(queryDTO.getName())) {
            queryWrapper.like(WfProcessDefinition::getName, queryDTO.getName());
        }

        // 按流程标识模糊查询
        if (StringUtils.hasText(queryDTO.getKey())) {
            queryWrapper.like(WfProcessDefinition::getKey, queryDTO.getKey());
        }

        // 按状态查询
        if (queryDTO.getStatus() != null) {
            queryWrapper.eq(WfProcessDefinition::getStatus, queryDTO.getStatus());
        }

        // 按业务类型查询
        if (queryDTO.getBusinessType() != null) {
            queryWrapper.eq(WfProcessDefinition::getBusinessType, queryDTO.getBusinessType());
        }

        // 按应用ID查询
        // 如果传递了 appId，则查询该应用的流程
        // 如果没有传递 appId，则只查询 app_id 为 NULL 的通用流程
        if (StringUtils.hasText(queryDTO.getAppId())) {
            queryWrapper.eq(WfProcessDefinition::getAppId, queryDTO.getAppId());
        } else {
            queryWrapper.isNull(WfProcessDefinition::getAppId);
        }

        // 按创建时间倒序排列
        queryWrapper.orderByDesc(WfProcessDefinition::getCreateTime);

        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public WfProcessDefinition getByBusinessTypeAndForm(Integer businessType, String systemFormKey, String businessFormId, Integer status) {
        // 参数校验
        if (businessType == null) {
            throw new RuntimeException("业务类型不能为空");
        }

        if (businessType != 1 && businessType != 2) {
            throw new RuntimeException("业务类型只能是1（系统表单）或2（业务表单）");
        }

        // 根据业务类型校验对应的表单标识参数
        if (businessType == 1 && !StringUtils.hasText(systemFormKey)) {
            throw new RuntimeException("系统表单类型时，systemFormKey不能为空");
        }

        if (businessType == 2 && !StringUtils.hasText(businessFormId)) {
            throw new RuntimeException("业务表单类型时，businessFormId不能为空");
        }

        // 构建查询条件
        LambdaQueryWrapper<WfProcessDefinition> queryWrapper = new LambdaQueryWrapper<>();

        // 按业务类型查询
        queryWrapper.eq(WfProcessDefinition::getBusinessType, businessType);

        // 根据业务类型选择对应的表单标识字段
        if (businessType == 1) {
            // 系统表单
            queryWrapper.eq(WfProcessDefinition::getSystemFormKey, systemFormKey);
        } else {
            // 业务表单
            queryWrapper.eq(WfProcessDefinition::getBusinessFormId, businessFormId);
        }

        // 如果指定了状态，则按状态过滤
        if (status != null) {
            queryWrapper.eq(WfProcessDefinition::getStatus, status);
        }

        // 按版本号降序排列，获取最新版本
        queryWrapper.orderByDesc(WfProcessDefinition::getVersion);

        // 查询第一条记录（最新版本）
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public java.util.List<String> getBoundFormIds() {
        LambdaQueryWrapper<WfProcessDefinition> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询业务表单类型
        queryWrapper.eq(WfProcessDefinition::getBusinessType, 2);
        // 只查询businessFormId不为空的
        queryWrapper.isNotNull(WfProcessDefinition::getBusinessFormId);
        // 只选择businessFormId字段
        queryWrapper.select(WfProcessDefinition::getBusinessFormId);

        // 查询列表
        java.util.List<WfProcessDefinition> list = baseMapper.selectList(queryWrapper);

        // 提取ID并去重
        return list.stream()
                .map(WfProcessDefinition::getBusinessFormId)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 宽松校验流程定义（用于创建/初始化）
     * 只检查基本的必填字段，允许空流程或不完整的流程
     */
    private void validateProcessDefinitionLoose(ProcessDefinitionDTO definitionDTO) {
        // 基本字段校验
        if (definitionDTO.getNodes() == null) {
            definitionDTO.setNodes(new java.util.ArrayList<>());
        }
        if (definitionDTO.getFlows() == null) {
            definitionDTO.setFlows(new java.util.ArrayList<>());
        }
        if (definitionDTO.getVariables() == null) {
            definitionDTO.setVariables(new java.util.ArrayList<>());
        }

        // 允许空流程，不做节点数量和类型的强制要求
        // 这样用户可以创建一个空流程，然后在设计器中逐步添加节点
    }

    /**
     * 严格校验流程定义（用于保存/更新）
     * 检查流程的完整性和合法性，确保流程可以正常运行
     */
    private void validateProcessDefinitionStrict(ProcessDefinitionDTO definitionDTO) {
        // 检查节点列表不为空
        if (definitionDTO.getNodes() == null || definitionDTO.getNodes().isEmpty()) {
            throw new RuntimeException("流程定义必须包含至少一个节点");
        }

        // 检查是否有且仅有一个开始节点
        long startNodeCount = definitionDTO.getNodes().stream()
                .filter(node -> "START".equals(node.getType()))
                .count();

        if (startNodeCount != 1) {
            throw new RuntimeException("流程定义必须包含且仅包含一个开始节点");
        }

        // 检查是否至少有一个结束节点
        long endNodeCount = definitionDTO.getNodes().stream()
                .filter(node -> "END".equals(node.getType()))
                .count();

        if (endNodeCount < 1) {
            throw new RuntimeException("流程定义必须至少包含一个结束节点");
        }

        // 节点级校验
        definitionDTO.getNodes().forEach(node -> {
            String nodeType = node.getType();
            if (ProcessConstants.NodeType.APPROVAL.equals(nodeType)) {
                // 检查 approvalTargets（多人审批）或 assignee（单人审批）
                boolean hasApprovalTargets = node.getApprovalTargets() != null && !node.getApprovalTargets().isEmpty();
                boolean hasAssignee = StringUtils.hasText(node.getAssignee());
                if (!hasApprovalTargets && !hasAssignee) {
                    throw new RuntimeException("审批节点[" + node.getName() + "]必须设置处理人");
                }
            } else if (ProcessConstants.NodeType.CC.equals(nodeType)) {
                // 检查 ccTargets（多人抄送）或 assignee（单人抄送）
                boolean hasCcTargets = node.getCcTargets() != null && !node.getCcTargets().isEmpty();
                boolean hasAssignee = StringUtils.hasText(node.getAssignee());
                if (!hasCcTargets && !hasAssignee) {
                    throw new RuntimeException("抄送节点[" + node.getName() + "]必须设置抄送对象");
                }
            } else if (ProcessConstants.NodeType.PARALLEL.equals(nodeType)) {
                if (node.getBranches() == null || node.getBranches().size() < 2) {
                    throw new RuntimeException("并行节点[" + node.getName() + "]至少需要配置两个分支");
                }
                if (node.getBranches().size() > 10) {
                    throw new RuntimeException("并行节点[" + node.getName() + "]最多只支持10个分支");
                }
                Set<String> branchNames = new HashSet<>();
                node.getBranches().forEach(branch -> {
                    if (!branchNames.add(branch.getName())) {
                        throw new RuntimeException("并行节点[" + node.getName() + "]的分支名称不能重复");
                    }
                });
                if (!StringUtils.hasText(node.getStrategy())) {
                    node.setStrategy("ALL");
                }
            }
        });
    }

    /**
     * 校验流程定义的合法性（已废弃，保留用于兼容）
     * @deprecated 使用 validateProcessDefinitionLoose 或 validateProcessDefinitionStrict 替代
     */
    @Deprecated
    private void validateProcessDefinition(ProcessDefinitionDTO definitionDTO) {
        // 默认使用严格验证
        validateProcessDefinitionStrict(definitionDTO);
    }
}
