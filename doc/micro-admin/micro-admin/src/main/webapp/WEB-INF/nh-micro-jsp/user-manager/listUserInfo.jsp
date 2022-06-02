<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户信息管理</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-js/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-js/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/common.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/zTree/js/jquery.ztree.core-3.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-js/js/zTree/css/zTreeStyle/zTreeStyle.css">
<script type="text/javascript">

function getDivContent(){
	alert($("#checkOrgdialog").html());
}

$(function(){
	$('#userInfoList').datagrid({
		nowrap:true,
		striped:true,
		pagination : true,
		fitColumns: true,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url:"<%=path%>/micromvc/uc/userList/getMyInfoList4Page",
		columns:[[
					{
						field : 'id',
						title : 'id',
						width : 100

					},
					{
						field : 'user_id',
						title : '登录名',
						width : 50,
						sortable:true
					},
					{
						field : 'user_name',
						title : '用户名称',
						width : 50,
						sortable:true
					},
					{
						field : 'user_status',
						title : '是否启用',
						width : 50,
						formatter: function(value, row, index){
							if(value == 0){
								return "启用";
							}else{
								return "禁用";
							}
						}
					},

					{
						field : 'user_mobile',
						title : '手机号',
						width : 50

					},
					{
						field : 'user_id_num',
						title : '身份证',
						width : 50

					},
					{
						field : 'user_email',
						title : 'email',
						width : 50

					},
					{
						field : 'user_remark',
						title : '备注',
						width : 200

					}					
				]],
        toolbar : [ {
			id : "add",
			text : "添加",
			iconCls:"icon-add",
			handler : function() {
				add();
			}
		},{
			id : "update",
			text : "修改",
			iconCls : "icon-edit",
			handler : function() {
				updateUserInfo();
			}
		},{
			id : "delete",
			text : "删除",
			iconCls : "icon-cancel",
			handler : function() {
				remove();
			}

		},{
			id : "changePwd",
			text : "密码重置",
			iconCls : "icon-reload",
			handler : function() {
				changePwd();
			}
		
		},{
			id : "setRole",
			text : "角色设置",
			iconCls : "icon-edit",
			handler : function() {
				SetRole();
			}
		
		},	
//		{
//			id : "setDept",
//			text : "部门设置",
//			iconCls : "icon-edit",
//			handler : function() {
//				clicke();
//			}		
//		},
		{
			id : "refresh",
			text : "刷新",
			iconCls : "icon-reload",
			handler : function() {
				refresh();
			}
		
		}],
        rownumbers:false,
        singleSelect:true
		
	});
});

function ReQuery(){
	var data = $('#searchForm').serializeObject();
	$('#userInfoList').datagrid('reload',data);
}

function clearForm(){
	$('#searchForm').form('clear');
}

var  setuser_type="add";
var  update_userid;

// [添加]按钮
function add(){
	setuser_type = "add";
		$("#addOne").form("clear");
		$("#addForm #addShowForm_temp").val("add");
		$("#addOne").dialog('open').dialog('setTitle', '用户信息添加');
}
/* 增加 用户*/
function addOne(){
	if($("#addForm").form('validate')==false){
		return;
	}
	if(!/^[\w]+$/.test($("#addForm #user_id").val())){
		$.messager.alert("操作提示", "登录名只能为英文数字或下划线！","error");  
		return;
	} 
	var dataO = $("#addForm").serialize();
	var temp = $("#addForm #addShowForm_temp").val();
	if(temp=="add"){
		var url="<%=path%>/micromvc/uc/userList/createInfo";
		$.post(url,dataO,function(data,stats){
			if(data !=""){
				$.messager.alert("操作提示", "存在同登录名的用户\n 请修改登录名！","error");  
			}else{
				if(stats=="success" ){
					$.messager.show({
						msg : "操作成功",
						title : "消息"
					});
					refresh();
					addCancel();
				}
			}
		});
	}else{
		updateSubmit();
	}
}
//取消新增用户
function addCancel() {
	$("#addForm").form("clear");
	$("#addOne").dialog('close');
}

//[修改]按钮
function updateUserInfo(){  
	setuser_type = "update";
	var sels = $("#userInfoList").datagrid("getSelected");
    if(sels==""||sels==null){
    	$.messager.alert("操作提示", "请选择行！","error");  
    }else{
    	$("#updateOneForm #passWord").remove();
    	$("#updateOneForm p").remove();
    	var sels = $("#userInfoList").datagrid("getSelected");
    	$("#updateOne").dialog('open').dialog('setTitle', '用户信息修改');
    	$("#updateOne").form("load", sels);
    	$("#updateOneForm #showForm_temp").val("update");
    }
}
//修改用户信息
function updateSubmit(){
	if($("#updateOneForm").form('validate')==false){
		return;
	}
	if(!/^[\w]+$/.test($("#updateOneForm #user_id").val())){
		$.messager.alert("操作提示", "登录名只能为英文数字或下划线！","error");  
		return;
	}
	var url="<%=path%>/micromvc/uc/userList/updateInfo";
		$.post(url, $("#updateOneForm").serialize(), function(data, stats) {
			if(data !=""){
				$.messager.alert("操作提示", "存在同登录名的用户\n 请修改登录名！","error");  
			}else{
				if(stats=="success" ){
					$.messager.show({
						msg : "操作成功",
						title : "消息"
					});
					refresh();
					updatecancel();
				}
			}
		});
};
//删除用户
function remove(){
	var sels = $('#userInfoList').datagrid("getSelected");
	if(sels == ''|| sels==null){
		$.messager.alert("操作提示", "请选择行！","error");  
	}else{
		$.messager.confirm("操作提示", "确认删除该用户吗？", function (data) {
			 if (data) {
				 var querydata = $('#searchForm').serializeObject();
					var id = sels.id;
					var url="<%=path%>/micromvc/uc/userList/delInfo?id="+id;
					$.post(url,function(data,stats){
						if(stats=="success" ){
							$.messager.show({
								msg : "操作成功",
								title : "消息"
							});
							$('#userInfoList').datagrid('reload',querydata);
						}
					});
			 }
		});
		
	}
}


function refresh(){
	var querydata = $('#searchForm').serializeObject();
	$('#userInfoList').datagrid('reload',querydata);
}

//取消重置密码
function changeCancel(){
	$("#changePwdForm").form("clear");
	$("#changePassword").dialog('close');
}

//[密码重置] 按钮
function changePwd(){
	var sels = $('#userInfoList').datagrid("getSelected");
	if(sels == ''|| sels==null){
		$.messager.alert("操作提示", "请选择行！","error");  
		return false;
	}else{
		$("#changePwdForm").form("clear");
		$("#changePwdForm").form("load",sels);
		$("#changePwdForm #user_password").val("");
		$("#changePassword").dialog('open').dialog('setTitle', '密码重置');
	}
}
//取消修改用户信息
function updatecancel() {
	$("#updateOneForm").form("clear");
	$("#updateOne").dialog('close');
}

//重置密码
function changeSubmit(){
	//var sels = $('#userInfoList').datagrid("getSelected");
	var newPwd = $("#changePassword #user_password").val();
	var confirmPwd = $("#changePassword #confirmPwd").val();
	if(newPwd==""||newPwd=="undifine"){
		$.messager.show({
			msg : "新密码不能为空！",
			title : "消息"
		});
		return false;
		}
	if(confirmPwd=="undifine"||confirmPwd==""){
		$.messager.show({
			msg : "确认密码不能为空!",
			title : "消息"
		});
		return false;
	}	
	if(newPwd!=confirmPwd){
		$.messager.show({
			msg : "新密码和确认密码不一致！",
			title : "消息"
		});
		return false;
	}
	var dataO = $("#changePwdForm").serializeObject();
	var url="<%=path%>/micromvc/uc/userList/modifyPass";

     $.post(url,dataO,function(data,stats){
			if(stats=="success"){
				$.messager.show({
					msg : "操作成功",
					title : "消息"
				});
				$("#changePassword").dialog('close');
			}

	});
}
//[角色设置]按钮
function SetRole() {
	var sels = $("#userInfoList").datagrid("getSelected");
	if(sels == ''|| sels==null){
		$.messager.alert("操作提示", "请选择行！","error");  
		return;
	}
	var update_userid = sels.user_id;
	$("#aur").dialog('open').dialog('setTitle', '角色配置');
	queryRole_Set(update_userid);
}


	function checkOrg() {
		if (seleObj != null && seleObj != "") {
			if (clicktype == "1") {
				$("#searchForm #userOrgId").val(seleObj.nodeId);
			} else if (clicktype == "2") {

				$("#addForm #userOrg").val(seleObj.name);
				$("#addForm #userOrgId").val(seleObj.nodeId);
			} else if (clicktype == "3") {
				$("#updateOneForm #userOrg").val(seleObj.name);
				$("#updateOneForm #userOrgId").val(seleObj.nodeId);
			}

			$("#checkOrgdialog").dialog('close');
		} else {
			$.messager.alert('信息提示', '请选择所属机构', 'info');
		}
	}

	function Orgclose() {
		$("#checkOrgdialog").dialog('close');
	}
	var clicktype;
	//[部门设置]
	function clicke() {
		var sels = $('#userInfoList').datagrid("getSelected");
		if(sels == ''|| sels==null){
			$.messager.alert("操作提示", "请选择行！","error");  
			return false;
		}
		var userId=sels.user_id;
		//clicktype = str;
		$("#checkOrgdialog").dialog('open').dialog('setTitle', '选择机构');
		initTree(userId);
	}

	var zTreeObj;
	var setting = {
	
        check: {
            enable: true,
            chkboxType: { "Y": "", "N": "" }
        },
		data : {
			key : {
				name : "dept_name"
			},
			simpleData : { //简单的数据源，一般开发中都是从数据库里读取，API有介绍，这里只是本地的                           
				enable : true,
				idKey : "dept_id", //id和pid，这里不用多说了吧，树的目录级别  
				pIdKey : "dept_parent_id",
				rootPId : "root" //根节点  
			}
		},		
		callback : {
			onClick : zTreeOnClick
		}
	};

	var zNodes;


	function initTree(userId) {
		var url="<%=path%>/micromvc/uc/deptList/getAllDeptList";
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			dataType : "json",
			url:url,
			//url : webPath + 'theorganizationController/QueryZTree.do',
			success : function(data) {
				zNodes = data;
				zTreeObj = $.fn.zTree.init($("#tree"), setting, zNodes);
				checkSelectTreeNode(zTreeObj,userId);
			},
			error : function() {
				alert("请求失败");
			}
		});
		
		
	}

	function checkSelectTreeNode(treeObj,userId){
		var url="<%=path%>/micromvc/uc/userDept/getInfoListAll";
		url=url+"&user_id="+userId;
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			dataType : "json",
			url:url,
			success : function(data) {
                for (var i = 0; i < data.length; i++) {
                    var node = treeObj.getNodeByParam("id", data[i].dept_id, null);
                    treeObj.checkNode(node);
                }
			},
			error : function() {
				alert("请求失败");
			}
		});		
	}
	var seleObj;
	function zTreeOnClick(event, treeId, treeNode) {
		//alert(treeNode.id + ", " + treeNode.name+","+treeNode.nodeType);
		var treeObj = $.fn.zTree.getZTreeObj("tree");
		var nodes = treeObj.getSelectedNodes();
		seleObj = nodes[0];
	};

	//角色设置_初始页面_查询所有角色
	var hasAttrDataGrid;
	var noAttrDataGrid;
	function queryRole_Set(userid) {
		$("#role_user_id").attr("value", userid);
		var ref_url="<%=path%>/micromvc/uc/userRole/getInfoList4Ref?user_id="+userid;
		
		hasAttrDataGrid = $('#hasAttrDataGrid').datagrid({
			url : ref_url,
			nowrap : true,
			striped : true,
			rownumbers : false,
			singleSelect : true,
				columns : [ [ {
						width : '1',
						title : 'id',
						field : 'id',
						sortable : false,
						hidden : true
					}, {
						width : '1',
						title : '角色id',
						field : 'role_id',
						sortable : false,
						hidden : true
					}, {
						width : '150',
						title : '角色名称',
						field : 'role_name',
						sortable : false
					} ] ]
			});
		
		var role_url="<%=path%>/micromvc/uc/roleList/getInfoListAll";

		noAttrDataGrid = $('#noAttrDataGrid').datagrid({
			url : role_url,
			nowrap : true,
			striped : true,
			rownumbers : false,
			singleSelect : true,
			columns : [ [ {
				width : '1',
				title : 'id',
				field : 'id',
				sortable : true,
				hidden : true
			}, {
				width : '1',
				title : 'role_id',
				field : 'role_id',
				sortable : true,
				hidden : true
			}, {
				width : '150',
				title : '角色名称',
				field : 'role_name',
				sortable : false
			} ] ]
		});

	}
	//角色设置_移入
	function moveIn() {
		var update_userid = $("#role_user_id").val();
		var obj = $('#noAttrDataGrid').datagrid('getSelected');
		if (obj == null) {
			$.messager.alert("操作提示"," 请选择要移入的属性！","error");  
			return;
		}
		var hrows = $('#hasAttrDataGrid').datagrid('getRows'); //这段代码是获取当前页的所有行。
		for (var i = 0; i < hrows.length; i++) {
			//获取每一行的数据
			if (obj.role_id == hrows[i].role_id) {
				$.messager.alert("操作提示","已经配置" + obj.role_name + "角色,请重新选择。","error");  
				return;
			}
		}
		
		var url="<%=path%>/micromvc/uc/userRole/createInfo";
		var dataO = {
			'user_id' : update_userid,
			'role_id' : obj.role_id
		};
		$.post(url, dataO, function(data, stats) {
			if (stats == "success") {
				$('#hasAttrDataGrid').datagrid('reload');
				$.messager.show({
					msg : "操作成功",
					title : "消息"
				});
			} else {
				$.messager.show({
					msg : data.msg,
					title : "消息"
				});

			}
		});

	}
	//角色设置_移除
	function moveOut() {
		var obj = $('#hasAttrDataGrid').datagrid('getSelected');
		if (obj == null) {
			$.messager.alert("操作提示"," 请选择要移出的属性！","error");  
			return;
		}
		var url="<%=path%>/micromvc/uc/userRole/delInfo";
		var dataO = {
			'id' : obj.id,
		};
		$.post(url, dataO, function(data, stats) {
			if (stats == "success") {
				$('#hasAttrDataGrid').datagrid('reload');
				$.messager.show({
					msg : "操作成功",
					title : "消息"
				});
			} else {
				$.messager.show({
					msg : data.msg,
					title : "消息"
				});

			}
		});

	}
	
    
	
	
</script>
</head>
<body id="userRole" class="easyui-layout">

	<div id="roleQuery" class="dQueryMod" region="north"
		style="height: 55px">
		<form id="searchForm">
			<table id="searchTable">
				<tr>
					<td>登录名：</td>
					<td><input type="text" id="user_id" name="user_id"class="dInputText"/></td>
					<td>用户名称：</td>
					<td><input type="text" id="user_name" name="user_name"class="dInputText" /></td>
					<td>角色标识：</td>
					<td><input type="text" id="role_id" name="role_id"class="dInputText" /></td>
					<td>
						<a href="#" class="easyui-linkbutton dRbtnSearch"iconCls="icon-search" onclick="ReQuery()">查询</a>
						<a href="#"class="easyui-linkbutton dRbtnClean" iconCls="icon-redo"onclick="clearForm()">清空</a>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div id="roleList" region="center">
		<div class="easyui-tabs l_listwid" id="accountTab">
			<table id="userInfoList"></table>
		</div>
	</div>
	<!-- 密码修改 -->
	<div id="changePassword" class="easyui-dialog" modal="true" align="center" style="padding: 10px; border: 0px; margin: 0px; width: 540px;"
		closed="true" resizable="true" inline="false">
		<form id="changePwdForm" action="" method="post">
			<table id="changePwdTable" style="margin-top: 35px; margin-left: -40px;">
				<tr>
					<td>
					<input	type="hidden" id="id" name="id" value="" />
					<input	type="hidden" id="user_id" name="user_id" value="" /> 
					<input	type="hidden" id="user_name" name="user_name" value="" />
					</td>
				</tr>
				<tr>
					<td align="right">新密码：</td>
					<td><input type="password" id="user_password" name="user_password" value="" /></td>
				</tr>
				<tr>
					<td align="right">确认密码：</td>
					<td><input type="password" id="confirmPwd" name="confirmPwd" value="" /></td>
				</tr>
			</table>
			<div id="buttons" style="margin-top: 20px; margin-left: 40px; padding-bottom: 10px;">
				<a class="easyui-linkbutton dPbtnDark70" href="javascript:changeSubmit();">确认</a> 
				<a class="easyui-linkbutton dPbtnLight70" href="javascript:changeCancel();">取消</a>
			</div>
		</form>
	</div>

	<!-- 修改用户信息 -->
	<div id="updateOne" class="easyui-dialog" modal="true" align="center" style="padding: 10px; border: 0px; margin: 0px; width: 540px;"
		closed="true" resizable="true" inline="false">
		<form id="updateOneForm" novalidate method="post" action="">
			<input type="hidden" id="showForm_temp" value="" />
			<table id="updateUserTable" style="margin-top: 10px; margin-left: -40px;">
				<tr>
					<!-- <td>Id：</td> -->
					<td><input type="hidden" id="id" name="id" value="" /></td>
				</tr>
				<tr>
					<td align="right">登录名：</td>
					<td><input type="text" id="user_id" name="user_id" value="" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr>
					<td align="right">用户名称：</td>
					<td><input type="text" id="user_name" name="user_name" value="" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr>
					<td align="right">是否启用：</td>
					<td>
						<select name="user_status" id="user_status" class="easyui-combobox" required="true"  style="width: 100px;" panelHeight="50px" editable="false">
							<option value="0">启用</option>
							<option value="1">禁用</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">电话：</td>
					<td><input type="text" id="user_mobile" name="user_mobile" value=""  validtype="mobile" class="easyui-validatebox" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')" /></td>
				</tr>
				<tr>
					<td align="right">email：</td>
					<td><input type="text" id="user_email" name="user_email" value=""  data-options="validType:'email'" class="easyui-validatebox"  /></td>
				</tr>				
				<tr>
					<td align="right">身份证号：</td>
					<td><input type="text" id="user_id_num" name="user_id_num" value="" onkeyup="value=value.replace(/[\W]/g,'') "  /></td>
				</tr>
				<tr>
					<td align="right">备注：</td>
					<td><input class="easyui-textbox" id="user_remark" name="user_remark" data-options="multiline:true" style="height: 60px;"/></td>
				</tr>

			</table>
			<div id="buttons" style="margin-top: 20px; margin-left: 40px; padding-bottom: 10px;">
				<a class="easyui-linkbutton dPbtnDark70"href="javascript:updateSubmit();">确认</a> 
				<a class="easyui-linkbutton dPbtnLight70"href="javascript:updatecancel();">取消</a>
			</div>
		</form>
	</div>

<!-- 添加用户 -->
	<div id="addOne" class="easyui-dialog" modal="true" align="center" style="padding: 10px; border: 0px; margin: 0px; width: 540px;"
		closed="true" resizable="true" inline="false">
		<form id="addForm" novalidate method="post" action="">
			<input type="hidden" id="addShowForm_temp" value="" />

			<table id="addTable" style="margin-top: 10px; margin-left: -40px;">
				<tr>
					<!-- <td>Id：</td> -->
					<td><input type="hidden" id="id" name="id" value="" /></td>
				</tr>
				<tr>
					<td align="right">登录名：</td>
					<td><input type="text" id="user_id" name="user_id" value=""  class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr>
					<td align="right">用户名称：</td>
					<td><input type="text" id="user_name" name="user_name" value="" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr>
					<td align="right">密码：</td>
					<td><input type="password" id="user_passWord" name="user_password" value="" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr>
					<td align="right">是否启用：</td>
					<td><select name="user_status" id="user_status" class="easyui-combobox" required="true" editable="false" style="width: 100px;hight:30px;">
							<option value="0" selected="selected">启用</option>
							<option value="1">禁用</option>
					</select></td>
				</tr>
				<tr>
					<td align="right">电话：</td>
					<td><input type="text" id="user_mobile" name="user_mobile" value="" validtype="mobile" class="easyui-validatebox" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')" /></td>
				</tr>
				<tr>
					<td align="right">email：</td>
					<td><input type="text" id="user_email" name="user_email" value="" data-options="validType:'email'" class="easyui-validatebox" /></td>
				</tr>				
				<tr>
					<td align="right">身份证号：</td>
					<td><input type="text" id="user_id_num" name="user_id_num" value=""  onkeyup="value=value.replace(/[\W]/g,'') "  /></td>
				</tr>
				<tr>
					<td align="right">备注：</td>
					<!-- <td><input type="text" id="remarks" name="remarks" value="" /></td> -->
					<td>
						<input class="easyui-textbox" id="user_remark" name="user_remark" data-options="multiline:true" style="height: 60px;"/>
					</td>
				</tr>

			</table>
			<div id="buttons"
				style="margin-top: 20px; margin-left: 40px; padding-bottom: 10px;">
				<a class="easyui-linkbutton dPbtnDark70" href="javascript:addOne();">确认</a>
				<a class="easyui-linkbutton dPbtnLight70" href="javascript:addcancel();">取消</a>
			</div>
		</form>
	</div>

	<div id="checkOrgdialog" class="easyui-dialog" modal="true" align="center" style="padding: 20px 10px 10px 10px; border: 0px; margin: 0px; width: 300px;"
		closed="true" resizable="true" inline="false">
		<div split="true" title="组织机构">
			<ul id="tree" class="ztree"></ul>
		</div>
		<div id="buttons" style="height: 30px; padding-top: 20px;">
			<a class="easyui-linkbutton dPbtnDark70" href="javascript:checkOrg();">确认</a> 
			<a class="easyui-linkbutton dPbtnLight70" href="javascript:Orgclose();">取消</a>
		</div>
	</div>

	<!-- 这是为角色添加用户的 -->
	<div id="aur" class="easyui-dialog" class="easyui-dialog" modal="true" align="center"
		style="padding: 20px 10px 15px 10px; border: 0px; margin: 0px; width: 700px;" closed="true" resizable="true" inline="false">
		<form id="userRoleForm" novalidate method="post" action="">
			<input type="hidden" id="role_user_id" value="" />
		</form>
		<div class="easyui-tab" style="width: 210px; height: auto; overflow: hidden; background: #f8f8f8; float: left; margin-left: 8px; border: 1px solid #e2e2e2;" title="系统角色">系统角色<br>
			<table id="noAttrDataGrid"></table>
		</div>

		<div style="width: 80px; float: left; margin: 20px 0 20px 20px;">
			<a href="javascript:moveIn();" class="easyui-linkbutton dPbtnMove" style="margin-bottom: 20px;">移入 》</a> 
			<a href="javascript:moveOut()" class="easyui-linkbutton dPbtnMove">《 移出</a>
		</div>
		<div class="easyui-tab" style="width: 210px; height: auto; overflow: hidden; background: #f8f8f8; float: left; margin-left: 30px; border: 1px solid #e2e2e2;" title="拥有角色">
			拥有角色<br>
			<table id="hasAttrDataGrid" style="height:75px;"></table>
		</div>
	</div>

</body>
</html>