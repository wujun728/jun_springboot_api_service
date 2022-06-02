// $(function(){
//     var businessId = $("#businessId").val();
//     var url = getContextPath()+"/risk/getQuestion";
//     var table = $("#table_list1");
//     $.ajax({
//         url:url,
//         type:'post',
//         dataType:'json',
//         data:{businessId:businessId},
//         success:function(data){
//             if(null == data || data.length === 0){
//                 table.append("<tr><td style=\"background-color: #1a8cff;height: 30px\"><span style=\"color: ghostwhite\">无数据</span></td></tr>");
//                 table.append("<tr><td align='center'>" +
//                     "<a  class='btn btn-primary' href='javascript:void(0);' onclick='goBack()'>关闭</a>" );
//             }else {
//                 for (var i = 0; i < data.length; i++) {
//                     table.append("<tr><td style=\"background-color: #1a8cff;height: 30px\"><span style=\"color: ghostwhite\">"+data[i].original_sort+"."+data[i].question+"</span></td></tr>")
//                     table.append("<tr><td><textarea placeholder=\"text文本1000字符以内\" name=\" \" id=\"\" style=\"width: 100%\"></textarea></td></tr>")
//                 }
//                 table.append("<tr><td style=\"background-color: #1a8cff;height: 30px\"><span style=\"color: ghostwhite\">风控初审</span></td></tr>")
//                 var auditType = data[0].question_type;
//                 if("first" === auditType){
//                     table.append("<tr><td><input type='radio' value='approve' onclick='showRemark(this.value)' checked='checked' name='result'>通过&nbsp;&nbsp;" +
//                         "<input type='radio' value='reject' onclick='showRemark(this.value)' name='result'>拒贷&nbsp;&nbsp;" +
//                         "<input type='radio' value='survey' onclick='showRemark(this.value)' name='result'>尽调&nbsp;&nbsp;" +
//                         "<input type='radio' value='replenish' onclick='showRemark(this.value)' name='result'>补充资料</td></tr>");
//                 }
//                 if("survey" === auditType){
//                     table.append("<tr><td>" +
//                         "<input type='radio' value='approve' name='result' onclick='showRemark(this.value)' checked='checked'>通过&nbsp;&nbsp;" +
//                         "<input type='radio' value='back' name='result' onclick='showRemark(this.value)'>退回</td></tr>");
//                 }
//                 table.append("<tr><td id='remarkTD'></td></tr>")
//                 table.append("<tr><td align='center'>" +
//                     "<a  class='btn btn-primary' href='javascript:void(0);' onclick='goBack()'>关闭</a>" +
//                     "<a  class='btn btn-primary' href='javascript:void(0);'>提交</a></td></tr>");
//             }
//         },
//         error:function(data){
//             table.append("<tr><td style=\"background-color: #1a8cff;height: 30px\"><span style=\"color: ghostwhite\">无 数 据</span></td></tr>");
//             table.append("<tr><td align='center'>" +
//                 "<a  class='btn btn-primary' href='javascript:void(0);' onclick='goBack()'>关闭</a>" );
//         }
//     });
// });

function getContextPath() {
    // var pathName = document.location.pathname;
    // var index = pathName.substr(1).indexOf("/");
    // return pathName.substr(0, index + 1);
    return "/micromvc";
}

function goBack(){
    window.history.back(-1);
    return false;
}
function controlParam(result) {
    if ("approve" === result) {
        $("#remark1").attr("disabled", "disabled");
        $("#remark2").attr("disabled", "disabled");
        $("#remark1").val("");
        $("#remark2").val("");
        $("#rentTime").removeAttr("disabled")
    }
    if ("reject" === result) {
        $("#rentTime").attr("disabled", "disabled");
        $("#remark2").attr("disabled", "disabled");
        $("#rentTime").val("");
        $("#remark2").val("");
        $("#remark1").removeAttr("disabled")
    }
    if ("replenish" === result) {
        $("#rentTime").attr("disabled", "disabled");
        $("#remark1").attr("disabled", "disabled");
        $("#rentTime").val("");
        $("#remark1").val("");
        $("#remark2").removeAttr("disabled")
    }
}

function commitAudit(){
    var result = $("input[name='result']:checked").val();
    var rentTime = $("#rentTime").val();
    var remark1 = $("#remark1").val();
    var remark2 = $("#remark2").val();
    var businessId = $("#businessId").val();
    var remark;
    if("approve" === result){
        if(rentTime == null || "" === rentTime){
            alert("请填写起租日期");
            return
        }
    }
    if("reject" === result){
        if(null == remark1 || "" === $.trim(remark1)){
            alert("请填写拒租原因");
            return
        }
        remark = remark1;
    }
    if("replenish" === result){
        if(null == remark2 || "" === $.trim(remark2)){
            alert("请填写补充说明");
            return
        }
        remark = remark2;
    }
    $.ajax({
        url:getContextPath()+"/risk/commitAuditInfo",
        type:'post',
        dataType:'json',
        data:{result:result,rentTime:rentTime,remark:remark,businessId:businessId},
        success:function(data){
            if("1" == data){
                alert("提交成功");
            }else{
                alert("提交失败");
            }
            window.location.href=getContextPath()+"/proxy/to?page=/nh-micro-jsp/risk-manager/listRiskManageInfo.jsp";
        },
        error:function(data){
        }
    });

}

function showRemark(val){
    var table = $("#table_list1");
    if(val === "reject"){
        $("#remarkTD").html("拒贷原因：<textarea placeholder=\"text文本1000字符以内\" name=\" \" id=\"\" style=\"width: 100%\"></textarea>")
    }
    if(val === "survey" || val === "replenish"){
        $("#remarkTD").html("备注：<textarea placeholder=\"text文本1000字符以内\" name=\" \" id=\"\" style=\"width: 100%\"></textarea>")
    }
    if(val === "back"){
        $("#remarkTD").html("退回原因：<textarea placeholder=\"text文本1000字符以内\" name=\" \" id=\"\" style=\"width: 100%\"></textarea>")
    }
    if(val === "approve"){
        $("#remarkTD").html("")
    }
}

function initDetailInfo() {
    var businessId = $("#businessId").val();
    $.ajax({
        async : false,
        cache : false,
        type : 'POST',
        dataType : "json",
        url:getContextPath()+"/risk/queryAuditDetail",
        data:{businessId:businessId},
        success : function(data) {
            var html = "";
            if(data.length === 0){
                $("#noneImg").attr("src","/nh-micro-js/images/none.png").css("display","block");
            }
            for (var i = 0; i < data.length; i++) {
                var head = "<div class=\"panel panel-primary\"><div class=\"panel-heading\">审核详情</div>" +
                    "<div class=\"panel-body\"><div class=\"form-group\">" +
                    "<label  class=\"col-sm-1 control-label\">审批结果</label>";
                var detail= "";
                var result = data[i].auditResult;
                if("approve" === result){
                    detail = "<div class=\"col-sm-2\">" +
                        "<label class=\"control-label\">审批通过</label>" +
                        "</div>" +
                        "<label  class=\"col-sm-1 control-label\">起租时间</label>" +
                        "<div class=\"col-sm-2\">" +
                        "<label class=\"control-label\">"+data[i].rentTime+"</label>" +
                        "</div>" +
                        "<label  class=\"col-sm-1 control-label\">审核时间</label>" +
                        "<div class=\"col-sm-2\">" +
                        "<label class=\"control-label\">"+data[i].auditTime+"</label>" +
                        "</div>"+
                        "</div></div></div>"

                }
                if("reject" === result){
                    detail = "<div class=\"col-sm-2\"><label class=\"control-label\">拒租</label></div>" +
                        "<label  class=\"col-sm-1 control-label\">审核时间</label>" +
                        "<div class=\"col-sm-2\">" +
                        "<label class=\"control-label\">"+data[i].auditTime+"</label>" +
                        "</div>" +
                        "</div>" +
                        "<div class=\"form-group\">" +
                        "<label  class=\"col-sm-1 control-label\">拒租原因</label>" +
                        "<div class=\"col-sm-11\"><label class=\"control-label\">"+data[i].remark+"</label>" +
                        "</div></div></div></div></div>";
                }
                if("replenish" === result){
                    detail = "<div class=\"col-sm-2\"><label class=\"control-label\">补充资料</label></div>" +
                        "<label  class=\"col-sm-1 control-label\">审核时间</label>" +
                        "<div class=\"col-sm-2\">" +
                        "<label class=\"control-label\">"+data[i].auditTime+"</label>" +
                        "</div>" +
                        "</div>" +
                        "<div class=\"form-group\">" +
                        "<label  class=\"col-sm-1 control-label\">补充说明</label>" +
                        "<div class=\"col-sm-11\"><label class=\"control-label\">"+data[i].remark+"</label>" +
                        "</div></div></div></div></div>";
                }
                html += head+detail;
            }
            html +="<div class=\"form-group\" align=\"center\">" +
                "        <input type=\"button\" class=\"btn btn-danger\" value=\"关闭\" onclick=\"window.history.go(-1)\" name=\"button\">" +
                "    </div>";
            $("#bodyTemp").append(html);
        },
        error : function() {
        }
    });
}