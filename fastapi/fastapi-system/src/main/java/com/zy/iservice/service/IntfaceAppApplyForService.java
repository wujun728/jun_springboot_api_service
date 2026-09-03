package com.zy.iservice.service;

import java.util.List;
import java.util.Map;

import com.zy.common.annotation.Log;
import com.zy.common.core.domain.AjaxResult;
import com.zy.common.enums.BusinessType;
import com.zy.iservice.domain.IntfaceAppApplyFor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 接口应用申请Service接口
 *
 * @author javachen
 * @date 2022-11-19
 */
public interface IntfaceAppApplyForService
{
    /**
     * 查询接口应用申请
     *
     * @param id 接口应用申请主键
     * @return 接口应用申请
     */
    public IntfaceAppApplyFor selectIntfaceAppApplyForById(Long id);

    /**
     * 查询接口应用申请列表
     *
     * @param intfaceAppApplyFor 接口应用申请
     * @return 接口应用申请集合
     */
    public List<IntfaceAppApplyFor> selectIntfaceAppApplyForList(IntfaceAppApplyFor intfaceAppApplyFor);

    /**
     * 新增接口应用申请
     *
     * @param intfaceAppApplyFor 接口应用申请
     * @return 结果
     */
    public int insertIntfaceAppApplyFor(IntfaceAppApplyFor intfaceAppApplyFor);

    /**
     * 修改接口应用申请
     *
     * @param intfaceAppApplyFor 接口应用申请
     * @return 结果
     */
    public int updateIntfaceAppApplyFor(IntfaceAppApplyFor intfaceAppApplyFor);

    /**
     * 批量删除接口应用申请
     *
     * @param ids 需要删除的接口应用申请主键集合
     * @return 结果
     */
    public int deleteIntfaceAppApplyForByIds(Long[] ids);

    /**
     * 删除接口应用申请信息
     *
     * @param id 接口应用申请主键
     * @return 结果
     */
    public int deleteIntfaceAppApplyForById(Long id);

    /**
     * 查询密钥
     * @param id 申请应用id
     * @return isResetSecret 密码过期后是否重置
     */
    public IntfaceAppApplyFor selectSecret(Long id);

    /**
     * 重置密钥
     */
    public IntfaceAppApplyFor resetSecret(Long id);

    /**
     * 根据密钥查询应用
     */
    public IntfaceAppApplyFor selectAppApplyBySecret(IntfaceAppApplyFor intfaceAppApplyFor);

}
