<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>日期选择器layerDate</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/font-awesome.min.css?v=4.3.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/style.css?v=3.2.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <!-- Sweet alert -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/sweetalert/sweetalert.min.js"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/jquery-2.1.1.min.js"></script>
    <script src="<%=path%>/nh-micro-js/js/jquery-browser.js"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/bootstrap.min.js?v=3.4.0"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/content.js?v=1.0.0"></script>
    <script type="text/javascript" src="<%=path%>/nh-micro-js/js/zTree/js/jquery.ztree.core-3.4.js"></script>
    <script type="text/javascript" src="<%=path%>/nh-micro-js/js/zTree/js/jquery.ztree.excheck-3.4.min.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-js/js/zTree/css/zTreeStyle/zTreeStyle.css">
<script type="text/javascript">  

$(document).ready(function(){
	var role_id = '${param.role_id}';
	var zTree;
	//第一次加载树
	initTree();
	var setting = {
		check : {
			/**复选框**/
			enable : true,
			chkStyle: "checkbox",
			chkboxType : {"Y":"ps", "N":"ps"}
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
				rootPId : "system" //根节点  
			}
		},
		callback : {
			onClick: zTreeOnClick
		}
	};

	function initTree() {
		var url = "<%=path%>/micromvc/uc/menuList/getInfoListAllWithUser";
		$.ajax({
			url:url,
			type:'post',
			dataType:'json',
			data:{"role_id":role_id},
			success:function(data,status){
					treeData=data.menus;
					$.fn.zTree.init($("#tree"),setting, changeUrlName(treeData));
					zTree = $.fn.zTree.getZTreeObj("tree");
					zTree.expandAll(true);
			}
		});            
	}
	
	$('#save').click(function () {
		var url = "<%=path%>/micromvc/uc/menuRole/createInfo";
		var nodes = zTree.getCheckedNodes(true);
		var nodeIds = "";
		for(var i=0;i<nodes.length;i++){
			if(nodeIds != ""){
				nodeIds+=",";	
			}
			nodeIds += nodes[i].code;
		}
		$.ajax({
			url:url,
			type:'post',
			dataType:'json',
			data:{"role_id":role_id,"menu_ids": nodeIds},
			success:function(data,status){
				if (data.resultCode == "000") {
					swal("操作成功！", "操作成功!", "success");
				}else{
					swal("操作失败！", data.msg, "error");
					refresh();
				}
			}
		});
	});
	
	checkAll = function(){
		zTree.checkAllNodes(true);
	}
	unCheckAll = function(){
		zTree.checkAllNodes(false);
	}
});

	function changeUrlName(treeData){
		var newtreeData;
		for(note in treeData){
			treeData[note].murl = treeData[note].url;	// url是关键字
			delete treeData[note].url;
			// 重命名 已停用的菜单
			if(treeData[note].status == "0"){
				treeData[note].name = treeData[note].name+"(已停用)";
			}
		}
		return treeData;
	}

	function refresh() {
	       initTree();//重新加载树
	}      

	//菜单树单击事件
	 function zTreeOnClick(event, treeId, treeNode) {
	      var treeObj = $.fn.zTree.getZTreeObj("tree");
	      var nodes = treeObj.getSelectedNodes();
	      var seleObj = nodes[0];
	 };
	        
 </SCRIPT> 
</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeIn">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>角色 ${param.role_id}，添加删除权限!</h5>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <a class="btn btn-success"  onclick="return checkAll();"><i class="fa fa-check"></i>全选</a>
                                <a class="btn btn-warning"  onclick="return unCheckAll();"><i class="fa fa-times"></i>取消</a>
                                <a class="btn btn-success" id="save"><i class="fa fa-save"></i>保存</a>
                                <a href="/micromvc/uc/roleList/toPage" class="btn btn-primary"><i class="fa fa-mail-reply"></i>返回</a>
                            </div>
                            <div class="ibox-content">
                                <ul id="tree" class="ztree"></ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="modal-form" class="modal fade" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-body">
	                <div class="row">
	                    <div class="ibox-title col-sm-10">
	                    	<h5>添加</h5>
	                        <div class="ibox-content">
			                    <form id="addlistReponsitoryForm" action="<%=path%>/micromvc/uc/menuList/createInfo" method="get" class="form-horizontal m-t">
									<div class="form-group">
			                            <label class="col-sm-3 control-label">新增类型：</label>
			                            <div class="col-sm-9">
			                                <label>
                                            <input type="radio" value="1" name="ismenu"><i></i>菜单</label>
                                        	<label>
                                            <input type="radio" value="0" name="ismenu"><i></i>按钮</label>	
                                        </div>
			                        </div>
									<div class="form-group">
			                            <label class="col-sm-3 control-label">上级菜单：</label>
			                            <div class="col-sm-9">
			                            	<input type="hidden" id="addpcode" name="pcode" value="" />
			                            	<input type="hidden" id="addlevels" name="levels" value="" />
			                                <input id="pname" name="pname" type="text" class="form-control" disabled="disabled">
			                            </div>
			                        </div>
			                        <div class="form-group">
			                            <label class="col-sm-3 control-label">菜单编码：</label>
			                            <div class="col-sm-9">
			                                <input id="addcode" name="code" minlength="2" type="text" class="form-control" required="" aria-required="true">
			                            </div>
			                        </div>
			                        <div class="form-group">
			                            <label class="col-sm-3 control-label">菜单名称：</label>
			                            <div class="col-sm-9">
			                                <input id="addname" name="name" type="name" minlength="2" class="form-control" required="" aria-required="true">
			                            </div>
			                        </div>
			                        <div class="form-group">
			                            <label class="col-sm-3 control-label">菜单URL：</label>
			                            <div class="col-sm-9">
			                                <input id="addurl" name="url" class="form-control">
			                            </div>
			                        </div>
			                        <div class="form-group">
			                            <label class="col-sm-3 control-label">菜单序号：</label>
			                            <div class="col-sm-9">
			                                <input id="addnum" type="number" class="form-control" name="num" required="" aria-required="true">
			                            </div>
			                        </div>
			                        <div class="form-group">
			                            <div class="col-sm-10 col-sm-offset-3">
			                                <button class="btn btn-success" type="submit"><i class="fa fa-plus"></i>添加</button>
			                                <button class="btn btn-cancel" data-dismiss="modal" type="button">取消</button>
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
    <!-- layerDate plugin javascript -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/laydate/laydate.js"></script>
</body>
</html>