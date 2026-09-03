package com.zapi.form.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.form.mapper.FormMapper;
import com.zapi.form.model.entity.Form;
import com.zapi.form.model.req.FormCreateRequest;
import com.zapi.form.model.req.FormUpdateRequest;
import com.zapi.form.model.vo.FormVO;
import com.zapi.form.service.FormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表单服务实现类
 *
 * @author zapi
 * @since 2024-09-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FormServiceImpl extends ServiceImpl<FormMapper, Form> implements FormService {

    @Override
    public String createForm(Form form) {
        // 如果表单配置为空，设置默认值
        if (form.getFormConfig() == null || form.getFormConfig().trim().isEmpty()) {
            form.setFormConfig("{}");
        }
        
        // 设置创建者（临时设置为系统，后续可以从当前登录用户获取）
        form.setCreateBy("system");
        form.setUpdateBy("system");
        
        save(form);
        return form.getId();
    }

    @Override
    public boolean updateForm(Form form) {
        // 检查表单是否存在
        Form existForm = getById(form.getId());
        if (existForm == null) {
            throw new RuntimeException("表单不存在");
        }

        // 如果表单配置为空，设置默认值
        if (form.getFormConfig() == null || form.getFormConfig().trim().isEmpty()) {
            form.setFormConfig("{}");
        }
        
        // 设置更新者（临时设置为系统，后续可以从当前登录用户获取）
        form.setUpdateBy("system");
        
        return updateById(form);
    }

    @Override
    public boolean deleteForm(String formId) {
        Form form = getById(formId);
        if (form == null) {
            throw new RuntimeException("表单不存在");
        }
        return removeById(formId);
    }

    @Override
    public FormVO getFormDetail(String formId) {
        Form form = getById(formId);
        if (form == null) {
            throw new RuntimeException("表单不存在");
        }

        FormVO formVO = new FormVO();
        BeanUtil.copyProperties(form, formVO);
        formVO.setStatusDesc(form.getStatus() == 1 ? "启用" : "禁用");
        return formVO;
    }

    @Override
    public Page<FormVO> getFormList(long current, long size, String formName, Integer status) {
        Page<Form> page = new Page<>(current, size);
        
        // 构建查询条件
        LambdaQueryWrapper<Form> queryWrapper = new LambdaQueryWrapper<>();
        
        // 只查询未关联应用条目的表单（entryId 为 null）
        queryWrapper.isNull(Form::getEntryId);
        
        // 按表单名称模糊查询
        if (StrUtil.isNotBlank(formName)) {
            queryWrapper.like(Form::getFormName, formName);
        }
        
        // 按状态查询
        if (status != null) {
            queryWrapper.eq(Form::getStatus, status);
        }
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc(Form::getCreateTime);
        
        Page<Form> formPage = page(page, queryWrapper);

        Page<FormVO> resultPage = new Page<>();
        BeanUtil.copyProperties(formPage, resultPage);

        List<FormVO> formVOList = formPage.getRecords().stream()
                .map(form -> {
                    FormVO formVO = new FormVO();
                    BeanUtil.copyProperties(form, formVO);
                    formVO.setStatusDesc(form.getStatus() == 1 ? "启用" : "禁用");
                    return formVO;
                })
                .collect(Collectors.toList());

        resultPage.setRecords(formVOList);
        return resultPage;
    }

    @Override
    public boolean changeFormStatus(String formId, Integer status) {
        Form form = getById(formId);
        if (form == null) {
            throw new RuntimeException("表单不存在");
        }

        form.setStatus(status);
        return updateById(form);
    }
}
