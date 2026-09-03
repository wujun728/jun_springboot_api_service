package com.zy.reverse.service;

import com.zy.iservice.domain.IntfaceReverseHistory;

import java.util.Map;

/**
 * 查询接口管理Service接口
 *
 * @author javachen
 * @date 2022-09-26
 */
public interface IReverseIntService
{
    /**
     * 执行调用方法
     * @return
     */
    public Object executeMethod(String prefix, String name, Map<String, Object> inputParamMap, IntfaceReverseHistory intfaceReverseHistory);

    /**
     * 接口执行方法
     * @return
     */
    public Object reverseInt(String prefix, String name, Map<String, Object> inputParamMap, IntfaceReverseHistory intfaceReverseHistory);

}
