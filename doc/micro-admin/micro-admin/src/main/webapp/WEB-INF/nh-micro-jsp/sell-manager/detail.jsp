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
        .control-label{
            text-align:left;
        }
    </style>
</head>
<body class="gray-bg">
<%@ include file="/WEB-INF/nh-micro-jsp/tab.jsp" %>
<form class="form-horizontal" role="form" id="create">
    <input type="hidden" name="id" value="${obj.id}">
    <input type="hidden" id="optType" value="${optType}">
    <div class="panel panel-primary">
        <div class="panel-heading">
            客户基本信息
        </div>
        <div class="panel-body">
            <div class="form-group">
                <label  class="col-sm-1 control-label">客户来源</label>
                <div class="col-sm-2">
                    <label class="control-label">
                    <c:if test="${obj.source==1}">机构推荐</c:if>
                    <c:if test="${obj.source==2}">熟人介绍</c:if>
                    <c:if test="${obj.source==3}">地推</c:if>
                    <c:if test="${obj.source==4}">网推</c:if>
                    <c:if test="${obj.source==5}">其他</c:if>
                    </label>
                </div>
                <c:if test="${obj.source==5}">
                <label  class="col-sm-2 control-label">其他来源</label>
                <div class="col-sm-3">
                    <label class="control-label" style="text-align:left;">${obj.other_source}</label>
                </div>
                </c:if>
            </div>
            <div class="form-group">
                <label  class="col-sm-1 control-label">客户姓名</label>
                <div class="col-sm-2">
                    <label class="control-label">${obj.cust_name}</label>
                </div>
                <label  class="col-sm-1 control-label">性别</label>
                <div class="col-sm-2">
                    <label class="control-label">
                        <c:if test="${obj.cust_sex==1}">男</c:if>
                        <c:if test="${obj.cust_sex==2}">女</c:if>
                    </label>
                </div>
                <label  class="col-sm-1 control-label">证件类型</label>
                <div class="col-sm-2">
                    <label class="control-label">
                        <c:if test="${obj.paper_type==1}">身份证</c:if>
                        <c:if test="${obj.paper_type==2}">军官证</c:if>
                        <c:if test="${obj.paper_type==3}">护照</c:if>
                    </label>
                </div>
                <label  class="col-sm-1 control-label">证件号码</label>
                <div class="col-sm-2">
                    <label class="control-label">${obj.cust_paper}</label>
                </div>
            </div>
            <div class="form-group">
                <label  class="col-sm-1 control-label">发证机关</label>
                <div class="col-sm-3">
                    <label class="control-label" style="text-align:left">${obj.paper_source}</label>
                </div>
                <label  class="col-sm-2 control-label">证件失效日期</label>
                <div class="col-sm-3">
                    <label class="control-label">${obj.paper_end_time == "9999-12-31" ?"长期":obj.paper_end_time}</label>
                </div>
            </div>
            <div class="form-group">
                <label  class="col-sm-1 control-label">婚姻状况</label>
                <div class="col-sm-2">
                    <label class="control-label">
                        <c:if test="${obj.marital==1}">已婚</c:if>
                        <c:if test="${obj.marital==2}">未婚</c:if>
                        <c:if test="${obj.marital==3}">离异</c:if>
                        <c:if test="${obj.marital==4}">丧偶</c:if>
                    </label>
                </div>
                <label  class="col-sm-1 control-label">通讯地址</label>
                <div class="col-sm-3">
                    <label class="control-label" style="text-align:left">${obj.cust_address}</label>
                </div>
            </div>
            <div class="form-group">
                <label  class="col-sm-1 control-label">移动电话</label>
                <div class="col-sm-2">
                    <label class="control-label">${obj.mobile}</label>
                </div>
                <label  class="col-sm-2 control-label">电子邮箱</label>
                <div class="col-sm-3">
                    <label class="control-label">${obj.email}</label>
                </div>
                <label  class="col-sm-1 control-label">微信号</label>
                <div class="col-sm-2">
                    <label class="control-label">${obj.wechat}</label>
                </div>
            </div>
            <div class="form-group">
                <label  class="col-sm-1 control-label">客户备注</label>
                <div class="col-sm-8">
                    <label class="control-label" style="text-align:left">${obj.remark}</label>
                </div>
            </div>
        </div>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            紧急联系人信息
        </div>
        <div class="panel-body">
            <div class="form-group">
                <label  class="col-sm-2 control-label">紧急联系人1&nbsp;&nbsp;姓名</label>
                <div class="col-sm-2">
                    <label class="control-label">${personList[0].name}</label>
                </div>
                <label  class="col-sm-2 control-label">与本人关系</label>
                <div class="col-sm-2">
                    <label class="control-label">${personList[0].relation}</label>
                </div>
                <label  class="col-sm-1 control-label">联系方式</label>
                <div class="col-sm-2">
                    <label class="control-label">${personList[0].mobile}</label>
                </div>
            </div>
            <c:if test="${ not empty personList[1]}">
            <div class="form-group">
                <label  class="col-sm-2 control-label">紧急联系人2&nbsp;&nbsp;姓名</label>
                <div class="col-sm-2">
                    <label class="control-label">${personList[1].name}</label>
                </div>
                <label  class="col-sm-2 control-label">与本人关系</label>
                <div class="col-sm-2">
                    <label class="control-label">${personList[1].relation}</label>
                </div>
                <label  class="col-sm-1 control-label">联系方式</label>
                <div class="col-sm-2">
                    <label class="control-label">${personList[1].mobile}</label>
                </div>
            </div>
            </c:if>

        </div>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            合伙人信息
        </div>
        <div class="panel-body">
            <div class="form-group">
                <label  class="col-sm-2 control-label">是否有合伙人</label>
                <div class="col-sm-10">
                    <label class="control-label">
                        <c:if test="${obj.partner==2}">无</c:if>
                        <c:if test="${obj.partner==1}">有</c:if>
                    </label>
                    <c:if test="${obj.partner==1}">
                        <label class="control-label">${obj.partner_nums}个</label>
                        <label class="control-label">&nbsp;&nbsp;分别占股${obj.shares[0]}%&nbsp;&nbsp;${obj.shares[1]}%</label>
                    </c:if>
                </div>
            </div>
            <c:if test="${obj.partner==1}">
            <div class="from-group">
                <label class="col-sm-2 control-label">租房是否征得合伙人同意</label>
                <div class="col-sm-3">
                    <label class="control-label">
                        <c:if test="${obj.partner_advice==1}">同意</c:if>
                        <c:if test="${obj.partner_advice==2}">不同意</c:if>
                        <c:if test="${obj.partner_advice==3}">未征求</c:if>
                    </label>
                </div>
            </div>
            </c:if>
        </div>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            租房信息
        </div>
        <div class="panel-body">
            <div class="form-group">
                <label class="col-sm-1 control-label">业务类型</label>
                <div class="col-sm-2">
                    <label class="control-label">
                        <c:if test="${obj.business_type==1}">开设新店</c:if>
                        <c:if test="${obj.business_type==2}">老店续租</c:if>
                    </label>
                </div>
                <label class="col-sm-2 control-label">租赁商铺</label>
                <div class="col-sm-2">
                    <label class="control-label">${obj.shop_name}</label>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">房租期限</label>
                <div class="col-sm-2">
                    <label class="control-label">${obj.rent_period}个月</label>
                </div>
                <label class="col-sm-2 control-label">预计起租时间</label>
                <div class="col-sm-3">
                    <label class="control-label">${obj.rent_start_time}</label>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">房租金额</label>
                <div class="col-sm-2">
                    <label class="control-label">人民币： ${obj.rent_amount}元/月</label>
                </div>
                <label class="col-sm-2 control-label">房租总金额</label>
                <div class="col-sm-2">
                    <label class="control-label"> 人民币：${obj.rent_amount * obj.rent_period}元</label>
                </div>
                <label class="col-sm-2 control-label">押金金额</label>
                <div class="col-sm-2">
                    <label class="control-label">人民币：${obj.deposit_amount}元</label>
                </div>
            </div>
        </div>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            商铺信息
        </div>
        <div class="panel-body">
            <div class="form-group">
                <label class="col-sm-2 control-label">商铺是否有公户</label>
                <div class="col-sm-2">
                    <label class="control-label">
                        <c:if test="${obj.is_pub_account==2}">无</c:if>
                        <c:if test="${obj.is_pub_account==1}">有</c:if>
                    </label>
                </div>
                <c:if test="${obj.is_pub_account==1}">
                    <label class="col-sm-1 control-label">公户账户</label>
                    <div class="col-sm-2">
                        <label class="control-label">${obj.pub_account}</label>
                    </div>
                    <label class="col-sm-1 control-label">开户行</label>
                    <div class="col-sm-3">
                        <label class="control-label" style="text-align:left;">${obj.bank_account}</label>
                    </div>
                </c:if>

            </div>
        </div>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            银行卡信息
        </div>
        <div class="panel-body">
            <div class="form-group">
                <label  class="col-sm-2 control-label">房租缴纳银行卡号1</label>
                <div class="col-sm-2">
                    <label class="control-label">${cardList[0].numbers}</label>
                </div>
                <label  class="col-sm-2 control-label">房租缴纳银行卡号2</label>
                <div class="col-sm-2">
                    <label class="control-label">${cardList[1].numbers}</label>
                </div>
            </div>
        </div>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            其他经营信息
        </div>
        <div class="panel-body">
            <div class="form-group">
                <label  class="col-sm-2 control-label">其他经营项目</label>
                <div class="col-sm-8">
                    <label class="control-label" style="text-align:left">${obj.other_item}</label>
                </div>
            </div>
            <div class="form-group">
                <label  class="col-sm-2 control-label">加盟品牌</label>
                <div class="col-sm-1">
                    <label class="control-label">
                        <c:if test="${obj.brand==2}">无</c:if>
                        <c:if test="${obj.brand==1}">有</c:if>
                    </label>
                </div>
                <c:if test="${obj.brand==1}">
                    <label  class="col-sm-2 control-label">品牌名称</label>
                    <div class="col-sm-2">
                        <label class="control-label" style="text-align:left">${obj.brand_name}</label>
                    </div>
                </c:if>

            </div>
        </div>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            影像信息
        </div>
        <div class="panel-body">
            <div class="form-group">
                <label  class="col-sm-2 control-label" >影像信息上传</label>
            </div>
            <c:forEach items="${fileList}" var="f">
                <div class="form-group">
                    <a  class=" btn btn-primary" href="${f.down_path}?filename=${f.file_name}">下载文件</a>
                    <label  class="col-sm-2 control-label" >附件名称：${f.file_name}</label>
                    <label  class="col-sm-3 control-label" >上传时间：${f.create_time}</label>
                </div>
                <div class="form-group">
                    <label  class="col-sm-2 control-label" >说明</label>
                    <div class="col-sm-8">
                        <textarea class="form-control" style="text-align:left">${f.remark}</textarea>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <%@ include file="include_quality_list.jsp" %>
    <div class="form-group" align="center">
        <input type="button" class="btn btn-danger" value="返回" onclick="window.history.go(-1)" name="button">
    </div>

</form>
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/laydate/laydate.js"></script>
<script src="<%=path%>/nh-micro-js/js/system/quality.js"></script>
</body>

</html>


<script>







</script>