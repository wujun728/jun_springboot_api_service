package com.zy.iservice.mapper;

import java.util.List;
import java.util.Map;

import com.zy.iservice.domain.IntfaceAppApplyFor;

/**
 * 接口应用申请Mapper接口
 *
 * @author javachen
 * @date 2022-11-19
 */
public interface IntfaceAppApplyForMapper
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
     * 删除接口应用申请
     *
     * @param id 接口应用申请主键
     * @return 结果
     */
    public int deleteIntfaceAppApplyForById(Long id);

    /**
     * 批量删除接口应用申请
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteIntfaceAppApplyForByIds(Long[] ids);

    /**
     * 查询密钥
     */
    public IntfaceAppApplyFor selectSecret(Long id);

    /**
     * 根据密钥查询应用
     */
    public IntfaceAppApplyFor selectAppApplyBySecret(IntfaceAppApplyFor intfaceAppApplyFor);

}
