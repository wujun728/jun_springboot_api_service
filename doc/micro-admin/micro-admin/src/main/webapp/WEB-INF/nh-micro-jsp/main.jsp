<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-js/js/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-js/js/easyui/themes/icon.css"/>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/json2.js"></script>

<script type="text/javascript" src="<%=path%>/nh-micro-js/js/zTree/js/jquery.ztree.core-3.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-js/js/zTree/css/zTreeStyle/zTreeStyle.css"/>
    <title>管理系统</title>
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
		var serverName="<%=path%>";
	</script>
</head>
<body class="easyui-layout">
	<div region="north" href="<%=path%>/micropage/nh-micro-jsp/layout/north.jsp" split="false" border="false" style="overflow: hidden; height: 30px;background: 	#D1E9E9">
    </div>
	<div region="west" href="<%=path%>/micropage/nh-micro-jsp/layout/west2.jsp" title="导航" split="false" style="width:200px;overflow: hidden;"></div>
	<div region="center" href="<%=path%>/micropage/nh-micro-jsp/layout/center.jsp" title="欢迎访问管理系统" style="overflow: hidden;"></div>
    <div region="south" split="false" style="height: 30px; background: #D2E0F2; ">
        <div class="footer"></div>
    </div>
</body>
</html>
