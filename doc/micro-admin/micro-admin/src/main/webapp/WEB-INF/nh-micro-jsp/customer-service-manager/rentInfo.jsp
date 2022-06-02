<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
	String path = request.getContextPath();
	String type = (String) request.getAttribute("type");
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
	<script src="<%=path%>/nh-micro-js/js/system/customer-service-manage.js"></script>
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/laydate/laydate.js"></script>
	<!-- jQuery Validation plugin javascript-->
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/validate/jquery.validate.min.js"></script>
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/validate/messages_zh.min.js"></script>
	<!-- Sweet alert -->
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/sweetalert/sweetalert.min.js"></script>
	<script src="<%=path%>/nh-micro-js/js/common.js"></script>
	<script>
		$(function () {
			initRentInfo();
			initPayInfo('<%=type%>');
            initReduceInfo();
        });
        function renderRentButton(cellValue,options,rowObject,type){
            var button = "";
            if(type === "KF"){
                if(rowObject.status === "100" || rowObject.status === "150"){//待交租或已逾期 减免
                    button += "<shiro:hasPermission name='reduce'>" +
                        "<a  class='btn btn-primary btn-xs' data-toggle='modal' data-target='#modal-form' onclick='reduce(\""+rowObject.id+"\",\""+rowObject.pay_amount_yuan+"\",\""+rowObject.overdue_amount_yuan+"\");'>减免</a>"+
                        "</shiro:hasPermission>";
                }
                if(rowObject.status === "110"){//缴费待审核 审核通过 审核退回 下载房租凭证
                    button += "<shiro:hasPermission name='approve_agree'>" +
                        "<a  class='btn btn-primary btn-xs' onclick='approve(\""+rowObject.id+"\",\"agree\")' >审核通过</a>"+
                        "<a  class='btn btn-primary btn-xs' onclick='approve(\""+rowObject.id+"\",\"reject\")' >审核退回</a>"+
                        "</shiro:hasPermission>";
                    button += "<shiro:hasPermission name='downloadFile'>" +
                        "<a  class='btn btn-primary btn-xs' onclick='downloadFile(\""+rowObject.id+"\")' >下载房租凭证</a>"+
                        "</shiro:hasPermission>";
                }

                if(rowObject.status === "140" || rowObject.status === "130"){//已缴费或缴费待确认 下载房租凭证
                    button += "<shiro:hasPermission name='downloadFile'>" +
                        "<a  class='btn btn-primary btn-xs' onclick='downloadFile(\""+rowObject.id+"\")' >下载房租凭证</a>"+
                        "</shiro:hasPermission>";
                }
            }
            if(type === "CW"){
                if(rowObject.status === "130"){//缴费待确认 确认缴费
                    button += "<shiro:hasPermission name='commit_pay'>" +
                        "<a  class='btn btn-primary btn-xs' onclick='confirmRent(\""+rowObject.id+"\")' >确认缴费</a>"+
                        "</shiro:hasPermission>";
                }
                if(rowObject.status === "140" || rowObject.status === "130"){//已缴费或缴费待确认 下载房租凭证
                    button += "<shiro:hasPermission name='downloadFileCW'>" +
                        "<a  class='btn btn-primary btn-xs' onclick='downloadFile(\""+rowObject.id+"\")' >下载房租凭证</a>"+
                        "</shiro:hasPermission>";
                }
			}

            return button;
        }
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
					<input type="hidden" id="businessId" value='${param.businessId}'/>
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
					<a  class='btn btn-primary' href="#" onclick="goBack()" >关闭</a>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="modal-form" class="modal fade" aria-hidden="true" >
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="row">
				<div class="col-sm-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 id="div_title">减免</h5>
							<div class="ibox-tools">
								<a id="modal-form-closer" data-toggle="modal" href="#modal-form">
									<i class="fa fa-times"></i>
								</a>
							</div>
						</div>
						<div class="ibox-content">
							<form class="form-horizontal" id="editForm">
								<input type="hidden" id="planId" name="planId"  />
								<input type="hidden" id="payAmount"   />
								<input type="hidden" id="payOverdue"  />
								<div class="form-group">
									<label class="col-sm-4 control-label">减免租金金额(元)：</label>
									<div class="col-sm-6">
										<input id="reduceAmount" name="reduceAmount" onblur="validAndCount(this,'amount');" class="form-control" type="text"
											   aria-required="true" aria-invalid="true" class="error">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">减免逾期费用金额(元)：</label>
									<div class="col-sm-6">
										<input id="reduceOverAmount" name="reduceOverAmount" onblur="validAndCount(this,'over');" class="form-control" type="text"
											   aria-required="true" aria-invalid="true" class="error">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label">总计：</label>
									<div class="col-sm-6">
										<input id="reduceTotal" name="reduceTotal" class="form-control" readonly="readonly">
									</div>
								</div>
								<div class="form-group">
									<div class="panel-body" id="html">
										<label  class="col-sm-4 control-label" >上传减免审批:</label>

										<div class="col-sm-6">
											<input  class="control" type="file" id="uploadFile"/>
										</div>
										<%--<div class="col-sm-1">--%>
											<%--<input  class="control" type="button" id="uploadButton" value="上传" onclick="upload()" />--%>
										<%--</div>--%>
									</div>
								</div>
								<input type="hidden" id="fileId" name="fileId">
								<input type="hidden" id="fileUrl" name="fileUrl">
								<input type="hidden" id="fileName" name="fileName">
								<div class="form-group">
									<div class="col-sm-11 col-sm-offset-5">
										<input type="button" class="btn btn-primary" value="提交" onclick="validParam();">
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>