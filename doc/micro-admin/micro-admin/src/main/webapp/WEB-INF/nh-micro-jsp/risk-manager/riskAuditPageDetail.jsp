<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String path = request.getContextPath();
	String businessId = (String) request.getAttribute("businessId");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>审核详情</title>
	<meta name="keywords" content="">
	<meta name="description" content="">
	<!-- jqgrid-->
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/font-awesome.min.css?v=4.3.0" rel="stylesheet">
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/jqgrid/ui.jqgrid.css?0820" rel="stylesheet">
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/animate.css" rel="stylesheet">
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/style.css?v=3.2.0" rel="stylesheet">
	<!-- 全局js -->
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/jquery-2.1.1.min.js"></script>
	<script src="<%=path%>/nh-micro-js/js/jquery.form.js"></script>
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/bootstrap.min.js?v=3.4.0"></script>
	<!-- jqGrid -->
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/jqgrid/i18n/grid.locale-cn.js?0820"></script>
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/jqgrid/jquery.jqGrid.min.js?0820"></script>
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/content.js?v=1.0.0"></script>
	<script src="<%=path%>/nh-micro-js/js/system/risk-manage-audit.js"></script>
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/laydate/laydate.js"></script>
	<script>
		$(function () {
			initDetailInfo();
        })
	</script>
</head>
<body class="gray-bg" id="bodyTemp">
<%--存放归属信息--%>
<%@ include file="/WEB-INF/nh-micro-jsp/tab.jsp" %>
<div class="form-group" align="center">
	<input type="hidden" id="businessId" value='<%=businessId%>'/>
	<img src="<%=path%>/nh-micro-js/images/none.png" align="center" alt="没有相关内容" style="display: none;" id="noneImg">
</div>
</body>
</html>