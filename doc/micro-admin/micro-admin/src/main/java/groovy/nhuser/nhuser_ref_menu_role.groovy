package groovy.nhuser

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger;


import com.nh.micro.controller.MicroUrlMapping
import com.nh.micro.rule.engine.core.*;

import groovy.json.*;
import groovy.template.AbstractConstants
import groovy.template.MicroControllerTemplate


@MicroUrlMapping(name="/uc/menuRole")
class MenuRole extends MicroControllerTemplate{
	private static Logger logger=Logger.getLogger(MenuRole.class);
	public String pageName="listRoleInfo.jsp";
	public String tableName="nh_micro_ref_menu_role";


	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}

	@MicroUrlMapping(name="/getInfoList4Ref")
	public void getInfoList4Ref(HttpServletRequest httpRequest, HttpServletResponse httpResponse){

		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");

		String pageName=getPageName(httpRequest);
		Map requestParamMap=new HashMap();
		Map sortMap=new HashMap();
		String menuId=httpRequest.getParameter("menu_id");
		requestParamMap.put("ref.menu_id", menuId);

		String tableName="nh_micro_ref_menu_role ref left join nh_micro_role role on ref.role_id=role.role_id";
		List retList=getInfoListAllService(requestParamMap, tableName,sortMap,"","ref.id,ref.menu_id,role.role_id,role.role_name");

		JsonBuilder jsonBuilder=new JsonBuilder(retList);
		String retStr=jsonBuilder.toString();

		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));

		httpRequest.setAttribute("forwardFlag", "true");
		return;
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
		try {
			Map requestParamMap =getRequestParamMap(httpRequest);
			String roleId = requestParamMap.get("role_id")
			String menuIds = requestParamMap.get("menu_ids")
			if(AbstractConstants.ADMIN_ROLE_ID.equals(roleId) && !getUserRoleId().contains(AbstractConstants.ADMIN_ROLE_ID)){
				output(AbstractResultCode.cant_not_add_system_manager, httpResponse)
				return
			}
			savePermissions(roleId, menuIds)
			outputSuccess(httpResponse)
		} catch (Exception e) {
			e.printStackTrace()
			outputFaild(httpResponse)
		}
	}
	
	private void savePermissions(String roleId, String menuIds){
		List userMenuIds = getUserPermissions(roleId)
		List newPerms = new ArrayList(Arrays.asList(menuIds.split(",")))
		List noChangeIds = new ArrayList(newPerms)
		noChangeIds.retainAll(userMenuIds)			// 交集, 保持不变
		newPerms.removeAll(userMenuIds)			// 差集, 将要新增的权限id
		userMenuIds.removeAll(noChangeIds)		// 差集, 将要删除的权限id
		for(Object menuId : newPerms){
			Map requestParamMap = new HashMap()
			requestParamMap.put("menu_id", menuId)
			requestParamMap.put("role_id", roleId)
			Integer retStatus=createInfoService(requestParamMap, tableName);
		}
		for(Object menuId : userMenuIds){
			List list = new ArrayList()
			list.add(menuId)
			list.add(roleId)
			String sql = "delete from " + tableName + "  where menu_id = ? and role_id = ?";
			Integer retStatus = updateInfoServiceBySql(sql, list);
		}
	}
	
	public List getUserPermissions(String roleId){
		List placeList = new ArrayList();
		placeList.add(roleId);
		List retList=getInfoListAllServiceBySql("select menu_id from  "+tableName+" where role_id=?", placeList);
		List ids = new ArrayList()
		for(Object obj : retList){
			ids.add(((Map)obj).get("menu_id"))
		}
		return ids
	}
	
	public List getUserPermissions(List roleIds){
		List placeList = new ArrayList();
		String instr = ""
		for(String role : roleIds){
			if(StringUtils.isNotBlank(instr)){
				instr = instr+","
			}
			instr =  instr + "'" + role + "'"
		}
		List retList=getInfoListAllServiceBySql("select menu_id from  "+tableName+" where role_id in ("+instr+")", placeList);
		List ids = new ArrayList()
		for(Object obj : retList){
			ids.add(((Map)obj).get("menu_id"))
		}
		return ids
	}
	
	@MicroUrlMapping(name="/delInfo")
	public void delInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.delInfo(httpRequest, httpResponse);
	}

	@MicroUrlMapping(name="/updateInfo")
	public void updateInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.updateInfo(httpRequest, httpResponse);
	}

}
