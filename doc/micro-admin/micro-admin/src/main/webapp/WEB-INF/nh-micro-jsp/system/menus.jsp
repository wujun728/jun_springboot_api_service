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
	<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-js/js/zTree/css/zTreeStyle/zTreeStyle.css">
<script type="text/javascript">  

$(document).ready(function(){
	//第一次加载树
	initTree();
	var updatelistReponsitoryForm = $("#updatelistReponsitoryForm");
	var addlistReponsitoryForm = $("#addlistReponsitoryForm");
	$('#enabled').click(function () {
		popup("1","启用");
	});
	
	//删除菜单按钮
	$('#disable').click(function () {
		popup("0","停用");
	});
	
	function popup(status, msg){
		var code=$("#updatelistReponsitoryForm #code").val();
		if(code == ""){
			swal({
		        title: "选择菜单或按钮",
		        text: "请先选择一个菜单或按钮。"
		    });
			return;
		}
		swal({
	        title: "您确定要"+msg+"这个菜单或按钮吗",
	        text: status=="1" ? msg : "停用后将无法使用此菜单或按钮，请谨慎操作！",
	        type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: msg,
	        closeOnConfirm: false
	    }, function () {
	    	changeStatus(status,msg); // 改变菜单状态
	    });
	}
	
	// 改变菜单状态操作
	function changeStatus(status, msg){
		var url = "<%=path%>/micromvc/uc/menuList/delInfo";
		$.post(url, {"id" : $("#id").val(), "status" : status}, function(data, status) {
			if (status == "success") {
				swal(msg + "成功！", "操作成功!", "success");
			}else{
				swal(msg + "失败！", "请联系管理员!", "error");
			}
			refresh();
		});    	
	}
	
	$(":radio").click(function(){
		$this = $(this);
		changeUI($this.closest("form"), $this.val());
	});
	
	// url排重(修改时)
	$("#updateBtn").click(function(){
		if($("#updatelistReponsitoryForm #id").val().trim()==''){
			return false;	// 未设置初始值不触发提交操作
		}
		operate({
			codeVal : $("#updatelistReponsitoryForm #code").val(),
			menuUrlVal : $("#updatelistReponsitoryForm #url").val(),
			checkUrl:"<%=path%>/micromvc/uc/menuList/isExistUrl",
			targetForm : updatelistReponsitoryForm,
			targetFormUrl : "<%=path%>/micromvc/uc/menuList/updateInfo"
		});
	});
	
	// url和code排重(添加时)
	$("#addBtn").click(function(){
		var addcode = $("#addlistReponsitoryForm #addcode");
		var addname = $("#addlistReponsitoryForm #addname");
		var addurl = $("#addlistReponsitoryForm #addurl");
		if(addcode.val().trim()=='' || addname.val().trim()==''){
			return;
		}
		operate({
			codeVal : addcode.val(),
			menuUrlVal : addurl.val(),
			checkUrl:"<%=path%>/micromvc/uc/menuList/isExistCodeAndUrl",
			targetForm : addlistReponsitoryForm,
			targetFormUrl : "<%=path%>/micromvc/uc/menuList/createInfo"
		});
	});
	
	function operate(data){
		$.ajax({
			url: data.checkUrl,
			type:'post',
			dataType:'json',
			data:{
				code : data.codeVal,
				url : data.menuUrlVal
			},
			success:function(result,status){
				if("000" == result.resultCode){
					data.targetForm.prop("action", data.targetFormUrl);
					data.targetForm.submit();
				}else{
					data.targetForm.prop("action","javascript:void(0);");
					swal("操作失败！", result.msg + "!", "error");
					return false;
				}
			},
			error : function(e,xhr) {
				data.targetForm.prop("action","javascript:void(0);");
				swal("操作失败！", "系统错误!", "error");
				return false;
			}
		});
	}
});

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
			rootPId : "system" //根节点  
		}
	},
	callback : {
		onClick: zTreeOnClick
	}
};


	function initTree() {
		var url = "<%=path%>/micromvc/uc/menuList/getInfoListAll";
		$.ajax({
			url:url,
			type:'post',
			dataType:'json',
			data:"",
			success:function(data,status){
					treeData=data;
					$.fn.zTree.init($("#tree"),setting, changeUrlName(treeData));  
			}
		});            
	}

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
	      setUser(seleObj);
	 };
	        
	function Adduser(){
	       var pcode=$("#updatelistReponsitoryForm #code").val();
	       var pname=$("#updatelistReponsitoryForm #name").val();
	       var levels=$("#updatelistReponsitoryForm #levels").val();
	       var num=$("#updatelistReponsitoryForm #num").val();
	       $("#addlistReponsitoryForm #addpcode").val(pcode);
	       $("#addlistReponsitoryForm #pname").val(pname);
	       $("#addlistReponsitoryForm #addlevels").val(Number(levels)+1);
	       $("#addlistReponsitoryForm #addnum").val(num);
	       $("#addlistReponsitoryForm [name=ismenu][value='1']").click();
	} 

	function setUser(seleObj){
	    if (seleObj != null&&seleObj !="") {
	    	$("#updatelistReponsitoryForm #id").val(seleObj.id);
	    	var ll = $("#updatelistReponsitoryForm [name=ismenu]").length;
	    	$("#updatelistReponsitoryForm [name=ismenu][value='"+seleObj.ismenu+"']").click();
	    	$("#updatelistReponsitoryForm #code").val(seleObj.code);
	    	$("#updatelistReponsitoryForm #name").val(seleObj.name.replace("(已停用)",""));
	    	$("#updatelistReponsitoryForm #pcode").val(seleObj.pcode);
	    	$("#updatelistReponsitoryForm #levels").val(seleObj.levels);
	    	$("#updatelistReponsitoryForm #num").val(seleObj.num);
	    	$("#updatelistReponsitoryForm #url").val(seleObj.murl);
	    	$("#alertMenuName").val(seleObj.name);
	    	changeUI($("#updatelistReponsitoryForm"),seleObj.ismenu);
	    }
	}
	
	
	// 改变ui
	function changeUI(target, type){
		if(type == "0"){	// 如果是按钮
			target.find("[name='url']").closest(".form-group").hide();
			target.find("[name='num']").closest(".form-group").hide();
			target.find(".control-label:contains('菜单名称：')").html("按钮名称：");
			target.find(".control-label:contains('菜单编码：')").html("按钮编码：");
	   	}else{
	   		target.find("[name='url']").closest(".form-group").show();
	   		target.find("[name='num']").closest(".form-group").show();
	   		target.find(".control-label:contains('按钮名称：')").html("菜单名称：");
	   		target.find(".control-label:contains('按钮编码：')").html("菜单编码：");
	   	}
	}
 </SCRIPT> 
</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeIn">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>菜单/按钮管理</h5>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-4">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <a data-toggle="modal" class="btn btn-success" href="form_basic.html#modal-form" onclick="return Adduser();"><i class="fa fa-plus"></i>添加</a>
                                <a data-toggle="modal" class="btn btn-warning" id="disable"><i class="fa fa-times"></i>停用</a>
                                <a data-toggle="modal" class="btn btn-primary" id="enabled"><i class="fa fa-check"></i>启用</a>
                            </div>
                            <div class="ibox-content">
                                <ul id="tree" class="ztree"></ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-8">
			            <div class="ibox float-e-margins">
			                <div class="ibox-title">
			                    <h5>选择左侧节点进行编辑</h5>
			                </div>
                			<div class="ibox-content">
			                    <form id="updatelistReponsitoryForm" action="javascript:void(0);" method="post" class="form-horizontal m-t">
			                    	<input type="hidden" id="id" name="id" value="" />
			                    	<input type="hidden" id="levels" name="levels" value="" />
									<input type="hidden" id="pcode" name="pcode" value="" />
			                        <div class="form-group">
			                            <label class="col-sm-2 control-label">编辑类型：</label>
			                            <div class="col-sm-9">
			                                <label>
                                            <input type="radio" value="1" name="ismenu"><i></i>菜单</label>
                                        	<label>
                                            <input type="radio" value="0" name="ismenu"><i></i>按钮</label>	
                                        </div>
			                        </div>
			                        <div class="form-group">
			                            <label class="col-sm-2 control-label">菜单编码：</label>
			                            <div class="col-sm-9">
			                                <input id="code" name="code" minlength="2" type="text" class="form-control" required="" aria-required="true" readonly="readonly">
			                            </div>
			                        </div>
			                        <div class="form-group">
			                            <label class="col-sm-2 control-label">菜单名称：</label>
			                            <div class="col-sm-9">
			                                <input id="name" type="name" minlength="2" class="form-control" name="name" required="" aria-required="true">
			                            </div>
			                        </div>
			                        <div class="form-group">
			                            <label class="col-sm-2 control-label">菜单URL：</label>
			                            <div class="col-sm-9">
			                                <input id="url" class="form-control" name="url">
			                            </div>
			                        </div>
			                        <div class="form-group">
			                            <label class="col-sm-2 control-label">菜单序号：</label>
			                            <div class="col-sm-9">
			                                <input id="num" type="number" class="form-control" name="num" required="" aria-required="true">
			                            </div>
			                        </div>
			                        <div class="form-group">
			                            <div class="col-sm-2 col-sm-offset-2">
			                                <button class="btn btn-info" id="updateBtn">
			                              		<i class="fa fa-pencil"></i>
			                              		<span class="bold">编辑</span
			                              	</button>
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
    <div id="modal-form" class="modal fade" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-body">
	                <div class="row">
	                    <div class="ibox-title col-sm-10">
	                    	<h5>添加</h5>
	                        <div class="ibox-content">
			                    <form id="addlistReponsitoryForm" action="javascript:void(0);" method="post" class="form-horizontal m-t">
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
			                                <button class="btn btn-success" id="addBtn"><i class="fa fa-plus"></i>添加</button>
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