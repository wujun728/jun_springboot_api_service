package com.zapi.form.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.enums.FormOperationEnum;
import com.zapi.form.annotation.DataBaseOperatePlus;
import com.zapi.form.model.entity.Form;
import com.zapi.form.model.req.FormCreateRequest;
import com.zapi.form.model.req.FormUpdateRequest;
import com.zapi.form.model.vo.FormVO;
import com.zapi.form.service.FormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 表单管理控制器
 *
 * @author zapi
 * @since 2024-09-24
 */
@Slf4j
@RestController
@RequestMapping("/api/form")
@RequiredArgsConstructor
@Api(tags = "表单管理")
public class FormController {

    private final FormService formService;

    @PostMapping
    @ApiOperation("创建表单")
    @DataBaseOperatePlus(
            operationType = FormOperationEnum.ADDTABLE,
            targetEntityType = Form.class
    )
    public Result<String> createForm(@Validated @RequestBody Form form) {
        String formId = formService.createForm(form);
        form.setId(formId);
        return Result.success(formId);
    }

    @PutMapping
    @ApiOperation("更新表单")
    @DataBaseOperatePlus(
            operationType = FormOperationEnum.UPDATETABLE,
            targetEntityType = Form.class
    )
    public Result<Boolean> updateForm(@Validated @RequestBody Form form) {
        boolean result = formService.updateForm(form);
        return Result.success(result);
    }

    @DeleteMapping("/{formId}")
    @ApiOperation("删除表单")
    public Result<Boolean> deleteForm(@ApiParam("表单ID") @PathVariable String formId) {
        boolean result = formService.deleteForm(formId);
        return Result.success(result);
    }

    @GetMapping("/{formId}")
    @ApiOperation("获取表单详情")
    public Result<FormVO> getFormDetail(@ApiParam("表单ID") @PathVariable String formId) {
        FormVO formVO = formService.getFormDetail(formId);
        return Result.success(formVO);
    }

    @GetMapping("/entry/{entryId}")
    @ApiOperation("根据应用条目ID获取表单")
    public Result<Form> getFormByEntryId(@ApiParam("应用条目ID") @PathVariable Long entryId) {
        Form form = formService.lambdaQuery()
                .eq(Form::getEntryId, entryId)
                .eq(Form::getDelFlag, 0)
                .one();
        return Result.success(form);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询表单列表")
    public Result<Page<FormVO>> getFormList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") long size,
            @ApiParam("表单名称") @RequestParam(required = false) String formName,
            @ApiParam("状态") @RequestParam(required = false) Integer status) {
        Page<FormVO> page = formService.getFormList(current, size, formName, status);
        return Result.success(page);
    }

    @PutMapping("/{formId}/status")
    @ApiOperation("启用/禁用表单")
    public Result<Boolean> changeFormStatus(
            @ApiParam("表单ID") @PathVariable String formId,
            @ApiParam("状态：0-禁用，1-启用") @RequestParam Integer status) {
        boolean result = formService.changeFormStatus(formId, status);
        return Result.success(result);
    }
}
