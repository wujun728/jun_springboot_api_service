<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>


<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>进件质检</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <!-- jqgrid-->
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/font-awesome.min.css?v=4.3.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/jqgrid/ui.jqgrid.css?0820" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/style.css?v=3.2.0" rel="stylesheet">
    <!-- 全局js -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/jquery-2.1.1.min.js"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/bootstrap.min.js?v=3.4.0"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/content.js?v=1.0.0"></script>
    <!-- 遮罩层 -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/layer.min.js"></script>
    <!-- jqGrid -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/jqgrid/i18n/grid.locale-cn.js?0820"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/jqgrid/jquery.jqGrid.min.js?0820"></script>
    <!-- Sweet alert -->
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/sweetalert/sweetalert.min.js"></script>
    <!-- jQuery Validation plugin javascript-->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/validate/messages_zh.min.js"></script>
    <style>
        table{  border-collapse: inherit !important; width: 100%; margin-bottom:10px;padding:10px; line-height:30px}
        table textarea{width: 100%;}
        #contractUploadInfo span,#contractUploadInfo td{padding-right: 10px}
        #contractUploadInfo span{width: 350px;display: inline-block}

    </style>
</head>
<body class="gray-bg">
<%@ include file="/WEB-INF/nh-micro-jsp/tab.jsp" %>
<form class="form-horizontal" role="form" id="create">
    <input type="hidden" name="id" value="${param.businessId}" id="business_id">
    <div class="panel panel-primary">
        <c:if test="${auditStatus eq -1 }">
            <div class="panel-heading">
               合同信息
            </div>

        <div class="panel-body">
            <div id="contractUploadInfo">${doContractPagination}</div>
            <table id="tb" style="border-collapse:separate; border-spacing:0px 10px;">
                <tr>
                    <td>
                        <dl>
                            <dd>
                                <shiro:hasPermission name="sell_contract_upload">
                                <c:if test="${auditStatus eq -1 }">
                                <input class="btn btn-primary" type="button" value="上传签约合同" onclick='javascript:$("#contractFile").click();' />
                                <input type="file" id="contractFile" name="contractFile"  style="display: none" onchange="ophiddenFile();" multiple="multiple" />
                                 <span style="color: red;">单个文件不得大于50M，且最多上传5个文件！</span>
                                </c:if>
                                </shiro:hasPermission>
                            </dd>
                        </dl>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="contractInfo"></div>
                    </td>
                </tr>

            </table>

            <div class="col-sm-12" style="text-align: center">
                <shiro:hasPermission name="sell_contract_upload">
                <c:if test="${auditStatus eq -1 }">
                <button id="uploadButton" type="button" class="btn btn-primary " onclick="uploadFile();">
                    <i class="fa fa-save" style="width: 13px;"></i>&nbsp;确定
                </button>
                <button type="reset" class="btn btn-primary " onclick="window.history.back(-1)" >
                    <i class="fa fa-close" style="width: 13px;"></i>&nbsp;关闭
                </button>
                </c:if>
                </shiro:hasPermission>
            </div>
        </div>
        </c:if>
        <div class="panel-heading">
             审核历史
        </div>
            <div id="contractUploadInfo1">${doPagination}</div>

    </div>


</form>
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/laydate/laydate.js"></script>
<script src="<%=path%>/nh-micro-js/js/system/quality.js"></script>
</body>

</html>


<script>
    function ophiddenFile(){
        var html = $("#contractInfo").html();
        var fileName ="";
        var files = document.getElementById("contractFile").files;
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            if(html==''){
                html = "<tr>"+
                        "<td>文件名称："+file.name +"</td>"+
                        "<td>文件大小:"+(file.size/1048576).toFixed(2)+"M</td>"+
                        "</tr>";
                $("#contractInfo").html(html);
            }else{
                html += "<tr>"+
                    "<td>文件名称："+file.name+"</td>"+
                    "<td>文件大小:"+(file.size/1048576).toFixed(2)+"M</td>"+
                    "</tr>";
                $("#contractInfo").html(html);
            }

        }
        $('#uploadButton').removeAttr("disabled");
    }

    var loading;
    function  uploadFile() {
        loading = layer.load();
        var id = $("#business_id").val();
        var files = document.getElementById("contractFile").files;
        if(files.length > 5){
            alert("上传文件个数超过5个，请重新上传！");
            $("#contractInfo").html("");
            layer.close(loading);
            return;
        }


        if (files.length != 0) {
            var formData = new FormData();
            for (var i = 0; i < files.length; i++) {
                var file = files[i];
                if(file.size > 52428800){
                    alert("文件:"+file.name+"的容量大于50M，请重新上传！");
                    $("#contractInfo").html("");
                    layer.close(loading);
                    return;
                }
                formData.append("file", file);
            }
        }else {
            alert("请上传文件");
            layer.close(loading);
            return;
        }
            $.ajax({
                url: '<%=path%>/micromvc/contract/uploadFile?id='+id,
                type: 'POST',
                cache: false,
                data: formData,
                dataType:"json",
                //这个参数是jquery特有的，不进行序列化，因为我们不是json格式的字符串，而是要传文件
                processData: false,
                //注意这里一定要设置contentType:false，不然会默认为传的是字符串，这样文件就传不过去了
                contentType: false,
                success: function (data) {

                    if(data.resultCode == 000){
                        var html = "<tr>"+
                            "<td>审核状态："+data.auditStatus +"</td>"+
                            "<td>审核时间:"+data.auditTime+"</td>"+
                            "</tr>" ;
                        var arr = data.listMap;
                        for(var i in arr){
                            html +=  "<tr>"+
                                "<td>文件名称：<a href='"+arr[i].down_path+"?filename="+arr[i].file_name+"'>"+arr[i].file_name +"</a></td>"+
                                "<td>上传时间:"+arr[i].create_time+"</td>" +
                                " </tr> "
                        }
                        $("#contractUploadInfo").html('');
                        $("#contractUploadInfo").html(html);
                        $("#contractInfo").html("");
                        $("#uploadButton").attr('disabled',"true");
                        alert("上传成功");
                        layer.close(loading);
                    }else{
                        alert("上传失败");
                        $("#contractInfo").html('');
                        layer.close(loading);
                    }

                },
                error:function () {
                    layer.close(loading);
                }
            });

    }

</script>