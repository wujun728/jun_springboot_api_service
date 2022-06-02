package groovy.nhuser

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ResponseBody

import com.common.admin.shiro.ShiroServiceImpl
import com.nh.micro.controller.MicroUrlMapping

import groovy.json.JsonBuilder
import groovy.template.AbstractResultCode
import groovy.template.MicroControllerTemplate


@MicroUrlMapping(name = "/uc/menuList")
class MenuList extends MicroControllerTemplate {
    private static Logger logger = Logger.getLogger(MenuList.class);
    public String pageName = "";
    public String tableName = "nh_micro_sysmenu";
	
	@Autowired
	public ShiroServiceImpl shiroServiceImpl;
	
	@Autowired
	public ShiroFilterFactoryBean shiroFilterFactoryBean

    public String getPageName(HttpServletRequest httpRequest) {
        return pageName;
    }

    public String getTableName(HttpServletRequest httpRequest) {
        return tableName;
    }
	
	private Map getUserPermissions(String roleId){
			Map sortMap=new HashMap();
			Map requestParamMap=new HashMap();
			requestParamMap.put("role_id", roleId);
			List placeList = new ArrayList();
			placeList.add(roleId);
			List retList=getInfoListAllServiceBySql("select * from  nh_micro_ref_menu_role where role_id=?", placeList);
			Map userRoles = new HashMap()
			for(Object role : retList){
				String menuId = ((HashMap) role).get("menu_id").toString()
				userRoles.put(menuId, menuId)
			}
			return userRoles
	}

    @MicroUrlMapping(name = "/getInfoListAllWithUser")
    public void getInfoListAllWithUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		try {
			Map userRoles = getUserPermissions(httpRequest.getParameter("role_id"));
			String sort = httpRequest.getParameter("sort");
			String order = httpRequest.getParameter("order");
			String pageName = getPageName(httpRequest);
			Map requestParamMap = getRequestParamMap(httpRequest);
			Map sortMap = new HashMap();
			sortMap.put("sort", sort);
			sortMap.put("order", order);
			List retList = getInfoListAllService(requestParamMap, tableName,sortMap);
			List newResult = new ArrayList()
			for(Map role : retList){
				if(StringUtils.isNotBlank(userRoles.get(role.get("code").toString()))){
					role.put("checked", true)
				} 
				newResult.add(role)
			}
			Map resultMap = new HashMap()
			resultMap.put("menus",newResult)
			outputSuccess(resultMap, httpResponse)
		} catch (Exception e) {
			e.printStackTrace()
			outputFaild(httpResponse);
		}
    }
	
	@MicroUrlMapping(name = "/getInfoListAll")
	public void getInfoListAll(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		super.getInfoListAll(httpRequest, httpResponse);
	}
	
	public List loadAllMenu(){
		try {
			List placeList = new ArrayList();
			List retList=getInfoListAllServiceBySql("select * from "+tableName, placeList);
			return retList
		} catch (Exception e) {
			e.printStackTrace()
			return null
		}
	}
	
    @MicroUrlMapping(name = "/getInfoList4Page")
    public void getInfoList4Page(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super.getInfoList4Page(httpRequest, httpResponse);
    }

    @MicroUrlMapping(name = "/createInfo")
    public void createInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		try {
			// 参数Map
			Map requestParamMap =getRequestParamMap(httpRequest);
			if(StringUtils.isBlank(requestParamMap.get("pcode"))){
				requestParamMap.put("pcode", "system")
			}
			requestParamMap.put("isopen", "1")
			requestParamMap.put("status", "1")
			if(StringUtils.isBlank(requestParamMap.get("num"))){
				requestParamMap.put("num", "0");
			}
			Integer retStatus = createInfoService(requestParamMap, tableName);
			shiroServiceImpl.updatePermission(shiroFilterFactoryBean);	// 更新权限
		} catch (Exception e) {
			e.printStackTrace()
		}
		httpRequest.getRequestDispatcher("/micropage/nh-micro-jsp/system/menus.jsp").forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
    }
	
	// code url 排重
	@ResponseBody
	@MicroUrlMapping(name = "/isExistCodeAndUrl")
	public void isExistCodeAndUrl(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		try{
			Map requestParamMap =getRequestParamMap(httpRequest);
			List placeList = new ArrayList();
			placeList.add(requestParamMap.get("code").toString())
			List retList=getInfoListAllServiceBySql("select * from  "+tableName+" where code=?", placeList);
			if(retList != null && retList.size() > 0){
				output(AbstractResultCode.menu_code_exist, httpResponse)
				return
			}
			if(StringUtils.isNotBlank(requestParamMap.get("url").toString())){
				placeList.clear()
				placeList.add(requestParamMap.get("url").toString())
				retList=getInfoListAllServiceBySql("select * from  "+tableName+" where url=?", placeList);
				if(retList != null && retList.size() > 0){
					output(AbstractResultCode.menu_url_exist, httpResponse)
					return
				}
			}
			outputSuccess(httpResponse)
		}catch (Exception e) {
			e.printStackTrace()
			outputFaild(httpResponse)
		}
	}
	
	// url 排重
	@ResponseBody
	@MicroUrlMapping(name = "/isExistUrl")
	public void isExistUrl(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		try{
			Map requestParamMap =getRequestParamMap(httpRequest);
			if(StringUtils.isNotBlank(requestParamMap.get("url").toString())){
				List placeList = new ArrayList();
				placeList.add(requestParamMap.get("url").toString())
				placeList.add(requestParamMap.get("code").toString())
				List retList=getInfoListAllServiceBySql("select * from  "+tableName+" where url=? and code != ?", placeList);
				if(retList != null && retList.size() > 0){
					output(AbstractResultCode.menu_url_exist, httpResponse)
					return
				}
			}
			outputSuccess(httpResponse)
		}catch (Exception e) {
			e.printStackTrace()
			outputFaild(httpResponse)
		}
	}
	
    @MicroUrlMapping(name = "/delInfo")
    public void delInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String id = httpRequest.getParameter("id");
        String status = httpRequest.getParameter("status");
		JsonBuilder jsonBuilder=new JsonBuilder("success");
		String retStr=jsonBuilder.toString();
		try {
			Map requestParamMap = new HashMap();
			requestParamMap.put("status", status);
			Integer retStatus=updateInfoByIdService(id, tableName, requestParamMap);
		} catch (Exception e) {
			retStr = new JsonBuilder("faild").toString();
			e.printStackTrace()
		}
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
    }

    @MicroUrlMapping(name = "/updateInfo")
    public void updateInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		Map requestParamMap=getRequestParamMap(httpRequest);
		Integer retStatus=updateInfoService(requestParamMap, tableName);
		shiroServiceImpl.updatePermission(shiroFilterFactoryBean);	// 更新权限
		httpRequest.getRequestDispatcher("/micropage/nh-micro-jsp/system/menus.jsp").forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
    }
}
