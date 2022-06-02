<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
	String path = request.getContextPath();
	String businessId = (String) request.getAttribute("businessId");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>交租信息</title>
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
	<script src="<%=path%>/nh-micro-js/js/system/payRent.js"></script>
	<script src="<%=path%>/nh-micro-js/js/common.js"></script>
	<script src="<%=path%>/nh-micro-js/js/moment.js"></script>
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/laydate/laydate.js"></script>
	<style>
		.ui-jqgrid tr.jqgrow td {
			white-space: normal !important;
			height:auto;
			vertical-align:text-top;
			padding-top:2px;
		}
	</style>
	<script>
        $(function () {
            initRentInfo();
            initPayInfo(1);
            initReduceInfo();
        })
	</script>
</head>
<body class="gray-bg">
<%--tab--%>
<%@ include file="/WEB-INF/nh-micro-jsp/tab.jsp" %>
<div class="wrapper wrapper-content  animated fadeInRight">
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox ">
				<div class="ibox-content">
					<input type="hidden" id="businessId" value='<%=businessId%>'/>
					<div class="panel panel-primary">
						<div class="panel-heading">
							交租计划表
						</div>
						<div class="panel-body">
							<div class="jqGrid_wrapper">
								<table id="table_list"></table>
							</div>
							<div class="jqGrid_wrapper">
								<table id="repay_plan_list"></table>
							</div>
						</div>
					</div>

					<div class="panel panel-primary">
						<div class="panel-heading">
							减免信息
						</div>
						<div class="panel-body">
							<div class="jqGrid_wrapper">
								<table id="reduce_list"></table>
							</div>
						</div>
					</div>
					<a  class='btn btn-primary' href="#" onclick="window.history.go(-1)" >关闭</a>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">房租凭证上传</h4>
			</div>
			<div class="modal-body">
				<input type="file" id="rentFile">
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" onclick="uploadRent()">上传</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</body>

</html>

<script>
    function renderPayButton(cellValue,options,rowObject){
        if(rowObject.status==100 || rowObject.status==120 || rowObject.status==150){
            return "<shiro:hasPermission name='uploadrent'>" +
                "<a class='btn btn-primary btn-xs' href='javascript:void(0)' onclick='javascript:openUploadWindow(\""+cellValue+"\")'>房租凭证上传</a>" +
                "</shiro:hasPermission>";
        }else {
            return "<shiro:hasPermission name='downloadrent'>" +
                "<a class='btn btn-primary btn-xs' href='javascript:void(0)' onclick='javascript:downloadRent(\""+cellValue+"\")'>下载房租凭证</a>" +
                "</shiro:hasPermission>";
        }
        return " ";
    }
</script>