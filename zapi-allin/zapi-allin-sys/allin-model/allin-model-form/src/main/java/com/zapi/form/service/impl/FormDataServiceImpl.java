package com.zapi.form.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.zapi.constants.DataBaseConstans;
import com.zapi.form.mapper.FormDataMapper;
import com.zapi.form.model.dto.*;
import com.zapi.form.model.entity.FormColumnInfo;
import com.zapi.form.model.entity.FormTableInfo;
import com.zapi.form.model.vo.BusinessDataPageVO;
import com.zapi.form.service.DataBaseService;
import com.zapi.form.service.FormColumnInfoService;
import com.zapi.form.service.FormDataService;
import com.zapi.form.service.FormTableInfoService;
import com.zapi.workflow.service.model.dto.StartProcessDTO;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import com.zapi.workflow.service.service.WfProcessInstanceService;
import com.zapi.workflow.service.model.entity.WfProcessDefinition;
import com.zapi.workflow.service.service.WfProcessDefinitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务数据通用服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FormDataServiceImpl implements FormDataService {

	@Autowired
	private  FormTableInfoService formTableInfoService;
	@Autowired
	private FormDataMapper formDataMapper;
	@Autowired
	private  FormColumnInfoService formColumnInfoService;

	@Autowired
	private FormServiceImpl formService;
	
	@Autowired
	private DataBaseService dataBaseService;

	@Autowired
	private WfProcessDefinitionService wfProcessDefinitionService;

	/**
	 * 流程实例服务
	 * 负责流程运行时的管理
	 */
	@Autowired
	private WfProcessInstanceService processInstanceService;


	private String resolveBusinessTableName(String formId) {
		FormTableInfo tableInfo = formTableInfoService.getByFormId(formId);
		if (tableInfo == null) {
			throw new IllegalArgumentException("表单未配置业务表: formId=" + formId);
		}
		return DataBaseConstans.BUSINESS_PREFIX + tableInfo.getTableName();
	}

	private List<Map<String, Object>> asEntries(Map<String, Object> data) {
		if (data == null || data.isEmpty()) {
			return Collections.emptyList();
		}
		// MyBatis 中遍历 Map 时顺序不保证，这里直接传 Map 列表即可
		return Collections.singletonList(data);
	}

	private List<Map<String, Object>> criteriaEntries(Map<String, Object> criteria) {
		if (criteria == null) {
			return Collections.emptyList();
		}
		return criteria.entrySet().stream()
			.map(e -> {
				Map<String, Object> m = new HashMap<>(2);
				m.put("key", e.getKey());
				m.put("value", e.getValue());
				return m;
			}).collect(Collectors.toList());
	}

	/**
	 * 安全获取操作用户ID
	 * 使用公共工具类 UserContext 获取当前登录用户ID
	 * 在开放接口/匿名请求中，兜底返回 "external_api"
	 */
	private String resolveOperatorUsername() {
		String userId = com.zapi.util.UserContext.getCurrentUserIdStr();
		return userId != null ? userId : "external_api";
	}
	


	@Override
	public Long insert(String formId, Map<String, Object> data) {
		String tableName = resolveBusinessTableName(formId);
		// 设置创建时间和创建人（未登录时默认 external_api）
		if (data != null) {
			data.put("is_deleted", 0);
			data.put("create_time", LocalDateTime.now());
			data.put("create_user", resolveOperatorUsername());
		}
		formDataMapper.insertDynamic(tableName, asEntries(data));
		// 这里返回主键，若为自增主键需再查，或在 data 中已含 id 则返回
		Object id = data != null ? data.get("id") : null;
		return id instanceof Number ? ((Number) id).longValue() : null;
	}

	@Override
	public boolean updateById(String formId, Long id, Map<String, Object> data) {
		String tableName = resolveBusinessTableName(formId);
		// 设置修改时间和修改人（未登录时默认 external_api）
		if (data != null) {
			data.put("update_time", LocalDateTime.now());
			data.put("update_user", resolveOperatorUsername());
		}
		return formDataMapper.updateByIdDynamic(tableName, id, asEntries(data)) > 0;
	}

	// 旧的 AND-only 方法已移除，统一走 DNF 版本

	/**
	 * 新增：按 DNF 条件更新（OR 的多个 AND 组合）
	 */
	@Override
	public boolean updateByCriteriaGroups(String formId, List<Map<String, Object>> criteriaList, Map<String, Object> data) {
		String tableName = resolveBusinessTableName(formId);
		if (data != null) {
			data.put("update_time", LocalDateTime.now());
			data.put("update_user", resolveOperatorUsername());
		}
		// 将 List<Map> 转换为 MyBatis 需要的 List<List<Map>>（每个 Map 表示 AND 组）
		List<List<Map<String, Object>>> groups = new ArrayList<>();
		if (criteriaList != null) {
			for (Map<String, Object> m : criteriaList) {
				groups.add(criteriaEntries(m));
			}
		}
		return formDataMapper.updateByCriteriaGroupsDynamic(tableName, groups, asEntries(data)) > 0;
	}

	/**
	 * 新增：按 DNF 条件删除（OR 的多个 AND 组合）
	 */
	@Override
	public boolean deleteByCriteriaGroups(String formId, List<Map<String, Object>> criteriaList) {
		String tableName = resolveBusinessTableName(formId);
		// 将 List<Map> 转换为 MyBatis 需要的 List<List<Map>>（每个 Map 表示 AND 组）
		List<List<Map<String, Object>>> groups = new ArrayList<>();
		if (criteriaList != null) {
			for (Map<String, Object> m : criteriaList) {
				groups.add(criteriaEntries(m));
			}
		}
		return formDataMapper.deleteByCriteriaGroupsDynamic(tableName, groups) > 0;
	}

	@Override
	public Map<String, Object> getById(String formId, Long id) {
		String tableName = resolveBusinessTableName(formId);
		Map<String, Object> result = formDataMapper.selectByIdDynamic(tableName, id);
		// 转换 switch 类型字段的布尔值为数字
		return convertSwitchFieldsInResult(formId, result);
	}

	@Override
	public boolean deleteById(String formId, Long id) {
		String tableName = resolveBusinessTableName(formId);
		// 逻辑删除前先更新审计字段（未登录时默认 external_api）
		Map<String, Object> deleteData = new HashMap<>();
		deleteData.put("update_time", LocalDateTime.now());
		deleteData.put("update_user", resolveOperatorUsername());
		// 先更新审计字段，再执行逻辑删除
		formDataMapper.updateByIdDynamic(tableName, id, asEntries(deleteData));
		return formDataMapper.deleteByIdDynamic(tableName, id) > 0;
	}

	@Override
	public Page<Map<String, Object>> page(String formId, Map<String, Object> criteria, int pageNo, int pageSize) {
		String tableName = resolveBusinessTableName(formId);
		int offset = Math.max(pageNo - 1, 0) * pageSize;
		List<Map<String, Object>> records = formDataMapper.pageQueryDynamic(tableName, criteriaEntries(criteria), offset, pageSize);
		Long count = formDataMapper.countQueryDynamic(tableName, criteriaEntries(criteria));
		
		// 转换 switch 类型字段的布尔值为数字
		List<Map<String, Object>> convertedRecords = new ArrayList<>();
		for (Map<String, Object> record : records) {
			convertedRecords.add(convertSwitchFieldsInResult(formId, record));
		}
		
		Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
		page.setTotal(count == null ? 0 : count);
		page.setRecords(convertedRecords);
		return page;
	}

	@Override
	public boolean deleteBatch(String formId, List<Long> ids) {
		String tableName = resolveBusinessTableName(formId);
		return formDataMapper.batchDeleteByIds(tableName, ids) > 0;
	}

	// ==================== 新增的业务方法实现 ====================

	@Override
	public BusinessDataPageVO pageBusinessData(BusinessDataPageDTO dto) {
		// 走已有的通用分页实现
		Page<Map<String, Object>> page = page(dto.getFormId(), dto.getCriteria(), dto.getPageNo(), dto.getPageSize());
		BusinessDataPageVO result = new BusinessDataPageVO();
		result.setRecords(page.getRecords());
		result.setTotal(page.getTotal());
		result.setColumns(buildTableColumns(dto.getFormId()));
		result.setPageNo((long) dto.getPageNo());
		result.setPageSize((long) dto.getPageSize());
		return result;
	}

	@Override
	public Map<String, Object> getBusinessDataById(BusinessDataGetDTO dto) {
		return getById(dto.getFormId(), dto.getId());
	}

	@Override
	public Long createBusinessData(BusinessDataCreateDTO dto) throws Exception {
		Map<String, Object> data = dto.getData();
		
		// 同步缺失的数据库列（防止表结构未同步导致的SQL错误）
		syncMissingColumns(dto.getFormId());
		
		// 过滤并转换字段
		data = filterAndConvertFields(dto.getFormId(), data);
		
		// 数据新增，需要手动添加ID、逻辑删除字段和审计字段
		if (data != null) {
			if (ObjectUtil.isEmpty(data.get("id"))) {
				data.put("id", IdWorker.getId());
			}
			data.put("is_deleted", 0);
			data.put("create_time", LocalDateTime.now());
			data.put("create_user", resolveOperatorUsername());
		}

		Long id = insert(dto.getFormId(), data);

		/***
		 * 发起工作流逻辑优化：
		 * 1. 如果前端传递了 hasFlow 字段，则根据该字段判断是否发起流程（hasFlow=1 表示流程表单）
		 * 2. 如果没有传递 hasFlow 字段，保持原有逻辑（查询流程定义表判断）
		 */
		boolean shouldStartWorkflow = false;
		
		if (dto.getHasFlow() != null) {
			// 新逻辑：根据前端传递的 hasFlow 字段判断
			log.info("根据前端传递的 hasFlow 字段判断是否发起流程，hasFlow={}", dto.getHasFlow());
			shouldStartWorkflow = (dto.getHasFlow() == 1);
		} else {
			// 原有逻辑：查询流程定义表判断
			log.info("未传递 hasFlow 字段，使用原有逻辑判断是否发起流程");
			shouldStartWorkflow = checkWorkflowByDefinition(dto.getFormId());
		}
		
		// 如果需要发起流程，则发起
		if (shouldStartWorkflow) {
			startWorkflowProcess(dto, id);
		}
		
		return id;
	}
	
	/**
	 * 原有逻辑：根据流程定义表判断是否需要发起流程
	 */
	private boolean checkWorkflowByDefinition(String formId) {
		WfProcessDefinition info = wfProcessDefinitionService.getByBusinessTypeAndForm(2, null, formId, 1);
		return ObjectUtil.isNotEmpty(info);
	}
	
	/**
	 * 发起工作流流程
	 */
	private void startWorkflowProcess(BusinessDataCreateDTO dto, Long businessDataId) {
		try {
			WfProcessDefinition info = wfProcessDefinitionService.getByBusinessTypeAndForm(2, null, dto.getFormId(), 1);
			if (ObjectUtil.isEmpty(info)) {
				log.warn("未找到对应的流程定义，formId={}", dto.getFormId());
				return;
			}
			
			StartProcessDTO startProcessDTO = new StartProcessDTO();
			startProcessDTO.setProcessDefinitionId(info.getId());
			/// 业务数据ID
			startProcessDTO.setBusinessKey(businessDataId.toString());
			/// 启动流程用户ID：优先从后端获取当前登录用户ID，如果获取不到再使用前端传递的startUser
			String startUser = resolveOperatorUsername();
			if ("external_api".equals(startUser) && ObjectUtil.isNotEmpty(dto.getStartUser())) {
				// 后端获取失败时，使用前端传递的startUser作为兜底
				startUser = dto.getStartUser();
			}
			startProcessDTO.setStartUser(startUser);
			log.info("发起流程，startUser={}", startUser);
			
			Map<String, Object> dataFlow = dto.getData();
			dataFlow.put("id", businessDataId.toString());
			startProcessDTO.setVariables(dataFlow);
			
			log.info("发起工作流请求参数：{}", JSONObject.toJSONString(startProcessDTO));
			WfProcessInstance result = processInstanceService.startProcess(startProcessDTO);
			log.info("发起工作流结束：{}", JSONObject.toJSONString(result));
		} catch (Exception e) {
			log.error("发起工作流失败", e);
			// 流程发起失败不影响数据保存
		}
	}

	@Override
	public boolean updateBusinessData(BusinessDataUpdateDTO dto) {
		Map<String, Object> data = dto.getData();
		
		// 同步缺失的数据库列（防止表结构未同步导致的SQL错误）
		syncMissingColumns(dto.getFormId());
		
		// 过滤并转换字段
		data = filterAndConvertFields(dto.getFormId(), data);
		
		// 设置修改时间和修改人（未登录时默认 external_api）
		if (data != null) {
			data.put("update_time", LocalDateTime.now());
			data.put("update_user", resolveOperatorUsername());
		}
		return updateById(dto.getFormId(), dto.getId(), data);
	}
	
	/**
	 * 过滤并转换字段
	 * 1. 只保留表单配置中定义的业务字段
	 * 2. 将文件类型字段的数组转换为JSON字符串
	 * 
	 * @param formId 表单ID
	 * @param data 原始数据
	 * @return 过滤并转换后的数据
	 */
	/**
	 * 同步缺失的数据库列
	 * 检查表单配置中的字段是否在数据库表中存在，如果不存在则自动添加
	 * 
	 * @param formId 表单ID
	 */
	private void syncMissingColumns(String formId) {
		try {
			// 获取表单的字段配置
			List<FormColumnInfo> columnInfos = formColumnInfoService.listByFormId(formId);
			if (columnInfos == null || columnInfos.isEmpty()) {
				return;
			}
			
			// 获取表名
			FormTableInfo tableInfo = formTableInfoService.getByFormId(formId);
			if (tableInfo == null || tableInfo.getTableName() == null) {
				return;
			}
			String tableName = tableInfo.getTableName();
			
			// 将 FormColumnInfo 转换为 FormColumnInfoDTO
			List<FormColumnInfoDTO> columnDTOs = columnInfos.stream()
				.filter(col -> col.getIsSysField() == null || col.getIsSysField() != 1) // 排除系统字段
				.map(col -> {
					FormColumnInfoDTO dto = new FormColumnInfoDTO();
					dto.setColumnName(col.getColumnName());
					dto.setColumnType(col.getColumnType());
					dto.setFormType(col.getFormType());
					dto.setColumnComment(col.getColumnComment());
					dto.setColumnLength(col.getColumnLength());
					dto.setIsNullable(col.getIsNullable());
					dto.setDefaultValue(col.getDefaultValue());
					return dto;
				})
				.collect(Collectors.toList());
			
			if (!columnDTOs.isEmpty()) {
				// 检查并添加缺失的列
				dataBaseService.addColumnsIfNotExistsWithDTO(tableName, columnDTOs);
				log.debug("同步表结构完成: formId={}, tableName={}, 字段数={}", formId, tableName, columnDTOs.size());
			}
		} catch (Exception e) {
			log.warn("同步表结构失败: formId={}, error={}", formId, e.getMessage());
			// 不抛出异常，让后续流程继续（可能字段已存在）
		}
	}
	
	/**
	 * 根据字段类型获取默认值
	 * @param formType 表单字段类型
	 * @return 默认值
	 */
	private Object getDefaultValueForFieldType(String formType) {
		if (formType == null) {
			return null;
		}
		
		switch (formType) {
			case "input":
			case "textarea":
			case "select":
			case "radio":
			case "date-picker":
			case "time-picker":
			case "datetime-picker":
			case "upload":
			case "user-select":
			case "department-select":
			case "role-select":
				// 字符串类型字段，默认为空字符串
				return "";
			
			case "number":
			case "slider":
				// 数字类型字段，默认为 0
				return 0;
			
			case "switch":
				// 开关类型字段，默认为 0（关闭）
				return 0;
			
			case "checkbox":
				// 多选框，默认为空数组的 JSON 字符串
				return "[]";
			
			default:
				// 其他类型默认为空字符串
				return "";
		}
	}
	
	private Map<String, Object> filterAndConvertFields(String formId, Map<String, Object> data) {
		if (data == null || data.isEmpty()) {
			return data;
		}
		
		// 获取表单的字段配置
		List<FormColumnInfo> columnInfos = formColumnInfoService.listByFormId(formId);
		if (columnInfos == null || columnInfos.isEmpty()) {
			log.warn("表单字段配置为空: formId={}", formId);
			return data;
		}
		
		// 构建字段名到字段配置的映射（只包含业务字段，排除系统字段）
		Map<String, Object> columnConfigMap = new HashMap<>();
		Set<String> uploadFields = new HashSet<>();
		Set<String> switchFields = new HashSet<>();
		Set<String> businessSelectFields = new HashSet<>(); // 业务选择组件（人员、部门、角色）
		Set<String> datePickerFields = new HashSet<>(); // 日期选择器字段
		
		for (FormColumnInfo col : columnInfos) {
			// 排除系统字段（isSysField == 1）
			if (col.getIsSysField() != null && col.getIsSysField() == 1) {
				continue;
			}
			
			String columnName = col.getColumnName();
			columnConfigMap.put(columnName, col);
			
			// 识别文件上传类型字段
			if ("upload".equals(col.getFormType())) {
				uploadFields.add(columnName);
			}
			
			// 识别开关类型字段
			if ("switch".equals(col.getFormType())) {
				switchFields.add(columnName);
			}
			
			// 识别业务选择组件字段（人员、部门、角色选择）
			if ("user-select".equals(col.getFormType()) 
				|| "department-select".equals(col.getFormType()) 
				|| "role-select".equals(col.getFormType())) {
				businessSelectFields.add(columnName);
			}
			
			// 识别日期选择器字段
			if ("date-picker".equals(col.getFormType()) 
				|| "time-picker".equals(col.getFormType()) 
				|| "datetime-picker".equals(col.getFormType())) {
				datePickerFields.add(columnName);
			}
		}
		
		// 过滤并转换数据
		Map<String, Object> filteredData = new HashMap<>();
		
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue();
			
			// 只保留配置中存在的业务字段
			if (!columnConfigMap.containsKey(fieldName)) {
				log.debug("过滤掉未配置的字段: {}", fieldName);
				continue;
			}
			
			// 文件类型字段转换：数组 -> JSON字符串
			if (uploadFields.contains(fieldName) && fieldValue instanceof List) {
				fieldValue = JSONObject.toJSONString(fieldValue);
				log.debug("文件字段转换: {} -> {}", fieldName, fieldValue);
			}
			
			// 日期选择器字段转换：数组 -> JSON字符串（处理日期范围选择器返回的数组）
			if (datePickerFields.contains(fieldName) && fieldValue instanceof List) {
				fieldValue = JSONObject.toJSONString(fieldValue);
				log.debug("日期字段转换: {} -> {}", fieldName, fieldValue);
			}
			
			// 开关类型字段转换：布尔值或字符串 -> 数字（0/1）
			if (switchFields.contains(fieldName)) {
				if (fieldValue instanceof Boolean) {
					fieldValue = ((Boolean) fieldValue) ? 1 : 0;
				} else if (fieldValue instanceof String) {
					String strVal = (String) fieldValue;
					fieldValue = "true".equalsIgnoreCase(strVal) || "1".equals(strVal) ? 1 : 0;
				} else if (fieldValue instanceof Number) {
					fieldValue = ((Number) fieldValue).intValue() == 0 ? 0 : 1;
				}
				log.debug("开关字段转换: {} -> {}", fieldName, fieldValue);
			}
			
			// 业务选择组件字段转换：数组 -> JSON字符串
			if (businessSelectFields.contains(fieldName)) {
				if (fieldValue instanceof List) {
					fieldValue = JSONObject.toJSONString(fieldValue);
					log.debug("业务选择字段转换: {} -> {}", fieldName, fieldValue);
				}
			}
			
			filteredData.put(fieldName, fieldValue);
		}
		
		// 补充缺失的字段（前端未传递但表单配置中存在的字段）
		for (FormColumnInfo col : columnInfos) {
			// 排除系统字段
			if (col.getIsSysField() != null && col.getIsSysField() == 1) {
				continue;
			}
			
			String columnName = col.getColumnName();
			
			// 如果字段在前端数据中不存在，则设置默认值
			if (!filteredData.containsKey(columnName)) {
				Object defaultValue = getDefaultValueForFieldType(col.getFormType());
				if (defaultValue != null) {
					filteredData.put(columnName, defaultValue);
					log.debug("补充缺失字段的默认值: {} -> {}", columnName, defaultValue);
				}
			}
		}
		
		log.debug("字段过滤和转换完成: 原始字段数={}, 过滤后字段数={}", data.size(), filteredData.size());
		
		return filteredData;
	}

	@Override
	public boolean deleteBusinessData(BusinessDataDeleteDTO dto) {
		return deleteById(dto.getFormId(), dto.getId());
	}

	@Override
	public boolean batchDeleteBusinessData(BusinessDataBatchDeleteDTO dto) {
		return deleteBatch(dto.getFormId(), dto.getIds());
	}

	@Override
	public Map<String, Object> getTableInfoByFormId(String formId) {
		String tableName = resolveBusinessTableName(formId);
		FormTableInfo tableInfo = formTableInfoService.getByFormId(formId);
		Map<String, Object> result = new HashMap<>();
		result.put("formConfig",formService.getFormDetail(formId).getFormConfig());
		result.put("tableInfo", tableInfo);
		result.put("columnInfos", formColumnInfoService.listByFormId(formId));
		return result;
	}

	/**
	 * 构建表格列（返回 {label, prop}，label 优先中文注释）
	 */
	private List<Map<String, String>> buildTableColumns(String formId) {
		return formColumnInfoService.listByFormId(formId).stream()
			.map(col -> {
				String prop = col.getColumnName();
				String label = (col.getColumnComment() == null || col.getColumnComment().trim().isEmpty())
					? prop
					: col.getColumnComment();
				Map<String, String> m = new HashMap<>(2);
				m.put("label", label);
				m.put("prop", prop);
				return m;
			})
			.collect(Collectors.toList());
	}
	
	/**
	 * 转换查询结果中 switch 类型字段的布尔值为数字
	 * MySQL tinyint(1) 在 JDBC 中会被映射为 Boolean，需要转换为 Integer 以便前端正确显示
	 * 
	 * @param formId 表单ID
	 * @param data 查询结果数据
	 * @return 转换后的数据
	 */
	private Map<String, Object> convertSwitchFieldsInResult(String formId, Map<String, Object> data) {
		if (data == null || data.isEmpty()) {
			return data;
		}
		
		// 获取表单的字段配置
		List<FormColumnInfo> columnInfos = formColumnInfoService.listByFormId(formId);
		if (columnInfos == null || columnInfos.isEmpty()) {
			return data;
		}
		
		// 找出所有 switch 类型的字段
		Set<String> switchFields = new HashSet<>();
		for (FormColumnInfo col : columnInfos) {
			if ("switch".equals(col.getFormType())) {
				switchFields.add(col.getColumnName());
			}
		}
		
		if (switchFields.isEmpty()) {
			return data;
		}
		
		// 转换 switch 字段的值
		Map<String, Object> result = new HashMap<>(data);
		for (String fieldName : switchFields) {
			if (result.containsKey(fieldName)) {
				Object value = result.get(fieldName);
				if (value instanceof Boolean) {
					result.put(fieldName, ((Boolean) value) ? 1 : 0);
				}
			}
		}
		
		return result;
	}
} 