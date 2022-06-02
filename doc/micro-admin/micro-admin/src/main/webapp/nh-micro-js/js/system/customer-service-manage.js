
/**
 * 初始化表格的列
 */
initColumn = function () {
    return [
        {name: 'operate', index: 'operate', width: 200,sortable: false,
            formatter: function (cellvalue, options, rowObject) {
               return renderBusinessButton(cellvalue,options,rowObject);
            }
        },
        {name: 'cust_name', index: 'cust_name', width: 90},
        {name: 'cust_paper', index: 'cust_paper', width: 100},
        {name: 'product_name', index: 'product_name', width: 80},
        {name: 'apply_amount', index: 'apply_amount', width: 80,
            formatter:function(cellValue,options,rowObject){
                return (cellValue/ 1 * rowObject.rent_period).toFixed(2);
            }},
        {name: 'apply_time', index: 'apply_time', width: 80},
        {name: 'status', index: 'status', width: 100, sortable: true},
        {name: 'statusVal', index: 'status', hidden:true},
        {name: 'user_name', index: 'user_name', width: 100, sortable: true},
        {name: 'create_time', index: 'create_time', width: 100, sortable: true},
        {name: 'update_time', index: 'update_time', width: 150}
    ];
};

initNames = function () {
    return ['操作', '姓名', '证件号', '产品名称', '房租总金额', '提交时间', '状态','状态值', '客户经理', '创建时间', '更新时间'];
}

// $(function(){
//     $.jgrid.defaults.styleUI = 'Bootstrap';
//     pageInit();
//     statusInit();
// });
function initServiceInfo(){
    $.jgrid.defaults.styleUI = 'Bootstrap';
    pageInit();
    statusInit();
}
function statusInit(){
    var url = getContextPath()+"/uc/dictItem/getInfoListByDicId";
    $.ajax({
        async : false,
        cache : false,
        type : 'POST',
        dataType : "json",
        url:url,
        data:{sort:"convert(item_id,signed)",order:"asc",dicId:"business_status"},
        success : function(data) {
           if(data != null){
               for (var i = 0; i < data.length; i++) {
            	   if(data[i].item_id <190){
            		   continue;
            	   }
                   var option = "<option value='"+data[i].item_id+"'>"+data[i].item_name+"</option>"
                   $("#status").append(option);
               }
           }
        },
        error : function() {
            alert("请求失败");
        }
    });

}
function pageInit(){
    var column = initColumn();
    var name = initNames();
    var url = getContextPath()+'/customerService/queryList';
    var  customerName= $("#customerName").val();
    var  idNumber= $("#idNumber").val();
    var  productName= $("#productName").val();
    var  status= $('#status option:selected').val();
    jQuery("#table_list").jqGrid(
        {
            url: url,
            datatype: "json",
            postData:{customerName:customerName,idNumber:idNumber,productName:productName,status:status},
            height: 450,
            autowidth: true,
            shrinkToFit: true,
            colNames : name,
            colModel : column,
            rowNum : 20,
            rowList : [ 10, 20, 30 ],
            pager : '#pager',
            jsonReader: {
                root: "viewJsonData", //json中的viewJsonData，对应Page中的 viewJsonData。
                page: "curPage", //json中curPage，当前页号,对应Page中的curPage。
                total: "totalPages",//总的页数，对应Page中的pageSizes
                records: "totalRecords",//总记录数，对应Page中的totalRecords
                repeatitems: false,
                id: "0"
            },
            viewrecords : true,
            hidegrid: false
        });

    // Add selection
    $("#table_list").setSelection(4, true);
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#table_list').setGridWidth(width);
    });
}

function getContextPath() {
    var contextPath = document.location.pathname;
    var index = contextPath.substr(1).indexOf("/");
    contextPath = contextPath.substr(0, index + 1);
    delete index;
    return contextPath;
}

search = function () {
    var  customerName= $("#customerName").val();
    var  idNumber= $("#idNumber").val();
    var  productName= $("#productName").val();
    var  status= $('#status option:selected').val();
    $("#table_list").jqGrid('setGridParam',{
        url:getContextPath()+"/customerService/queryList",
        postData:{customerName:customerName,idNumber:idNumber,productName:productName,status:status}//发送数据
    }).trigger("reloadGrid");
};

resetSearch = function () {
    $("#customerName").val("");
    $("#idNumber").val("");
    $("#productName").val("");
    $("#status").val("");
};

function getContextPath() {
    // var pathName = document.location.pathname;
    // var index = pathName.substr(1).indexOf("/");
    // return pathName.substr(0, index + 1);
    return "/micromvc";
}

/**
 * 合同审核
 * @param id
 */
function contract(id,status){
    window.location.href=getContextPath()+'/contract/queryAuditContract?id='+id+"&status="+status;
}

/**
 * 交租审核
 * @param id
 */
function rent(id){
    window.location.href=getContextPath()+'/customerService/gotoRentPage';
}

/**
 * 查看，根据状态查看不同的信息 包括交租审核/交租信息/合同审核信息
 * @param id
 */
function detail(id,status){
    window.location.href=getContextPath()+'/busi/detail?type=KF&businessId='+id+"&status="+status+"&active=base";
}

function initRentInfo(){
    $.jgrid.defaults.styleUI = 'Bootstrap';
    var column = [{name: 'order_no', index: 'order_no', width: 90},
        {name: 'shop_name', index: 'shop_name', width: 100},
        {name: 'rent_amount', index: 'rent_amount', width: 80,
            formatter:function(cellValue,options,rowObject){
                return (cellValue/ 1).toFixed(2)+"\n大写:"+convertCurrency(cellValue);
            }
        },
        {name: 'deposit_amount', index: 'deposit_amount', width: 80,
            formatter:function(cellValue,options,rowObject){
                return (cellValue/ 1).toFixed(2)+"\n大写:"+convertCurrency(cellValue);
            }
        },
        {name: 'contract_period', index: 'contract_period', width: 80}
    ];
    var name = ['序号', '商铺名称', '租金总计金额（元）', '押金金额（元）', '合同期'];
    var url = getContextPath()+'/customerService/getShopInfo';
    var businessId = $("#businessId").val();
    jQuery("#table_list").jqGrid(
        {
            url: url,
            datatype: "json",
            postData:{id:businessId},
            autoheight: true,
            autowidth: true,
            shrinkToFit: true,
            colNames : name,
            colModel : column,
            viewrecords : true,
            hidegrid: false
        });

    // Add selection
    $("#table_list").setSelection(4, true);
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#table_list').setGridWidth(width);
    });
}

function initPayInfo(type){
    $.jgrid.defaults.styleUI = 'Bootstrap';
    var column = [{name: 'period', index: 'period', width: 90},
        {name: 'pay_date_format', index: 'pay_date_format', width: 100},
        {name: 'pay_amount_yuan', index: 'pay_amount', width: 80,
            formatter:function(cellValue,options,rowObject){
                return (cellValue/ 1).toFixed(2);
            }
        },
        {name: 'overdue_day', index: 'overdue_day', width: 80},
        {name: 'overdue_amount_yuan', index: 'overdue_amount', width: 80,
            formatter:function(cellValue,options,rowObject){
                return (cellValue/ 1).toFixed(2);
            }
        },
        {name: 'pay_status', index: 'pay_status', width: 80},
        {name: 'operate', index: 'operate', width: 150,
            formatter: function (cellvalue, options, rowObject) {
               return renderRentButton(cellvalue,options,rowObject,type)
            }
        }

    ];
    var name = ['期数', '交租时间', '交租金额（元）', '逾期天数（天）', '逾期费用金额','租金缴费状态','操作'];
    var url = getContextPath()+'/customerService/getPayInfo';
    var businessId = $("#businessId").val();
    jQuery("#repay_plan_list").jqGrid(
        {
            url: url,
            datatype: "json",
            postData:{id:businessId},
            autoheight: true,
            autowidth: true,
            shrinkToFit: true,
            colNames : name,
            colModel : column,
            viewrecords : true,
            hidegrid: false
        });

    // Add selection
    // $("#repay_plan_list").setSelection(4, true);
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#repay_plan_list').setGridWidth(width);
    });
}

function initReduceInfo() {
    $.jgrid.defaults.styleUI = 'Bootstrap';
    var column = [{name: 'period', index: 'period', width: 90},
        {name: 'reduce_amount', index: 'reduce_amount', width: 100,
            formatter:function(cellValue,options,rowObject){
                return (cellValue/ 1).toFixed(2);
            }
        },
        {name: 'reduce_overdue_amount', index: 'reduce_overdue_amount', width: 80,
            formatter:function(cellValue,options,rowObject){
                return (cellValue/ 1).toFixed(2);
            }
        },
        {name: 'reduce_total', index: 'reduce_total', width: 80,
            formatter:function(cellValue,options,rowObject){
                return (cellValue/ 1).toFixed(2);
            }
        },
        {name: 'reduce_time', index: 'reduce_time', width: 80},
        {name: 'reduce_user_name', index: 'reduce_user_name', width: 80},
        {name: 'operate', index: 'operate', width: 80,
            formatter: function (cellvalue, options, rowObject) {
                return "<a  class='btn btn-primary btn-xs' href='"+rowObject.attachment_url+"?filename="+rowObject.attachment_name+"' target='_blank' >下载减免凭证</a>";
            }}
    ];
    var name = ['减免期数', '减免租金金额（元）', '减免逾期费用金额（元）', '总计（元）', '减免时间','减免人','操作'];
    var url = getContextPath()+'/customerService/getReduceInfo';
    var businessId = $("#businessId").val();
    jQuery("#reduce_list").jqGrid(
        {
            url: url,
            datatype: "json",
            postData:{id:businessId},
            autoheight: true,
            autowidth: true,
            shrinkToFit: true,
            colNames : name,
            colModel : column,
            viewrecords : true,
            hidegrid: false
        });
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#reduce_list').setGridWidth(width);
    });
}

function validAndCount(obj,type){
    var value = $(obj).val();
    var total = 0;
    if(null == value || "" === value){

    }else {
        total = parseFloat(value);
        if(!isNumber(value)){
            swal("请输入正确的数字！","","error");
            $(obj).val("");
            $("#reduceTotal").val("");
            return "1";
        }
    }
    if(type === "amount"){
        var reduceOverAmount = $("#reduceOverAmount").val();
        if(null != reduceOverAmount && "" !== reduceOverAmount){
            if(!isNumber(reduceOverAmount)){
                swal("请输入正确的数字！","","error");
                $("#reduceOverAmount").val("");
                $("#reduceTotal").val("");
                return "1";
            }
            total += parseFloat(reduceOverAmount);
        }
    }
    if(type === "over"){
        var reduceAmount = $("#reduceAmount").val();
        if(null != reduceAmount && "" !== reduceAmount){
            if(!isNumber(reduceAmount)){
                swal("请输入正确的数字！","","error");
                $("#reduceAmount").val("");
                return "1";
            }
            total += parseFloat(reduceAmount);
        }
    }
    $("#reduceTotal").val(total);
}

/**
 * 表单提交
 * @type {string}
 */
function validParam(){
    var reduceAmount = $("#reduceAmount").val();
    var reduceOverAmount = $("#reduceOverAmount").val();
    var planId = $("#planId").val();
    var businessId = $("#businessId").val();
    var payAmount = $("#payAmount").val();
    var payOverdue = $("#payOverdue").val();
    var total = 0;
    if((null != reduceAmount && "" !== reduceAmount) || (null != reduceOverAmount && "" !== reduceOverAmount)){
        if(null != reduceAmount  && "" !== reduceAmount ){
            if(!isNumber(reduceAmount)){
                swal("请输入正确的数字！","","error");
                refreshReduce();
                return "1";
            }else if(parseFloat(payAmount)<parseFloat(reduceAmount) || !isNumber(payAmount)){
                swal("减免租金金额不能大于交租金额！","","error");
                refreshReduce();
                return "1";
            }
            total += parseFloat(reduceAmount);
        }
        if(null != reduceOverAmount  && "" !== reduceOverAmount){
            if(!isNumber(reduceOverAmount)){
                swal("请输入正确的数字！","","error");
                refreshReduce();
                return "1";
            }else if(parseFloat(payOverdue)<parseFloat(reduceOverAmount) || !isNumber(payOverdue)){
                swal("减免逾期金额不能大于逾期费用金额！","","error");
                refreshReduce();
                return "1";
            }
            total += parseFloat(reduceOverAmount);
        }
    }else {
        swal("减免金额或者减免逾期金额请至少填写一项！","","error");
        refreshReduce();
        return "1";
    }
    if(total ===0 ){
        swal("减免金额或者减免逾期金额不能都为0！","","error");
        refreshReduce();
        return "1";
    }
    var fileUploadResult = upload();
    if("fail" === fileUploadResult){
        swal("文件上传失败！请联系管理员","","error");
        return "1";
    }
    if("null" === fileUploadResult){
        swal("请选择上传的文件！","","error");
        return "1";
    }
    var fileId = $("#fileId").val();
    var fileUrl = $("#fileUrl").val();
    var fileName = $("#fileName").val();
    if(null == fileId || "" === fileId){
        swal("请上传减免凭证！","","error");
        return "1";
    }
    $("#reduceTotal").val(total);
    $.ajax({
        async : false,
        cache : false,
        type : 'POST',
        dataType : "json",
        url:getContextPath()+"/customerService/saveReduceInfo",
        data:{reduceAmount:reduceAmount,reduceOverAmount:reduceOverAmount,reduceTotal:total,planId:planId,fileId:fileId,fileUrl:fileUrl,fileName:fileName},
        success : function(data) {
            if (data.resultCode == "000") {
                swal("添加成功！", "", "success");
                $("#reduceAmount").val("");
                $("#reduceOverAmount").val("");
                $("#planId").val("");
                $("#reduceTotal").val("");
                $("#uploadFile").val("");
                $("#fileId").val("");
                $("#fileUrl").val("");
                $("[id^=file_]").remove();
                $("#modal-form-closer").click();
            }else{
                swal("添加失败！", "请联系管理员!", "error");
            }
            $("#reduce_list").jqGrid('setGridParam',{
                url:getContextPath()+'/customerService/getReduceInfo',
                postData:{id:businessId}
            }).trigger("reloadGrid");
            $("#repay_plan_list").jqGrid('setGridParam',{
                url:getContextPath()+'/customerService/getPayInfo',
                postData:{id:businessId}
            }).trigger("reloadGrid");
        }
    });
}
function refreshReduce(){
    $("#reduceAmount").val("");
    $("#reduceOverAmount").val("");
    $("#reduceTotal").val("");
}

function reduce(id,payAmount,payOverdue){
    $("#planId").val(id);
    $("#payAmount").val(payAmount);
    $("#payOverdue").val(payOverdue);
}

function isNumber(value) {         //验证是否为数字
    var patrn = /^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))|0$/;
    if (patrn.exec(value) == null || value == "") {
        return false
    } else {
        return true
    }
}

function approve(planId,result){
    var title = "确认审核通过吗？";
    var url = getContextPath()+"/customerService/approveRentInfo";
    var businessId = $("#businessId").val();
    if("reject" === result){
        title =  "确认审核退回吗？";
    }
    swal({
        title: title,
        text: "",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "确认",
        cancelButtonText:"取消",
        closeOnConfirm: false
    }, function () {
        $.ajax({
            async : false,
            cache : false,
            type : 'POST',
            dataType : "json",
            url:url,
            data:{planId:planId,result:result,businessId:businessId},
            success : function(data) {
                if (data.resultCode == "000") {
                    swal("操作成功！", "操作成功!", "success");
                }else{
                    swal("操作失败！", "请联系管理员!", "error");
                }
                $("#repay_plan_list").jqGrid('setGridParam',{
                    url:getContextPath()+'/customerService/getPayInfo',
                    postData:{id:businessId}
                }).trigger("reloadGrid");
            }
        });
    });
}

function goBack(){
    window.history.back();
    return false;
}

function upload(){
    var result = "fail";
    var files = $('#uploadFile')[0].files; //获取file控件中的内容
    if (!files || files.length === 0) {
        result = "null";
        return result;
    }
    var formData = new FormData();
    formData.append('file', files[0]);
    formData.append("business_type","140");
    $.ajax({
        url: "/micromvc/file/testFile",    //请求的url地址
        dataType: "json",   //返回格式为json
        async: false,//请求异步
        data: formData,    //参数值
        type: "POST",   //请求方式
        contentType: false,
        processData: false,
        success: function (data) {
            swal("文件上传成功","","success");
            $("#fileId").val(data.id);
            $("#fileUrl").val(data.down_path);
            $("#fileName").val(data.file_name);
            $("#uploadFile").val("");
            // var html ='<div style="display: block" id="file_'+data.id+'">';
            // html += '<div class = "from-group">';
            // html += '<a  class=" btn btn-default" href="'+data.down_path+'">下载文件</a>';
            // html += '<label  class="col-sm-2 control-label" >附件名称：'+data.file_name+'</label>';
            // html += '<label  class="col-sm-3 control-label" >上传时间：'+data.create_time+'</label>';
            // html += '<a  class=" btn btn-default" onclick = "delFile('+data.id+')">删除</a></div>';
            // html += '</div>';
            // $("#html").append(html);
            // $("#uploadButton").css("disabled","disabled");
            result =  "success"
        },
        error: function () {
            swal("文件上传失败","","error");
            result =  "fail";
        }
    });
    return result;
}
//删除文件
function delFile(id){
    $.ajax({
        url:"/micromvc/file/deleteFile",
        type:"post",
        dataType:"json",
        data:{
            id:id
        },
        success:function(data){
            if(data.result="000"){
                $("#file_"+id).hide();
                $("#uploadButton").css("disabled").remove();
            }
        }
    })
}


function confirmRent(planId){
    var title = "确认缴费吗？";
    var url = getContextPath()+"/customerService/confirmRent";
    var businessId = $("#businessId").val();
    swal({
        title: title,
        text: "",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "确认",
        cancelButtonText:"取消",
        closeOnConfirm: false
    }, function () {
        $.ajax({
            async : false,
            cache : false,
            type : 'POST',
            dataType : "json",
            url:url,
            data:{planId:planId,businessId:businessId},
            success : function(data) {
                if (data.resultCode == "000") {
                    swal("操作成功！", "操作成功!", "success");
                }else{
                    swal("操作失败！", "请联系管理员!", "error");
                }
                $("#repay_plan_list").jqGrid('setGridParam',{
                    url:getContextPath()+'/customerService/getPayInfo',
                    postData:{id:businessId}
                }).trigger("reloadGrid");
            },
            error:function () {
                swal("操作失败！", "请联系管理员!", "error");
            }
        });
    });
}

/**
 * 下载房租凭证
 * @param planId 交租计划id
 */
function downloadFile(planId){
    var businessId = $("#businessId").val();
    $.ajax({
        url:'/micromvc/rent/downRentFile',
        type:'post',
        dataType:'json',
        async: false,
        data:{
            id:planId
        },
        success:function(data){
            if(data.resultCode=="000"){
                if(data.id){
                    window.location.href=data.down_path+"?filename="+data.file_name;
                }else{
                    swal("文件不存在","","error");
                }
            }
        }

    })
}