package com.jun.plugin.base.service;

import com.jun.plugin.base.entity.SysUser;
import com.jun.plugin.base.vo.req.LoginReqVO;
import com.jun.plugin.base.vo.resp.LoginRespVO;

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
