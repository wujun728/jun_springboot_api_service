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
import java.text.SimpleDateFormat
import java.util.List;
import java.util.Map;

import groovy.json.*;

import com.nh.micro.dao.mapper.MicroMapperTemplate
import com.nh.micro.db.*;


class MatchRuleTime  extends MicroMapperTemplate{
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
		Integer min=null;
		Integer max=null;
		if(tempMin!=null && !"".equals(tempMin)){
			min=new Integer(tempMin);
		}
		if(tempMax!=null && !"".equals(tempMax)){
			max=new Integer(tempMax);
		}

		String sTimeStr=sourceInfo.get(MatchConst.DataInfo.time);
		if(sTimeStr==null || "".equals(sTimeStr)){
			contextMap.put("msg", "挂单起始时间值为空,导致时间范围规则匹配失败");
			return false;
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date sDate=sdf.parse(sTimeStr);

		List resultList=new ArrayList();
		for(Map targetMap:targetList){
			String tempTime=targetMap.get(MatchConst.DataInfo.time);
			if(tempTime==null && "".equals(tempTime)){
				continue;
			}
			Date targetTime=sdf.parse(tempTime);
			
			long days=(sDate.getTime()-targetTime.getTime())/(1000*3600*24);
			
			
			if(min!=null){
				if(days<min){
					continue;
				}
			}
			if(max!=null){
				if(days>max){
					continue;
				}
			}
			resultList.add(targetMap);

		}
		if(resultList.size()<=0){
			System.out.println("match_rule_rate status="+false+" ruleId="+ruleId);
			contextMap.put("msg", "时间范围规则匹配失败");
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
