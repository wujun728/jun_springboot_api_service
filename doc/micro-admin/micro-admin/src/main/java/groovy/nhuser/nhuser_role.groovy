package groovy.nhuser

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


import com.nh.micro.controller.MicroUrlMapping
import com.nh.micro.rule.engine.core.*;

import groovy.json.*;
import groovy.template.AbstractResultCode
import groovy.template.MicroControllerTemplate


@MicroUrlMapping(name="/uc/roleList")
class RoleList extends MicroControllerTemplate{
	private static Logger logger=Logger.getLogger(RoleList.class);
	public String pageName="listRoleInfo.jsp";
	public String tableName="nh_micro_role";
	
	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}
	
	@MicroUrlMapping(name="/toPage")
	public void toPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		setDictionaryInfos("role_type", httpRequest)
		httpRequest.getRequestDispatcher("/micropage/nh-micro-jsp/system/roleManage.jsp").forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
	}
	
	@MicroUrlMapping(name="/getInfoListAll")
	public void getInfoListAll(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.getInfoListAll(httpRequest, httpResponse);
		
	}
	
	@MicroUrlMapping(name="/getInfoList4Page")
	public void getInfoList4Page(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.getInfoList4Page(httpRequest, httpResponse);
	}
	
	@MicroUrlMapping(name="/createInfo")
	public void createInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		Map requestParamMap=getRequestParamMap(httpRequest);
		try {
			if("system".equals(requestParamMap.get("role_type").toString())){
				output(AbstractResultCode.cant_not_add_system_manager, httpResponse)
				return ;
			}
			//1.校验是否存在相同的角色id
			String roleId = requestParamMap.get("role_id");
			Map checkMap=getInfoByBizIdService(roleId, tableName,"role_id");
			if(checkMap!=null && !checkMap.isEmpty() ) {
				output(AbstractResultCode.role_id_already_exist, httpResponse)
				return ;
			}
			requestParamMap.put("create_time", "now()");
			requestParamMap.put("update_time", "now()");
			createInfoService(requestParamMap, tableName);
			outputSuccess(httpResponse)
		} catch (Exception e) {
			e.printStackTrace()
			outputFaild(httpResponse)
		}
	}
	
	@MicroUrlMapping(name="/delInfo")
	public void delInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.delInfo(httpRequest, httpResponse);
	}
	
	@MicroUrlMapping(name="/updateInfo")
	public void updateInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		String id = httpRequest.getParameter("id")
		String roleId = httpRequest.getParameter("role_id")
		try {
			Map requestParamMap=getRequestParamMap(httpRequest);
			Map checkMap=getInfoByBizIdService(roleId, tableName,"role_id");
			// 1. 不允许添加超级管理员账户
			if("system".equals(requestParamMap.get("role_type").toString()) || "system".equals(checkMap.get("role_type").toString())){
				output(AbstractResultCode.cant_not_add_system_manager, httpResponse)
				return ;
			}
			// 2.校验是否存在相同的角色id
			if(checkMap!=null && !checkMap.isEmpty() && !id.equals(checkMap.get("id").toString())) {
				output(AbstractResultCode.role_id_already_exist, httpResponse)
				return ;
			}
			Integer retStatus=updateInfoService(requestParamMap, tableName);
			outputSuccess(httpResponse)
		} catch (Exception e) {
			e.printStackTrace()
			outputFaild(httpResponse)
		}
	}
}
