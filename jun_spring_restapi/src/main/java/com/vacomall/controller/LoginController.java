package com.vacomall.controller;

import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vacomall.common.anno.Log;
import com.vacomall.common.bean.Response;
import com.vacomall.common.util.ValidateUtil;
import com.vacomall.entity.SysUser;
import com.vacomall.plugin.token.TokenManager;
import com.vacomall.service.ISysUserService;
/**
 * 登录控制器
 * @author Gaojun.Zhou
 * @date 2016年12月30日 上午11:26:17
 */
@RestController
@RequestMapping("/login")
public class LoginController {  
    
	/**
	 * Token服务
	 */
	@Resource(name="redisTokenManager") private TokenManager tokenManager;
	
	@Autowired private  ISysUserService sysUserService;
	
	@Log("用户登录")
	@ApiOperation(value = "用户登陆",response = Response.class)
    @RequestMapping(value = "/doLogin",method=RequestMethod.POST)  
    public  Response hello(@Valid SysUser user, BindingResult result){
		
		if(result.hasErrors()){
			return new Response().failure("error",ValidateUtil.toStringJson(result));
		}
		SysUser sysUser = sysUserService.login(user);
		if(sysUser != null){
			String token = tokenManager.createToken(sysUser.getUserName());
			Map<String, Object> map = new HashMap<String, Object>();
			//返回用户对象
			map.put("user",sysUser);
			//返回给客户端Token,用于安全校验,需要客户端保存起来
			map.put("token", token);
			return new Response().success(map);
		}
		throw new RuntimeException("用户名或密码错误");
    }  
	
	/**
	 * 退出系统
	 * @return
	 */
	@Log("用户退出")
	@ApiOperation(value = "用户登出",response = Response.class)
    @RequestMapping(value = "/logout",method=RequestMethod.POST)  
    public  Response logout(){
		
		return new Response().success("退出成功.");
    }  
}
