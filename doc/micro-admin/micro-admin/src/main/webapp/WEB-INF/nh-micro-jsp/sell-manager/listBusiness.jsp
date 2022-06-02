<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>业务管理</title>
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
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/bootstrap.min.js?v=3.4.0"></script>
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/content.js?v=1.0.0"></script>
	<!-- 遮罩层 -->
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/layer.min.js"></script>
	<!-- jqGrid -->
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/jqgrid/i18n/grid.locale-cn.js?0820"></script>
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/jqgrid/jquery.jqGrid.min.js?0820"></script>
	<!-- Sweet alert -->
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/sweetalert/sweetalert.min.js"></script>
	<!-- jQuery Validation plugin javascript-->
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/validate/jquery.validate.min.js"></script>
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/validate/messages_zh.min.js"></script>

</head>
<body class="gray-bg">
<div class="wrapper wrapper-content  animated fadeInRight">
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox ">
				<div class="ibox-content">
					<h4 class="m-t">业务管理</h4>
					<div class="row row-lg">
						<div class="col-sm-12">
							<div class="row">
								<form>
								<div class="col-sm-2">
									<input type="text" class="form-control" id="custName" placeholder="客户姓名">
								</div>
								<div class="col-sm-2">
									<input type="text" class="form-control" id="custPaper" placeholder="证件号">
								</div>
								<div class="col-sm-3">
									<input type="text" class="form-control" id="productName" placeholder="产品名称">
								</div>
								<div class="col-sm-2">
									<select class="form-control" id="busiStatus" placeholder="">
										<option value="">请选择</option>

									</select>

								</div>
								<div class="col-sm-3">
									<button type="button" class="btn btn-primary " onclick="business.search()">
										<i class="fa fa-search" style="width: 13px;"></i>&nbsp;搜索
									</button>
									<button type="reset" class="btn btn-primary " >
										<i class="fa fa-trash" style="width: 13px;"></i>&nbsp;重置
									</button>
								</div>
								</form>
							</div>
						</div>
					</div>
					<div class="jqGrid_wrapper" style="margin-top: 10px">
						<shiro:hasPermission name="addbusiness">
						<h4>
						<button class="btn btn-primary" onclick="business.toAdd()">新增进件</button>
						</h4>
						</shiro:hasPermission>
						<table id="table_list"></table>
						<div id="pager"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden"  id="roletype">
</div>
<script>
	function renderBusinessButton(cellValue,options,rowObject){
        var str = "<shiro:hasPermission name='detailbusiness'>" +
			"<a class='btn btn-primary btn-xs' href='javascript:void(0)' onclick='javascript:get(\""+cellValue+"\",\""+rowObject.status+"\")'>详情</a>" +
			"</shiro:hasPermission>";
        if(rowObject.status==110 || rowObject.status==140 ){//暂存或质检退回
            str += "<shiro:hasPermission name='editbusiness'>" +
				"<a class='btn btn-primary btn-xs' href='javascript:void(0)' onclick='javascript:edit(\""+cellValue+"\",\""+rowObject.status+"\")'>编辑</a>" +
                "</shiro:hasPermission>";
        }else if(rowObject.status==120){//放弃

        }else if(rowObject.status==130){//等待质检
            str += "<shiro:hasPermission name='editbusiness'>" +
				"<a class='btn btn-primary btn-xs' href='javascript:void(0)' onclick='javascript:edit(\""+cellValue+"\",\""+rowObject.status+"\")'>编辑</a>" +
				"</shiro:hasPermission>";
            str += "<shiro:hasPermission name='abandonbusiness'>" +
				"<a class='btn btn-primary btn-xs' href='javascript:void(0)' onclick='javascript:changeStatus(\""+cellValue+"\")'>放弃租赁</a>" +
                "</shiro:hasPermission>";
            str += "<shiro:hasPermission name='qualitybusiness'>" +
				"<a class='btn btn-primary btn-xs' href='javascript:void(0)' onclick='javascript:quality(\""+cellValue+"\",\""+rowObject.status+"\")'>质检</a>" +
                "</shiro:hasPermission>";
        }else if(rowObject.status==160 || rowObject.status==170){//据租
            str += "<shiro:hasPermission name='editbusiness'>" +
                "<a class='btn btn-primary btn-xs' href='javascript:void(0)' onclick='javascript:edit(\""+cellValue+"\",\""+rowObject.status+"\")'>编辑</a>" +
                "</shiro:hasPermission>";
            str += "<shiro:hasPermission name='abandonbusiness'>" +
                "<a class='btn btn-primary btn-xs' href='javascript:void(0)' onclick='javascript:changeStatus(\""+cellValue+"\")'>放弃租赁</a>" +
                "</shiro:hasPermission>";

        }else if(rowObject.status==150 || (rowObject.status>170 && rowObject.status<=200) ){//
            str += "<shiro:hasPermission name='abandonbusiness'>" +
				"<a class='btn btn-primary btn-xs' href='javascript:void(0)' onclick='javascript:changeStatus(\""+cellValue+"\")'>放弃租赁</a>" +
                "</shiro:hasPermission>";
        }else if(rowObject.status >=210  ){//带交租
            str += "<shiro:hasPermission name='payrentbusiness'>" +
				"<a class='btn btn-primary btn-xs' href='javascript:void(0)' onclick='javascript:payRent(\""+cellValue+"\",\""+rowObject.status+"\")'>交租信息</a>" +
                "</shiro:hasPermission>";
        }

        return str;
	}
</script>
<script src="<%=path%>/nh-micro-js/js/system/sell-manage.js"></script>
</body>

</html>