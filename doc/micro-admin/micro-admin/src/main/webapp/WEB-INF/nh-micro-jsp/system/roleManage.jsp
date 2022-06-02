<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>角色管理</title>
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
	<!-- 自定义js -->
	<script src="<%=path%>/nh-micro-js/js/system/role-manage.js"></script>
</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-content">
                        <h4 class="m-t">角色管理</h4>
                        <div class="row row-lg">
                            <div class="col-sm-12">
                                <div class="row">
                                    <div class="col-sm-3">
                                        <div class="input-group">
                                            <div class="input-group-btn">
                                                <button data-toggle="dropdown" class="btn btn-white dropdown-toggle" type="button">角色标识
                                                </button>
                                            </div>
                                            <input type="text" class="form-control" id="s_role_id" placeholder="">
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="input-group">
                                            <div class="input-group-btn">
                                                <button data-toggle="dropdown" class="btn btn-white dropdown-toggle" type="button">角色名称
                                                </button>
                                            </div>
                                            <input type="text" class="form-control" id="s_role_name" placeholder="">
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <button type="button" class="btn btn-primary " id="search">
                                            <i class="fa fa-search" style="width: 13px;"></i>&nbsp;搜索
                                        </button>
                                        <button type="button" class="btn btn-primary " id="reSearch">
                                            <i class="fa fa-refresh" style="width: 13px;"></i>&nbsp;重置
                                        </button>
                                        <shiro:hasPermission name="button">
                                        	<a id="add" data-toggle="modal" class="btn btn-success" ><i class="fa fa-plus"></i>新增</a>
                                        </shiro:hasPermission>
                                        <shiro:hasPermission name="editrole">
                                        	<a id="modify" data-toggle="modal" class="btn btn-info" ><i class="fa fa-pencil"></i>编辑</a>
                                        </shiro:hasPermission>
                                        <!-- <a id="delete" data-toggle="modal" class="btn btn-warning" href=""><i class="fa fa-times"></i>删除</a> -->
                                    </div>
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
	<div id="modal-form" class="modal fade" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="row">
	                <div class="col-sm-12">
	                    <div class="ibox float-e-margins">
	                        <div class="ibox-title">
	                            <h5 id="div_title">编辑数据角色</h5>
	                            <div class="ibox-tools">
	                                <a id="modal-form-closer" data-toggle="modal" href="#modal-form">
	                                    <i class="fa fa-times"></i>
	                                </a>
	                            </div>
	                        </div>
	                        <div class="ibox-content">
	                            <form class="form-horizontal m-t" id="editForm">
	                              <input type="hidden" id="id" name="id"  />
	                                <div class="form-group">
	                                    <label class="col-sm-4 control-label">角色标识：</label>
	                                    <div class="col-sm-6">
	                                        <input id="role_id" name="role_id" class="form-control" type="text"
	                                               aria-required="true" aria-invalid="true" class="error">
	                                    </div>
	                                </div>
	                                <div class="form-group">
	                                    <label class="col-sm-4 control-label">角色名称：</label>
	                                    <div class="col-sm-6">
	                                       <input id="role_name" name="role_name" class="form-control" type="text"
	                                               aria-required="true" aria-invalid="true" class="error">
	                                    </div>
	                                </div>
	                                <div class="form-group">
	                                    <label class="col-sm-4 control-label">角色类型：</label>
	                                    <div class="col-sm-6">
	                                       <select id="role_type" name="role_type" class="form-control ">
	                                       		<option></option>
	                                       		<c:forEach var="entry" items="${role_type}"  >
	                            				<option value="${entry.item_id}">${entry.item_name }</option>
	                            				</c:forEach>
											</select>
	                                    </div>
	                                </div>
	                                <div class="form-group">
	                                    <div class="col-sm-11 col-sm-offset-5">
	                                        <button class="btn btn-primary" type="submit">提交</button>
	                                        <button class="btn btn-cancel" data-dismiss="modal" type="button" id="close" style="display: none;">关闭</button>
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