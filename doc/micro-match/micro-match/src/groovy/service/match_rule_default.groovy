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

import java.math.BigDecimal
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import groovy.json.*;

import com.nh.micro.dao.mapper.MicroMapperTemplate
import com.nh.micro.db.*;


class MatchRuleProduct  extends MicroMapperTemplate{

	public boolean matchInfo(String ruleId,List ruleList,int index,String dirFlag,Map sourceInfo,List targetList,Map contextMap){

		String sourceAmount=sourceInfo.get(MatchConst.DataInfo.amount);
		BigDecimal sAmount=new BigDecimal(sourceAmount);
		String sourceId=sourceInfo.get(MatchConst.DataInfo.id);
		String matchId=contextMap.get("matchId");
		List resultList=new ArrayList();
		for(Map targetMap:targetList){
			String targetId=targetMap.get(MatchConst.DataInfo.id);
			if(sAmount<=0){
				break;
			}
			String targetAmount=targetMap.get(MatchConst.DataInfo.amount);
			BigDecimal tAmount=new BigDecimal(targetAmount);
			if(tAmount<=0){
				continue;
			}

			BigDecimal mAmount=0;
			if(sAmount<=tAmount){
				mAmount=new BigDecimal(sAmount);
			}else{
				mAmount=new BigDecimal(tAmount);
			}
			
			int status=0;
			Map buyFreezeMap=new HashMap();
			buyFreezeMap.put("match_id",matchId);
			buyFreezeMap.put("match_amount",mAmount);
			buyFreezeMap.put("create_time","now()");
			
			if(dirFlag.equals(MatchConst.MatchDirector.S2B)){
				buyFreezeMap.put("order_id",targetId);
			}else{
				buyFreezeMap.put("order_id",sourceId);
			}
			status=execGroovyRetObjByDbTran("match_rule_default","freeze",buyFreezeMap,"match_buy");
			if(status!=1){
				continue;
			}
			String freezeId=buyFreezeMap.get("id");
			Map saleFreezeMap=new HashMap();
			saleFreezeMap.put("match_id",matchId);
			saleFreezeMap.put("match_amount",mAmount);
			saleFreezeMap.put("create_time","now()");
			if(dirFlag.equals(MatchConst.MatchDirector.S2B)){
				saleFreezeMap.put("order_id",sourceId);
			}else{
				saleFreezeMap.put("order_id",targetId);
			}
			status=execGroovyRetObjByDbTran("match_rule_default","freeze",saleFreezeMap,"match_sale");
			if(status!=1){
				unfreeze(freezeId);
				continue;
			}
			Map row=new HashMap();
			if(sAmount<=tAmount){
				row.put("orderId", targetId);
				row.put("amount", new BigDecimal(sAmount));
				sAmount=0;
			}else{
				row.put("orderId", targetId);
				row.put("amount", new BigDecimal(tAmount));
				sAmount=sAmount.subtract(tAmount);
			}
			resultList.add(row);

		}
		if(resultList.size()<=0){
			return false;
		}
		System.out.println(resultList);
		boolean rollbackFlag=false;
		for(Map result:resultList){
			String matchAmount=result.get("amount");
			String targetId=result.get("orderId");
			Map insertMap=new HashMap();
			insertMap.put("match_id",matchId);
			insertMap.put("match_amount",matchAmount);
			insertMap.put("create_time","now()");
			if(dirFlag.equals(MatchConst.MatchDirector.S2B)){
				insertMap.put("buy_id",targetId);
				insertMap.put("sale_id", sourceId);
			}else{
				insertMap.put("buy_id",sourceId);
				insertMap.put("sale_id", targetId);
			}
			createInfoService(insertMap,"match_result");
		}
		return true;
	}
	
	private int unfreeze(String freezeId){
		Map infoMap=getInfoByIdService(freezeId,"match_freeze");
		if(infoMap==null){
			return 0;
		}
		String tableName=infoMap.get("table_name");
		String matchAmount=infoMap.get("match_amount");
		String orderId=infoMap.get("order_id");
		String sql="update "+tableName+" set account_amount=account_amount+"+matchAmount+" where id=? ";
		List paramList=new ArrayList();
		paramList.add(orderId);
		int status=updateInfoServiceBySql(sql,paramList);
		if(status!=1){
			return 0;
		}
	}
	
	private int freeze(Map insertMap,String tableName){
		int status=0;
		if(tableName.equals("match_buy")){
			String orderId=insertMap.get("order_id");
			String matchAmount=insertMap.get("match_amount");
			String matchId=insertMap.get("match_id");
			String sql="update match_buy set account_amount=account_amount-"+matchAmount+" where id=? and account_amount>="+matchAmount;
			List paramList=new ArrayList();
			paramList.add(orderId);
			status=updateInfoServiceBySql(sql,paramList);
			if(status!=1){
				return 0;
			}
			
			Map freezeMap=new HashMap();
			freezeMap.put("order_id", orderId);
			freezeMap.put("table_name", "match_buy");
			freezeMap.put("match_id", matchId);
			freezeMap.put("status", "0");
			freezeMap.put("create_time", "now()");
			freezeMap.put("match_amount", matchAmount);
			status=createInfoService(freezeMap,"match_freeze");
			
		}else{
			String orderId=insertMap.get("order_id");
			String matchAmount=insertMap.get("match_amount");
			String matchId=insertMap.get("match_id");
			String sql="update match_sale set account_amount=account_amount-"+matchAmount+" where id=? and account_amount>="+matchAmount;
			List paramList=new ArrayList();
			paramList.add(orderId);
			status=updateInfoServiceBySql(sql,paramList);
			if(status!=1){
				return 0;
			}
			
			Map freezeMap=new HashMap();
			freezeMap.put("order_id", orderId);
			freezeMap.put("table_name", "match_sale");
			freezeMap.put("match_id", matchId);
			freezeMap.put("status", "0");
			freezeMap.put("create_time", "now()");
			freezeMap.put("match_amount", matchAmount);
			status=createInfoService(freezeMap,"match_freeze");
		}
		return status;
	}
	

}
