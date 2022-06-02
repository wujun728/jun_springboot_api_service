<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <HEAD>
  <TITLE> 组织菜单</TITLE>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-js/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-js/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/zTree/js/jquery.ztree.core-3.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-js/js/zTree/css/zTreeStyle/zTreeStyle.css">
<script type="text/javascript">  

//删除菜单
function Deluser(menu_id,alertMenuName){
	if(menu_id==null || menu_id==""){
		$.messager.alert("操作提示", "请选择要删除的菜单！","error");  
		return;
	}
	$.messager.confirm("信息提示", "确认要删除‘" + alertMenuName + "’吗？",
				function(confirm) {
					if (confirm) {
						var url = "<%=path%>/micromvc/uc/menuList/delInfo";
						$.post(url, {"id" : menu_id}, function(data, status) {
							if (status == "success") {
								$.messager.show({
									msg : "删除成功",
									title : "消息"
								});
								$("#updatelistReponsitoryForm").form("clear");
								refresh();
								
							}
						});
					}
				});
	refresh();        	
} 

//[添加]菜单
function addlistReponsitory() {
	if($("#addlistReponsitoryForm").form('validate')==false){
		return;
	}
	$("#addlistReponsitoryForm #addparentId").val("");
	var url = "<%=path%>/micromvc/uc/menuList/createInfo";
	var dataO = $("#addlistReponsitoryForm").serialize();
	if($("#addlistReponsitoryForm").form('validate')){
		$.post(url, dataO, function(data, status) {
			$.messager.show({
				msg : "添加成功",
				title : "消息"
			});
			refresh();
			cancelGro("addlistReponsitory");
			
		});
		
	}else{
		$.messager.alert('信息提示', '校验失败，修改后提交', 'info');
	} 
}

//[修改]按钮
function upodatelistReponsitory(){
	if($("#updatelistReponsitoryForm").form('validate')==false){
		return;
	}
    var url = "<%=path%>/micromvc/uc/menuList/updateInfo";
    var dataO = $("#updatelistReponsitoryForm").serialize();
	if($("#updatelistReponsitoryForm").form('validate')){
	   	$.post(url,dataO,function(data,status){
	   		 $.messager.show({
	   			msg : "修改成功",
	   			title : "消息"
	   		 });
	   	     refresh();
	   		 $("#updatelistReponsitoryForm").form("clear");
	   	});
	   				
	 }else{
	   		$.messager.alert('信息提示', '校验失败，修改后提交', 'info');
	 }
}

var setting = {
	check : {
		/**复选框**/
		enable : false,
		chkboxType : {
			"Y" : "",
			"N" : ""
		}
	},
	view : {
		expandSpeed : 300
		//设置树展开的动画速度，IE6下面没效果，  
	},
	data : {
		key : {
			name : "name"
		},
		simpleData : { //简单的数据源，一般开发中都是从数据库里读取，API有介绍，这里只是本地的                           
			enable : true,
			idKey : "code", //id和pid，这里不用多说了吧，树的目录级别  
			pIdKey : "pcode",
			rootPId : "root" //根节点  
		}
	},
	callback : {
		onClick: zTreeOnClick
	}
};

//第一次加载树
$(function() {
	initTree();
});

function initTree() {
	var url = "<%=path%>/micromvc/uc/menuList/getInfoListAll";
	$.ajax({
		url:url,
		type:'post',
		dataType:'json',
		data:"",
		success:function(data,status){
				treeData=data;
				$.fn.zTree.init($("#tree"),setting, treeData);  
		}
	});            
}
        
function refresh() {
       initTree();//重新加载树
}      

//菜单树单击事件
 function zTreeOnClick(event, treeId, treeNode) {
      var treeObj = $.fn.zTree.getZTreeObj("tree");
      var nodes = treeObj.getSelectedNodes();
      var seleObj = nodes[0];
      setUser(seleObj);
 };

        
function Adduser(){
       var parent_id=$("#updatelistReponsitory #menu_id").val();
       $("#addlistReponsitory #menu_parent_id").val(parent_id);
      	$("#addlistReponsitory").dialog('open').dialog('setTitle', '添加菜单');
} 

function setUser(seleObj){
    if (seleObj != null&&seleObj !="") {
    	$("#updatelistReponsitoryForm #id").val(seleObj.id);
    	$("#updatelistReponsitoryForm #menu_id").val(seleObj.menu_id);
    	$("#updatelistReponsitoryForm #menu_name").val(seleObj.menu_name);
    	$("#updatelistReponsitoryForm #menu_parent_id").val(seleObj.menu_parent_id);
    	$("#updatelistReponsitoryForm #menu_dept_type").val(seleObj.menu_dept_type);
    	$("#updatelistReponsitoryForm #remark").val(seleObj.remark);
    	$("#updatelistReponsitoryForm #menu_url").val(seleObj.menu_url);
    	$("#alertMenuName").val(seleObj.menu_name);
    }	
} 
       
        
    	function cancelGro(flag) {
    		$("#"+flag+"Form").form("clear");
    		$("#"+flag).dialog("close");
    	}

//关联角色     	
function SetRole(menu_id) {
	if(menu_id==null || menu_id==""){
		$.messager.alert("操作提示", "请选择要关联角色的菜单！","error");  
		return;
	}
    $("#aur").dialog('open').dialog('setTitle', '角色配置');
    queryRole_Set(menu_id);
}

    var hasAttrDataGrid;
    var noAttrDataGrid;
    var v_menu_id;
function queryRole_Set(menu_id) {
    var ref_url="<%=path%>/micromvc/uc/menuRole/getInfoList4Ref?menu_id="+menu_id;
    v_menu_id=menu_id;
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

//给菜单分配角色_移入
function moveIn() {
    var obj = $('#noAttrDataGrid').datagrid('getSelected');
    if (obj == null) {
    	$.messager.alert("操作提示", "请选择要移入的属性！","error");  
    	return;
    }	
    var hrows = $('#hasAttrDataGrid').datagrid('getRows'); //这段代码是获取当前页的所有行。
    for (var i = 0; i < hrows.length; i++) {
    	//获取每一行的数据
    	if (obj.role_id == hrows[i].role_id) {
    		$.messager.alert("操作提示", "已经配置" + obj.role_name + "角色,请重新选择。","error");  
    		return;
    	}
    }
   	var url="<%=path%>/micromvc/uc/menuRole/createInfo";
   	var dataO = {
    	'menu_id' : v_menu_id,
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
//给菜单分配角色_移除
function moveOut() {
    var obj = $('#hasAttrDataGrid').datagrid('getSelected');
    if (obj == null) {
    	$.messager.alert("操作提示", "请选择要移入的属性！","error");  
    	return;
    }
    var url="<%=path%>/micromvc/uc/menuRole/delInfo";
    var dataO = {
    	'id' : obj.id,
   	};
   	$.post(url, dataO, function(data, stats) {
    	if (stats == "success") {
    		$('#hasAttrDataGrid').datagrid('reload');
    		//$('#noAttrDataGrid').datagrid('reload');
    		$.messager.show({
    			msg : data.msg,
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
    	
    	
 </SCRIPT>  
 </HEAD>
<BODY class="easyui-layout">
<div region="west"  split="true" title="组织菜单" style="width:240px;">
   <ul id="tree" class="ztree"></ul>
</div>
     <div id="dialogDemo" class="easyui-dialog" style="width:300px; height:200px;" title="添加" iconCls="icon-edit" closed="true" resizable="true" inline="false">
      Content
   </div>
<div region="center"  title="菜单信息" split="true">  
    <div id="toolbar" class="dRbtnsToolbar">
        <a href="#" class="easyui-linkbutton " iconcls="icon-add" onclick="Adduser()">添加</a>
        <!-- <a href="#" class="easyui-linkbutton " iconcls="icon-edit" onclick="upodatelistReponsitory()" >修改</a> -->
        <a href="#" class="easyui-linkbutton " iconcls="icon-cancel" onclick="Deluser($('#updatelistReponsitory #id').val(),$('#alertMenuName').val())" >删除</a>
        <a href="#" class="easyui-linkbutton " iconcls="icon-edit" onclick="SetRole($('#updatelistReponsitory #menu_id').val())" >关联角色</a>
    </div>
    
  		<div id="updatelistReponsitory" align="left" buttons="#buttonsAdd" class="dRumPack">
			
			<!-- <input type="hidden" id="updatelistReponsitory_temp" value="" /> -->
			<form id="updatelistReponsitoryForm" novalidate action=""  method="post">
				<table>
					<tr style="width:90px;">
						<!-- <td>Id：</td> -->
						<td>
							<input type="hidden" id="id" name="id" value="" />
							<input type="hidden" id="alertMenuName" name="alertMenuName" value="" />
						</td>
					</tr>	
					<tr>
						<td colspan="2" valign="top">
		        			<hr style="filter: alpha(opacity=100,finishopacity=0,style=1)" width="100%" color="#9baebf" size="1">
		        		</td>
					</tr>
					<tr>
						<td colspan="2" style="height:30px;">--------选择左侧某菜单，进行修改操作-------</td>
					</tr>				
					<tr>
						<td align="right">菜单编码：</td>
						<td><input type="text" id="menu_id" name="menu_id"  onkeyup="value=value.replace(/[\W]/g,'') "  class="easyui-validatebox" required="true" maxlength="50" ></input></td>
					</tr>
					<tr>
						<td align="right">菜单名称：</td>
						<td><input type="text" id="menu_name" name="menu_name" class="easyui-validatebox" required="true" maxlength="20"></input></td>
					</tr>
					<tr>
						<td align="right">菜单URL：</td>
						<td><input type="text" id="menu_url" name="menu_url"  style="width:450px"  maxlength="500"></input></td>
					</tr>


					<tr>
						<td align="right"></td>
						<td>
							<input type="hidden" id="menu_parent_id" name="menu_parent_id" />
						</td>
					</tr>
				</table>
			</form>
			<div id="buttonsAdd" style="padding:10px 0 0 70px;">
				<a class="easyui-linkbutton dPbtnDark70" href="javascript:upodatelistReponsitory();">修改</a>
				<a class="easyui-linkbutton dPbtnLight70" href="javascript:cancelGro('updatelistReponsitory');">取消</a>
			</div>
		</div>    

	</div>
		<!-- 添加菜单 -->
 		<div id="addlistReponsitory" class="easyui-dialog" modal="true" align="center" 
			style="padding:20px 10px 10px 10px; width:600px;" closed="true" resizable="true" inline="false">
			<input type="hidden" id="listReponsitory_temp" value="" />
			<form id="addlistReponsitoryForm" novalidate action="" method="post" >
				<table>
					<tr style="width:100px;">
						<td align="right">菜单编码：</td>
						<td><input type="text" id="menu_id" name="menu_id"  onkeyup="value=value.replace(/[\W]/g,'') " class="easyui-validatebox" required="true" maxlength="50"></input></td>
					</tr>
					<tr>
						<td align="right">菜单名称：</td>
						<td><input type="text" id="menu_name" name="menu_name" class="easyui-validatebox" required="true" maxlength="20"></input></td>
					</tr>
					<tr>
						<td align="right">菜单URL：</td>
						<td><input type="text" id="menu_url" name="menu_url" style="width:450px" maxlength="500"></input></td>
					</tr>


					<tr>
						<td align="right"></td>
						<td>
							<input type="hidden" id="menu_parent_id" name="menu_parent_id" />
						</td>
					</tr>


				</table>
			</form>
			<div id="buttonsAdd" style="padding:10px 0 0 70px;">
				<a class="easyui-linkbutton dPbtnDark70" href="javascript:addlistReponsitory();">确认</a>
				<a class="easyui-linkbutton dPbtnLight70" href="javascript:cancelGro('addlistReponsitory');">取消</a>
			</div>
		</div> 	

	<!-- 这是为角色添加用户的 -->
	<div id="aur" class="easyui-dialog" class="easyui-dialog" modal="true" align="center"
		style="padding: 20px 10px 15px 10px; border: 0px; margin: 0px; width: 700px;" closed="true" resizable="true" inline="false">
		<div class="easyui-tab" style="width: 210px; height: auto; overflow: hidden; background: #f8f8f8; float: left; margin-left: 8px; border: 1px solid #e2e2e2;" title="系统角色">
			系统所有的角色<br>
			<table id="noAttrDataGrid"></table>
		</div>

		<div style="width: 100px; float: left; margin: 20px 0 20px 20px;">
			<a href="javascript:moveIn();" class="easyui-linkbutton dPbtnMove" style="margin-bottom: 20px;">移入 》</a> 
			<a href="javascript:moveOut()" class="easyui-linkbutton dPbtnMove">《 移出</a>
		</div>

		<div class="easyui-tab" style="width: 210px; height: auto; overflow: hidden; background: #f8f8f8; float: left; margin-left: 30px; border: 1px solid #e2e2e2;" title="拥有角色">
			拥有该菜单的角色<br>
			<table id="hasAttrDataGrid"></table>
		</div>
	</div>

		
</BODY>
</html>