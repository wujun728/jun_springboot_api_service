package groovy.nhuser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.nh.micro.controller.MicroUrlMapping
import com.nh.micro.rule.engine.core.*;
import groovy.template.MicroMvcTemplate;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

@MicroUrlMapping(name="/proxy")
class NhProxy {

	@MicroUrlMapping(name="/to")
	public void execLoginGo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){

		String page=httpRequest.getParameter("page");
		if(!page.contains("nhlogin.jsp")) {
			HttpSession httpSession=httpRequest.getSession();
			String userName = httpSession.getAttribute("nhUserName");
			if(userName==null || ("").equals(userName)) {
				String nhFailPage="/WEB-INF/nh-micro-jsp/infonologin.jsp";
				httpRequest.getRequestDispatcher(nhFailPage).forward(httpRequest, httpResponse);
				return;
			}
		}
		
		String realPage="/WEB-INF"+page;
		httpRequest.getRequestDispatcher(realPage).forward(httpRequest, httpResponse);
		return;
	}
	 
}
