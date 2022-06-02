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


class MatchRuleMatchNum  extends MicroMapperTemplate{
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
	
	private boolean resetList4MatchNum(List indexList,int matchNum,int last){
/*		for(int i=0;i<matchNum;i++){
			indexList.add(i);
		}*/
		int totalCol=matchNum-1;
		return resetList(indexList,totalCol,last,totalCol);

	}
	
	private boolean resetList(List indexList,int totalCol,int last,int col){
		int tempLast=last-totalCol+col;
		int index=indexList.get(col);
		if(col==0){
			if(index>=tempLast){
				return false;
			}
		}
		if(index<tempLast){
			indexList.set(col, index+1);
			int j=1;
			for(int i=col+1;i<totalCol;i++){
				j++;
				indexList.set(i, index+1+j);
			}
			return true;
		}
		return resetList(indexList,totalCol,last,col-1);
	}
	
	
	
	private List checkGroup(List indexList, BigDecimal sourceAmount,List targetList,Integer matchNum){
		List resultList=new ArrayList();
		int last=targetList.size()-1;
		if(last<matchNum){
			return resultList;
		}

		
		
		while(1){
			if(indexList.size()==0){
				for(int j=0;j<matchNum;j++){
				 indexList.add(j);
				}
			}else{
				boolean nextStatus=resetList4MatchNum(indexList,matchNum,last);
				if(nextStatus==false){
					return resultList;
				}
			}
			BigDecimal total=0;
			for(int i=0;i<matchNum;i++){
				int index=indexList.get(i);
				Map sourceInfo=targetList.get(index);
				String tempAmount=sourceInfo.get(MatchConst.DataInfo.amount);
				BigDecimal amount=new BigDecimal(tempAmount);
				total=total.add(amount);
			}
			if(total>=sourceAmount){
				break;
			}
/*			boolean s=resetList4MatchNum(indexList,matchNum,last);
			if(s==false){
				return resultList;
			}*/
		}

		for(int i=0;i<matchNum;i++){
			int index=indexList.get(i);
			Map sourceInfo=targetList.get(index);
			resultList.add(sourceInfo);
		}
		return resultList;
		
	}
	
	public boolean matchInfo(String ruleId,List ruleList,int index,String dirFlag,Map sourceInfo,List targetList,Map contextMap){
		Map configMap=queryOneConfig(ruleId);
		String tempMin=configMap.get("min");
		String tempMax=configMap.get("max");
		Integer min=1;
		Integer max=10000;
		if(tempMin!=null && !"".equals(tempMin)){
			min=new Integer(tempMin);
		}
		if(tempMax!=null && !"".equals(tempMax)){
			max=new Integer(tempMax);
		}
		
		int size=targetList.size();
		if(size<min){
			System.out.println("match_rule_amount status="+false+" ruleId="+ruleId);
			return false;
		}
		String tempAmount=sourceInfo.get(MatchConst.DataInfo.amount);
		BigDecimal sourceAmount=new BigDecimal(tempAmount);
		
		for(int i=min;i<=max;i++){
			List indexList=new ArrayList();
/*			for(int j=0;j<i;j++){
				indexList.add(j);
			}*/
			while(1){
				List checkList=checkGroup(indexList, sourceAmount, targetList, i);
				if(checkList.size()<=0){
					break;
				}
				boolean status=false;
				int nextIndex=index+1;
				int totalSize=ruleList.size();
				if(nextIndex<totalSize){
					String nextRuleId=ruleList.get(nextIndex);
					String groovyName=queryGroovyName(nextRuleId);
					status=GroovyExecUtil.execGroovyRetObj(groovyName,"matchInfo",nextRuleId,ruleList,nextIndex,dirFlag,sourceInfo,checkList,contextMap);
					if(status==true){

						return true;
					}
		
				}else{
					status=GroovyExecUtil.execGroovyRetObj("match_rule_default","matchInfo","",ruleList,nextIndex,dirFlag,sourceInfo,checkList,contextMap);
					if(status==true){

						return true;
					}
				}
			}
			
		}
		contextMap.put("msg", "撮合调试规则匹配失败");
		System.out.println("match_rule_matchnum status="+false+" ruleId="+ruleId);
		return false;
	}

}
