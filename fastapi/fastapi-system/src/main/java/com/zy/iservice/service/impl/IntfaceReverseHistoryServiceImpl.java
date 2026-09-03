package com.zy.iservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zy.common.utils.DateUtils;
import com.zy.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zy.iservice.mapper.IntfaceReverseHistoryMapper;
import com.zy.iservice.domain.IntfaceReverseHistory;
import com.zy.iservice.service.IntfaceReverseHistoryService;

/**
 * 接口调用历史Service业务层处理
 *
 * @author javachen
 * @date 2022-11-22
 */
@Service
public class IntfaceReverseHistoryServiceImpl implements IntfaceReverseHistoryService
{
    @Autowired
    private IntfaceReverseHistoryMapper intfaceReverseHistoryMapper;

    /**
     * 查询接口调用历史
     *
     * @param id 接口调用历史主键
     * @return 接口调用历史
     */
    @Override
    public IntfaceReverseHistory selectIntfaceReverseHistoryById(Long id)
    {
        return intfaceReverseHistoryMapper.selectIntfaceReverseHistoryById(id);
    }

    /**
     * 查询接口调用历史列表
     *
     * @param intfaceReverseHistory 接口调用历史
     * @return 接口调用历史
     */
    @Override
    public List<IntfaceReverseHistory> selectIntfaceReverseHistoryList(IntfaceReverseHistory intfaceReverseHistory)
    {
        //如果不是admin的话,只能查看自己创建的应用
        if(!SecurityUtils.getLoginUser().getUser().isAdmin()){
            Map<String, Object> params = new HashMap<>();
            params.put("dataScope"," and reverse_user_id = "+SecurityUtils.getLoginUser().getUserId());
            intfaceReverseHistory.setParams(params);
        }
        return intfaceReverseHistoryMapper.selectIntfaceReverseHistoryList(intfaceReverseHistory);
    }

    /**
     * 新增接口调用历史
     *
     * @param intfaceReverseHistory 接口调用历史
     * @return 结果
     */
    @Override
    public int insertIntfaceReverseHistory(IntfaceReverseHistory intfaceReverseHistory)
    {
        intfaceReverseHistory.setCreateTime(DateUtils.getNowDate());
        return intfaceReverseHistoryMapper.insertIntfaceReverseHistory(intfaceReverseHistory);
    }

    /**
     * 修改接口调用历史
     *
     * @param intfaceReverseHistory 接口调用历史
     * @return 结果
     */
    @Override
    public int updateIntfaceReverseHistory(IntfaceReverseHistory intfaceReverseHistory)
    {
        return intfaceReverseHistoryMapper.updateIntfaceReverseHistory(intfaceReverseHistory);
    }

    /**
     * 批量删除接口调用历史
     *
     * @param ids 需要删除的接口调用历史主键
     * @return 结果
     */
    @Override
    public int deleteIntfaceReverseHistoryByIds(Long[] ids)
    {
        return intfaceReverseHistoryMapper.deleteIntfaceReverseHistoryByIds(ids);
    }

    /**
     * 删除接口调用历史信息
     *
     * @param id 接口调用历史主键
     * @return 结果
     */
    @Override
    public int deleteIntfaceReverseHistoryById(Long id)
    {
        return intfaceReverseHistoryMapper.deleteIntfaceReverseHistoryById(id);
    }
}
