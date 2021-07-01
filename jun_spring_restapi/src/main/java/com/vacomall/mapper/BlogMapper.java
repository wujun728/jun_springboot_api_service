package com.vacomall.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.entity.Blog;

/**
 *
 * Blog 表数据库控制层接口
 *
 */
public interface BlogMapper extends BaseMapper<Blog> {

	List<Map<Object, Object>> selectMap(Page<Map<Object, Object>> page);

}