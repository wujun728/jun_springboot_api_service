package com.zy.iservice.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.zy.common.utils.DateUtils;
import com.zy.common.utils.SecurityUtils;
import com.zy.iservice.domain.IntfaceAppApplyFor;
import com.zy.iservice.mapper.IntfaceAppApplyForMapper;
import com.zy.iservice.service.IntfaceAppApplyForService;
import com.zy.iservice.service.IntfaceAppAuthService;
import com.zy.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口应用申请Service业务层处理
 *
 * @author javachen
 * @date 2022-11-19
 */
@Service
public class IntfaceAppApplyForServiceImpl implements IntfaceAppApplyForService
{
    @Autowired
    private IntfaceAppApplyForMapper intfaceAppApplyForMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IntfaceAppAuthService intfaceAppAuthService;


    /**
     * 查询接口应用申请
     *
     * @param id 接口应用申请主键
     * @return 接口应用申请
     */
    @Override
    public IntfaceAppApplyFor selectIntfaceAppApplyForById(Long id)
    {
        return intfaceAppApplyForMapper.selectIntfaceAppApplyForById(id);
    }

    /**
     * 查询接口应用申请列表
     *
     * @param intfaceAppApplyFor 接口应用申请
     * @return 接口应用申请
     */
    @Override
    public List<IntfaceAppApplyFor> selectIntfaceAppApplyForList(IntfaceAppApplyFor intfaceAppApplyFor)
    {
        //如果不是admin的话,只能查看自己创建的应用
        if(!SecurityUtils.getLoginUser().getUser().isAdmin()){
            Map<String, Object> params = new HashMap<>();
            params.put("dataScope"," and create_id = "+SecurityUtils.getLoginUser().getUserId());
            intfaceAppApplyFor.setParams(params);
        }
        return intfaceAppApplyForMapper.selectIntfaceAppApplyForList(intfaceAppApplyFor);
    }

    /**
     * 新增接口应用申请
     *
     * @param intfaceAppApplyFor 接口应用申请
     * @return 结果
     */
    @Override
    public int insertIntfaceAppApplyFor(IntfaceAppApplyFor intfaceAppApplyFor)
    {
        Integer expireDay = Integer.parseInt(configService.selectConfigByKey("sys.authCode.expire.day"));

        intfaceAppApplyFor.setCreateId(SecurityUtils.getUserId());
        intfaceAppApplyFor.setCreateBy(SecurityUtils.getUsername());
        intfaceAppApplyFor.setCreateTime(DateUtils.getNowDate());
        intfaceAppApplyFor.setUpdateBy(SecurityUtils.getUsername());
        intfaceAppApplyFor.setUpdateTime(DateUtils.getNowDate());
        intfaceAppApplyFor.setClientId(RandomUtil.randomString(19));
        intfaceAppApplyFor.setClientSecret(RandomUtil.randomString(19));
        intfaceAppApplyFor.setClientStartTime(new Date());
        intfaceAppApplyFor.setClientExpireDay(expireDay);

        return intfaceAppApplyForMapper.insertIntfaceAppApplyFor(intfaceAppApplyFor);
    }

    /**
     * 修改接口应用申请
     *
     * @param intfaceAppApplyFor 接口应用申请
     * @return 结果
     */
    @Override
    public int updateIntfaceAppApplyFor(IntfaceAppApplyFor intfaceAppApplyFor)
    {
        intfaceAppApplyFor.setUpdateBy(SecurityUtils.getUsername());
        intfaceAppApplyFor.setUpdateTime(DateUtils.getNowDate());
        intfaceAppApplyFor.setClientId(null);
        intfaceAppApplyFor.setClientSecret(null);
        intfaceAppApplyFor.setName(null);

        return intfaceAppApplyForMapper.updateIntfaceAppApplyFor(intfaceAppApplyFor);
    }

    /**
     * 批量删除接口应用申请
     *
     * @param ids 需要删除的接口应用申请主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteIntfaceAppApplyForByIds(Long[] ids)
    {
        intfaceAppAuthService.deleteIntfaceAppAuthController(ids[0]);
        return intfaceAppApplyForMapper.deleteIntfaceAppApplyForByIds(ids);
    }

    /**
     * 删除接口应用申请信息
     *
     * @param id 接口应用申请主键
     * @return 结果
     */
    @Override
    public int deleteIntfaceAppApplyForById(Long id)
    {
        return intfaceAppApplyForMapper.deleteIntfaceAppApplyForById(id);
    }

    /**
     * 查询密钥
     */
    @Override
    public IntfaceAppApplyFor selectSecret(Long id){
        IntfaceAppApplyFor intfaceAppApplyFor = intfaceAppApplyForMapper.selectSecret(id);

        //计算两个日期时间差
        int different = DateUtils.differentDaysByMillisecond(intfaceAppApplyFor.getClientStartTime(), new Date());

        Integer expireDay = intfaceAppApplyFor.getClientExpireDay();

        //如果超出令牌有效期,重置密钥
        if(different>expireDay){
            IntfaceAppApplyFor intfaceAppApplyForTemp = resetSecret(id);
            intfaceAppApplyForTemp.setClientEndTime(DateUtil.offsetDay(intfaceAppApplyForTemp.getClientStartTime(), intfaceAppApplyForTemp.getClientExpireDay()));
            return intfaceAppApplyForTemp;
        }

        intfaceAppApplyFor.setClientEndTime(DateUtil.offsetDay(intfaceAppApplyFor.getClientStartTime(), intfaceAppApplyFor.getClientExpireDay()));

        return intfaceAppApplyFor;
    }

    /**
     * 重置密钥
     */
    @Override
    public IntfaceAppApplyFor resetSecret(Long id){
        Integer expireDay = Integer.parseInt(configService.selectConfigByKey("sys.authCode.expire.day"));

        IntfaceAppApplyFor intfaceAppApplyFor = selectIntfaceAppApplyForById(id);
        intfaceAppApplyFor.setClientId(RandomUtil.randomString(19));
        intfaceAppApplyFor.setClientSecret(RandomUtil.randomString(19));
        intfaceAppApplyFor.setClientStartTime(new Date());
        intfaceAppApplyFor.setClientExpireDay(expireDay);

        intfaceAppApplyForMapper.updateIntfaceAppApplyFor(intfaceAppApplyFor);
        return intfaceAppApplyFor;
    }


    /**
     * 根据密钥查询应用
     */
    @Override
    public IntfaceAppApplyFor selectAppApplyBySecret(IntfaceAppApplyFor intfaceAppApplyFor){
        return intfaceAppApplyForMapper.selectAppApplyBySecret(intfaceAppApplyFor);
    }

}
