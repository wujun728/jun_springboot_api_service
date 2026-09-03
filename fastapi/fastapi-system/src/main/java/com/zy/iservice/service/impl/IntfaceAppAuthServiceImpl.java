package com.zy.iservice.service.impl;

import com.zy.iservice.domain.IntfaceAppAuth;
import com.zy.iservice.domain.bo.InsertUpdateIntfaceAppAuthBO;
import com.zy.iservice.domain.bo.SelectIntfaceAppAuthListBO;
import com.zy.iservice.mapper.IntfaceAppAuthMapper;
import com.zy.iservice.service.IntfaceAppAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 接口应用授权Service业务层处理
 *
 * @author javachen
 * @date 2022-11-19
 */
@Service
public class IntfaceAppAuthServiceImpl implements IntfaceAppAuthService
{
    @Autowired
    private IntfaceAppAuthMapper intfaceAppAuthMapper;

    /**
     * 查询接口应用授权列表
     *
     * @param selectIntfaceAppAuthListBO
     * @return 接口应用授权
     */
    @Override
    public List<SelectIntfaceAppAuthListBO> selectIntfaceAppAuthList(SelectIntfaceAppAuthListBO selectIntfaceAppAuthListBO)
    {
        List<SelectIntfaceAppAuthListBO> list = intfaceAppAuthMapper.selectIntfaceAppAuthList(selectIntfaceAppAuthListBO);
        return list;
    }

    /**
     * 新增接口应用授权
     *
     * @param insertUpdateIntfaceAppAuthBO 接口应用授权
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertIntfaceAppAuth(InsertUpdateIntfaceAppAuthBO insertUpdateIntfaceAppAuthBO)
    {
        for (int i = 0; i < insertUpdateIntfaceAppAuthBO.getSelectIntfaceId().length; i++) {
            IntfaceAppAuth intfaceAppAuth = new IntfaceAppAuth();
            intfaceAppAuth.setIntfaceAppId(insertUpdateIntfaceAppAuthBO.getIntfaceAppId());
            intfaceAppAuth.setSelectIntfaceId(insertUpdateIntfaceAppAuthBO.getSelectIntfaceId()[i]);
            intfaceAppAuthMapper.insertIntfaceAppAuth(intfaceAppAuth);
        }
    }

    /**
     * 取消授权
     */
    @Override
    public void cancleAuth(InsertUpdateIntfaceAppAuthBO insertUpdateIntfaceAppAuthBO){
        intfaceAppAuthMapper.cancleAuth(insertUpdateIntfaceAppAuthBO);
    }

    /**
     * 删除应用授权
     */
    @Override
    public void deleteIntfaceAppAuthController(Long id){
        intfaceAppAuthMapper.deleteIntfaceAppAuthController(id);
    }

    /**
     * 查看是否有应用授权
     */
    @Override
    public IntfaceAppAuth cheHaveIntfaceAppAuth(IntfaceAppAuth intfaceAppAuth){
        return intfaceAppAuthMapper.cheHaveIntfaceAppAuth(intfaceAppAuth);
    }

}
