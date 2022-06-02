$(function(){
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
	            {name : 'item_id',index : 'item_id',width : 90},
	            {name : 'item_name',index : 'item_name',width : 100},
	            {name : 'dict_id',index : 'dict_id',width : 100}
	        ]
	    return columns;
	};

	user.initNames = function () {
	    var names = ['id','字典标识', '字典名称', '字典类型'];
	    return names;
	}
	
	// 接口地址
	user.url = {
		searchUrl : '/micromvc/uc/dictItem/getInfoList4Page?dict_id='+$("#dict_id").val(),	// 查询地址
		deleteUrl : '/micromvc/uc/dictItem/delInfo', 			// 删除地址
		createUrl : '/micromvc/uc/dictItem/createInfo', 		// 创建地址
		updateUrl : '/micromvc/uc/dictItem/updateInfo'	 		// 更新地址
	}
	var table_list = $("#table_list");
	
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
		var item_id = $("#s_item_id").val();
	    var item_name = $("#s_item_name").val();
	    table_list.jqGrid('setGridParam', {
	        url: user.url.searchUrl,
	        postData: {'item_id': item_id,'item_name':item_name},
	        page: 1
	    }).trigger("reloadGrid");
	}
	
	//清空查询form，重新查询
	$("#reSearch").click(function () {
		$("#s_item_id").val('');
	    $("#s_item_name").val('');
		doSerch();
	})
	
	//添加按钮
	$("#add").click(function() {
	    clearForm();
	    $(this).attr("href","#modal-form");
	    $("#div_title").text("新增数据字典"+$("#dict_id").val()+"子项");
	});

	// 请空新增文本编辑框
	clearForm = function(){
		$("#id").val('');
		$("#item_id").val('');
	    $("#item_name").val('');
	    validator.resetForm();	// 重置表单验证
	}
	
	//修改按钮
    $("#modify").click(function(){
        clearForm();
        var rowData = getRowData($(this), table_list);
        if (rowData != "") {
            $(this).attr("href","#modal-form");
            setDataToForm(rowData);
            $("#div_title").text("修改数据字典"+$("#dict_id").val()+"子项");
        }
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
        $('#item_id').val(data.item_id);
        $('#item_name').val(data.item_name);
        //记录修改前的字典类型
        old_meta_key = data.item_id;
    }
    
    // 删除数据字典,和其子项
    $("#delete").click(function() {
		var rowData = getRowData($(this), table_list);
		if(rowData==null || rowData=="" || typeof(rowData) == "undefined"){
			return;
		}
		swal({ 
	        title: "警告！",  
	        text: "您确定要删除该字典项？",  
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
	                url: user.url.deleteUrl,
	                data: {id:rowData.id, dict_id:rowData.dict_id, item_id:rowData.item_id},
	                dataType: "json",
	                success: function (obj) {
                        if (obj.resultCode == "000"){
	                        swal("删除成功！", "", "success");
	                    }else{
	                    	swal("删除失败！", "", "error");
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
            item_id: {
                required: true,
                minlength: 1,
                maxlength: 50
            },
            item_name: {
                required: true,
                minlength: 1,
                maxlength: 50
            }
        },
        messages: {
            item_id: {
                required: icon + "请输入字典项标识",
                minlength: icon + "字典项标识必须2个字符以上",
                maxlength: icon + "字典项标识必须50个字符以内"
            },
            item_name: {
                required: icon + "请输入字典项名称",
                minlength: icon + "字典项名称必须2个字符以上",
                maxlength: icon + "字典项名称必须50个字符以内"
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
