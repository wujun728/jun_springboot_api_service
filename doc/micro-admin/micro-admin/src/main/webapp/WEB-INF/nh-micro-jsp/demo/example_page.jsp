<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>示例页面</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/font-awesome.min.css?v=4.3.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/style.css?v=3.2.0" rel="stylesheet">
	<link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/jqgrid/ui.jqgrid.css?0820" rel="stylesheet">
    <style>
        .mask_example{
            background-color: #666;
            position: fixed;
            z-index: 9999;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            opacity: 0.5;
            display: none;
        }
        .mask_example .loading{
            position: fixed;
            left: 50%;
            top: 50%;
        }
    </style>
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row"> 
        	<div class="col-sm-12">
				<!--查询条件-->
                <div class="ibox float-e-margins">
                    <div class="ibox-title bt_min_h40">
                        <h5>条件查询</h5>
                    </div>
                    <div class="ibox-content clearfix">
                        <form role="form" class="form-inline">
                        	<!--活动名称和活动时间-->
                            <div class="form-group">
                                  <label class="font-noraml">查询名称:</label>
                                  <input id="userName" class="form-control">
                            </div>
							<div class="form-group">
								<label class="font-noraml" style="width:110px;">查询时间:</label>
								<div class="input-daterange input-group">
									<input type="text" id="activityStart" class="laydate-icon form-control layer-date"  value="">
									<span class="input-group-addon bt_border0">—</span>
									<input type="text" id="activityEnd" class="laydate-icon form-control layer-date"  value="">
								</div>
							</div>
                        	<!--还款日期-->
                            <div class="form-group">
								  <label class="font-noraml">查询状态:</label>
								  <select id="repaymentStatus" name="" class="form-control" style="width:157px;">
									   <option list="0">状态一</option>
									   <option list="1">状态二</option>
                                       <option list="2">状态三</option>
								 </select>
							</div>
							<button type="button" class="btn btn-primary" id="search">查询</button>
							  
					
						</form>
                    </div>
                </div>
				<div class="ibox-content clearPadding">
                    <!--活动数据表格-->
                    <div class="jqGrid_wrapper">
                            <table id="table_list_1"></table>
                            <div id="pager_list_1"></div>
                    </div>
                </div>
				
            </div>
        </div>     
    </div>
    <div class="mask_example">
        <div class="sk-spinner sk-spinner-wave loading">
            <div class="sk-rect1"></div>
            <div class="sk-rect2"></div>
            <div class="sk-rect3"></div>
            <div class="sk-rect4"></div>
            <div class="sk-rect5"></div>
        </div>
    </div>
    <!-- 全局js -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/jquery-2.1.1.min.js"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/bootstrap.min.js?v=3.4.0"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/layer.min.js"></script>
    <!-- 自定义js -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/hplus.js?v=3.2.0"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/contabs.js"></script>
    <!-- 第三方插件 -->

	<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/laydate/laydate.js"></script>
     <!-- jqGrid -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/jqgrid/i18n/grid.locale-cn.js?0820"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/jqgrid/jquery.jqGrid.min.js?0820"></script>
    <!-- table -->
    <script>
        $(document).ready(function () {

             $.jgrid.defaults.styleUI = 'Bootstrap';
            // Examle data for jqGrid
            var mydata = [
                {
                    id: "1",
                    invdate: "2010-05-24",
                    name: "test",
                    note: "note",
                    tax: "10.00",
                    total: "2111.00"
                },
                {
                    id: "2",
                    invdate: "2010-05-25",
                    name: "test2",
                    note: "note2",
                    tax: "20.00",
                    total: "320.00"
                },
                {
                    id: "3",
                    invdate: "2007-09-01",
                    name: "test3",
                    note: "note3",
                    tax: "30.00",
                    total: "430.00"
                },
                {
                    id: "4",
                    invdate: "2007-10-04",
                    name: "test",
                    note: "note",
                    tax: "10.00",
                    total: "210.00"
                },
                {
                    id: "5",
                    invdate: "2007-10-05",
                    name: "test2",
                    note: "note2",
                    tax: "20.00",
                    total: "320.00"
                },
                {
                    id: "6",
                    invdate: "2007-09-06",
                    name: "test3",
                    note: "note3",
                    tax: "30.00",
                    total: "430.00"
                },
                {
                    id: "7",
                    invdate: "2007-10-04",
                    name: "test",
                    note: "note",
                    tax: "10.00",
                    total: "210.00"
                },
                {
                    id: "8",
                    invdate: "2007-10-03",
                    name: "test2",
                    note: "note2",
                    amount: "300.00",
                    tax: "21.00",
                    total: "320.00"
                },
                {
                    id: "9",
                    invdate: "2007-09-01",
                    name: "test3",
                    note: "note3",
                    amount: "400.00",
                    tax: "30.00",
                    total: "430.00"
                },
                {
                    id: "11",
                    invdate: "2007-10-01",
                    name: "test",
                    note: "note",
                    amount: "200.00",
                    tax: "10.00",
                    total: "210.00"
                },
                {
                    id: "12",
                    invdate: "2007-10-02",
                    name: "test2",
                    note: "note2",
                    amount: "300.00",
                    tax: "20.00",
                    total: "320.00"
                },
                {
                    id: "13",
                    invdate: "2007-09-01",
                    name: "test3",
                    note: "note3",
                    amount: "400.00",
                    tax: "30.00",
                    total: "430.00"
                },
                {
                    id: "14",
                    invdate: "2007-10-04",
                    name: "test",
                    note: "note",
                    amount: "200.00",
                    tax: "10.00",
                    total: "210.00"
                },
                {
                    id: "15",
                    invdate: "2007-10-05",
                    name: "test2",
                    note: "note2",
                    amount: "300.00",
                    tax: "20.00",
                    total: "320.00"
                },
                {
                    id: "16",
                    invdate: "2007-09-06",
                    name: "test3",
                    note: "note3",
                    amount: "400.00",
                    tax: "30.00",
                    total: "430.00"
                },
                {
                    id: "17",
                    invdate: "2007-10-04",
                    name: "test",
                    note: "note",
                    amount: "200.00",
                    tax: "10.00",
                    total: "210.00"
                },
                {
                    id: "18",
                    invdate: "2007-10-03",
                    name: "test2",
                    note: "note2",
                    amount: "300.00",
                    tax: "20.00",
                    total: "320.00"
                },
                {
                    id: "19",
                    invdate: "2007-09-01",
                    name: "test3",
                    note: "note3",
                    amount: "400.00",
                    tax: "30.00",
                    total: "430.00"
                },
                {
                    id: "21",
                    invdate: "2007-10-01",
                    name: "test",
                    note: "note",
                    amount: "200.00",
                    tax: "10.00",
                    total: "210.00"
                },
                {
                    id: "22",
                    invdate: "2007-10-02",
                    name: "test2",
                    note: "note2",
                    amount: "300.00",
                    tax: "20.00",
                    total: "320.00"
                },
                {
                    id: "23",
                    invdate: "2007-09-01",
                    name: "test3",
                    note: "note3",
                    amount: "400.00",
                    tax: "30.00",
                    total: "430.00"
                },
                {
                    id: "24",
                    invdate: "2007-10-04",
                    name: "test",
                    note: "note",
                    amount: "200.00",
                    tax: "10.00",
                    total: "210.00"
                },
                {
                    id: "25",
                    invdate: "2007-10-05",
                    name: "test2",
                    note: "note2",
                    amount: "300.00",
                    tax: "20.00",
                    total: "320.00"
                },
                {
                    id: "26",
                    invdate: "2007-09-06",
                    name: "test3",
                    note: "note3",
                    amount: "400.00",
                    tax: "30.00",
                    total: "430.00"
                },
                {
                    id: "27",
                    invdate: "2007-10-04",
                    name: "test",
                    note: "note",
                    amount: "200.00",
                    tax: "10.00",
                    total: "210.00"
                },
                {
                    id: "28",
                    invdate: "2007-10-03",
                    name: "test2",
                    note: "note2",
                    amount: "300.00",
                    tax: "20.00",
                    total: "320.00"
                },
                {
                    id: "29",
                    invdate: "2007-09-01",
                    name: "test3",
                    note: "note3",
                    amount: "400.00",
                    tax: "30.00",
                    total: "430.00"
                }
            ];
            $("#table_list_1").jqGrid({
                data: mydata,
                datatype: "local",
                height: 250,
                autowidth: true,
                shrinkToFit: true,
                rowNum: 14,
                rowList: [10, 20, 30],
                colNames: ['序号', '日期', '客户', '金额', '运费', '总额', '备注'],
                colModel: [
                    {
                        name: 'id',
                        index: 'id',
                        width: 60,
                        sorttype: "int"
                    },
                    {
                        name: 'invdate',
                        index: 'invdate',
                        width: 90,
                        sorttype: "date",
                        formatter: "date"
                    },
                    {
                        name: 'name',
                        index: 'name',
                        width: 100
                    },
                    {
                        name: 'amount',
                        index: 'amount',
                        width: 80,
                        align: "right",
                        sorttype: "float",
                        formatter: "number"
                    },
                    {
                        name: 'tax',
                        index: 'tax',
                        width: 80,
                        align: "right",
                        sorttype: "float"
                    },
                    {
                        name: 'total',
                        index: 'total',
                        width: 80,
                        align: "right",
                        sorttype: "float"
                    },
                    {
                        name: 'note',
                        index: 'note',
                        width: 150,
                        sortable: false
                    }
                ],
                pager: "#pager_list_1",
                viewrecords: true,
                caption: "示例页面",
                hidegrid: false
                })
        })
    </script>
    <script>
    // loading
    $("#search").click(function(){
        $(".mask_example").show();
        setTimeout(function(){
            $(".mask_example").hide();
        },2000)
    })
    // laydate
    var start = {
            elem: '#activityStart',
            format: 'YYYY/MM/DD hh:mm:ss',
            min: laydate.now(), //设定最小日期为当前日期
            max: '2099-06-16 23:59:59', //最大日期
            istime: true,
            istoday: false,
            choose: function (datas) {
                end.min = datas; //开始日选好后，重置结束日的最小日期
                end.start = datas //将结束日的初始值设定为开始日
            }
        };
        var end = {
            elem: '#activityEnd',
            format: 'YYYY/MM/DD hh:mm:ss',
            min: laydate.now(),
            max: '2099-06-16 23:59:59',
            istime: true,
            istoday: false,
            choose: function (datas) {
                start.max = datas; //结束日选好后，重置开始日的最大日期
            }
        };
        laydate(start);
        laydate(end);
    </script>
</body>
</html>