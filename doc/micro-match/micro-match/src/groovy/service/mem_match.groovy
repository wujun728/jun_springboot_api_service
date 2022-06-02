package groovy.service

import java.math.BigDecimal;
import java.util.List
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.nh.micro.dao.mapper.MicroMapperTemplate
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;
import com.nh.micro.template.MicroColObj;

import foo.cons.MatchConst
import groovy.json.JsonSlurper;
import groovy.template.MicroMvcTemplate;


class MemMatch extends MicroMapperTemplate {
	public void execMatch(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		Map reportMap=new HashMap();
		String rules=httpRequest.getParameter("rules");
		String flag=httpRequest.getParameter("directionFlag");
		String dirFlag=null;
		String recordId=null;
		if(flag!=null && flag.equals("buy2sale")){
			recordId=httpRequest.getParameter("buyId");
			dirFlag="buy2sale";
		}else{
			recordId=httpRequest.getParameter("saleId");
			dirFlag="sale2buy";
		}

		String matchId=UUID.randomUUID().toString();
		String[] ruleArray=rules.split(",");
		List ruleList=ruleArray.toList();
		Map contextMap=new HashMap();
		contextMap.put("msg", "");
		boolean status=matchRule(ruleList,matchId,recordId,dirFlag,contextMap);
		gOutputParam.setResultMsg(contextMap.get("msg"));
		if(status==false){
			gOutputParam.setResultStatus(1);
		}

		return;
	}

	public boolean matchRule(List ruleList,String matchId,String recordId,String dirFlag,Map contextMap){
		Map sourceInfo=null;
		List targetList=null;
		if(dirFlag.equals("buy2sale")){
			sourceInfo=buy2saleSource(recordId);
			targetList=buy2saleList(recordId);
		}else{
			sourceInfo=sale2buySource(recordId);
			targetList=sale2buyList(recordId);
		}

		String firstId=ruleList.get(0);
		String firstGroovyName=queryGroovyName(firstId);
		contextMap.put("matchId", matchId);
		
		if(sourceInfo==null){
			contextMap.put("msg", "待撮合记录为空");
			return false;
		}
		String sourceAmount=sourceInfo.get(MatchConst.DataInfo.amount);
		if(sourceAmount==null || (new BigDecimal(sourceAmount))<=0){
			contextMap.put("msg", "待撮合金额为0");
			return false;
		}
		
		boolean status=GroovyExecUtil.execGroovyRetObj(firstGroovyName, "matchInfo", firstId,ruleList,0,dirFlag,sourceInfo,targetList,contextMap);
		
		return status;
	}
	
	private String queryGroovyName(String ruleId){
		Map rowMap=getInfoByBizIdService(ruleId,"match_rule","rule_id");
		String groovyName=rowMap.get("groovy_name");

		return groovyName;
		
	}
	private Map buy2saleSource(String buyId){
		Map buyInfo=getInfoByIdService(buyId,"match_buy");
		
		Map sourceInfo=new HashMap();
		
		sourceInfo.put(MatchConst.DataInfo.id,buyInfo.get("id"));
		sourceInfo.put(MatchConst.DataInfo.amount,buyInfo.get("account_amount"));
		sourceInfo.put(MatchConst.DataInfo.productClass,buyInfo.get("product_class"));
		sourceInfo.put(MatchConst.DataInfo.platformClass,buyInfo.get("platform_class"));
		sourceInfo.put(MatchConst.DataInfo.rate,buyInfo.get("lender_rate"));
		sourceInfo.put(MatchConst.DataInfo.time, buyInfo.get("lender_start_date"))
		return sourceInfo;
	}
	private List buy2saleList(String buyId){

		List<Map> nlist=new ArrayList();
		List<Map> listInfo=getInfoListAllServiceBySql("select * from match_sale where account_amount>0 order by match_priority desc, create_time desc");

		for(Map row:listInfo){
			Map targetInfo=new HashMap();
			targetInfo.put(MatchConst.DataInfo.id,row.get("id"));
			targetInfo.put(MatchConst.DataInfo.amount,row.get("account_amount"));
			targetInfo.put(MatchConst.DataInfo.productClass,row.get("product_class"));
			targetInfo.put(MatchConst.DataInfo.platformClass,row.get("platform_class"));
			targetInfo.put(MatchConst.DataInfo.rate,row.get("borrow_rate"));
			targetInfo.put(MatchConst.DataInfo.time,row.get("borrow_start_date"));
			nlist.add(targetInfo);

		}	
		return nlist;
		
	}


	private Map sale2buySource(String saleId){
		Map saleInfo=getInfoByIdService(saleId,"match_sale");
		
		Map sourceInfo=new HashMap();
		
		sourceInfo.put(MatchConst.DataInfo.id,saleInfo.get("id"));
		sourceInfo.put(MatchConst.DataInfo.amount,saleInfo.get("account_amount"));
		sourceInfo.put(MatchConst.DataInfo.productClass,saleInfo.get("product_class"));
		sourceInfo.put(MatchConst.DataInfo.platformClass,saleInfo.get("platform_class"));
		sourceInfo.put(MatchConst.DataInfo.rate,saleInfo.get("borrow_rate"));
		sourceInfo.put(MatchConst.DataInfo.time, saleInfo.get("borrow_start_date"));
		return sourceInfo;
	}
	private List sale2buyList(String saleId){

		List<Map> nlist=new ArrayList();
		List<Map> listInfo=getInfoListAllServiceBySql("select * from match_buy where account_amount>0 order by match_priority desc, create_time desc");

		for(Map row:listInfo){
			Map targetInfo=new HashMap();
			targetInfo.put(MatchConst.DataInfo.id,row.get("id"));
			targetInfo.put(MatchConst.DataInfo.amount,row.get("account_amount"));
			targetInfo.put(MatchConst.DataInfo.productClass,row.get("product_class"));
			targetInfo.put(MatchConst.DataInfo.platformClass,row.get("platform_class"));
			targetInfo.put(MatchConst.DataInfo.rate,row.get("lender_rate"));
			targetInfo.put(MatchConst.DataInfo.time, row.get("lender_start_date"));
			nlist.add(targetInfo);

		}
		return nlist;
		
	}

	
}
