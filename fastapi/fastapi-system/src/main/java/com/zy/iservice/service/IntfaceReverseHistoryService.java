package com.zy.iservice.service;

import java.util.List;
import com.zy.iservice.domain.IntfaceReverseHistory;

/**
 * 接口调用历史Service接口
 *
 * @author javachen
 * @date 2022-11-22
 */
public interface IntfaceReverseHistoryService
{
    /**
     * 查询接口调用历史
     *
     * @param id 接口调用历史主键
     * @return 接口调用历史
     */
    public IntfaceReverseHistory selectIntfaceReverseHistoryById(Long id);

    /**
     * 查询接口调用历史列表
     *
     * @param intfaceReverseHistory 接口调用历史
     * @return 接口调用历史集合
     */
    public List<IntfaceReverseHistory> selectIntfaceReverseHistoryList(IntfaceReverseHistory intfaceReverseHistory);

    /**
     * 新增接口调用历史
     *
     * @param intfaceReverseHistory 接口调用历史
     * @return 结果
     */
    public int insertIntfaceReverseHistory(IntfaceReverseHistory intfaceReverseHistory);

    /**
     * 修改接口调用历史
     *
     * @param intfaceReverseHistory 接口调用历史
     * @return 结果
     */
    public int updateIntfaceReverseHistory(IntfaceReverseHistory intfaceReverseHistory);

    /**
     * 批量删除接口调用历史
     *
     * @param ids 需要删除的接口调用历史主键集合
     * @return 结果
     */
    public int deleteIntfaceReverseHistoryByIds(Long[] ids);

    /**
     * 删除接口调用历史信息
     *
     * @param id 接口调用历史主键
     * @return 结果
     */
    public int deleteIntfaceReverseHistoryById(Long id);
}
