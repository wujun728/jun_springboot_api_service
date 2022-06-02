<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>撮合结果信息管理</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/common.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/zTree/js/jquery.ztree.core-3.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/zTree/css/zTreeStyle/zTreeStyle.css">
<script type="text/javascript">

$(function(){
	$('#infoList').datagrid({
		nowrap:true,
		striped:true,
		pagination : true,
		fitColumns: true,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url:"<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=match_mvc_result_list&groovySubName=getInfoList4Page&sort=create_time&order=desc",
		columns:[[
					{
						field : 'id',
						title : 'id',
						width : 100

					},
					{
						field : 'match_id',
						title : '撮合id',
						width : 100
					},
					{
						field : 'buy_id',
						title : '买单id',
						width : 100
					},
					{
						field : 'sale_id',
						title : '卖单id',
						width : 100
					},
					{
						field : 'match_amount',
						title : '撮合金额',
						width : 100

					},
					{
						field : 'create_time',
						title : '撮合时间',
						width : 100

					}
				]],
        toolbar : [ {
			id : "delete",
			text : "删除",
			iconCls : "icon-edit",
			handler : function() {
				remove();
			}

		},{
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



	//条件查询
	function ReQuery(){
		var data = $('#searchForm').serializeObject();
		$('#infoList').datagrid('reload',data);
	}
	
	//重置查询条件
	function clearForm(){
		$('#searchForm').form('clear');
	}
	
	//刷新
	function refresh(){
		//var querydata = $('#searchForm').serializeObject();
		$('#infoList').datagrid('reload',querydata);
	}

	/* 增加 */
	function add(){
			$("#addOne").form("clear");
			$("#addOne").dialog('open').dialog('setTitle', '信息添加');
	}	
	
	function addOne(){
		var dataO = $("#addForm").serialize();
		var temp = $("#addForm #addShowForm_temp").val();
	
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=match_mvc_result_list&groovySubName=createInfo";
		$.post(url,dataO,function(data,stats){
			if(stats=="success" ){
				$.messager.show({
					msg : "操作成功",
					title : "消息"
				});
				refresh();
				addCancel();
			}
		});
	
	}
	function addCancel() {
		$("#addForm").form("clear");
		$("#addOne").dialog('close');
	}
	
	/* 修改 */
	function updateInfo(){  
		var sels = $("#infoList").datagrid("getSelected");
	    if(sels==""||sels==null){
		    alert("请选择行");
	    }else{
	    	var sels = $("#infoList").datagrid("getSelected");
	    	$("#updateOne").dialog('open').dialog('setTitle', '信息修改');
	    	$("#updateOne").form("load", sels);
	    }
		
	}	
	
	function updateOne(){
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=match_mvc_result_list&groovySubName=updateInfo";
		$.post(url, $("#updateOneForm").serialize(), function(data, stats) {
			if (stats == "success" ) {
				$.messager.show({
					msg : "操作成功",
					title : "消息"
				});
				refresh();
				updateCancel();

			}
		});
	}
	
	function updateCancel() {
		$("#updateOneForm").form("clear");
		$("#updateOne").dialog('close');
	}

	/*删除*/
	function remove(){
		var sels = $('#infoList').datagrid("getSelected");
		if(sels == ''|| sels==null){
			alert('请选择行');
		}else{
			var result = confirm("确定要删除吗？");
			if(result == true){
				var querydata = $('#searchForm').serializeObject();
				var id = sels.id;
				var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=match_mvc_result_list&groovySubName=delInfo";
				url=url+"&id="+id;
				$.post(url,function(data,stats){
							if(stats=="success" ){
								$.messager.show({
									msg : data.msg,
									title : "消息"
								});
								$('#infoList').datagrid('reload',querydata);
							}
						}
					);
			        
			}
		}
	}
</script>
</head>
<body class="easyui-layout">
	<div id="infoQuery" class="dQueryMod" region="north"
		style="height: 55px">
		<form id="searchForm">
			<table id="searchTable">
				<tr>
					<td>撮合id：</td>
					<td><input type="text" id="match_id" name="match_id" /></td>
					<td><a href="#" class="easyui-linkbutton "
						iconCls="icon-search" onclick="ReQuery()">查询</a><a href="#"
						class="easyui-linkbutton" iconCls="icon-redo"
						onclick="clearForm()">清空</a></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="roleList" region="center">
		<div class="easyui-tabs l_listwid" id="accountTab">
			<table id="infoList"></table>
		</div>
	</div>

	<!-- 修改 -->
	<div id="updateOne" class="easyui-dialog" modal="true" align="center"
		style="padding: 10px; border: 0px; margin: 0px; width: 540px;"
		closed="true" resizable="true" inline="false">
		<form id="updateOneForm" novalidate method="post" action="">
			<input type="hidden" id="showForm_temp" value="" />
			<table id="updateTable"
				style="margin-top: 10px; margin-left: -40px;">
				<tr>
					<!-- <td>Id：</td> -->
					<td><input type="hidden" id="id" name="id" value="" /></td>
				</tr>
				<tr>
					<td align="right">用户名称：</td>
					<td><input type="text" id="user_name" name="user_name" value="" /></td>
				</tr>
				<tr>
					<td align="right">借款利息：</td>
					<td><input type="text" id="borrow_rate" name="borrow_rate" value="" /></td>
				</tr>
				<tr>
					<td align="right">待撮合金额：</td>
					<td><input type="text" id="account_amount" name="account_amount" value="" /></td>
				</tr>
				<tr>
					<td align="right">所属产品类型：</td>
					<td><input type="text" id="product_class" name="product_class" value="" /></td>
				</tr>
				<tr>
					<td align="right">所属平台类型：</td>
					<td><input type="text" id="platform_class" name="platform_class" value="" /></td>
				</tr>
				<tr>
					<td align="right">借款开始日期：</td>
					<td><input class="easyui-datebox" id="borrow_start_date" name="borrow_start_date" data-options="formatter:myformatter"></input></td>
				</tr>

			</table>
			<div id="buttons"
				style="margin-top: 20px; margin-left: 40px; padding-bottom: 10px;">
				<a class="easyui-linkbutton dPbtnDark70"
					href="javascript:updateOne();">确认</a> <a
					class="easyui-linkbutton dPbtnLight70"
					href="javascript:updateCancel();">取消</a>
			</div>
		</form>
	</div>

	<div id="addOne" class="easyui-dialog" modal="true" align="center"
		style="padding: 10px; border: 0px; margin: 0px; width: 540px;"
		closed="true" resizable="true" inline="false">
		<form id="addForm" novalidate method="post" action="">
			<input type="hidden" id="addShowForm_temp" value="" />

			<table id="addTable" style="margin-top: 10px; margin-left: -40px;">
				<tr>
					<!-- <td>Id：</td> -->
					<td><input type="hidden" id="id" name="id" value="" /></td>
				</tr>
				<tr>
					<td align="right">用户名称：</td>
					<td><input type="text" id="user_name" name="user_name" value="" /></td>
				</tr>
				<tr>
					<td align="right">借款利息：</td>
					<td><input type="text" id="borrow_rate" name="borrow_rate" value="" /></td>
				</tr>
				<tr>
					<td align="right">待撮合金额：</td>
					<td><input type="text" id="account_amount" name="account_amount" value="" /></td>
				</tr>
				<tr>
					<td align="right">所属产品类型：</td>
					<td><input type="text" id="product_class" name="product_class" value="" /></td>
				</tr>
				<tr>
					<td align="right">所属平台类型：</td>
					<td><input type="text" id="platform_class" name="platform_class" value="" /></td>
				</tr>
				<tr>
					<td align="right">借款开始日期：</td>
					<!-- <td><input type="text" id="remarks" name="remarks" value="" /></td> -->
					<td><input class="easyui-datebox" id="borrow_start_date" name="borrow_start_date"></input></td>
				</tr>


			</table>
			<div id="buttons"
				style="margin-top: 20px; margin-left: 40px; padding-bottom: 10px;">
				<a class="easyui-linkbutton dPbtnDark70" href="javascript:addOne();">确认</a>
				<a class="easyui-linkbutton dPbtnLight70"
					href="javascript:addCancel();">取消</a>
			</div>
		</form>
	</div>


</body>
</html>