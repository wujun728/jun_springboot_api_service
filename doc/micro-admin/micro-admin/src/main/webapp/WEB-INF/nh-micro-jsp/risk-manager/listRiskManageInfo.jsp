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
	<title>风险工单列表</title>
	<meta name="keywords" content="">
	<meta name="description" content="">
	<!-- jqgrid-->
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/font-awesome.min.css?v=4.3.0" rel="stylesheet">
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/jqgrid/ui.jqgrid.css?0820" rel="stylesheet">
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/animate.css" rel="stylesheet">
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/style.css?v=3.2.0" rel="stylesheet">
	<script>
        function renderBusinessButton(cellValue,options,rowObject){
            var button = "";
            if(rowObject.statusVal === "150"){//租赁审核中
                button += "<shiro:hasPermission name='audit'>" +
                    "<a  class='btn btn-primary btn-xs' href='javascript:audit(\""+rowObject.id+"\",\""+rowObject.statusVal+"\");'>审核</a>"+
                    "</shiro:hasPermission>";
            }
            button += "<shiro:hasPermission name='audit_history'>" +
                "<a  class='btn btn-primary btn-xs' href='javascript:auditHistory(\""+rowObject.id+"\",\""+rowObject.statusVal+"\");'>详情</a>"+
                "</shiro:hasPermission>";
            return button;
        }
	</script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content  animated fadeInRight">
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox ">
				<div class="ibox-content">
					<h4 class="m-t" >条件查询</h4>
					<div class="row row-lg">
						<div class="col-sm-12">
							<div class="row">
								<form id="searchForm" method="post">
									<div class="col-sm-3">
										<div class="input-group">
											<div class="input-group-btn">
												<button data-toggle="dropdown" class="btn btn-white dropdown-toggle" type="button">客户姓名
												</button>
											</div>
											<input type="text" class="form-control" id="customerName" placeholder="">
										</div>
									</div>
									<div class="col-sm-3">
										<div class="input-group">
											<div class="input-group-btn">
												<button data-toggle="dropdown" class="btn btn-white dropdown-toggle" type="button">证件号
												</button>
											</div>
											<input type="text" class="form-control" id="idNumber" placeholder="">
										</div>
									</div>
									<div class="col-sm-3">
										<div class="input-group">
											<div class="input-group-btn">
												<button data-toggle="dropdown" class="btn btn-white dropdown-toggle" type="button">产品名称
												</button>
											</div>
											<input type="text" class="form-control" id="productName" placeholder="">
										</div>
									</div>
									<div class="col-sm-3">
										<div class="input-group">
											<div class="input-group-btn">
												<button data-toggle="dropdown" class="btn btn-white dropdown-toggle" type="button">状态
												</button>
											</div>
											<select id="status" name="" class="form-control" style="width:157px;">
												<option value="">---请选择---</option>
											</select>
										</div>
									</div>
									<div class="col-sm-3" style="margin-top: 10px">
										<button type="button" class="btn btn-primary " onclick="risk.search()">
											<i class="fa fa-search" style="width: 13px;"></i>&nbsp;搜索
										</button>
										<button type="button" class="btn btn-primary " onclick="risk.resetSearch()">
											<i class="fa fa-trash" style="width: 13px;"></i>&nbsp;重置
										</button>
									</div>
								</form>
							</div>
						</div>
					</div>
					<div class="jqGrid_wrapper" style="margin-top: 10px">
						<table id="table_list"></table>
						<div id="pager"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 全局js -->
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/jquery-2.1.1.min.js"></script>
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/bootstrap.min.js?v=3.4.0"></script>
<!-- jqGrid -->
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/jqgrid/i18n/grid.locale-cn.js?0820"></script>
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/jqgrid/jquery.jqGrid.min.js?0820"></script>
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/content.js?v=1.0.0"></script>
<script src="<%=path%>/nh-micro-js/js/system/risk-manage.js"></script>
</body>

</html>