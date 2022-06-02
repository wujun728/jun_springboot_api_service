package com.project.frame.handler;

import groovy.lang.GroovyObject;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;
import com.nh.esb.core.INhCmdConst;
import com.nh.esb.core.INhCmdHandler;
import com.nh.esb.core.NhCmdRequest;
import com.nh.esb.core.NhCmdResult;
import com.nh.esb.service.servlet.NhServletCmdContextHolder;

@Component
public class WsGroovyCmdHandler implements INhCmdHandler {

	@Override
	public void execHandler(NhCmdRequest request, NhCmdResult result) {
		String groovyName=request.getSubName();
		String inputData=request.getCmdData();
		Map inMap=(Map) JSONObject.toBean(JSONObject.fromObject(inputData),Map.class);
		if(inMap==null){
			inMap=new HashMap();
		}
		Map outMap=new HashMap();
		GInputParam gInputParam=new GInputParam(inMap);
		GOutputParam gOutputParam=new GOutputParam(outMap);
		GContextParam gContextParam=new GContextParam();
		gContextParam.getContextMap().put("httpRequest", NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpRequest());
		gContextParam.getContextMap().put("httpResponse", NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpResponse());
		gContextParam.getContextMap().put("httpSession", NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpSession());
		
		
		String groovySubName=NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpRequest().getParameter("groovySubName");
		if(groovySubName==null ||"".equals(groovySubName)){
			groovySubName="execGroovy";
		}else{
			//groovySubName="exec"+String.valueOf(groovySubName.charAt(0)).toUpperCase()+groovySubName.substring(1);
		}
/*		if(!groovyName.equals("nhlogin")){
			String userName=(String) NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpSession().getAttribute("nhUserName");
			if(userName==null || "".equals(userName)){
				result.setResultStatus(INhCmdConst.STATUS_ERROR);
				result.setErrMsg("no_login_error");
				result.setResultCode("1111");
				return;
			}
		}*/

		GroovyObject authObj=GroovyExecUtil.getGroovyObj(groovyName);
		if(authObj!=null){
			Object[] typeArray=new Object[3];
			typeArray[0]=String.class;
			typeArray[1]=String.class;
			typeArray[2]=Map.class;
			
			if(authObj.getMetaClass().respondsTo(authObj, "checkExecAuth",typeArray).size()>0){
				Map paramMap=new HashMap();
				paramMap.put("httpRequest", NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpRequest());
				paramMap.put("httpResponse", NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpResponse());
				paramMap.put("httpSession", NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpSession());
				Boolean authFlag=(Boolean) GroovyExecUtil.execGroovyRetObj(groovyName, "checkExecAuth",groovyName, groovySubName,paramMap);
				if(authFlag==null || authFlag==false){
					result.setResultStatus(INhCmdConst.STATUS_ERROR);
					result.setErrMsg("no_auth_error");
					result.setResultCode("1112");
					return;				
				}
			}
		}
		boolean status=GroovyExecUtil.execGroovy(groovyName, groovySubName,gInputParam, gOutputParam,gContextParam);
		if(status==false){
			result.setResultStatus(INhCmdConst.STATUS_ERROR);
			result.setErrMsg("exec_groovy_error");
			return;
		}
		
		
/*		boolean status=GroovyExecUtil.execGroovySimple4Obj(groovyName, gInputParam, gOutputParam,gContextParam);
		if(status==false){
			result.setResultStatus(INhCmdConst.STATUS_ERROR);
			result.setErrMsg("exec_groovy_error");
			return;
		}*/
		String retStr=JSONObject.fromObject(gOutputParam).toString();
		result.setResultData(retStr);
		
	}

}
