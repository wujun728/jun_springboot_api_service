package com.zapi.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.form.model.dto.*;
import com.zapi.form.model.vo.BusinessDataPageVO;

import java.util.List;
import java.util.Map;

/**
 * 业务数据通用服务接口
 * 统一通过 formId 解析业务物理表（TableInfo）与字段配置（ColumnInfo），再执行业务数据操作
 *
 * 支持：新增、修改、查询、删除、分页查询、批量删除
 *
 * @author zhonglz
 * @date 2025年08月21日
 */
public interface FormDataService {

	/**
	 * 新增业务数据
	 * @param formId 表单ID（用于解析业务表与字段配置）
	 * @param data   字段名-值 Map
	 * @return 新增记录主键ID
	 */
	Long insert(String formId, Map<String, Object> data);

	/**
	 * 根据ID修改业务数据
	 * @param formId 表单ID
	 * @param id     业务数据主键ID
	 * @param data   字段名-值 Map
	 * @return 是否成功
	 */
	boolean updateById(String formId, Long id, Map<String, Object> data);

	/**
	 * 按 DNF 条件更新（OR 的多个 AND 组合）
	 * @param formId 表单ID
	 * @param criteriaList 若干 AND 条件组合列表，每个 Map 为 AND 集合
	 * @param data 更新内容
	 */
	boolean updateByCriteriaGroups(String formId, List<Map<String, Object>> criteriaList, Map<String, Object> data);

	/**
	 * 按 DNF 条件删除（OR 的多个 AND 组合）
	 * @param formId 表单ID
	 * @param criteriaList 若干 AND 条件组合列表，每个 Map 为 AND 集合
	 * @return 是否成功
	 */
	boolean deleteByCriteriaGroups(String formId, List<Map<String, Object>> criteriaList);

	/**
	 * 根据ID查询业务数据
	 * @param formId 表单ID
	 * @param id     业务数据主键ID
	 * @return 字段名-值 Map
	 */
	Map<String, Object> getById(String formId, Long id);

	/**
	 * 根据ID删除业务数据
	 * @param formId 表单ID
	 * @param id     业务数据主键ID
	 * @return 是否成功
	 */
	boolean deleteById(String formId, Long id);

	/**
	 * 分页查询业务数据
	 * @param formId    表单ID
	 * @param criteria  查询条件（字段名-值）
	 * @param pageNo    页码（从1开始）
	 * @param pageSize  页大小
	 * @return Page 结果，记录为 Map
	 */
	Page<Map<String, Object>> page(String formId, Map<String, Object> criteria, int pageNo, int pageSize);

	/**
	 * 批量删除业务数据
	 * @param formId 表单ID
	 * @param ids    业务数据主键ID列表
	 * @return 是否成功
	 */
	boolean deleteBatch(String formId, List<Long> ids);

	// ==================== 新增的业务方法 ====================
	/**
	 * 分页查询业务数据（封装版本）
	 * @param dto 分页查询请求参数
	 * @return 分页查询结果
	 */
	BusinessDataPageVO pageBusinessData(BusinessDataPageDTO dto);

	/**
	 * 根据ID查询业务数据（封装版本）
	 * @param dto 查询请求参数
	 * @return 业务数据
	 */
	Map<String, Object> getBusinessDataById(BusinessDataGetDTO dto);

	/**
	 * 新增业务数据（封装版本）
	 * @param dto 新增请求参数
	 * @return 新增记录主键ID
	 */
	Long createBusinessData(BusinessDataCreateDTO dto) throws Exception;

	/**
	 * 修改业务数据（封装版本）
	 * @param dto 修改请求参数
	 * @return 是否成功
	 */
	boolean updateBusinessData(BusinessDataUpdateDTO dto);

	/**
	 * 删除业务数据（封装版本）
	 * @param dto 删除请求参数
	 * @return 是否成功
	 */
	boolean deleteBusinessData(BusinessDataDeleteDTO dto);

	/**
	 * 批量删除业务数据（封装版本）
	 * @param dto 批量删除请求参数
	 * @return 是否成功
	 */
	boolean batchDeleteBusinessData(BusinessDataBatchDeleteDTO dto);

	/**
	 * 根据表单ID查询表信息和字段列表
	 * @param formId 表单ID
	 * @return 包含表信息和字段列表的Map
	 */
	Map<String, Object> getTableInfoByFormId(String formId);
	

} 