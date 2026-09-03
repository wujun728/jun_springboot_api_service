package com.zy.imanager.service.impl;

import java.util.List;

import com.zy.base.domain.AppManager;
import com.zy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zy.imanager.mapper.SelectIntfaceManagerMapper;
import com.zy.imanager.domain.SelectIntfaceManager;
import com.zy.imanager.service.ISelectIntfaceManagerService;

/**
 * 查询接口管理Service业务层处理
 *
 * @author javachen
 * @date 2022-09-26
 */
@Service
public class SelectIntfaceManagerServiceImpl implements ISelectIntfaceManagerService
{
    @Autowired
    private SelectIntfaceManagerMapper selectIntfaceManagerMapper;

    /**
     * 查询查询接口管理
     *
     * @param id 查询接口管理主键
     * @return 查询接口管理
     */
    @Override
    public SelectIntfaceManager selectSelectIntfaceManagerById(Long id)
    {
        return selectIntfaceManagerMapper.selectSelectIntfaceManagerById(id);
    }

    /**
     * 查询查询接口管理列表
     *
     * @param selectIntfaceManager 查询接口管理
     * @return 查询接口管理
     */
    @Override
    public List<SelectIntfaceManager> selectSelectIntfaceManagerList(SelectIntfaceManager selectIntfaceManager)
    {
        return selectIntfaceManagerMapper.selectSelectIntfaceManagerList(selectIntfaceManager);
    }

    /**
     * 新增查询接口管理
     *
     * @param selectIntfaceManager 查询接口管理
     * @return 结果
     */
    @Override
    public int insertSelectIntfaceManager(SelectIntfaceManager selectIntfaceManager)
    {
        selectIntfaceManager.setCreateTime(DateUtils.getNowDate());
        return selectIntfaceManagerMapper.insertSelectIntfaceManager(selectIntfaceManager);
    }

    /**
     * 修改查询接口管理
     *
     * @param selectIntfaceManager 查询接口管理
     * @return 结果
     */
    @Override
    public int updateSelectIntfaceManager(SelectIntfaceManager selectIntfaceManager)
    {
        selectIntfaceManager.setUpdateTime(DateUtils.getNowDate());
        return selectIntfaceManagerMapper.updateSelectIntfaceManager(selectIntfaceManager);
    }

    /**
     * 批量删除查询接口管理
     *
     * @param ids 需要删除的查询接口管理主键
     * @return 结果
     */
    @Override
    public int deleteSelectIntfaceManagerByIds(Long[] ids)
    {
        return selectIntfaceManagerMapper.deleteSelectIntfaceManagerByIds(ids);
    }

    /**
     * 删除查询接口管理信息
     *
     * @param id 查询接口管理主键
     * @return 结果
     */
    @Override
    public int deleteSelectIntfaceManagerById(Long id)
    {
        return selectIntfaceManagerMapper.deleteSelectIntfaceManagerById(id);
    }

    /**
     * 更新SelectIntfaceManager表中appManager相关信息
     * @return 结果
     */
    @Override
    public void updateAppManagerInfo(AppManager appManager){
           selectIntfaceManagerMapper.updateAppManagerInfo(appManager);
    }

    /**
     * 修改转换信息
     */
    @Override
    public void updateConverInfo(SelectIntfaceManager selectIntfaceManager){
        selectIntfaceManagerMapper.updateConverInfo(selectIntfaceManager);
    }

}
