<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="keywords" content="">
<meta name="description" content="">
<style>
   	#wrapper{background-color: #f3f3f4;}
   	.content-tabs{display: block;}
   	.page-tabs a{font-size: 14px;border:1px solid #333; color:#000; margin:0 4px;border-radius:6px 6px 0 0}
   	.page-tabs a:hover{background:#none;}
   	.page-tabs a.disabled{background-color: rgba(204, 204, 204, 1);color:#fff}
   	.page-tabs a.active,.page-tabs a.disabled{border:none}
   	.tabCon{width:100%;font-size: 14px; padding:20px 100px}
   	.tabCon .con{display: none; height: 100%;}
   	.tabCon .con.show{display: block;}
   	.white{background:#fff}
   	.page-tabs a:hover, .content-tabs .roll-nav:hover{background:none;}
   	.page-tabs a.disabled:hover{background-color: rgba(204, 204, 204, 1);color:#fff}
   	.page-tabs a.active{color:#fff;background-color: rgba(22, 155, 213, 1);}
   	.page-tabs a.active:hover, .page-tabs a.active i:hover{background-color: rgba(22, 155, 213, 1);}
   	.btn-rounded{background:#fff}
</style>
<script>
$(document).ready(function() {
	var tabs = $(".page-tabs");
	var businessId = "${param.businessId}";	// 当前业务id
	var active = "${param.active}";			// 当前页面激活类型
	var status = "${param.status}";			// 业务状态
	var order = [							// 规则数据
		{
			status:"110",			// 暂存
			enabled:"base"
		},{
			status:"120",			// 放弃
			enabled:"base"
		},{
			status:"130",			// 等待质检
			enabled:"base"
		},{
			status:"140",			// 质检退回
			enabled:"base"
		},{
			status:"150",			// 租赁审核中
			enabled:"base,audit"
		},{
			status:"160",			// 拒租
			enabled:"base,audit"
		},{
			status:"170",			// 补充资料
			enabled:"base,audit"
		},{
			status:"180",			// 合同签约
			enabled:"base,audit,contract"
		},{
			status:"190",			// 合同签约后审核
			enabled:"base,audit,contract"
		},{
			status:"200",			// 重新签约
			enabled:"base,audit,contract"
		},{
			status:"210",			// 待交租
			enabled:"base,audit,contract,rental"
		},{
			status:"220",			// 交租待审核
			enabled:"base,audit,contract,rental"
		},{
			status:"230",			// 交租审核失败
			enabled:"base,audit,contract,rental"
		},{
			status:"240",			// 交租待确认
			enabled:"base,audit,contract,rental"
		},{
			status:"250",			// 已交租
			enabled:"base,audit,contract,rental"
		},{
			status:"260",			// 已逾期
			enabled:"base,audit,contract,rental"
		},{
			status:"270",			// 租金缴清
			enabled:"base,audit,contract,rental"
		},{
			status:"280",			// 已结清
			enabled:"base,audit,contract,rental"
		}
	];
	
	// 根据传入状态匹配对应开启tab集合
	for(index in order){
		if(status == order[index].status){
			init(order[index].enabled);	// 初始化tabs
			break;
		}
	}
	
	
	function init(enabled){
		tabs.find("a[tabName='"+active+"']").addClass("active");// 操作高亮tab
		var enableds = enabled.split(",");
		for(enIndex in enableds){	// 开启有效tab
			tabs.find("a[tabName='"+enableds[enIndex]+"']").removeClass("disabled");
		}
		tabs.find("a.disabled").attr("href","javascript:;");	// 去除未启用tab url地址
	}
	
	/* tabs.bind("click", function() {
		var $this = $(this);
		var index = $this.index();
		if (!$this.hasClass("disabled")) {
			$this.addClass("active").siblings().removeClass("active");
			$this.addClass("active").siblings().removeClass("active");
		}
	}); */
});
</script>
</head>
<body>
	<div id="wrapper" style="padding-top: 10px;">
		<div class="row content-tabs" style="background-color: #f3f3f4;">
			<nav class="page-tabs">
				<div class="page-tabs-content">
					<a href="<%=path%>/micromvc/busi/detail?businessId=${param.businessId}&active=base&status=${param.status}&type=${param.type}" class="btn-rounded disabled" tabName="base">基础信息</a>
					<a href="<%=path%>/micromvc/risk/gotoAuditDetailPage?businessId=${param.businessId}&active=audit&status=${param.status}&type=${param.type}" class="btn-rounded disabled" tabName="audit">审核信息</a>
					<a href="<%=path%>/micromvc/contract/queryContract?businessId=${param.businessId}&active=contract&status=${param.status}&type=${param.type}" class="btn-rounded disabled" tabName="contract">合同信息</a>
					<a href="<%=path%>/micromvc/customerService/gotoRentPage?businessId=${param.businessId}&active=rental&status=${param.status}&type=${param.type}" class="btn-rounded disabled" tabName="rental">交租信息</a>
				</div>
			</nav>
		</div>
	</div>
</body>
</html>