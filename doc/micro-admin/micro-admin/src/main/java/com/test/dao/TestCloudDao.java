package com.test.dao;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.test.dto.TestDto;

@Mapper
public interface TestCloudDao extends BaseMapper<TestDto> {

	public List<Map> getInfo4Name(Pagination page,String name);
}
