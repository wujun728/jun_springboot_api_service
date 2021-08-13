package com.jun.plugin.base.service.impl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jun.plugin.base.entity.SysUser;
import com.jun.plugin.base.exception.BusinessException;
import com.jun.plugin.base.mapper.SysUserMapper;
import com.jun.plugin.base.service.RedisService;
import com.jun.plugin.base.service.Userservice;
import com.jun.plugin.base.utils.PasswordUtils;
import com.jun.plugin.base.vo.req.LoginReqVO;
import com.jun.plugin.base.vo.resp.LoginRespVO;

/**
 * @ClassName: UserServiceImpl
 * TODO:类文件简单描述
 * @author Wujun
 * @Version: 0.0.1
 */
@Service
public class UserServiceImpl implements Userservice {
    @Value("${session.timeout}")
    private Integer sessionTimeout;
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private RedisService redisService;
    
    @Override
    public LoginRespVO login(LoginReqVO vo) {
        SysUser userByUserName = sysUserMapper.getUserByUserName(vo.getUsername());
        if(null==userByUserName){
            throw new BusinessException(4000002,"该账号不存在");
        }
        if(userByUserName.getStatus()==2){
            throw new BusinessException(4000002,"该账号被锁定，请联系系统管理员");
        }
        if(!PasswordUtils.matches(userByUserName.getSalt(),vo.getPassword(),userByUserName.getPassword())){
            throw new BusinessException(4000002,"用户名密码不匹配");
        }
        LoginRespVO loginRespVO=new LoginRespVO();
        loginRespVO.setId(userByUserName.getId());
        loginRespVO.setSessionId(UUID.randomUUID().toString());
        //把凭证存入到reids
        redisService.set(loginRespVO.getSessionId(),loginRespVO.getId(),sessionTimeout, TimeUnit.MINUTES);
        return loginRespVO;
    }

    @Override
    public SysUser detail(String id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }
}
