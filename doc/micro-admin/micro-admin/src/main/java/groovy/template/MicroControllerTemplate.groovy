package groovy.template;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils
import org.apache.shiro.SecurityUtils
import org.apache.shiro.session.Session
import org.apache.shiro.subject.Subject
import org.slf4j.Logger
import org.slf4j.LoggerFactory


import com.nh.micro.db.*;

import groovy.json.JsonBuilder;
import groovy.template.AbstractConstants;


/**
 * 
 * @author ninghao
 *
 */
class MicroControllerTemplate extends MicroServiceBizTemplate{

	Logger logger = LoggerFactory.getLogger(MicroControllerTemplate.getClass());

	public boolean checkExecAuth(String groovyName,String groovyMethod,HttpServletRequest request){

		return true;
	}
	
	/**
	 * 获取当前登录用户信息
	 * @return
	 */
	Map getUserInfoMap(){
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession()
		return session.getAttribute(AbstractConstants.LOGIN_USER_INFO)
	}
	
	/**
	 * 获取当前登录用户名
	 * @return
	 */
	String getUserLoginName(){
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession()
		return session.getAttribute(AbstractConstants.LOGIN_USER_NAME)
	}
	
	/**
	 * 获取当前登录用户id
	 * @return
	 */
	String getUserId(){
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession()
		return session.getAttribute(AbstractConstants.LOGIN_USER_ID)
	}
	
	/**
	 * 获取当前登录用户角色id
	 * @return
	 */
	String[] getUserRoleId(){
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession()
		return (String[])session.getAttribute(AbstractConstants.LOGIN_USER_ROLEIDS)
	}
	
	/**
	 * 查询字典，批量
	* @Title: getDictionaryInfos
	* @param @param keys
	* @param @param httpRequest    参数
	* @return void    返回类型
	* @throws
	 */
	void setDictionaryInfos(String keys, HttpServletRequest httpRequest){
		for(String key : keys.split(",")){
			List list = getDictionaryInfo(key);
			httpRequest.setAttribute(key, list);
		}
	}
	
	/**
	 * 得到字典信息-不分页
	 * @param keys
	 * @return
	 */
	List getDictionaryInfo(String key){
		String sql = "select * from nh_micro_dict_items where dict_id = '" + key +"' and item_name != '不限' order by create_time asc"
		return getInfoListAllServiceBySql(sql)
	}
	
	/** 输出默认成功返回值, 加上自己需要的值 */
	public void outputSuccess(Map returnMap, HttpServletResponse httpResponse){
		output(returnMap, AbstractResultCode.success.getCode(), AbstractResultCode.success.getName(), httpResponse)
	}
	
	/** 输出默认成功返回值 */
	public void outputSuccess(HttpServletResponse httpResponse){
		output(null, AbstractResultCode.success.getCode(), AbstractResultCode.success.getName(), httpResponse)
	}
	
	/** 输出逻辑错误枚举对应返回值 */
	public void output(AbstractResultCode resultCode, HttpServletResponse httpResponse){
		output(null, resultCode.getCode(), resultCode.getName(), httpResponse)
	}
	
	/** 输出默认系统错误返回值 */
	public void outputFaild(HttpServletResponse httpResponse){
		output(null, AbstractResultCode.error.getCode(), AbstractResultCode.error.getName(), httpResponse)
	}
	
	/** 输出默认系统错误返回code, 与自定义错误描述 */
	public void outputFaild(String msg, HttpServletResponse httpResponse){
		if(StringUtils.isBlank(msg)){
			msg = AbstractResultCode.error.getName()
		}
		output(null, AbstractResultCode.error.getCode(), msg, httpResponse)
	}
	
	/** 输出自定义返回值 */
	public void output(Map returnMap, String resultCode, String msg, HttpServletResponse httpResponse){
		if(returnMap == null){
			returnMap = new HashMap()
		}
		returnMap.put("resultCode", resultCode)
		returnMap.put("msg", msg)
		outputJsonStream(returnMap, httpResponse)
	}

	private void outputJsonStream(Map returnMap, HttpServletResponse httpResponse){
		JsonBuilder jsonBuilder=new JsonBuilder(returnMap)
		String retStr=jsonBuilder.toString()
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"))
	}

	public Map getRequestParamMap(HttpServletRequest request) {
		// 参数Map
		Map properties = request.getParameterMap();
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if(null == valueObj){
				value = null;
			}else if(valueObj instanceof String[]){
				String[] values = (String[])valueObj;
				for(int i=0;i<values.length;i++){
					if(i==0){
						value = values[i] ;
					}else{
						value = value+","+values[i];
					}
				}
				//value = value.substring(0, value.length()-1);
			}else{
				value = valueObj.toString();
			}
			//String dbName=name.substring("search_".length()-1);
			String dbName=name;
			returnMap.put(dbName, value);
		}
		return returnMap;
	}


	public String getPageName(HttpServletRequest httpRequest){
		return httpRequest.getParameter("pageName");
	}
	public String getTableName(HttpServletRequest httpRequest){
		//return httpRequest.getParameter("tableName");
		return "xxx";
	}
	
	public Map createPageMap(HttpServletRequest httpRequest){
		String page=httpRequest.getParameter("page")
		String rows=httpRequest.getParameter("rows")
		String order=httpRequest.getParameter("sord")
		String sort=httpRequest.getParameter("sidx")

		Map<String,Object> pageMap = new HashMap<String,Object>()
		pageMap.put("page", page)
		pageMap.put("rows", rows)
		pageMap.put("sort",  sort)
		pageMap.put("order", order)
		return pageMap;
	}
	
	public Map createRetDataMap(int page,int rows, Map retMap){
		Map retDataMap=new HashMap();
		retDataMap.put("curPage", page);
		retDataMap.put("pageSizes", rows);
		int totalRecords=retMap.get("total");

		int result = totalRecords / rows;
		int totalPages = (totalRecords % rows == 0) ? result : (result + 1);
		retDataMap.put("totalRecords", totalRecords);
		retDataMap.put("totalPages", totalPages);
		retDataMap.put("viewJsonData", retMap.get("rows"));
		return retDataMap;
	}

	private Map delNullKey(Map requestParamMap){
		Map paramMap=new HashMap();
		Set keySet=requestParamMap.keySet();
		for(String key:keySet){
			String value=requestParamMap.get(key);
			if(value!=null && !"".equals(value)){
				paramMap.put(key, value);
			}
		}
		return paramMap;
	}

	public void getInfoList4Page(HttpServletRequest httpRequest, HttpServletResponse httpResponse){

		String page=httpRequest.getParameter("page");
		String rows=httpRequest.getParameter("rows");
		String order=httpRequest.getParameter("sord")
		String sort=httpRequest.getParameter("sidx")
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);

		Map requestParamMap=getRequestParamMap(httpRequest);
		Map paramMap=delNullKey(requestParamMap);
		Map pageMap=new HashMap();
		pageMap.put("page", page);
		pageMap.put("rows", rows);
		pageMap.put("sort", sort);
		pageMap.put("order", order);
		Map retMap=getInfoList4PageService(paramMap, tableName,pageMap);
		Map retDataMap=createRetDataMap(Integer.valueOf(page),Integer.valueOf(rows),retMap);
		JsonBuilder jsonBuilder=new JsonBuilder(retDataMap);
		String retStr=jsonBuilder.toString();

		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));

		return;
	}

	public void createInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sTime = sf.format(new Date());
		Integer retStatus=createInfoService(requestParamMap, tableName);

		return;
	}


	public void updateInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Integer retStatus=updateInfoService(requestParamMap, tableName);
		return;
	}

	public void delInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Integer retStatus=delInfoService(requestParamMap, tableName);
		return;
	}

	public void getInfoById(HttpServletRequest httpRequest, HttpServletResponse httpResponse){

		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Map retMap=getInfoByIdService(requestParamMap, tableName);
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		return;
	}


	public void getInfoListAll(HttpServletRequest httpRequest, HttpServletResponse httpResponse){

		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Map sortMap=new HashMap();
		sortMap.put("sort", sort);
		sortMap.put("order", order);
		List retList=getInfoListAllService(requestParamMap, tableName,sortMap);
		JsonBuilder jsonBuilder=new JsonBuilder(retList);
		String retStr=jsonBuilder.toString();
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));

		return;
	}
	public void getInfoListByDicId(HttpServletRequest httpRequest, HttpServletResponse httpResponse){

		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");
		String dicId=httpRequest.getParameter("dicId");
		String tableName=getTableName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Map sortMap=new HashMap();
		sortMap.put("sort", sort);
		sortMap.put("order", order);
		String where = "dict_id='"+dicId+"'";
		List retList=getInfoListAllService(requestParamMap, tableName,sortMap,where);
		JsonBuilder jsonBuilder=new JsonBuilder(retList);
		String retStr=jsonBuilder.toString();
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));

		return;
	}
	public static void removeNullValue(Map map){
		Set set = map.keySet();
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			Object obj = (Object) iterator.next();
			Object value =(Object)map.get(obj);
			remove(value, iterator);
		}
	}
	private static void remove(Object obj,Iterator iterator){
		if(obj instanceof String){
			String str = (String)obj;
			if(str==null || "".equals(str)){  //过滤掉为null和""的值 主函数输出结果map：{2=BB, 1=AA, 5=CC, 8=  }
				//            if("".equals(str.trim())){  //过滤掉为null、""和" "的值 主函数输出结果map：{2=BB, 1=AA, 5=CC}
				iterator.remove();
			}

		}else if(obj instanceof Collection){
			Collection col = (Collection)obj;
			if(col==null||col.isEmpty()){
				iterator.remove();
			}

		}else if(obj instanceof Map){
			Map temp = (Map)obj;
			if(temp==null||temp.isEmpty()){
				iterator.remove();
			}

		}else if(obj instanceof Object[]){
			Object[] array =(Object[])obj;
			if(array==null||array.length<=0){
				iterator.remove();
			}
		}else{
			if(obj==null){
				iterator.remove();
			}
		}
	}

}
