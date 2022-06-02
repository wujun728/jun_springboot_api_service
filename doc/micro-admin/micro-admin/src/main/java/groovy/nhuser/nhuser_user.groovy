package groovy.nhuser;

import java.security.MessageDigest
import java.util.ArrayList
import java.util.List
import java.util.Map

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils


import com.nh.micro.controller.MicroUrlMapping;
import com.nh.micro.rule.engine.core.*;

import groovy.json.*;
import groovy.template.AbstractActiveStatus
import groovy.template.AbstractConstants
import groovy.template.AbstractResultCode
import groovy.template.MicroControllerTemplate;


@MicroUrlMapping(name="/uc/userList")
class NhuserUser extends MicroControllerTemplate{
	public String pageName="listRoleInfo.jsp";
	public String tableName="nh_micro_user";


	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}
	
	public List getUserByUserName(String userName){
		String sql = "select * from nh_micro_user  where user_id =  ? and user_status = ?";
		List place = new ArrayList();
		place.add(userName);
		place.add(AbstractActiveStatus.enabled.getCode());
		return getInfoListAllServiceBySql( sql, place);
	}
	
	@MicroUrlMapping(name="/toPage")
	public void toPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		httpRequest.setAttribute("allRoles", getAllRoles())
		httpRequest.getRequestDispatcher("/micropage/nh-micro-jsp/system/userManage.jsp").forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
	}

	/**
	 * 重置密码
	 * @param httpRequest
	 * @param httpResponse
	 */
	@MicroUrlMapping(name="/modifyPass")
	public void modifyPass(HttpServletRequest httpRequest, HttpServletResponse httpResponse){

		String id=httpRequest.getParameter("id");	//获取主键id
		String user_password=httpRequest.getParameter("user_password");		//获取用户密码
		Map requestMap=new HashMap();
		requestMap.put("user_password", digist(user_password,"UTF-8"));

		Integer retStatus=updateInfoByIdService(id,"nh_micro_user",requestMap);
		GOutputParam gOutputParam=new GOutputParam();
		gOutputParam.setResultObj(retStatus);
		JsonBuilder jsonBuilder=new JsonBuilder(gOutputParam);
		String retStr=jsonBuilder.toString();
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		return;
	}

	@MicroUrlMapping(name="/getInfoListAll")
	public void getInfoListAll(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.getInfoListAll(httpRequest, httpResponse);
	}
	
	/**
	 * 获取全部角色
	 * @return
	 */
	public List getAllRoles(){
		try {
			List placeList = new ArrayList();
			List retList=getInfoListAllServiceBySql("select * from  nh_micro_role", placeList);
			return retList
		} catch (Exception e) {
			e.printStackTrace()
			return null
		}
	}
	
	private List getAllRolesIdByUid(String userId){
		Map requestParamMap = new HashMap();
		Map sortMap=new HashMap();
		sortMap.put("sort", "create_time");
		sortMap.put("order", "asc");
		try {
			requestParamMap.put("user_id", userId)
			List retList=getInfoListAllService(requestParamMap, "nh_micro_ref_user_role", sortMap);
			List ids = new ArrayList()
			for(Object obj : retList){
				ids.add(((Map)obj).get("role_id"))
			}
			return ids
		} catch (Exception e) {
			e.printStackTrace()
			return null
		}
	}
	
	public Map getUserInfosByUid(String userId){
		List placeList = new ArrayList();
		placeList.add(userId);
		List retList = getInfoListAllServiceBySql(
			"SELECT u.id, u.`user_id`,u.`user_id`,u.`user_name`,u.`user_status`,GROUP_CONCAT(r.`role_id`)AS roles "+
			"FROM `nh_micro_user` u LEFT JOIN `nh_micro_ref_user_role` r ON u.`user_id`=r.`user_id` "+
			"WHERE u.`user_id` = ? "+
			"GROUP BY u.`user_id`,u.`user_name`,u.`id`,u.`user_status` ", placeList);
		return retList!=null &&retList.size()>0 ? (Map)retList.get(0) : null;
	}

	@MicroUrlMapping(name="/getInfoList4Page")
	public void getInfoList4Page(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		String user_name = httpRequest.getParameter("user_name")
		String user_id = httpRequest.getParameter("user_id")
		String user_status = httpRequest.getParameter("user_status")
		Map pageMap = createPageMap(httpRequest)
		String whereSql = "";
		if (StringUtils.isNotBlank(user_id)){
			whereSql = " and u.user_id = '" + user_id + "'"
		}
		if (StringUtils.isNotBlank(user_name)){
			whereSql = " and u.user_name = '" + user_name + "'"
		}
		if (StringUtils.isNotBlank(user_status)){
			whereSql = " and u.user_status = '" + user_status + "'"
		}
		String sql = "SELECT u.*, roles.roles, roles.roleIds FROM `nh_micro_user` u, "+
				"(SELECT ur.user_id, GROUP_CONCAT( r.role_name ) AS roles , GROUP_CONCAT( r.role_id ) AS roleIds "+
				"FROM  `nh_micro_ref_user_role` ur LEFT JOIN `nh_micro_role` r ON ur.`role_id`=r.`role_id` "+
				"LEFT JOIN `nh_micro_user` u ON u.`user_id`=ur.`user_id` GROUP BY ur.user_id) AS roles  "+
				"WHERE roles.user_id = u.user_id and 1=1 "
		logger.info(sql + whereSql)
		Map<String,Object> resultMap=getInfoList4PageServiceByMySql(sql + whereSql, pageMap)
		Map returnMap = createRetDataMap(Integer.valueOf(pageMap.get("page")),Integer.valueOf(pageMap.get("rows")), resultMap)
		outputSuccess(returnMap, httpResponse)
	}

	//创建用户
	@MicroUrlMapping(name="/createInfo")
	public void createInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		Map requestParamMap=getRequestParamMap(httpRequest);
		//1.校验是否存在同登录名的用户
		//查询数据库中是否存在同登录名的用户
		String userId = requestParamMap.get("user_id");
		String roles = requestParamMap.get("roles").toString()
		try {
			if(roles.indexOf(AbstractConstants.ADMIN_ROLE_ID) != -1){
				output(AbstractResultCode.cant_not_add_system_manager, httpResponse)
				return ;
			}
			Map checkMap=getInfoByBizIdService(userId,"nh_micro_user","user_id");
			if(checkMap!=null && !checkMap.isEmpty() ) {
				output(AbstractResultCode.duplication_user_name, httpResponse)
				return ;
			}
			String pwd = requestParamMap.get("user_password");
			requestParamMap.put("user_password", digist(pwd,"UTF-8"));
			requestParamMap.put("create_time", "now()");
			requestParamMap.put("update_time", "now()");
			createInfoService(requestParamMap, tableName);
			saveRoles(userId, roles)
			outputSuccess(httpResponse)
		} catch (Exception e) {
			e.printStackTrace()
			outputFaild(httpResponse)
		}
	}
	
	private void saveRoles(String userId, String roleIds){
		List userRoleIds = getAllRolesIdByUid(userId)
		List newRoles = new ArrayList(Arrays.asList(roleIds.split(",")))
		List noChangeIds = new ArrayList(newRoles)
		noChangeIds.retainAll(userRoleIds)		// 交集, 保持不变
		newRoles.removeAll(userRoleIds)			// 差集, 将要新增的角色id
		userRoleIds.removeAll(noChangeIds)		// 差集, 将要删除的角色id
		for(Object roleId : newRoles){
			Map requestParamMap = new HashMap()
			requestParamMap.put("user_id", userId)
			requestParamMap.put("role_id", roleId.toString())
			Integer retStatus=createInfoService(requestParamMap, "nh_micro_ref_user_role");
		}
		for(Object roleId : userRoleIds){
			List list = new ArrayList()
			list.add(userId)
			list.add(roleId.toString())
			String sql = "delete from  nh_micro_ref_user_role where user_id = ? and role_id = ?";
			Integer retStatus = updateInfoServiceBySql(sql, list);
		}
	}

	@MicroUrlMapping(name="/delInfo")
	public void delInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		String id = httpRequest.getParameter("id");
		String user_id = httpRequest.getParameter("user_id");
		String user_status = httpRequest.getParameter("user_status");
		try {
			if("admin".equals(user_id)){
				output(AbstractResultCode.admin_cant_not_stop, httpResponse)
				return
			}
			Map requestParamMap = new HashMap();
			requestParamMap.put("user_status", user_status);
			Integer retStatus=updateInfoByIdService(id, tableName, requestParamMap);
			outputSuccess(httpResponse)
		} catch (Exception e) {
			e.printStackTrace()
			outputFaild(httpResponse)
		}
	}

	/**
	 * 修改用户信息
	 */
	@MicroUrlMapping(name="/updateInfo")
	public void updateInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		//1.校验是否存在同登录名的用户
		//查询数据库中是否存在同登录名的用户
		String id = httpRequest.getParameter("id")
		String userId = httpRequest.getParameter("user_id")
		String user_password=httpRequest.getParameter("user_password");		//获取用户密码
		try {
			Map checkMap=getInfoByBizIdService(userId,"nh_micro_user","user_id")
			if(checkMap!=null && !checkMap.isEmpty() && !checkMap.get("id").toString().equals(id)) {
				output(AbstractResultCode.duplication_user_name)
				return
			}
			Map requestParamMap=getRequestParamMap(httpRequest);
			// 只允许admin自己修改超级管理员信息
			if(AbstractConstants.ADMIN_LOGIN_NAME.equals(userId) && !getUserLoginName().equals(AbstractConstants.ADMIN_LOGIN_NAME)){
				output(AbstractResultCode.cant_not_add_system_manager, httpResponse)
				return
			}
			if(StringUtils.isNotBlank(user_password)){
				requestParamMap.put("user_password", digist(user_password,"UTF-8"));
			}else{
				requestParamMap.remove("user_password")
			}
			Integer retStatus=updateInfoService(requestParamMap, tableName);
			saveRoles(userId, requestParamMap.get("roles").toString())
			outputSuccess(httpResponse)
		} catch (Exception e) {
			e.printStackTrace()
			outputFaild(httpResponse)
		}
	}


	/**
	 * 加密
	 * @param text   要加密的值
	 * @param encode 编码格式  UTF-8
	 * @return
	 */
	public static String digist(String text, String encode) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			// 定义编码方式
			byte[] bufs = text.getBytes(encode);
			md.update(bufs);
			byte[] b = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


}
