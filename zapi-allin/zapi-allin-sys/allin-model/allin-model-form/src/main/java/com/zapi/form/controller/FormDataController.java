package com.zapi.form.controller;

import com.zapi.common.Result;
import com.zapi.form.model.dto.*;
import com.zapi.form.model.vo.BusinessDataPageVO;
import com.zapi.form.service.FormDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 业务数据管理控制器
 *
 * @author zhonglz
 * @date 2025年08月21日
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/form-data")
public class FormDataController {
	@Autowired
	private final FormDataService formDataService;

	/****
	 * 分页查询
	 * @param dto
	 * @return
	 */
	@PostMapping("/page")
    public Result<BusinessDataPageVO> page(@RequestBody BusinessDataPageDTO dto) {
        BusinessDataPageVO result = formDataService.pageBusinessData(dto);
        return Result.success(result);
    }

	/****
	 * 主键查询
	 * @param dto
	 * @return
	 */
	@PostMapping("/get")
	public Result<Map<String, Object>> get(@RequestBody BusinessDataGetDTO dto) {
		Map<String, Object> result = formDataService.getBusinessDataById(dto);
		return Result.success(result);
	}

	/****
	 * 新增
	 * @param dto
	 * @return
	 */
	@PostMapping("/create")
	public Result<Long> create(@RequestBody BusinessDataCreateDTO dto) throws Exception {
		Long result = formDataService.createBusinessData(dto);
		return Result.success(result);
	}

	/****
	 * 修改
	 * @param dto
	 * @return
	 */
	@PostMapping("/update")
	public Result<Boolean> update(@RequestBody BusinessDataUpdateDTO dto) {
		boolean result = formDataService.updateBusinessData(dto);
		return Result.success(result);
	}

	/****
	 * 删除
	 * @param dto
	 * @return
	 */
	@PostMapping("/delete")
	public Result<Boolean> delete(@RequestBody BusinessDataDeleteDTO dto) {
		boolean result = formDataService.deleteBusinessData(dto);
		return Result.success(result);
	}

	/****
	 * 批量删除
	 * @param dto
	 * @return
	 */
	@PostMapping("/batch-delete")
	public Result<Boolean> batchDelete(@RequestBody BusinessDataBatchDeleteDTO dto) {
		boolean result = formDataService.batchDeleteBusinessData(dto);
		return Result.success(result);
	}

	/****
	 * 根据表单ID查询表信息和字段列表
	 * @param formId
	 * @return
	 */
	@GetMapping("/table-info/{formId}")
	public Result<Map<String, Object>> getTableInfoByFormId(@PathVariable("formId") String formId) {
		Map<String, Object> result = formDataService.getTableInfoByFormId(formId);
		return Result.success(result);
	}
} 