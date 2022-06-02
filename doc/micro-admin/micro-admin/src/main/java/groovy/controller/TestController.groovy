package groovy.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.nh.micro.rule.engine.core.plugin.MicroAop;
import com.nh.micro.rule.engine.core.plugin.MicroDefaultLogProxy;
import com.nh.micro.service.InjectGroovy;
import com.nh.micro.template.MicroDbProxy;
import com.nh.micro.template.MicroTMProxy;

import foo.dto.MicroTestDto;
import foo.service.TestService;


class TestController  {  
	@Resource
	public TestService testService;
	

	public Map echo(String id) {
		System.out.println("this is controller proxy");
		MicroTestDto data=testService.test(id);
		Map retMap=new HashMap();
		retMap.put("status", "0");
		retMap.put("data", data);
		return retMap;
	}
	
	public Map create() {
		System.out.println("this is controller proxy");
		testService.create();
		Map retMap=new HashMap();
		retMap.put("status", "0");
		return retMap;
	}
	
	public Map show() {
		System.out.println("this is controller proxy");
		Map retMap=new HashMap();
		retMap.put("text", "this is show0");
		return retMap;
	}
}
