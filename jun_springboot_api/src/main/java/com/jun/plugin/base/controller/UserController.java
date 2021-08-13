package com.jun.plugin.base.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jun.plugin.base.service.Userservice;
import com.jun.plugin.base.utils.DataResult;
import com.jun.plugin.base.vo.req.LoginReqVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * @ClassName: UserController
 * TODO:类文件简单描述
 * @author Wujun
 * @Version: 0.0.1
 */
@RestController
@RequestMapping("/api")
@Api(tags = "用户模块相关接口")
public class UserController {
    @Autowired
    private Userservice userservice;
    @PostMapping("/user/login")
    @ApiOperation(value = "登录接口")
    public DataResult login(@RequestBody LoginReqVO vo){
        DataResult result=DataResult.success();
        result.setData(userservice.login(vo));
        return result;
    }
    @GetMapping("/user/{id}")
    @ApiOperation(value = "获取用户详情")
    @RequiresPermissions("sys:user:detail")
    public DataResult detail(@PathVariable("id") @ApiParam(value = "用户Id") String id){
        DataResult result=DataResult.success();
        result.setData(userservice.detail(id));
        return result;
    }
}
