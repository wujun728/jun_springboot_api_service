package groovy.controller

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional

import groovy.json.*;
import groovy.template.MicroControllerTemplate
import groovy.template.MicroMvcTemplate;
import java.util.List;
import java.util.Map;
import com.nh.micro.controller.MicroUrlMapping
import com.nh.micro.rule.engine.core.*;
import com.nh.micro.rule.engine.core.plugin.MicroAop
import com.nh.micro.rule.engine.core.plugin.MicroDefaultLogProxy
import com.nh.micro.template.MicroDbProxy
import com.nh.micro.template.MicroTMProxy

@MicroUrlMapping(name="/api")
@MicroAop(name=[MicroDefaultLogProxy.class,MicroTMProxy.class,MicroDbProxy.class], property=["","",""])
class MatchMvcApi extends MicroControllerTemplate{

	@MicroUrlMapping(name="/createBuyOrder")
	@Transactional
	public void createBuyOrder(HttpServletRequest request, HttpServletResponse response){
		Map reqMap=getRequestParamMap(request);
		Map createMap=new HashMap();
		
		createMap.put("user_name", request.getParameter("user_name"));
		createMap.put("lender_rate", request.getParameter("lender_rate"));
		createMap.put("account_amount", request.getParameter("account_amount"));
		createMap.put("product_class", request.getParameter("product_class"));
		createMap.put("platform_class", request.getParameter("platform_class"));
		createMap.put("lender_start_date", request.getParameter("lender_start_date"));
		
		createMap.put("input_amount", request.getParameter("input_amount"));
		createMap.put("create_time", "now()");
		createMap.put("match_priority", request.getParameter("match_priority"));
		createMap.put("lender_no", request.getParameter("lender_no"));
		createMap.put("user_id", request.getParameter("user_id"));
		
		int status=createInfoService(createMap,"match_buy");
		String orderId=createMap.get("id");
		Map retMap=new HashMap();
		if(status==1){
			retMap.put("status", 0);
			retMap.put("code", "success");
			retMap.put("orderId", orderId);
		}else{
			retMap.put("status", 1);
			retMap.put("code", "error");
		}
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		response.getOutputStream().write(retStr.getBytes("UTF-8"));

	}
	
	
	@MicroUrlMapping(name="/createSaleOrder")
	@Transactional
	public void createSaleOrder(HttpServletRequest request, HttpServletResponse response){
		Map reqMap=getRequestParamMap(request);
		Map createMap=new HashMap();
		
		createMap.put("user_name", request.getParameter("user_name"));
		createMap.put("borrow_rate", request.getParameter("borrow_rate"));
		createMap.put("account_amount", request.getParameter("account_amount"));
		createMap.put("product_class", request.getParameter("product_class"));
		createMap.put("platform_class", request.getParameter("platform_class"));
		createMap.put("borrow_start_date", request.getParameter("borrow_start_date"));
		
		createMap.put("input_amount", request.getParameter("input_amount"));
		createMap.put("create_time", "now()");
		createMap.put("match_priority", request.getParameter("match_priority"));
		createMap.put("borrow_no", request.getParameter("borrow_no"));
		createMap.put("user_id", request.getParameter("user_id"));
		
		int status=createInfoService(createMap,"match_sale");
		String orderId=createMap.get("id");
		Map retMap=new HashMap();
		if(status==1){
			retMap.put("status", 0);
			retMap.put("code", "success");
			retMap.put("orderId", orderId);
		}else{
			retMap.put("status", 1);
			retMap.put("code", "error");
		}
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		response.getOutputStream().write(retStr.getBytes("UTF-8"));

	}
	
	@MicroUrlMapping(name="/execMatch")
	@Transactional
	public void execMatch(HttpServletRequest request, HttpServletResponse response){
		HttpServletRequest httpRequest = request;
		Map reportMap=new HashMap();
		String rules=httpRequest.getParameter("rules");
		if(rules==null || "".equals(rules)){
			rules="all";
		}
		String recordId=httpRequest.getParameter("recordId");
		String dirFlag=httpRequest.getParameter("dirFlag");


		String matchId=UUID.randomUUID().toString();
		String[] ruleArray=rules.split(",");
		List ruleList=new ArrayList();
		if(rules.equals("all")){
			List<Map> queryList=getInfoListAllService(new HashMap(),"match_rule",new HashMap());
			for(Map row:queryList){
				ruleList.add(row.get("rule_id"));
			}
		}else{
			ruleList=ruleArray.toList();
		}
		
		Map contextMap=new HashMap();
		contextMap.put("msg", "");
		boolean status=GroovyExecUtil.execGroovyRetObj("mem_match","matchRule",ruleList,matchId,recordId,dirFlag,contextMap);
		if(status==true){
			Map updateMap=new HashMap();
			updateMap.put("match_time", "now()");
			updateInfoByIdService(recordId,"match_buy",updateMap);
		}
		
		Map retMap=new HashMap();
		if(status==true){
			retMap.put("status", 0);
			retMap.put("code", "success");
			retMap.put("matchId", matchId);
		}else{
			retMap.put("status", 1);
			retMap.put("code", "error");
			retMap.putAt("msg", contextMap.get("msg"));
			retMap.put("matchId", matchId);
		}
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		response.getOutputStream().write(retStr.getBytes("UTF-8"));

	}
	
	@MicroUrlMapping(name="/queryMatchResult")
	public void queryMatchResult(HttpServletRequest request, HttpServletResponse response){

		Map queryMap=new HashMap();
		
		queryMap.put("match_id", request.getParameter("match_id"));
		queryMap.put("buy_id", request.getParameter("buy_id"));
		queryMap.put("sale_id", request.getParameter("sale_id"));

		Map sortMap=new HashMap();
		sortMap.put("sort", "create_time");
		sortMap.put("order", "desc");
		
		List retList=getInfoListAllService(queryMap,"match_sale",sortMap);
		
		Map retMap=new HashMap();

			retMap.put("status", 0);
			retMap.put("code", "success");
			retMap.put("data", retList);

		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		response.getOutputStream().write(retStr.getBytes("UTF-8"));

	}
	
		
}
