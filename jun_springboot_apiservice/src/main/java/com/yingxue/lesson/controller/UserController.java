package com.yingxue.lesson.controller;

import com.yingxue.lesson.service.Userservice;
import com.yingxue.lesson.utils.DataResult;
import com.yingxue.lesson.vo.req.LoginReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @ClassName: UserController
 * TODO:类文件简单描述
 * @Author: 小霍
 * @UpdateUser: 小霍
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
