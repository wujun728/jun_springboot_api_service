function initRentInfo(){
    $.jgrid.defaults.styleUI = 'Bootstrap';
    var column = [{name: 'order_no', index: 'order_no', width: 90},
        {name: 'shop_name', index: 'shop_name', width: 100},
        {name: 'rent_amount', index: 'rent_amount', width: 80,formatter:function(cellValue,options,rowObject){
            var str = convertCurrency(cellValue)
            return cellValue +"<br>大写："+str;
        }},
        {name: 'deposit_amount', index: 'deposit_amount', width: 80,formatter:function(cellValue,options,rowObject){
            var str = convertCurrency(cellValue)
            return cellValue +"<br>大写："+str;
        }},
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
    var column = [{name: 'period', index: 'period', key:true,width: 90},
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
        {name: 'id', index: 'id',width: 80,formatter:function(cellValue,options,rowObject){
            return renderPayButton(cellValue,options,rowObject);

        }}
    ];
    var name = ['期数', '交租时间', '交租金额（元）', '逾期天数（天）', '逾期费用金额','租金缴费状态','操作'];
    var url = getContextPath()+'/customerService/getPayInfo';
    var businessId = $("#businessId").val();
    jQuery("#repay_plan_list").jqGrid(
        {
            url: url,
            datatype: "json",
            postData:{'id':businessId},
            autoheight: true,
            autowidth: true,
            shrinkToFit: true,
            colNames : name,
            colModel : column,
            viewrecords : true,
            hidegrid: false,
            loadComplete: function () {//使用loadComplete方法替换gridComplete，gridComplete会被其他事件(clearGridData等)触发
                var ids = jQuery("#repay_plan_list").jqGrid('getDataIDs');
                for (var i = 0; i < ids.length; i++) {
                    var curRowData = jQuery("#repay_plan_list").jqGrid('getRowData', ids[i]);
                    var status = curRowData['pay_status'];
                    if(status!="已缴费"){
                        var target = jQuery("#repay_plan_list").find("[title='"+status+"']");
                        var tr = target.closest("tr").next("tr");
                        var td = tr.find("td:last");
                        var newBtn = "上期“已缴费”时才可进行凭证上传！";
                        td.html(newBtn);
                    }


                }
            },
            gridComplete: function () {
            },

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
        {name: 'id',index:'id',width:90,formatter:function(cellValue,options, rowObject){
            return "<a class='btn btn-primary btn-xs'  href='"+rowObject.attachment_url+"?filename="+rowObject.attachment_name+"'>下载附件</a>";
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

function getContextPath() {
    // var pathName = document.location.pathname;
    // var index = pathName.substr(1).indexOf("/");
    // return pathName.substr(0, index + 1);
    return "/micromvc";
}
//本期还款计划id
var id=null;
function openUploadWindow(o){
    $("#rentFile").val("")
    $("#myModal").modal('show');
    id=o;
}

//下载房租凭证
function downloadRent(o){
    $.ajax({
        url:'/micromvc/rent/downRentFile',
        type:'post',
        dataType:'json',
        async: false,
        data:{
            id:o
        },
        success:function(data){
            if(data.resultCode=="000"){
                if(data.id){
                    var url = data.down_path+"?filename="+data.file_name;
                    window.location.href=url;
                }else{
                    alert("文件不存在")
                }

            }
        }

    })
}

function uploadRent(){
    var files = $('#rentFile')[0].files; //获取file控件中的内容
    if (!files) {
        return;
    }
    var formData = new FormData();
    formData.append('file', files[0]);
    formData.append('business_id', id);//次id为还款计划的id 不是进件业务id
    formData.append("business_type","120");

    $.ajax({
        url: "/micromvc/rent/uploadRentFile",    //请求的url地址
        dataType: "json",   //返回格式为json
        async: false,//请求是否异步，默认为异步，这也是ajax重要特性
        data: formData,    //参数值
        type: "POST",   //请求方式
        contentType: false,  //必须false才会避开jQuery对 formdata 的默认处理 , XMLHttpRequest会对 formdata 进行正确的处理
        processData: false,
        success:function(data){
            if(data != null){
                alert("上传成功！");
                $("#myModal").modal('hide');
                $("#rentFile").val("")
                jQuery("#repay_plan_list").trigger("reloadGrid")
            }
        }
    })
}


