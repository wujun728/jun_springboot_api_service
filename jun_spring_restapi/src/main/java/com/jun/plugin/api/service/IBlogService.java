package com.jun.plugin.api.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.jun.plugin.api.entity.Blog;

/**
 *
 * Blog 表数据服务层接口
 *
 */
public interface IBlogService extends IService<Blog> {

	Page<Map<Object, Object>> selectBlogPage(Page<Map<Object, Object>> page);
	

}