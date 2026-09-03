package com.zy.imanager.mapper;

import java.util.List;

import com.zy.base.domain.AppManager;
import com.zy.imanager.domain.SelectIntfaceManager;

/**
 * 查询接口管理Mapper接口
 *
 * @author javachen
 * @date 2022-09-26
 */
public interface SelectIntfaceManagerMapper
{
    /**
     * 查询查询接口管理
     *
     * @param id 查询接口管理主键
     * @return 查询接口管理
     */
    public SelectIntfaceManager selectSelectIntfaceManagerById(Long id);

    /**
     * 查询查询接口管理列表
     *
     * @param selectIntfaceManager 查询接口管理
     * @return 查询接口管理集合
     */
    public List<SelectIntfaceManager> selectSelectIntfaceManagerList(SelectIntfaceManager selectIntfaceManager);

    /**
     * 新增查询接口管理
     *
     * @param selectIntfaceManager 查询接口管理
     * @return 结果
     */
    public int insertSelectIntfaceManager(SelectIntfaceManager selectIntfaceManager);

    /**
     * 修改查询接口管理
     *
     * @param selectIntfaceManager 查询接口管理
     * @return 结果
     */
    public int updateSelectIntfaceManager(SelectIntfaceManager selectIntfaceManager);

    /**
     * 删除查询接口管理
     *
     * @param id 查询接口管理主键
     * @return 结果
     */
    public int deleteSelectIntfaceManagerById(Long id);

    /**
     * 批量删除查询接口管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSelectIntfaceManagerByIds(Long[] ids);

    /**
     * 更新SelectIntfaceManager表中appManager相关信息
     * @return 结果
     */
    public void updateAppManagerInfo(AppManager appManager);

    /**
     * 修改转换信息
     */
    public void updateConverInfo(SelectIntfaceManager selectIntfaceManager);

}
