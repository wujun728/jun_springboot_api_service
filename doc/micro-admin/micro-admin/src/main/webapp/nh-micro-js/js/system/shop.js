$(function(){
    $.jgrid.defaults.styleUI = 'Bootstrap';

    changeCity("0","province")
    changeCity("0","s_province")

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
	    		{name: 'id', index: 'id', sortable: false, align: 'center',formatter:function (cellvalue, options, rowObject) {
                    return renderButton(cellvalue, options, rowObject)
                } },
	            {name : 'name',index : 'name',width : 90},
	            {name : 'code',index : 'code',width : 100},
			    {name : 'area',index : 'area',width:90},
			    {name : 'detail_address',index : 'detail_address',width:300,formatter:function(cellvalue, options, rowObject){
                    var province = region[rowObject.province];
                    var city = region[rowObject.city];
                    var county = region[rowObject.county];
					return province+city+county+cellvalue;
				}}
	        ]
	    return columns;
	};

	user.initNames = function () {
	    var names = ['操作','商铺名称', '商铺编号', '商铺面积㎡','商铺地址'];
	    return names;
	}
	
	detail = function(id){	// 跳转查看详情页,也可自行弹窗显示
        $.ajax({
            url:user.url.detailUrl,
            type:'post',
            dataType:'json',
            data:{
                id:id
            },
            success:function(data){
                $('#c_name').text(data.name);
                $('#c_code').text(data.code);
                $('#c_create_time').text(data.create_time);
                $('#c_update_time').text(data.update_time);
                $("#c_area").text(data.area+"㎡");
                var province = region[data.province]
                var city = region[data.city]
                var county = region[data.county]
                $("#c_address").text(province+city+county+data.detail_address);
                $('#c_floor_price').text(((data.floor_price)/100).toFixed(2)+"元");
                $('#c_guide_price').text(((data.guide_price)/100).toFixed(2)+"元");
            },
            error:function(xhr){
            }
        })
    }
	
	// 接口地址
	user.url = {
		searchUrl : '/micromvc/shop/list',	// 查询地址
		createUrl : '/micromvc/shop/createInfo', 		// 创建地址
		updateUrl : '/micromvc/shop/updateInfo', 		// 更新地址
		detailUrl : '/micromvc/shop/getById'		// 详情地址
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

	doSerch = function () {
		var name = $("#s_name").val();
	    var code = $("#s_code").val();
        var start = $("#s_start").val();
        var end = $("#s_end").val();
        var province = $("#s_province").val();
        var city = $("#s_city").val();
        var county = $("#s_county").val();
	    table_list.jqGrid('setGridParam', {
	        url: user.url.searchUrl,
	        postData: {'name': name,'code':code,'start':start,'end':end,'province':province,'city':city,'county':county},
	        page: 1
	    }).trigger("reloadGrid");
	}
	
	//清空查询form，重新查询
	$("#reSearch").click(function () {
		$("#s_name").val('');
	    $("#s_code").val('');
		doSerch();
	})
	
	//添加按钮
	$("#add").click(function() {
	    clearForm();
	    $(this).attr("href","#modal-form");
	    $("#div_title").text("新增店铺");
	});

	// 请空新增文本编辑框
	clearForm = function(){
		$("#id").val('');
		$("#name").val('');
	    $("#code").val('');
	    $("#floor_price").val('');
	    $("#guide_price").val('');
	    $("#area").val('');
	    validator.resetForm();	// 重置表单验证
	}


	
	//修改按钮
     editShop = function(o){
        clearForm();
        $("#div_title").text("修改店铺");
		$.ajax({
			url:user.url.detailUrl,
			type:'post',
			dataType:'json',
			data:{
				id:o
			},
			success:function(data){
                setDataToForm(data)
			},
			error:function(xhr){
			}
		})
	}
    
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
     	changeCity(data.province,"city")
        changeCity(data.city,"county")
        $('#id').val(data.id);
        $('#name').val(data.name);
        $('#code').val(data.code);
        $('#area').val(((data.area) / 1).toFixed(2));
        $("#detail_address").val(data.detail_address);
		$("#province").val(data.province);
		$("#city").val(data.city);
		$("#county").val(data.county);
        $('#floor_price').val(((data.floor_price)/100).toFixed(0));
        $('#guide_price').val(((data.guide_price)/100).toFixed(0));
    }
    

    
    // validate signup form on keyup and submit 添加文本框验证与提交
    var icon = "<i class='fa fa-times-circle'></i> ";
    var validator = $("#editForm").validate({
        rules: {
            name: {
                required: true,
                minlength: 1,
                maxlength: 50
            },
            code: {
                required: true,
                minlength: 1,
                maxlength: 50
            },
            area:{
            	number:true,
                maxlength: 50,
                minNumber:true
			},
            floor_price:{
                digits:true,
                maxlength: 50
			},
			guide_price:{
                digits:true,
                maxlength: 50
			},
            detail_address:{
                required: true,
                maxlength: 50
			}


        },
        messages: {
            name: {
                required: icon + "请输入店铺名称",
                minlength: icon + "店铺名称必须2个字符以上",
                maxlength: icon + "店铺名称必须50个字符以内"
            },
            code: {
                required: icon + "请输入店铺编码",
                minlength: icon + "店铺编码必须2个字符以上",
                maxlength: icon + "店铺编码必须50个字符以内"
            },
			area:{number:icon + "请输入数字",maxlength: icon + "50个字符以内"},
			floor_price:{digits:icon + "请输入整数",maxlength: icon + "50个字符以内"},
            guide_price:{digits:icon + "请输入整数",maxlength: icon + "50个字符以内"},
            detail_address:{
                required: icon + "请输入详细地址",
                maxlength: icon + "50个字符以内"
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

    function changeCity(o,id){
        $.ajax({
            async : false,
            cache : false,
            url:"/micromvc/region/getRegion",
            type:"post",
            dataType:"json",
            data:{
                parent_id:o
            },
            success:function(data){
                $("#"+id).html("<option value=''>请选择</option> ")
                if(data!=null){
                    for(var i = 0;i < data.length;i++){
                        var html ="<option value='"+data[i].pc_code+"'>"+data[i].pc_name+"</option>"
                        $("#"+id).append(html)
                    }
                }
            }
        })
    }

    $("#province").change(function(){
        $("#county").html("<option value=''>请选择</option>")
        changeCity(this.value,"city");
    })

    $("#city").change(function(){
        changeCity(this.value,"county");
    })

    $("#s_province").change(function(){
        $("#s_county").html("<option value=''>请选择</option>")
        changeCity(this.value,"s_city");
    })

    $("#s_city").change(function(){
        changeCity(this.value,"s_county");
    })

});


