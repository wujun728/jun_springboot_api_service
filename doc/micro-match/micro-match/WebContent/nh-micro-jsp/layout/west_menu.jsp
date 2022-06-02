<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path=request.getContextPath();
String userName=(String)session.getAttribute("nhUserName");
Boolean adminFlag=userName.startsWith("admin");
%>

[
<% if (adminFlag){%>
{
	"id":1,
	"text":"系统管理",
	"iconCls":"icon-channels",
	"children":[{
		"id":12,
		"text":"用户列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/nh-micro-jsp/user-manager/listUserInfo.jsp"
		  }
	}	  
	]
},
	
	<%} %>	
	

{
	"id":10,
	"text":"撮合管理",
	"iconCls":"icon-channels",
	"children":[
	{
		"id":100,
		"text":"买单列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/nh-micro-jsp/match-page/listBuyInfo.jsp"
		  }
	},
	{
		"id":101,
		"text":"卖单列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/nh-micro-jsp/match-page/listSaleInfo.jsp"
		  }
	},
	{
		"id":100,
		"text":"撮合结果列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/nh-micro-jsp/match-page/listResultInfo.jsp"
		  }
	},
	{
		"id":101,
		"text":"撮合规则列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/nh-micro-jsp/match-page/listRuleInfo.jsp"
		  }
	}
	]
}	
	
	
]