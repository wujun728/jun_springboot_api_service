package com.zy.iservice.mapper;

import com.zy.iservice.domain.IntfaceAppAuth;
import com.zy.iservice.domain.bo.InsertUpdateIntfaceAppAuthBO;
import com.zy.iservice.domain.bo.SelectIntfaceAppAuthListBO;

import java.util.List;
import java.util.Map;

/**
 * 接口应用授权Mapper接口
 *
 * @author javachen
 * @date 2022-11-19
 */
public interface IntfaceAppAuthMapper
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
     * @param intfaceAppAuth 接口应用授权
     * @return 结果
     */
    public int insertIntfaceAppAuth(IntfaceAppAuth intfaceAppAuth);

    /**
     * 取消授权
     */
    public void cancleAuth(InsertUpdateIntfaceAppAuthBO insertUpdateIntfaceAppAuthBO);

    /**
     * 删除应用授权
     */

    public void deleteIntfaceAppAuthController(Long intfaceAppId);

    /**
     * 查看是否有应用授权
     */
    public IntfaceAppAuth cheHaveIntfaceAppAuth(IntfaceAppAuth intfaceAppAuth);

}
