package groovy.dictionary;

import java.text.SimpleDateFormat

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.support.rowset.*;

import com.nh.micro.cache.base.*;
import com.nh.micro.controller.MicroUrlMapping
import com.nh.micro.db.*;

import groovy.json.*;
import groovy.template.MicroControllerTemplate

@MicroUrlMapping(name="/uc/dictItem")
class DictItem extends MicroControllerTemplate{
public String pageName="listDictItemsInfo";
public String tableName="nh_micro_dict_items";
public String getPageName(HttpServletRequest httpRequest){
	return pageName;
}
public String getTableName(HttpServletRequest httpRequest){
	return tableName;
}
/**
 * 跳转页面
 * @param gInputParam
 * @param gOutputParam
 * @param gContextParam
 */
@MicroUrlMapping(name="/toList")
	public void toList(HttpServletRequest httpRequest,HttpServletResponse httpResponse){
		httpRequest.setAttribute("dict_id",httpRequest.getParameter("dict_id"));
		httpRequest.getRequestDispatcher("/WEB-INF/nh-micro-jsp/dictionary-page/listDictItemsInfo.jsp").forward(httpRequest, httpResponse);
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	@MicroUrlMapping(name="/getInfoListAll")
	public void getInfoListAll(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.getInfoListAll(httpRequest, httpResponse);
		
	}
	@MicroUrlMapping(name="/getInfoListByDicId")
	public void getInfoListByDicId(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.getInfoListByDicId(httpRequest, httpResponse);

	}

	@MicroUrlMapping(name="/getInfoList4Page")
	public void getInfoList4Page(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.getInfoList4Page(httpRequest, httpResponse);
	}
	
	@MicroUrlMapping(name="/createInfo")
	public void createInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		try {
			Map requestParamMap =getRequestParamMap(httpRequest);	// 参数Map
			requestParamMap.put("create_time", sdf.format(new Date()))
			Integer retStatus = createInfoService(requestParamMap, tableName)
			outputSuccess(httpResponse)
		} catch (Exception e) {
			e.printStackTrace()
			outputFaild(httpResponse)
		}
	}
	
	@MicroUrlMapping(name="/delInfo")
	public void delInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		try {
			Map requestParamMap =getRequestParamMap(httpRequest);	// 参数Map
			Integer retStatus = delInfoService(requestParamMap, tableName)
			logger.info("删除字典类型:{}, 子项:{}", requestParamMap.get("dict_id"), requestParamMap.get("item_id"))
			outputSuccess(httpResponse)
		} catch (Exception e) {
			e.printStackTrace()
			outputFaild(httpResponse)
		}
	}
	
	@MicroUrlMapping(name="/updateInfo")
	public void updateInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		try {
			Map requestParamMap =getRequestParamMap(httpRequest);	// 参数Map
			Integer retStatus = updateInfoService(requestParamMap, tableName)
			outputSuccess(httpResponse)
		} catch (Exception e) {
			e.printStackTrace()
			outputFaild(httpResponse)
		}
	}
}
