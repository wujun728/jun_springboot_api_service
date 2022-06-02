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
	<title>新增进件</title>
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
	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/validate/additional-methods.min.js"></script>
	<script src="<%=path%>/nh-micro-js/js/region.js"></script>
	<style>
		.red{
			color: red;
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
				<label for="source" class="col-sm-1 control-label">客户来源<span class="red">*</span></label>
				<div class="col-sm-2">
                   <select id = "source" class="form-control" name="source">
					   <option value="">请选择</option>
					   <option value="1" ${obj.source==1?"selected":""}>机构推荐</option>
					   <option value="2" ${obj.source==2?"selected":""}>熟人介绍</option>
					   <option value="3" ${obj.source==3?"selected":""}>地推</option>
					   <option value="4" ${obj.source==4?"selected":""}>网推</option>
					   <option value="5" ${obj.source==5?"selected":""}>其他</option>
				   </select>
				</div>
				<label  class="col-sm-2 control-label">其他来源</label>
				<div class="col-sm-3">
					<input type="text" class="form-control" id="other_source" name="other_source"  placeholder="text文本50字符" value="${obj.other_source}"
						    readonly="readonly">
				</div>
			</div>
			<div class="form-group">
				<label  class="col-sm-1 control-label">客户姓名<span class="red">*</span></label>
				<div class="col-sm-2">
					<input type="text" class="form-control" id="cust_name" name="cust_name" placeholder="text文本50字符" value="${obj.cust_name}">
				</div>
				<label for="sex" class="col-sm-1 control-label">性别<span class="red">*</span></label>
				<div class="col-sm-2">
					<input type="radio" name="cust_sex" id="sex" value="1" ${obj.cust_sex==1?"checked":""}> 男
					<input type="radio" name="cust_sex"  value="2" ${obj.cust_sex==2?"checked":""}> 女
				</div>
				<label for="paperType" class="col-sm-1 control-label">证件类型<span class="red">*</span></label>
				<div class="col-sm-2">
					<select id = "paperType" class="form-control" name="paper_type">
						<option value="1" ${obj.paper_type==1?"selected":""}>身份证</option>
						<option value="2" ${obj.paper_type==2?"selected":""}>军官证</option>
						<option value="3" ${obj.paper_type==3?"selected":""}>护照</option>
					</select>
				</div>
				<label  class="col-sm-1 control-label">证件号码<span class="red">*</span></label>
				<div class="col-sm-2">
					<input type="text" class="form-control" id="cust_paper" placeholder="text文本50字符" name="cust_paper" value="${obj.cust_paper}" card="true">
				</div>
			</div>
			<div class="form-group">
				<label for="paperSource" class="col-sm-1 control-label">发证机关<span class="red">*</span></label>
				<div class="col-sm-3">
					<input type="text" class="form-control" id="paperSource" placeholder="text文本100字符" name="paper_source" value="${obj.paper_source}">
				</div>
				<label  class="col-sm-2 control-label">证件失效日期<span class="red">*</span></label>
				<div class="col-sm-2">
					<select id="change" class="form-control">
						<option value="1">请选择日期</option>
						<option value="2">长期</option>
					</select>
				</div>
				<div class="col-sm-2" id="timeWindow" style="display: block">
					<input type="text" class="form-control layer-date" id="endTime" placeholder="请选择" name="paper_end_time" value="${obj.paper_end_time}">
				</div>
			</div>
			<div class="form-group">
				<label for="marital" class="col-sm-1 control-label">婚姻状况<span class="red">*</span></label>
				<div class="col-sm-2">
					<select id = "marital" class="form-control" name="marital">
						<option value="1" ${obj.marital==1?"selected":""}>已婚</option>
						<option value="2" ${obj.marital==2?"selected":""}>未婚</option>
						<option value="3" ${obj.marital==3?"selected":""}>离异</option>
						<option value="4" ${obj.marital==4?"selected":""}>丧偶</option>
					</select>
				</div>
				<label for="custAddress" class="col-sm-1 control-label">通讯地址<span class="red">*</span></label>
				<div class="col-sm-6">
					<input type="text" class="form-control" id="custAddress" placeholder="text文本100字符" name="cust_address" value="${obj.cust_address}">
				</div>
			</div>
			<div class="form-group">
				<label for="mobile" class="col-sm-1 control-label">移动电话<span class="red">*</span></label>
				<div class="col-sm-2">
					<input type="text" class="form-control" id="mobile" placeholder="数字50字符" name="mobile" value="${obj.mobile}">
				</div>
				<label for="email" class="col-sm-1 control-label">电子邮箱</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="email" placeholder="text文本50字符" name="email" value="${obj.email}">
				</div>
				<label for="wechat" class="col-sm-1 control-label">微信号</label>
				<div class="col-sm-2">
					<input type="text" class="form-control" id="wechat" placeholder="text文本50字符" name="wechat" value="${obj.wechat}">
				</div>
			</div>
			<div class="form-group">
				<label for="remark" class="col-sm-1 control-label">客户备注</label>
				<div class="col-sm-8">
					<textarea class="form-control"  id = "remark" name="remark" placeholder="text文本500字符">${obj.remark}</textarea>
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
				<label  class="col-sm-2 control-label">紧急联系人1&nbsp;&nbsp;姓名<span class="red">*</span></label>
				<div class="col-sm-2">
					<input type="text" class="form-control" placeholder="text文本50字符" name="ename" value="${personList[0].name}" required>
				</div>
				<label  class="col-sm-2 control-label">与本人关系<span class="red">*</span></label>
				<div class="col-sm-2">
					<input type="text" class="form-control" placeholder="text文本50字符" name="erelation" value="${personList[0].relation}" required>
				</div>
				<label  class="col-sm-1 control-label">联系方式<span class="red">*</span></label>
				<div class="col-sm-2">
					<input type="text" class="form-control" placeholder="数字50字符" name="emobile" value="${personList[0].mobile}" required>
				</div>
			</div>
			<div class="form-group">
				<label  class="col-sm-2 control-label">紧急联系人2&nbsp;&nbsp;姓名</label>
				<div class="col-sm-2">
					<input type="text" class="form-control" placeholder="text文本50字符" name="ename" value="${personList[1].name}">
				</div>
				<label  class="col-sm-2 control-label">与本人关系</label>
				<div class="col-sm-2">
					<input type="text" class="form-control" placeholder="text文本50字符" name="erelation" value="${personList[1].relation}">
				</div>
				<label  class="col-sm-1 control-label">联系方式</label>
				<div class="col-sm-2">
					<input type="text" class="form-control" placeholder="数字50字符" name="emobile" value="${personList[1].mobile}">
				</div>
			</div>

		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading">
          合伙人信息
		</div>
		<div class="panel-body">
			<div class="form-group">
				<label  class="col-sm-2 control-label">是否有合伙人<span class="red">*</span></label>
				<div class="col-sm-10">
					<span>
					<input type="radio" name="partner"  value="2" ${obj.partner==2?"checked":""} required> 无
					<input type="radio" name="partner"  value="1" ${obj.partner==1?"checked":""} > 有
					</span>
					<input type="text" style="width: 50px" name="partner_nums" value="${obj.partner_nums}" id="partner_nums" readonly="readonly">个
					分别占股<input type="text" style="width: 50px" name="share" value="${obj.shares[0]}" readonly="readonly">%
					<input type="text" style="width: 50px" name="share" value="${obj.shares[1]}" readonly="readonly">%

				</div>
			</div>

			<div class="from-group">
				<label class="col-sm-2 control-label">租房是否征得合伙人同意</label>
				<div class="col-sm-3">
					<input type="radio" name="partner_advice" value="1" ${obj.partner_advice==1?"checked":""}> 同意
					<input type="radio" name="partner_advice" value="2" ${obj.partner_advice==2?"checked":""}> 不同意
					<input type="radio" name="partner_advice" value="3" ${obj.partner_advice==3?"checked":""}> 未征求
				</div>
			</div>

		</div>
	</div>
	  <div class="panel panel-primary">
		  <div class="panel-heading">
			  租房信息
		  </div>
		  <div class="panel-body">
			  <div class="form-group">
				  <label class="col-sm-2 control-label">业务类型<span class="red">*</span></label>
				  <div class="col-sm-2">
					  <input type="radio" name="business_type" value="1" ${obj.business_type==1?"checked":""} required>开设新店&nbsp;&nbsp;
					  <input type="radio" name="business_type" value="2" ${obj.business_type==2?"checked":""}>老店续租
				  </div>
			  </div>
			  <div class="form-group">
				  <label class="col-sm-2 control-label">租赁商铺<span class="red">*</span></label>
				  <div class="col-sm-2">
					  <select id="s_province"  class="form-control" type="text" name="province"
							  aria-required="true" aria-invalid="true" class="error">
						  <option value="">请选择</option>
					  </select>
				  </div>
				  <div class="col-sm-2">
					  <select id="s_city"  class="form-control" type="text" name="city"
							  aria-required="true" aria-invalid="true" class="error">
						  <option value="">请选择</option>
					  </select>
				  </div>
				  <div class="col-sm-2">
					  <select id="s_county"  class="form-control" type="text" name="county"
							  aria-required="true" aria-invalid="true" class="error">
						  <option value="">请选择</option>
					  </select>
				  </div>
				  <div class="col-sm-2">
					  <select id="s_shop_id" name="shop_id" class="form-control" type="text"
							  aria-required="true" aria-invalid="true" class="error" required>
						  <option value="">请选择</option>
					  </select>
					  <input type="hidden" id="shop_name" name="shop_name" value="${obj.shop_name}">
				  </div>
			  </div>
			  <div class="form-group">
				  <label class="col-sm-2 control-label">房租期限<span class="red">*</span></label>
				  <div class="col-sm-2">
					  <select class="form-control" name="rent_period" id="rent_period">
						  <option value="3" ${obj.rent_period==3?"selected":""}>3个月</option>
						  <option value="4" ${obj.rent_period==4?"selected":""}>4个月</option>
						  <option value="6" ${obj.rent_period==6?"selected":""}>6个月</option>
						  <option value="12" ${obj.rent_period==12?"selected":""}>12个月</option>
					  </select>
				  </div>
				  <label class="col-sm-2 control-label">预计起租时间<span class="red">*</span></label>
				  <div class="col-sm-3">
					  <input id="rent_start_time" class="form-control layer-date" type="text"  name="rent_start_time" value="${obj.rent_start_time  }" placeholder="请选择起租时间">
				  </div>
			  </div>
			  <div class="form-group">
				  <label class="col-sm-2 control-label">房租金额（元/月）人民币<span class="red">*</span></label>
				  <div class="col-sm-2">
					  <input class="form-control" id="rent_amount" type="text" placeholder="数字50字符" name="rent_amount"   value="${obj.rent_amount  }" >
				  </div>
				  <label class="col-sm-2 control-label">房租总金额（元）</label>
				  <div class="col-sm-2">
					  <label class="control-label">人民币：<span id="totalAmount">${obj.rent_period * obj.rent_amount}</span></label>
				  </div>
				  <label class="col-sm-2 control-label">押金金额（元）人民币<span class="red">*</span></label>
				  <div class="col-sm-2">
					  <input class="form-control" type="text" placeholder="数字50字符" name="deposit_amount"   value="${obj.deposit_amount }" >
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
				  <label class="col-sm-2 control-label">商铺是否有公户<span class="red">*</span></label>
				  <div class="col-sm-2">
					  <input type="radio" name="is_pub_account" value="1" ${obj.is_pub_account==1?"checked":""} required>有
					  <input type="radio" name="is_pub_account" value="2" ${obj.is_pub_account==2?"checked":""}>无
				  </div>
				  <label class="col-sm-1 control-label">公户账户</label>
				  <div class="col-sm-2">
					  <input type="text" class="form-control" placeholder="数字50字符" name="pub_account" value="${obj.pub_account}" readonly="readonly" >
				  </div>
				  <label class="col-sm-1 control-label">开户行</label>
				  <div class="col-sm-4">
					  <input type="text" class="form-control" placeholder="text文本100字符" name="bank_account" value="${obj.bank_account}" readonly="readonly" >
				  </div>
			  </div>
		  </div>
	  </div>
	  <div class="panel panel-primary">
		  <div class="panel-heading">
			  银行卡信息
		  </div>
		  <div class="panel-body">
			  <div class="form-group">
				  <label  class="col-sm-2 control-label">房租缴纳银行卡号1<span class="red">*</span></label>
				  <div class="col-sm-2">
					  <input type="text" class="form-control" placeholder="数字50字符" name="bankNum" value="${cardList[0].numbers}" required>
				  </div>
				  <label  class="col-sm-2 control-label">房租缴纳银行卡号2</label>
				  <div class="col-sm-2">
					  <input type="text" class="form-control" placeholder="数字50字符" name="bankNum" value="${cardList[1].numbers}" >
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
					  <textarea class="form-control" name="other_item" placeholder="text文本500字符">${obj.other_item}</textarea>
				  </div>
			  </div>
			  <div class="form-group">
				  <label  class="col-sm-2 control-label">加盟品牌</label>
				  <div class="col-sm-1">
					  <input type="radio" name="brand" value="2" ${obj.brand==2?"checked":""}>无
					  <input type="radio" name="brand" value="1" ${obj.brand==1?"checked":""}>有
				  </div>
				  <label  class="col-sm-2 control-label">品牌名称</label>
				  <div class="col-sm-2">
					  <input type="text" name="brand_name" class="form-control" placeholder="text文本50字符" value="${obj.brand_name}" readonly="readonly">
				  </div>
			  </div>
		  </div>
	  </div>
      <div class="panel panel-primary" >
		  <div class="panel-heading">
			  影像信息
		  </div>
		  <div class="panel-body" id="html">
              <div class="form-group">
				  <label  class="col-sm-3 control-label" >影像信息上传(不能大于50M)</label>

				  <div class="col-sm-2">
					  <input  class="control" type="file" id="upfile"/>
				  </div>
				  <div class="col-sm-1">
					  <input  class="control" type="button" value="上传" onclick="upload()" />
				  </div>
				  <div class="col-sm-2">
					  <span>可多次上传</span>
				  </div>
			  </div>
			  <div class="form-group">
				  <label  class="col-sm-2 control-label" >说明</label>
				  <div class="col-sm-8">
					  <textarea class="form-control" id="file_remark" placeholder="text文本500字符" name="file_remark" maxlength="500"></textarea>
				  </div>
			  </div>

			  <c:forEach items="${fileList}" var="f">
				  <div style="display: block;" id="${f.id}">
			  <div class="form-group">

				  <a  class=" btn btn-primary" href="${f.down_path}?filename=${f.file_name}" >下载文件</a>
				  <label  class="col-sm-3 control-label" style="text-align:left">附件名称：${f.file_name}</label>
				  <label  class="col-sm-3 control-label" >上传时间：${f.create_time}</label>

				  <a  class=" btn btn-warning" onclick="delFile('${f.id}')">删除</a>
			  </div>
			  <div class="form-group">
				  <label  class="col-sm-2 control-label" >说明</label>
				  <div class="col-sm-8">
				      <textarea class="form-control" readonly>${f.remark}</textarea>
				  </div>
			  </div>
				  </div>
			  </c:forEach>
		  </div>
	  </div>
	  <input type="hidden" id="fileIds" name="fileIds">
	  <div class="form-group" align="center">
		  <input type="button" class="btn btn-danger" value="取消" onclick="window.history.go(-1)">
		  <input type="submit" class="btn btn-warning" value="暂存" name="submit_name">
		  <input type="submit" class="btn btn-primary" value="提交" name="submit_name">
	  </div>

  </form>
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/laydate/laydate.js"></script>
<script src="<%=path%>/nh-micro-js/js/system/sell-manage.js"></script>
</body>

</html>


<script>
    laydate({
        elem: '#endTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
        event: 'focus', //响应事件。如果没有传入event，则按照默认的click
		type: 'datetime'
    });
    laydate({
        elem: '#rent_start_time', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
        event: 'focus', //响应事件。如果没有传入event，则按照默认的click
        type: 'datetime'
    });

    renderShop();
	//编辑时渲染店铺信息
	function renderShop(){
	    if($("#optType").val()=="edit"){
	    var html = "<option value="+'${obj.shop_id}'+">"+'${obj.shop_name}'+"</option>";
	    $("#s_shop_id").html(html)
            changeCity("0","s_province")
            changeCity("${obj.province}","s_city")
            changeCity("${obj.city}","s_county")
            $("#s_province").val("${obj.province}")
            $("#s_city").val("${obj.city}")
            $("#s_county").val("${obj.county}")
		}
	}

	$("#change").change(function(){
	    if(this.value == "1"){
            $("#endTime").val("")
	        $("#timeWindow").show()
		}else{
            $("#timeWindow").hide()
			$("#endTime").val("9999-12-31")
		}
	})

    if($("#optType").val()=="edit"){
	    if("${obj.paper_end_time}" == "9999-12-31"){
	        $("#change").val("2")
			$("#timeWindow").hide()
        }
        if("${obj.source}" == "5"){
            $("#other_source").attr("readOnly",false)
        }
        if("${obj.partner}" == "1"){
            $("#partner_nums").prop("readOnly",false)
            $("input[name='share']").prop("readOnly",false)
        }
        if("${obj.brand}" =="1"){
            $("[name='brand_name']").prop("readOnly",false);
        }
        if("${obj.is_pub_account}" == "1"){
            $("[name='pub_account']").prop("readOnly",false);
            $("[name='bank_account']").prop("readOnly",false);
            $("[name='pub_account']").attr("required",true);
            $("[name='bank_account']").attr("required",true);
        }
        if("${obj.paper_type}" == "1"){
            //$("#cust_paper").attr("card",true);
        }else if("${obj.paper_type}" == "2"){
            $("#cust_paper").removeAttr("card");
            $("#cust_paper").attr("jgcard",true);
        }else if("${obj.paper_type}" == "3"){
            $("#cust_paper").removeAttr("card");
            $("#cust_paper").attr("hzcard",true);
        }


    }

//处理新增tab问题
    if($("#optType").val()=="add"){
        $(".page-tabs").find("a[tabName='base']").addClass("active");// 操作高亮tab
        $(".page-tabs").find("a[tabName='base']").attr("href","javascript:;");// 操作高亮tab
        $(".page-tabs").find("a.disabled").attr("href","javascript:;");
	}






</script>