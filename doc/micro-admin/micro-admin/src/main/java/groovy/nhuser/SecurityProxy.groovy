package groovy.nhuser

import java.security.MessageDigest

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.IncorrectCredentialsException
import org.apache.shiro.authc.UnknownAccountException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.session.Session
import org.apache.shiro.subject.Subject

import com.nh.micro.rule.engine.core.GroovyExecUtil

import groovy.template.AbstractConstants

import org.apache.commons.lang3.StringUtils

class SecurityProxy {
	public boolean isAuthenticated(){
		return SecurityUtils.getSubject().isAuthenticated();
	}
	
	public String passCheck(String username,String password){
		String error="";
		try {
			// 用户名密码验证
			UsernamePasswordToken token = new UsernamePasswordToken(username, digist(password,"UTF-8"));
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.login(token);//验证角色和权限
		} catch (UnknownAccountException e) {
			error = "用户不存在或者已停用";
		} catch (IncorrectCredentialsException e) {
			error = "用户名/密码错误";
		} catch (AuthenticationException e) {
			//其他错误，比如锁定，如果想单独处理请单独catch处理
			error = "其他错误：" + e.getMessage();
		} catch (Exception e) {
			e.printStackTrace()
			error = "未知错误"
		}
		return error;
	}
	
	public void saveUserInfoToSession(String username){
		if(StringUtils.isBlank(username)){
			return
		}
		Map userMap = (Map) GroovyExecUtil.execGroovyRetObj("nhuser_user", "getUserInfosByUid", username);
		Session session = SecurityUtils.getSubject().getSession()
		session.setAttribute(AbstractConstants.LOGIN_USER_INFO, userMap)
		session.setAttribute(AbstractConstants.LOGIN_USER_NAME, username);
		session.setAttribute(AbstractConstants.LOGIN_USER_ID,userMap.get("id").toString());
		session.setAttribute(AbstractConstants.LOGIN_USER_ROLEIDS,userMap.get("roles").split(","));
	}
	public void logout(){
		SecurityUtils.getSubject().logout()
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
