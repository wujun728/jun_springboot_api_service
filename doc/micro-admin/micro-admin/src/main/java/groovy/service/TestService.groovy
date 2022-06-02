
package groovy.service;


import javax.annotation.Resource;


import foo.dao.TestDao;
import foo.dto.MicroTestDto;
import foo.repository.*;
import groovy.json.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.nh.micro.dao.mapper.DefaultPageInfo;
import com.nh.micro.dao.mapper.InjectDao;
import com.nh.micro.rule.engine.core.plugin.MicroAop;
import com.nh.micro.rule.engine.core.plugin.MicroDefaultLogProxy;
import com.nh.micro.template.MicroDbProxy;
import com.nh.micro.template.MicroServiceTemplateSupport;
import com.nh.micro.template.MicroTMProxy;



/**
 * 
 * @author ninghao
 *
 */
//@MicroAop(name=[MicroDefaultLogProxy.class,MicroTMProxy.class,MicroDbProxy.class], property=["","",""])
class TestService  {  
	
	@Autowired
	public TestDao testDao;
 

	public MicroTestDto test(String id){
		
		Map paramMap=new HashMap();
		paramMap.put("id", id);
		MicroTestDto microTestDto=testDao.queryInfoById(paramMap);

/*		List<MicroTestDto> list=testDao.getInfoListAllMapper(microTestDto, ""); 
		
		DefaultPageInfo pageInfo=new DefaultPageInfo();
		pageInfo.setPageNo(1);
		List<MicroTestDto> retList=testDao.queryInfosByPage(paramMap, pageInfo);
		Long total=pageInfo.getTotal();
		System.out.println("total="+total);*/
		return microTestDto;
		
		
	}
	
	public void create(){
		Map paramMap=new HashMap();
		paramMap.put("meta_key", (new Date()).toString());
		testDao.insertInfo(paramMap);
	}

}
