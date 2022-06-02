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
	<title>审核页面</title>
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
            $("#remark1").attr("disabled", "disabled");
            $("#remark2").attr("disabled", "disabled");
            $("#rentTime").removeAttr("disabled")
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
					<h4 class="m-t text-success" >审核信息</h4>
					<div class="row row-lg">
						<div class="col-sm-12">
							<div class="row">
								<form id="commitForm" method="post">
								<input type="hidden" id="businessId" value='<%=businessId%>'/>
								<table id="table_list1" width="100%">
									<tr>
										<td style="height: 50px;">
											<label class="col-sm-3" style="width: 100px;margin-top: 5px">
												<input type='radio' value='approve' checked='checked' name='result' onclick="controlParam(this.value);">通过
											</label>
											<label class="col-sm-3" style="width: 100px;position: relative;margin-top: 10px" >起租时间：</label>
											<input class="form-control layer-date" name="rentTime" id="rentTime" placeholder="请填写起租时间" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})">
										</td>
									</tr>
									<tr>
										<td>
											<label class="col-sm-3" style="width: 100px;margin-top: 5px">
												<input type='radio' value='reject' name='result' onclick="controlParam(this.value);">拒租
											</label>
										</td>
									</tr>
									<tr>
										<td>
											拒租原因：
											<textarea placeholder="text文本1000字符以内" name="remark" id="remark1" style="width: 100%"></textarea>
										</td>
									</tr>
									<tr>
										<td>
											<label class="col-sm-3" style="width: 100px;margin-top: 5px">
												<input type='radio' value='replenish' name='result' onclick="controlParam(this.value);">补充资料
											</label>
										</td>
									</tr>
									<tr>
										<td>
											补充说明：
											<textarea placeholder="text文本1000字符以内" name="remark" id="remark2" style="width: 100%"></textarea>
										</td>
									</tr>
									<tr><td align='center'>
											<a  class='btn btn-primary' href="#" onclick="goBack()" >关闭</a>
											<a  class='btn btn-primary' href='#' onclick="commitAudit();">提交</a>
										</td>
									</tr>
								</table>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>

</html>