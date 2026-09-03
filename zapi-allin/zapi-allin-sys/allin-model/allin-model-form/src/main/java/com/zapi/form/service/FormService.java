package com.zapi.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.form.model.entity.Form;
import com.zapi.form.model.req.FormCreateRequest;
import com.zapi.form.model.req.FormUpdateRequest;
import com.zapi.form.model.vo.FormVO;

/**
 * 表单服务接口
 *
 * @author zapi
 * @since 2024-09-24
 */
public interface FormService extends IService<Form> {

    /**
     * 创建表单
     *
     * @param form 创建请求
     * @return 表单ID
     */
    String createForm(Form form);

    /**
     * 更新表单
     *
     * @param form 更新请求
     * @return 是否成功
     */
    boolean updateForm(Form form);

    /**
     * 删除表单
     *
     * @param formId 表单ID
     * @return 是否成功
     */
    boolean deleteForm(String formId);

    /**
     * 根据ID获取表单详情
     *
     * @param formId 表单ID
     * @return 表单详情
     */
    FormVO getFormDetail(String formId);

    /**
     * 分页查询表单列表
     *
     * @param current  当前页
     * @param size     每页大小
     * @param formName 表单名称（可选）
     * @param status   状态（可选）
     * @return 分页结果
     */
    Page<FormVO> getFormList(long current, long size, String formName, Integer status);

    /**
     * 启用/禁用表单
     *
     * @param formId 表单ID
     * @param status 状态：0-禁用，1-启用
     * @return 是否成功
     */
    boolean changeFormStatus(String formId, Integer status);
}
