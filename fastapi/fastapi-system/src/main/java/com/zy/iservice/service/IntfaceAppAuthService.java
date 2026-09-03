package com.zy.iservice.service;

import com.zy.iservice.domain.IntfaceAppAuth;
import com.zy.iservice.domain.bo.InsertUpdateIntfaceAppAuthBO;
import com.zy.iservice.domain.bo.SelectIntfaceAppAuthListBO;

import java.util.List;

/**
 * 接口应用授权Service接口
 *
 * @author javachen
 * @date 2022-11-19
 */
public interface IntfaceAppAuthService
{
    /**
     * 查询接口应用授权列表
     *
     * @param selectIntfaceAppAuthListBO
     * @return 接口应用授权集合
     */
    public List<SelectIntfaceAppAuthListBO> selectIntfaceAppAuthList(SelectIntfaceAppAuthListBO selectIntfaceAppAuthListBO);

    /**
     * 新增接口应用授权
     *
     * @param insertIntfaceAppAuthBO 接口应用授权
     * @return 结果
     */
    public void insertIntfaceAppAuth(InsertUpdateIntfaceAppAuthBO insertIntfaceAppAuthBO);

    /**
     * 取消授权
     */
    public void cancleAuth(InsertUpdateIntfaceAppAuthBO insertIntfaceAppAuthBO);

    /**
     * 删除应用授权
     */
    public void deleteIntfaceAppAuthController(Long id);

    /**
     * 查看是否有应用授权
     */
    public IntfaceAppAuth cheHaveIntfaceAppAuth(IntfaceAppAuth intfaceAppAuth);

}
