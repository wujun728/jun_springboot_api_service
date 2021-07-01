package com.vacomall.controller;

import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.common.anno.TokenSecurity;
import com.vacomall.common.bean.Response;
import com.vacomall.common.util.ValidateUtil;
import com.vacomall.entity.Blog;
import com.vacomall.service.IBlogService;
/**
 * 博客控制器
 * @author Gaojun.Zhou
 * @date 2016年12月30日 上午11:26:00
 */
@RestController
@RequestMapping("/blog")
public class BlogController {  
	
	@Autowired private  IBlogService blogService;
	/**
	 * 分页查询博客
	 */
	@ApiOperation(value = "分页查询博客",response = Response.class)
    @RequestMapping(value = "/list",method=RequestMethod.GET)  
    public  Response list(@RequestParam (required = true) Integer pageIndex,@RequestParam (defaultValue="10")Integer size){
		
		Page<Blog> pageData = blogService.selectPage(new Page<Blog>(pageIndex, size));
		return new Response().success(pageData);
		
    }
	/**
	 * 分页多表连接查询博客
	 */
	@ApiOperation(value = "分页多表连接查询博客",response = Response.class)
    @RequestMapping(value = "/page",method=RequestMethod.GET)  
    public  Response page(@RequestParam (required = true) Integer pageIndex,@RequestParam (defaultValue="10")Integer size){
		Page<Map<Object, Object>> pageData = blogService.selectBlogPage(new Page<Map<Object,Object>>());
		return new Response().success(pageData);
		
    }
	
	/**
	 * 获取一条博客数据
	 */
	@ApiOperation(value = "获取一条博客数据",response = Response.class)
    @RequestMapping(value = "/get/{id}",method=RequestMethod.GET)  
    public  Response get(@PathVariable("id") String id){
		return new Response().success(blogService.selectById(id));
		
    }
	/**
	 * 新增博客
	 */
	@ApiOperation(value = "新增博客",response = Blog.class)
    @RequestMapping(value = "/add",method=RequestMethod.POST)  
    public  Response add(@Valid Blog blog,BindingResult result){
		if(result.hasErrors()){
			return new Response().failure("error",ValidateUtil.toStringJson(result));
		}
		blog.setCreateTime(new Date());
		blogService.insert(blog);
		return new Response().success();
    }
	/**
	 * 更新博客
	 */
	@ApiOperation(value = "更新博客",response = Blog.class)
    @RequestMapping(value = "/update",method=RequestMethod.POST)  
    public  Response update(@Valid Blog blog,BindingResult result){
		if(result.hasErrors()){
			return new Response().failure("error",ValidateUtil.toStringJson(result));
		}
		blogService.updateById(blog);
		return new Response().success();
    }
	/**
	 * 删除博客
	 */
	@TokenSecurity //表示需要登录进行Token校验
	@ApiOperation(value = "删除博客",response = Response.class)
    @RequestMapping(value = "/delete/{id}",method=RequestMethod.DELETE)  
    public  Response delete(@PathVariable("id") String id){
		blogService.deleteById(id);
		return new Response().success();
    }
	
}
