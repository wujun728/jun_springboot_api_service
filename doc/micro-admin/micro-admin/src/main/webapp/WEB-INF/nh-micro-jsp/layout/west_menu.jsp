<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path=request.getContextPath();

%>

[
{
	"id":1,
	"text":"系统管理",
	"iconCls":"icon-channels",
	"children":[{
		"id":"userlist",
		"text":"用户列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/micromvc/proxy/to?page=/nh-micro-jsp/user-manager/listUserInfo.jsp"
		  }
	},{
		"id":"rolelist",
		"text":"角色列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/micromvc/proxy/to?page=/nh-micro-jsp/user-manager/listRoleInfo.jsp"
		  }
	},{
		"id":"dictlist",
		"text":"字典列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/micromvc/proxy/to?page=/nh-micro-jsp/dictionary-page/listDictionaryInfo.jsp"
		  }
	},
	{
		"id":"menumgr",
		"text":"菜单管理",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/micromvc/proxy/to?page=/nh-micro-jsp/user-manager/listMenuInfo.jsp"
		  }
	},
    {
		"id":"suanlist",
		"text":"算法列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/micromvc/proxy/to?page=/nh-micro-jsp/product-center-page/listProductAlgoInfo.jsp"
		 }
	},
	{
		"id":"zuhesuanlist",
		"text":"组合算法列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/micromvc/proxy/to?page=/nh-micro-jsp/product-center-page/listProductCombineAlgoInfo.jsp"
		  }
	}	  
	]
}
,
{
	"id":2,
	"text":"产品管理",
	"iconCls":"icon-channels",
	"children":[
		{
			"id":"productlist",
			"text":"产品列表",
			"iconCls":"icon-nav",
			"attributes":{
			  "url":"/<%=path %>/micromvc/proxy/to?page=/nh-micro-jsp/product/listProductInfo.jsp"
			  }
		},
		{
			"id":"techlist",
			"text":"产品方案列表",
			"iconCls":"icon-nav",
			"attributes":{
			  "url":"/<%=path %>/micromvc/proxy/to?page=/nh-micro-jsp/product-tech/listProductTechInfo.jsp"
			  }
		}
	]
}
,
{
	"id":3,
	"text":"参数配置",
	"iconCls":"icon-channels",
	"children":[
		{
			"id":"holidaymgr",
			"text":"节假日日历",
			"iconCls":"icon-nav",
			"attributes":{
			  "url":"/<%=path %>/micromvc/proxy/to?page=/nh-micro-jsp/paramseting/holidays.jsp"
			  }
		}
	  	
	]
}

]