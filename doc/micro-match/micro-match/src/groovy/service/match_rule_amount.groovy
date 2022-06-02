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


class MatchRuleAmount  extends MicroMapperTemplate{
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
		String tempMin=configMap.get("min");
		String tempMax=configMap.get("max");
		BigDecimal min=null;
		BigDecimal max=null;
		if(tempMin!=null && !"".equals(tempMin)){
			min=new BigDecimal(tempMin);
		}
		if(tempMax!=null && !"".equals(tempMax)){
			max=new BigDecimal(tempMax);
		}

		
		List resultList=new ArrayList();
		for(Map targetMap:targetList){
			String tempAmount=targetMap.get(MatchConst.DataInfo.amount);
			BigDecimal targetAmount=new BigDecimal(tempAmount);
			if(min!=null){
				if(targetAmount<min){
					continue;
				}
			}
			if(max!=null){
				if(targetAmount>max){
					targetAmount=max;
				}
			}
			Map resultRow=new HashMap();
			
			resultRow.putAll(targetMap);
			resultRow.put(MatchConst.DataInfo.amount, targetAmount.toString());
/*			resultRow.put(MatchConst.DataInfo.id, targetMap.get(MatchConst.DataInfo.id));
			resultRow.put(MatchConst.DataInfo.amount, targetAmount.toString());
			resultRow.put(MatchConst.DataInfo.productClass, targetMap.get(MatchConst.DataInfo.productClass));
			resultRow.put(MatchConst.DataInfo.platformClass, targetMap.get(MatchConst.DataInfo.platformClass));
			resultRow.put(MatchConst.DataInfo.matchPriority, targetMap.get(MatchConst.DataInfo.matchPriority));
			resultRow.put(MatchConst.DataInfo.rate, targetMap.get(MatchConst.DataInfo.rate));
			resultRow.put(MatchConst.DataInfo.time, targetMap.get(MatchConst.DataInfo.time));*/
			resultList.add(resultRow);

		}
		if(resultList.size()<=0){
			System.out.println("match_rule_amount status="+false+" ruleId="+ruleId);
			contextMap.put("msg", "金额范围规则匹配失败");
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
		
		return status;
	}

}
