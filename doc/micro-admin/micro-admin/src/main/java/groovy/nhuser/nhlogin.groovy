package groovy.nhuser

import java.security.MessageDigest

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger

import org.springframework.beans.factory.annotation.Value

import com.nh.micro.controller.MicroUrlMapping
import com.nh.micro.rule.engine.core.GroovyExecUtil

import groovy.template.MicroMvcTemplate
import groovy.template.AbstractConstants;

@MicroUrlMapping(name="/uc")
class NhLogin  extends MicroMvcTemplate{
	@Value("\${test.skipimgcheckcode}")
	private String skipimgcheckcode;
	
	private static Logger logger=Logger.getLogger(NhLogin.class);
	String nhMainPage="/micropage/nh-micro-jsp/index-new.jsp";
	String loignPage="/micropage/nh-micro-jsp/nhlogin.jsp";

	@MicroUrlMapping(name="/logingo")
	public void execLoginGo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		String username = httpRequest.getParameter("username");
		String password = httpRequest.getParameter("password");
		String imgcode = httpRequest.getParameter("imgcode");
		String scode = httpRequest.getSession().getAttribute("scode") + "";
		HttpSession httpSession = httpRequest.getSession();
		try {
			if(GroovyExecUtil.execGroovyRetObj("SecurityProxy", "isAuthenticated")){
				GroovyExecUtil.execGroovyRetObj("SecurityProxy", "saveUserInfoToSession", username);
				toMainPage(httpRequest, httpResponse)
				return
			}
			if(StringUtils.isBlank(scode) || StringUtils.isBlank(password) || StringUtils.isBlank(username)){
				toErrorPage(httpRequest, httpResponse, "请登录")
				return
			}
			//	验证码验证
			if(!"true".equals(skipimgcheckcode) && (StringUtils.isBlank(imgcode) || StringUtils.isBlank(scode) ||  !imgcode.equals(scode))){
				httpSession.removeAttribute("scode")
				toErrorPage(httpRequest, httpResponse, "验证码错误")
				return
			}
			httpSession.removeAttribute("scode")
			String error = "";
			error=GroovyExecUtil.execGroovyRetObj("SecurityProxy", "passCheck", username, password);
			if(error != "") {
				toErrorPage(httpRequest, httpResponse, error)//出错了，返回登录页面
				return
			} 
			//登录成功
			logger.info( "User [" + username + "] logged in successfully." );
			GroovyExecUtil.execGroovyRetObj("SecurityProxy", "saveUserInfoToSession", username);
			
			toMainPage(httpRequest, httpResponse)
			return
		} catch (Exception e) {
			e.printStackTrace()
			toErrorPage(httpRequest, httpResponse, e.getMessage())
			return
		}
	}
	


	private void toMainPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		//查询菜单
		loadMenuInfo(httpRequest)
		httpRequest.setAttribute("forwardFlag", "true");
		httpRequest.getRequestDispatcher(nhMainPage).forward(httpRequest, httpResponse);
	}

	private void toErrorPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String msg){
		httpRequest.setAttribute("forwardFlag", "true");
		httpRequest.setAttribute("nhloginMsg", msg);
		httpRequest.getRequestDispatcher(loignPage).forward(httpRequest, httpResponse);
	}
	//查询菜单
	private void loadMenuInfo(HttpServletRequest httpRequest){
		HttpSession httpSession = httpRequest.getSession();
		String userId = httpSession.getAttribute("nhUserName");

		// 关联权限表查询
		String sql = "SELECT m1.id AS id,m1.icon AS icon," +
				"( CASE WHEN (m2.id = 0 OR m2.id IS NULL) THEN 0  ELSE m2.id END ) AS parentId, m1.name AS NAME, m1.url AS url, m1.levels AS levels, m1.ismenu AS ismenu,"+
				"m1.num AS num "+
				"FROM nh_micro_sysmenu m1 LEFT JOIN nh_micro_sysmenu m2 ON m1.pcode = m2.code "+
				"WHERE m1.ismenu = 1 AND m1.status =1 AND m1.ismenu = 1 AND "+
				"m1.code IN ( SELECT DISTINCT (mr.menu_id) FROM nh_micro_ref_menu_role mr,nh_micro_ref_user_role ur WHERE mr.role_id = ur.role_id AND ur.user_id = ?) "+
				"ORDER BY levels, num ASC"

		List placeList = new ArrayList();
		placeList.add(userId);
		List retList = getInfoListAllServiceBySql(sql, placeList);
		List menuList = new ArrayList();
		menuList = getMenuTree(retList);
		httpRequest.setAttribute("menuInfo", menuList);
	}

	private List getMenuTree(List nodeList){
		List menuList = new ArrayList();
		for (Map treeNode : nodeList) {
			Integer level = Integer.parseInt(treeNode.get("levels"));
			if(level == 1){
				dgMenu(nodeList, level + 1, treeNode);
				menuList.add(treeNode);
			}
		}
		//对菜单排序
		//		Collections.sort(menuList);
		return menuList;
	}

	/**
	 * 递归获取当前节点子节点 支持三级菜单
	 * @param nodeList
	 * @param parentId
	 * @param levels
	 * @param parentMenu
	 */
	private void dgMenu(List nodeList, Integer levels, Map parentMenu){
		if(levels > 3){
			return;
		}
		if(levels == 3){
			String a = 1;
		}
		List subMenuList = new ArrayList()
		Integer parentId = Integer.parseInt(parentMenu.get("id"));
		for(Map treeNode: nodeList){
			Integer level = Integer.parseInt(treeNode.get("levels")) ;
			Integer pId = Integer.parseInt(treeNode.get("parentId"));
			if(level == 3){
				String b = 1;
			}
			if((level == levels) && (pId == parentId)){
				dgMenu(nodeList, level + 1, treeNode);
				subMenuList.add(treeNode);
			}
		}
		if(subMenuList.size() > 0){
			//对菜单排序
			//			Collections.sort(subMenuList);
			parentMenu.put("children", subMenuList);
		}
	}

	@MicroUrlMapping(name="/logoutgo")
	public void execLogOutGo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		HttpSession httpSession=httpRequest.getSession();
		httpSession.removeAttribute(AbstractConstants.LOGIN_USER_NAME);
		httpSession.removeAttribute(AbstractConstants.LOGIN_USER_ID);
		httpSession.removeAttribute(AbstractConstants.LOGIN_USER_ROLEIDS);
		httpSession.removeAttribute(AbstractConstants.LOGIN_USER_INFO);
		GroovyExecUtil.execGroovyRetObj("SecurityProxy", "logout");
		httpRequest.setAttribute("forwardFlag", "true");
		httpRequest.getRequestDispatcher("/micropage/nh-micro-jsp/nhlogin.jsp").forward(httpRequest, httpResponse);
		return;

	}

	private boolean checkValid(Map rowMap){
		if(rowMap==null){
			return false;
		}
		String status=rowMap.get("user_status");
		if(status==null || "".equals(status)){
			return false;
		}
		Integer si=Integer.valueOf(status);
		if(si==0){
			return true;
		}
		return false;

	}
	private boolean checkPassword(String user_id,String inPass){
		String oldPass=getPassword(user_id);
		if(oldPass==null|| inPass==null){
			return false;
		}
		if(inPass.equals(oldPass)){
			return true;
		}
		return false;
	}
	private String getPassword(String user_id){

		Map rowMap=getInfoByBizIdService(user_id,"nh_micro_user","user_id");
		if(rowMap==null){
			return null;
		}
		String pass=rowMap.get("user_password");
		return pass;
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
