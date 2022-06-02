package groovy.nhuser

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.nh.micro.controller.MicroUrlMapping
import com.nh.micro.rule.engine.core.*;

import groovy.template.MicroControllerTemplate
import groovy.template.MicroMvcTemplate;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import org.apache.log4j.Logger;

import groovy.json.*;

@MicroUrlMapping(name="/uc/userRole")
class UserRole extends MicroControllerTemplate{
	private static Logger logger=Logger.getLogger(UserRole.class);
	public String pageName="listRoleInfo.jsp";
	public String tableName="nh_micro_ref_user_role";

	
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
		sortMap.put("sort", "");
		String userId=httpRequest.getParameter("user_id");
		requestParamMap.put("ref.user_id", userId);

		String tableName="nh_micro_ref_user_role ref left join nh_micro_role role on ref.role_id=role.role_id";
		List retList=getInfoListAllService(requestParamMap, tableName,sortMap,"","ref.id,ref.role_id,role.role_name");

		JsonBuilder jsonBuilder=new JsonBuilder(retList);
		String retStr=jsonBuilder.toString();

		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	
	public List getRoleListByUserId(String userId){
		Map paramMap=new HashMap();
		paramMap.put("user_id", userId);
		String tableName="nh_micro_ref_user_role";
		List<Map> dataList=getInfoListAllService(paramMap, tableName,new HashMap());
		List retList=new ArrayList();
		if(dataList==null){
			return retList;
		}
		for(Map rowMap:dataList){
			String roleId=rowMap.get("role_id");
			retList.add(roleId);
		}
		return retList;
		
	}
	
	@MicroUrlMapping(name="/createInfo")
	public void createInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.createInfo(httpRequest, httpResponse);
		
	}
	
	@MicroUrlMapping(name="/delInfo")
	public void delInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.delInfo(httpRequest, httpResponse);
	}
	
}
