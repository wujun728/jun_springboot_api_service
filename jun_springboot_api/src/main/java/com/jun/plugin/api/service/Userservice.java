package com.jun.plugin.api.service;

import com.jun.plugin.api.entity.SysUser;
import com.jun.plugin.api.vo.req.LoginReqVO;
import com.jun.plugin.api.vo.resp.LoginRespVO;

/**
 * @ClassName: Userservice
 * TODO:类文件简单描述
 * @author Wujun
 * @Version: 0.0.1
 */
public interface Userservice {
    LoginRespVO login(LoginReqVO vo);

    SysUser detail(String id);
}
