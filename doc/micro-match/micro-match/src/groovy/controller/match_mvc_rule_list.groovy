package groovy.controller

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import java.sql.PreparedStatement;
import groovy.json.*;
import groovy.template.MicroMvcTemplate;

import com.nh.micro.db.*;
import java.text.SimpleDateFormat;
import java.util.List;

import com.nh.micro.cache.base.*;
import com.nh.micro.db.Cutil;


class MatchMvcRuleList extends MicroMvcTemplate{

	public String tableName="match_rule";
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}

	public void execPublishDefaultRule(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		JdbcTemplate jdbcTemplate=gContextParam.getContextMap().get("jdbcTemplate");
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");

		String sql="select * from match_rule";
		List dataList=(new MicroMetaDao()).queryObjJoinByCondition(sql);
		List selectRule=new ArrayList();
		Map cacheMap=new HashMap();
		cacheMap.put("select_rule", selectRule);
		int size=dataList.size();
		for(int i=0;i<size;i++){
			Map row=dataList.get(i);
			String ruleId=row.get("rule_id");
			String ruleParam=row.get("rule_param");
			Map ruleParamMap=new JsonSlurper().parseText(ruleParam);
			selectRule.add(ruleId);
			cacheMap.put(ruleId+"_param", ruleParamMap);
		}
		JsonBuilder jsonBuilder=new JsonBuilder(cacheMap);
		String cacheData=jsonBuilder.toString();
		
		
		return;
	}
	
	
}
