package com.test.service;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.test.dao.TestCloudDao;
import com.test.dto.TestDto;

@Service
public class TestCloudService {
	@Autowired
	public TestCloudDao testDao;
	
	
	
	@Transactional
	public List<Map> testQuery4Name(String name){
		TestDto testDto=testDao.selectById(5);
		List<TestDto> dtoList = testDao.selectList(null);
		Pagination page=new Pagination();
		page.setOffset(0).setLimit(10);
		List<TestDto> dtoListPage=testDao.selectPage(page, null);
		//System.out.println(testDto.toString());
		Pagination page2=new Pagination();
		page2.setOffset(0).setLimit(10);
		List<Map> retList=testDao.getInfo4Name(page2,name);
		return retList;
	}
}
