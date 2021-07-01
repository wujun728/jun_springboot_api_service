package com.yingxue.lesson.service.impl;

import com.yingxue.lesson.entity.SysUser;
import com.yingxue.lesson.exception.BusinessException;
import com.yingxue.lesson.mapper.SysUserMapper;
import com.yingxue.lesson.service.RedisService;
import com.yingxue.lesson.service.Userservice;
import com.yingxue.lesson.utils.PasswordUtils;
import com.yingxue.lesson.vo.req.LoginReqVO;
import com.yingxue.lesson.vo.resp.LoginRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: UserServiceImpl
 * TODO:类文件简单描述
 * @Author: 小霍
 * @UpdateUser: 小霍
 * @Version: 0.0.1
 */
@Service
public class UserServiceImpl implements Userservice {
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
        redisService.set(loginRespVO.getSessionId(),loginRespVO.getId(),60, TimeUnit.MINUTES);
        return loginRespVO;
    }

    @Override
    public SysUser detail(String id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }
}
