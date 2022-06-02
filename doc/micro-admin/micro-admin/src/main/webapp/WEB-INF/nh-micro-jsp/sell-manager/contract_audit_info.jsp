<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


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
        table{  border-collapse: inherit !important; width: 90%; margin-bottom:10px;padding:10px; line-height:30px}
        table textarea{width: 100%;}
    </style>
</head>
<body class="gray-bg">
<form class="form-horizontal" role="form" id="create">
    <input type="hidden" name="id" value="${id}" id="business_id">
    <div class="panel panel-primary">
        <c:if test="${auditStatus eq -1 }">
        <div class="panel-heading">
           合同信息
        </div>
        </c:if>
        <div class="panel-body">
            <table id="tb" style="border-collapse:separate; border-spacing:0px 10px;">
                <tr>
                    <td>
                        <div id="contractUploadInfo">${doAuditContract}</div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="contractInfo"></div>
                    </td>
                </tr>

            </table>
            <c:if test="${auditStatus eq -1 }">
            <div class="col-sm-12" style="text-align: center">
                <button type="button" class="btn btn-primary " onclick="contractAudit();">
                    <i class="fa fa-save" style="width: 13px;"></i>&nbsp;确定
                </button>
                <button type="reset" class="btn btn-primary " onclick="window.history.back(-1)" >
                    <i class="fa fa-close" style="width: 13px;"></i>&nbsp;关闭
                </button>
            </div>
            </c:if>
        </div>
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
    function contractAudit(){

        var auditStatus = $("input[name='audit']:checked").val();
        var remark = $("#remark").val();
        var id = $("#business_id").val();
        if(remark == '' && auditStatus == -1){
            alert("请填写审核说明！");
            return;
        }
        $.ajax({
            url: '<%=path%>/micromvc/contract/auditContract',
            type: 'POST',
            cache: false,
            data: {
                "id":id,
                "remark":remark,
                "status":auditStatus
            },
            dataType:"json",
            success: function (data) {
                alert("操作成功！");
                window.history.back(-1);
            },
            error: function () {
                alert("操作失败！");
                window.history.back(-1);
            }
        });
    }


    $("#remark").on("input propertychange", function () {
        var content = document.getElementById('num');
        var $this = $(this),
            _val = $this.val();
        var count = $this.val().length;
        if (_val.length > 1000) {
            $this.val(_val.substring(0, 1000));
            content.innerText = count;
            $("#num").attr("style","color:red");
        }else{
            content.innerText = count;
            $("#num").attr("style","color:#676a6c");
        }
    });


</script>