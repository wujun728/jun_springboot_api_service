/**
 * 角色管理的单例
 */
var business = {
    id: "sellManageTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
business.initColumn = function () {
    var columns = [
        {name : 'id',index : 'id',width : 250,formatter:function(cellValue,options,rowObject){
            return renderBusinessButton(cellValue,options,rowObject);

        }},
        {name : 'cust_name',index : 'cust_name',width : 90},
        {name : 'cust_paper',index : 'cust_paper',width : 100},
        {name : 'product_name',index : 'product_name',width : 80},
        {name : 'rent_amount',index : 'apply_amount',width : 110,formatter:function(cellValue,options,rowObject){
            return (cellValue / 100 * rowObject.rent_period).toFixed(2);
        }},
        {name : 'apply_time',index : 'apply_time',width : 80},
        {name : 'statusName',index : 'statusName',width : 80,sortable : false},
        {name : 'user_name',index : 'user_name',width : 100,sortable : false},
        {name : 'create_time',index : 'create_time',width : 100,sortable : false},
        {name : 'update_time',index : 'update_time',width : 100,sortable : false}
    ]
    return columns;
};

business.initNames = function () {
    var names = ['操作', '姓名', '证件号', '产品名称', '房租总金额(元)','提交时间', '状态','客户经理','创建时间','更新时间'];
    return names;
}

$(function(){
    getUserRoleId()
    $.jgrid.defaults.styleUI = 'Bootstrap';
    pageInit();
    statusInit();
    if($("#optType").val()!="edit"){
      changeCity("0","s_province")
    }



});

function getUserRoleId(){
    $.ajax({
        url:"/micromvc/busi/getUserRoleId",
        type:'post',
        dataType:'json',
        async : false,
        cache : false,
        success:function (data) {
            if(data.roletype){
                $("#roletype").val(data.roletype)
            }
        }
    })
}

/**
 * 业务状态初始化
 */
function statusInit(){
    var url = "/micromvc/uc/dictItem/getInfoListByDicId";
    var roletype = $("#roletype").val()
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
                    if(roletype == "director"){
                        if(data[i].item_id == 110){
                            continue;
                        }
                    }
                    var option = "<option value='" + data[i].item_id + "'>" + data[i].item_name + "</option>"
                    $("#busiStatus").append(option);
                }
            }
        }
    });
}

function pageInit(){
    var column = business.initColumn();
    var name = business.initNames();
    var url = '/micromvc/busi/list';
    jQuery("#table_list").jqGrid(
        {
            url: url,
            datatype: "json",
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
            hidegrid: false,
        });

    // Add selection
    $("#table_list").setSelection(4, true);



    // Add responsive to jqGrid
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

business.search = function () {
    var custName = $("#custName").val();
    var custPaper = $("#custPaper").val();
    var productName = $("#productName").val();
    var busiStatus = $("#busiStatus").val();

    $("#table_list").jqGrid('setGridParam',{
        datatype:'json',
        postData:{'custName':custName,'custPaper':custPaper,'productName':productName,'busiStatus':busiStatus},
        page:1
    }).trigger("reloadGrid");


}

business.reload = function (){
    jQuery("#table_list").trigger("reloadGrid")
}

business.toAdd = function(){
    window.location.href = "/micromvc/busi/toAdd";
}


/**
 * 表单提交
 * @type {string}
 */
var icon = "<i class='fa fa-times-circle'></i> ";
$("#create").validate({
   rules:{
       source:{required:true},
       other_source:{required:false,maxlength: 50},
       cust_name:{required: true, minlength: 1,maxlength: 50},
       cust_sex:{required:true},
       paper_type:{required:true},
       cust_paper:{required:true,maxlength: 50},
       paper_source:{required: true, minlength: 1,maxlength: 100},
       marital:{required:true},
       cust_address:{required:true, minlength: 1,maxlength: 100},
       mobile:{required:true,mobile:true,maxlength: 50},
       email:{email:true,maxlength: 50},
       wechat:{maxlength: 50},
       paper_end_time:{required:true},
       rent_amount:{required:true,digits:true,maxlength:50},
       deposit_amount:{required:true,digits:true,maxlength:50},
       partner_nums:{digits:true},
       share:{number:true},
       bankNum:{digits:true,maxlength:50},
       rent_start_time:{required:true},
       remark:{maxlength:500},
       file_remark:{maxlength:500},
       other_item:{maxlength:500},
       ename:{maxlength:50},
       brand_name:{maxlength:50},
       erelation:{maxlength:50},
       emobile:{mobile:true,maxlength:50},
       pub_account:{digits:true,maxlength:50},
       bank_account:{maxlength:100},
    },
    messages:{
        source:{required: icon + "请选择客户来源"},
        cust_name: {
            required: icon + "请输入客户姓名",
            minlength: icon + "客户姓名必须1个字符以上",
            maxlength: icon + "客户姓名必须50个字符以内"
        },
        other_source:{maxlength: icon + "50个字符以内"},
        cust_sex:{required: icon + "请选择客户性别"},
        paper_type:{required: icon + "请选择证件类型"},
        cust_paper:{required: icon + "请输入证件号码",minlength: icon + "证件号码必须1个字符以上", maxlength: icon + "证件号码必须50个字符以内"},
        paper_source:{required: icon + "请输入发证机关",minlength: icon + "发证机关必须1个字符以上", maxlength: icon + "发证机关必须100个字符以内"},
        paper_end_time:{required: icon + "请选择证件失效日期"},
        marital:{required: icon + "请选择证件婚姻状况"},
        cust_address:{required: icon + "请输入通讯地址",minlength: icon + "通讯地址必须1个字符以上",maxlength: icon + "通讯地址必须100个字符以内"},
        mobile:{required: icon + "请输入移动电话",mobile:icon + "手机号错误",maxlength: icon + "50个字符以内"},
        rent_amount:{required: icon + "请输入有效金额",maxlength: icon + "50个字符以内",digits:icon + "请输入整数"},
        deposit_amount:{required: icon + "请输入有效金额",maxlength: icon + "50个字符以内",digits:icon + "请输入整数"},
        partner_nums:{digits:icon + "请输入整数"},
        share:{number:icon + "请输入有效数字"},
        bankNum:{digits: icon + "请输入有效的银行卡号",maxlength:icon + "50个字符以内"},
        rent_start_time:{required: icon +"请选择起租时间"},
        email:{maxlength: icon + "50个字符以内",email: icon + "邮箱格式错误"},
        wechat:{maxlength: icon + "50个字符以内"},
        remark:{maxlength: icon + "500个字符以内"},
        file_remark:{maxlength: icon + "500个字符以内"},
        other_item:{maxlength: icon + "500个字符以内"},
        ename:{maxlength: icon + "50个字符以内"},
        bank_account:{maxlength: icon + "100个字符以内"},
        erelation:{maxlength: icon + "50个字符以内"},
        brand_name:{maxlength: icon + "50个字符以内"},
        emobile:{mobile:icon + "手机号格式错误",maxlength: icon + "50个字符以内"},
        pub_account:{digits:icon + "请输入数字",maxlength: icon + "50个字符以内"},
    },
    errorPlacement: function(error, element) {
        if (element.is(":radio")){
            error.appendTo(element.next().parent());
        } else {
            error.appendTo(element.parent());//默认，这个必须些，不写会影响其他样式问题
        }
    },
    submitHandler:function(form){
       var url ="/micromvc/busi/createInfo";
       if($("#optType").val()=="edit"){
           url = "/micromvc/busi/updateInfo"
       }
        $.ajax({
            url: url,
            data: $("#create").serialize(),
            dataType: 'json',
            type: 'post',
            success: function (data) {
                if(data.resultCode=="000"){
                    alert("操作成功！")
                    window.history.go(-1);
                    business.reload()
                }else{
                    swal("保存失败！", data.message, "error");
                }
            }
        })
    }
})


function get(businessId,status){
    window.location.href="/micromvc/busi/detail?businessId="+businessId+"&status="+status+"&active=base";
}

function edit(businessId,status){
    window.location.href="/micromvc/busi/toEdit?businessId="+businessId+"&status="+status+"&active=base";
}

function quality(businessId,status){
    window.location.href="/micromvc/busi/toQuality?businessId="+businessId+"&status="+status+"&active=base";
}

//查看交租信息
function payRent(businessId,status){
    window.location.href="/micromvc/rent/toPayRent?businessId="+businessId+"&status="+status+"&active=rental";
}

/**
 * 改变进件状态
 * @param o
 */
function changeStatus(o){
    if(confirm("确定要放弃租赁吗？")){
        $.ajax({
            url:'/micromvc/busi/abandonLoan',
            type:'post',
            dataType:'json',
            data:{
                id:o
            },
            success:function(data){
                if(data.resultObj ==1){
                    swal("操作成功！", "", "success");
                    jQuery("#table_list").trigger("reloadGrid")
                }else{
                    swal("保存失败！", data.message, "error");
                }
            }
        })
    }

}



/**
 * 根据select  radio所选值控制内容显示
 */
$("#source").change(function(){
    if(this.value==5){
        $("#other_source").attr("readOnly",false)
    }else{
        $("#other_source").prop("readOnly",true)
        $("#other_source").val("")
    }
})

$("input[name='partner']").change(function(){
    if(this.value==1){
        $("#partner_nums").prop("readOnly",false)
        $("input[name='share']").prop("readOnly",false)
    }else{
        $("#partner_nums").prop("readOnly",true)
        $("input[name='share']").prop("readOnly",true)
        $("#partner_nums").val("")
        $("input[name='share']").val("")
    }
})

/**
 * 计算房租总金额
 */
$("[name='rent_period']").change(function(){
    var rentAmount = $("input[name='rent_amount']").val();
    var totalAmount = (this.value * rentAmount).toFixed(2)
    $("#totalAmount").text(totalAmount)
})

$("input[name='rent_amount']").change(function(){
    var rent_period = $("[name='rent_period']").val();
    var totalAmount = (this.value * rent_period).toFixed(2)
    $("#totalAmount").text(totalAmount)
})

$("input[name='is_pub_account']").change(function(){
    if(this.value==1){
        $("[name='pub_account']").prop("readOnly",false);
        $("[name='bank_account']").prop("readOnly",false);
        $("[name='pub_account']").attr("required",true)
        $("[name='bank_account']").attr("required",true)
    }else{
        $("[name='pub_account']").prop("readOnly",true);
        $("[name='bank_account']").prop("readOnly",true);
        $("[name='pub_account']").val("");
        $("[name='bank_account']").val("");
        $("[name='pub_account']").removeAttr("required");
        $("[name='bank_account']").removeAttr("required");
    }
})

$("input[name='brand']").change(function(){
    if(this.value==1){
        $("[name='brand_name']").prop("readOnly",false);
    }else{
        $("[name='brand_name']").prop("readOnly",true);
        $("[name='brand_name']").val("");
    }
})

$("#paperType").change(function(){
    if(this.value==1){//身份证
        $("#cust_paper").attr("card",true);
        $("#cust_paper").removeAttr("hzcard");
        $("#cust_paper").removeAttr("jgcard");
    }else if(this.value==2){//军官证
        $("#cust_paper").attr("jgcard",true);
        $("#cust_paper").removeAttr("card");
        $("#cust_paper").removeAttr("hzcard");

    }else if(this.value==3){//护照
        $("#cust_paper").attr("hzcard",true);
        $("#cust_paper").removeAttr("card");
        $("#cust_paper").removeAttr("jgcard");
    }

})

//上传文件后将文件id保存在此 多个用"-"分隔  整体提交表单时再将业务id关联到文件表中
var fileIds = '';

//文件上传
function upload(){
    var files = $('#upfile')[0].files; //获取file控件中的内容
    if (!files) {
        return;
    }
    if(files[0].size>1024*1024*50){
        alert("文件不能大于50M");
        return;
    }
    var filename = files[0].name;
    var index1=filename.lastIndexOf(".");
    var index2=filename.length;
    var suffix=filename.substring(index1+1,index2);//后缀名
    var formData = new FormData();
    formData.append('file', files[0]);
    formData.append("business_type","110");
    formData.append("remark",$("#file_remark").val());
    $.ajax({
        url: "/micromvc/file/testFile",    //请求的url地址
        dataType: "json",   //返回格式为json
        async: false,//请求是否异步，默认为异步，这也是ajax重要特性
        data: formData,    //参数值
        type: "POST",   //请求方式
        contentType: false,  //必须false才会避开jQuery对 formdata 的默认处理 , XMLHttpRequest会对 formdata 进行正确的处理
        processData: false, //必须false才会自动加上正确的Content-Type
        success: function (data) {
            if(data.id){
                var url = data.down_path+"?filename="+data.file_name;
                var html ='<div style="display: block" id="'+data.id+'">';
                html += '<div class = "from-group">';
                html += '<a  class=" btn btn-primary" href="'+url+'" >下载文件</a>';
                html += '<label  class="col-sm-3 control-label" style="text-align:left;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">附件名称：'+data.file_name+'</label>';
                html += '<label  class="col-sm-3 control-label" >上传时间：'+data.create_time+'</label>';
                html += '<a  class=" btn btn-warning" onclick = delFile("'+data.id+'")>删除</a></div>';
                html += '<div class="form-group">';
                html += '<label  class="col-sm-2 control-label" >说明</label>';
                html += '<div class="col-sm-8">';
                html += '<textarea class="form-control" readonly>';
                if(data.remark){
                    html += data.remark
                }
                html += '</textarea>';
                html += '</div></div></div>';
                $("#html").append(html);
                fileIds += data.id+"-";
                $("#fileIds").val(fileIds)
                $("#file_remark").val("");
                $("#upfile").val("");
            }else{
                alert("系统错误，请重试")
            }

        },
        error: function () {
            alert("系统错误，请重试")
        }
    });
}

//三级联动
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

$("#s_province").change(function(){
    $("#shop_name").val(" ")
    $("#s_county").html("<option value=''>请选择</option>")
    $("#s_shop_id").html("<option value=''>请选择</option>")
    changeCity(this.value,"s_city");
})

$("#s_city").change(function(){
    $("#shop_name").val(" ")
    $("#s_shop_id").html("<option value=''>请选择</option>")
    changeCity(this.value,"s_county");
})

$("#s_county").change(function(){
    $("#shop_name").val(" ")
    selectShop(this.value,"s_shop_id");
})

$("#s_shop_id").change(function(){
    $("#shop_name").val(" ")
    var name = $("#s_shop_id option:selected").text();
    if(name != "请选择"){
        $("#shop_name").val(name)
    }

})

function selectShop(o,id){
    $.ajax({
        async : false,
        cache : false,
        url:"/micromvc/shop/getShopByCounty",
        type:"post",
        dataType:"json",
        data:{
            county:o
        },
        success:function(data){
            $("#"+id).html("<option value=''>请选择</option> ")
            if(data!=null){
                for(var i = 0;i < data.length;i++){
                    var html ="<option value='"+data[i].id+"'>"+data[i].name+"</option>"
                    $("#"+id).append(html)
                }
            }
        }
    })
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
                $("#"+id).hide()
            }
        }
    })
}



