$(function(){
	
	$('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    });
	
    $.jgrid.defaults.styleUI = 'Bootstrap';

    var user = {
	    id: "userManageTable",	//表格id
	    seItem: null,		//选中的条目
	    table: null,
	    layerIndex: -1
	};

	/**
	 * 初始化表格的列
	 */
	user.initColumn = function () {
	    var columns = [
	    		{name: 'id', index: 'id', sortable: false, align: 'center', hidden:true,width: '15%'},
	    		{name : 'user_name',index : 'user_name',width : 100},
	            {name : 'user_id',index : 'user_id', width : 90},
	            {name : 'roles',index : 'roles', width : 90},
	            {name : 'roleIds',index : 'roleIds', hidden:true},
	            {name : 'user_status',index : 'user_status', hidden:true},
	            {name : 'user_status_str',index : 'user_status_str', width : 90,
	            	formatter: function (cellvalue,options,rowObject) {
                        if (rowObject.user_status !=null && rowObject.user_status == "1"){
                            var detail = "停用";
                        }else{
                            var detail = "启用";
                        }
                        return detail;
                    }
	            },
	            {label: '详情', name: '',index: 'oper', width: 100, align: 'center', sortable: false,
	            	formatter:function (cellvalue, options, rowObject) {
		                var d = "<a class='btn btn-primary btn-xs' data-toggle=\"modal\" onclick='javascript:detail(\""+rowObject.id+"\",this)'>详情</a>"
		                	+ "<a class='btn btn-success btn-xs' data-toggle=\"modal\" onclick='javascript:edit(\""+rowObject.id+"\",this)'>编辑</a>";
		            	return d;
	            	}
	            }
	        ]
	    return columns;
	};

	user.initNames = function () {
	    var names = ['id','姓名','用户名', '角色','角色ids','状态值','状态', '操作'];
	    return names;
	}
	
	detail = function(id, target){	// 跳转查看详情页,也可自行弹窗显示
		edit(id, target);
	    $("#div_title").text("查看用户信息");
	    $("#submit").hide();
		$("#close").show();
    }
	
	// 接口地址
	user.url = {
		searchUrl : '/micromvc/uc/userList/getInfoList4Page',	// 查询地址
		deleteUrl : '/micromvc/uc/userList/delInfo', 			// 删除地址
		createUrl : '/micromvc/uc/userList/createInfo', 		// 创建地址
		updateUrl : '/micromvc/uc/userList/updateInfo', 		// 更新地址
		detailUrl : '/micromvc/uc/userList/toList?id='		// 详情地址
	}
	var table_list = $("#table_list");	// 弹出窗口id
	
	pageInit();
	
	function pageInit(){
	    var column = user.initColumn();
	    var name = user.initNames();
	    var loading;// 遮罩层
	    table_list.jqGrid(
	        {
	        	beforeRequest: function () {
	        		loading = layer.load();	// 开启加载遮罩
	                table_list.jqGrid("clearGridData");
	            },	//请求前的函数
	            url: user.url.searchUrl,
	            loadui: "Disable",
	            datatype: "json",
	            deepempty: true,
	            sortable: false,
	            height: 450,
	            autowidth: true,
	            shrinkToFit: true,
	            colNames : name,
	            colModel : column,
	            rowNum : 20,
	            rowList : [ 10, 20, 30 ],
	            sortname: 'create_time',
	            sortorder: 'desc',
	            pager : '#pager',
	            viewrecords : true,
	            rownumbers:true,//添加左侧行号
	            jsonReader: {
	                root: "viewJsonData", //json中的viewJsonData，对应Page中的 viewJsonData。
	                page: "curPage", //json中curPage，当前页号,对应Page中的curPage。
	                total: "totalPages",//总的页数，对应Page中的pageSizes
	                records: "totalRecords",//总记录数，对应Page中的totalRecords
	                repeatitems: false,
	                id: "0"
	            },
	            loadComplete: function () {//使用loadComplete方法替换gridComplete，gridComplete会被其他事件(clearGridData等)触发
		            var ids = table_list.jqGrid('getDataIDs');
		            for (var i = 0; i < ids.length; i++) {
		                var curRowData = table_list.jqGrid('getRowData', ids[i]);
		                var id = curRowData['id'];
		                console.info(id);
		            }
		            layer.close(loading);//数据加载完成后，取消遮罩
	            },
	            gridComplete: function () {
	            },
	            hidegrid: false
	        }
	    );
	
	    // Add selection
	    table_list.setSelection(4, true);
	
	    // Add responsive to jqGrid
	    $(window).bind('resize', function () {
	        var width = $('.jqGrid_wrapper').width();
	        table_list.setGridWidth(width);
	    });
	}
	
	function getContextPath() {
	    var contextPath = document.location.pathname;
	    var index = contextPath.substr(1).indexOf("/");
	    contextPath = contextPath.substr(0, index + 1);
	    delete index;
	    return contextPath;
	}
	
	//loading
	$("#search").click(function () {
	    doSerch();
	});
	
	doSerch = function () {
		var user_id = $("#s_user_id").val();
	    var user_name = $("#s_user_name").val();
	    var user_status = $("#s_user_status").val();
	    table_list.jqGrid('setGridParam', {
	        url: user.url.searchUrl,
	        postData: {'user_id': user_id,'user_name':user_name, 'user_status':user_status},
	        page: 1
	    }).trigger("reloadGrid");
	}
	
	//清空查询form，重新查询
	$("#reSearch").click(function () {
		$("#s_user_id").val('');
	    $("#s_user_name").val('');
	    $("#s_user_status").val('');
		doSerch();
	})
	
	//添加按钮
	$("#add").click(function() {
	    clearForm();
	    $(this).attr("href","#modal-form");
	    $("#div_title").text("新增用户信息");
		$("#submit").show();
		$("#close").hide();
	});

	// 请空新增文本编辑框
	clearForm = function(){
		$("#id").val('');
		$("#user_id").val('');
	    $("#user_name").val('');
	    $("#user_password").val('');
	    $("input[type=checkbox]").iCheck('uncheck');
		validator.resetForm();	// 重置表单验证
	}
	
	// 行级编辑用户按钮
	edit = function(id, target){
		var ids = table_list.jqGrid('getDataIDs');
		var rowData;
        for (var i = 0; i < ids.length; i++) {
            var curRowData = table_list.jqGrid('getRowData', ids[i]);
            if(id == curRowData['id']){
            	rowData = curRowData;
            	break;
            }
        }
		editInfo(rowData, $(target));
		$("#submit").show();
		$("#close").hide();
		$("#user_password").rules("remove");  
	}
	
	// 编辑用户方法
	function editInfo(rowData, targer){
		clearForm();			// 请空表单内容
		if (rowData != "") {
			targer.attr("href","#modal-form");
            setDataToForm(rowData);
            $("#div_title").text("修改用户信息");
        }
	}
	
	// 顶部修改用户按钮
    $("#modify").click(function(){
    	editInfo(getRowData($(this), table_list), $(this));
    	$("#submit").show();
		$("#close").hide();
    });
    
    //获取列表行数据
    function getRowData(target, table_list) {
        var id = table_list.jqGrid("getGridParam", "selrow");
        if (id != null && id.length != 0) {
            return table_list.jqGrid('getRowData', id);
        }else{
        	target.attr("href","");	// 防止在没选中数据的时候弹出窗口
            swal("请选择一行记录！", "", "");
            return "";
        }
    }

    //将后台获取到的数据，写到页面中
    function setDataToForm(data) {
        $('#id').val(data.id);
        $('#user_id').val(data.user_id);
        $('#user_name').val(data.user_name);
        $('#user_status').val(data.user_status);
        var servicesArray = data.roleIds.split(",");
        $("input[type=checkbox][name=roles]").each(function (i, e) {
            for (var i = 0; i < servicesArray.length; i++) {
                if ($(this).val() == servicesArray[i]) {
                    $(this).iCheck('check');
                }
            }
        });
    }
    
    // 停用用户信息,和其子项
    $("#delete").click(function() {
		var rowData = getRowData($(this), table_list);
		if(rowData==null || rowData=="" || typeof(rowData) == "undefined"){
			return;
		}
		if('admin'==rowData.user_id){
			swal("系统管理员账户不能禁用！", "", "error");
			return;
		}
		swal({ 
	        title: "警告！",  
	        text: "您确定要停用用户["+ rowData.user_name +"]？",  
	        type: "warning", 
	        showCancelButton: true,
	        confirmButtonText: "确定",
	        cancelButtonText: "取消",
	        closeOnConfirm: false,
	        confirmButtonColor: "#ec6c62" 
	    }, function() {
	        if (rowData && rowData.id) {
	        	$.ajax({
	                type: "POST",
	                url: user.url.deleteUrl + "?user_status=1",
	                data: {id:rowData.id, user_id:rowData.user_id},
	                dataType: "json",
	                success: function (obj) {
                        if (obj.resultCode == "000"){
	                        swal("停用成功！", "", "success");
	                    }else if(obj.resultCode == "999"){
	                    	swal("停用失败！", "", "error");
	                    }else{
	                    	swal(obj.msg, "", "error");
	                    }
	                    table_list.trigger("reloadGrid");
	                },
	                error: function (xhr) {
	                }
	            });
	        }
	    });
	});
    
    // validate signup form on keyup and submit 添加文本框验证与提交
    var icon = "<i class='fa fa-times-circle'></i> ";
    var validator = $("#editForm").validate({
        rules: {
            user_id: {
                required: true,
                minlength: 1,
                maxlength: 50
            },
            user_name: {
                required: true,
                minlength: 1,
                maxlength: 50
            },
            user_password: {
                required: true,
                minlength: 1,
                maxlength: 18
            },
            roles: {
                required: true
            }
        },
        messages: {
        	user_id: {
                required: icon + "请输入用户名",
                minlength: icon + "用户名必须2个字符以上",
                maxlength: icon + "用户名必须50个字符以内"
            },
            user_name: {
                required: icon + "请输入姓名",
                minlength: icon + "姓名必须2个字符以上",
                maxlength: icon + "姓名必须50个字符以内"
            },
            user_password: {
                required: icon + "请输入密码",
                minlength: icon + "密码必须1个字符以上",
                maxlength: icon + "密码必须18个字符以内"
            }
        },
        submitHandler: function (form) {
        	var formUrl = user.url.createUrl;
        	var id=$("#id").val();
        	if(id!=null && id!=0 && id.length>1){
        		formUrl = user.url.updateUrl;
        	}
            $.ajax({
                type: "POST",
                url: formUrl,
                data: $(form).serialize(),
                dataType: "json",
                success: function (obj) {
                    if (obj.resultCode == "000") {
                        clearForm();
                        $("#modal-form-closer").click();
                        swal("保存成功！", "", "success");
                        table_list.trigger("resetSelection");	// 取消选中
                        table_list.trigger("reloadGrid");		// 重新加载数据
                    }else{
                    	swal("保存失败！", obj.msg, "error");
                    }
                },
                error: function (xhr) {
                }
            });
            return false;
        }
    });
});
