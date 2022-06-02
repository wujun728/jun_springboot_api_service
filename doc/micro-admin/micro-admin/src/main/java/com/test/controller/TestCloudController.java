package com.test.controller;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.service.TestCloudService;



@RestController
@RequestMapping(value = "/test")
public class TestCloudController {
	@Autowired
	public TestCloudService testCloudService;

	@Resource
	StringRedisTemplate stringRedisTemplate;
	
    @RequestMapping(value = "/add" ,method = RequestMethod.GET)
    @ResponseBody
    public Integer add(@RequestParam(value="a") Integer a, @RequestParam(value="b") Integer b){
		return a+b;
    	
    }
    
    @RequestMapping(value = "/testQuery4Name" ,method = RequestMethod.GET)
    @ResponseBody
    public List<Map> testQuery4Name(@RequestParam(value="name") String name){
		
    	return testCloudService.testQuery4Name(name);
    	
    }
    
    @RequestMapping(value = "/testRedis" ,method = RequestMethod.GET)
    @ResponseBody
    public String queryRedis(@RequestParam(value="key") String key){
    	String value=stringRedisTemplate.opsForValue().get(key);
		return value;
    	
    }
}
