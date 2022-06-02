package groovy.service

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil

import foo.cons.MatchConst

import com.nh.micro.rule.engine.core.GContextParam;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import groovy.json.*;

import com.nh.micro.dao.mapper.MicroMapperTemplate
import com.nh.micro.db.*;


class MatchRuleProduct  extends MicroMapperTemplate{
	private Map queryOneConfig(String ruleId){
		Map rowMap=getInfoByBizIdService(ruleId,"match_rule","rule_id");

		String ruleParamStr=rowMap.get("rule_param");
		Map ruleParam=new JsonSlurper().parseText(ruleParamStr);
		return ruleParam;
		
	}
	private String queryGroovyName(String ruleId){
		Map rowMap=getInfoByBizIdService(ruleId,"match_rule","rule_id");
		String groovyName=rowMap.get("groovy_name");

		return groovyName;
		
	}
	public boolean matchInfo(String ruleId,List ruleList,int index,String dirFlag,Map sourceInfo,List targetList,Map contextMap){
		Map configMap=queryOneConfig(ruleId);
		List<Map> pipeiList=configMap.get("pipeiList");

		String sourceProduct=sourceInfo.get(MatchConst.DataInfo.productClass);
		String sourcePlatform=sourceInfo.get(MatchConst.DataInfo.platformClass);
		
		List resultList=new ArrayList();
		for(Map targetMap:targetList){
			String targetProduct=targetMap.get(MatchConst.DataInfo.productClass);
			String targetPlatform=targetMap.get(MatchConst.DataInfo.platformClass);
			boolean flag=checkInfo(sourceProduct,sourcePlatform,targetProduct,targetPlatform,dirFlag,pipeiList);
			if(flag==true){
				resultList.add(targetMap);
			}
		}
		if(resultList.size()<=0){
			contextMap.put("msg", "产品类型映射规则匹配失败");
			System.out.println("match_rule_amount status="+false+" ruleId="+ruleId);
			return false;
		}
		boolean status=false;
		int nextIndex=index+1;
		int totalSize=ruleList.size();
		if(nextIndex<totalSize){
			String nextRuleId=ruleList.get(nextIndex);
			String groovyName=queryGroovyName(nextRuleId);
			status=GroovyExecUtil.execGroovyRetObj(groovyName,"matchInfo",nextRuleId,ruleList,nextIndex,dirFlag,sourceInfo,resultList,contextMap);

		}else{
			status=GroovyExecUtil.execGroovyRetObj("match_rule_default","matchInfo","",ruleList,nextIndex,dirFlag,sourceInfo,resultList,contextMap);
			
		}
		//System.out.println("match_rule_product status="+status+" ruleId="+ruleId);
		return status;
	}

	
	private boolean checkInfo(
		String sourceProduct,String sourcePlatform,String targetProduct,String targetPlatform,
		String dirFlag,List pipeiList){
		
		String buyProduct="";
		String saleProduct="";
		String buyPlatform="";
		String salePlatform="";
		if(dirFlag.equals(MatchConst.MatchDirector.B2S)){
			buyProduct=sourceProduct;
			buyPlatform=sourcePlatform;
			saleProduct=targetProduct;
			salePlatform=targetPlatform;
		}else{
			buyProduct=targetProduct;
			buyPlatform=targetPlatform;
			saleProduct=sourceProduct;
			salePlatform=sourcePlatform;
		}
		if(buyProduct==null){
			buyProduct="";
		}
		if(buyPlatform==null){
			buyPlatform="";
		}
		if(saleProduct==null){
			saleProduct="";
		}
		if(salePlatform==null){
			salePlatform="";
		}

		for(Map rowMap:pipeiList){
			String bp=rowMap.get("buyProduct");
			String bt=rowMap.get("buyPlatform");
			String sp=rowMap.get("saleProduct");
			String st=rowMap.get("salePlatform");
			
			if(bp==null){
				bp="";
			}
			if(bt==null){
				bt="";
			}
			if(sp==null){
				sp="";
			}
			if(st==null){
				st="";
			}
			
			if(bp.equals(buyProduct) || bp.equals("")){
				if(bt.equals(buyPlatform) || bt.equals("")){
					if(sp.equals(saleProduct) || sp.equals("")){
						if(st.equals(salePlatform) || st.equals("")){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
