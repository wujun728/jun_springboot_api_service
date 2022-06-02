
/**
 * 初始化表格的列
 */
initColumn = function () {
    return [
        {name: 'operate', index: 'operate', width: 200, sortable: false,
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
            }
        },
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

function initAccountInfo(){
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
                   if(data[i].item_id <240){
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
    var url = getContextPath()+'/accountManage/queryRentList';
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

search = function () {
    var  customerName= $("#customerName").val();
    var  idNumber= $("#idNumber").val();
    var  productName= $("#productName").val();
    var  status= $('#status option:selected').val();
    $("#table_list").jqGrid('setGridParam',{
        url:getContextPath()+"/accountManage/queryRentList",
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
 * 交租详情
 * @param id
 */
function rentDetail(id,status){
    window.location.href=getContextPath()+'/busi/detail?type=CW1&businessId='+id+"&status="+status+"&active=base";
}

/**
 * 查看，根据状态查看不同的信息
 * @param id
 */
function detail(id,status){
    window.location.href=getContextPath()+'/busi/detail?type=CW&businessId='+id+"&status="+status+"&active=base";
}