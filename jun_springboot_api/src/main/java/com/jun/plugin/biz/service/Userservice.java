package com.jun.plugin.biz.service;

import com.jun.plugin.biz.entity.SysUser;
import com.jun.plugin.biz.vo.req.LoginReqVO;
import com.jun.plugin.biz.vo.resp.LoginRespVO;

/**
 * @ClassName: Userservice
 * TODO:类文件简单描述
 * @Author: 小霍
 * @UpdateUser: 小霍
 * @Version: 0.0.1
 */
public interface Userservice {
    LoginRespVO login(LoginReqVO vo);

    SysUser detail(String id);
}
