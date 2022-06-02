$(function () {
    
    // 饼状图
    var pieChart = echarts.init(document.getElementById("echarts-pie-chart"));
    
    $.ajax({
        type: "POST",
        url: "/micromvc/center/businessInfos",
        data: {},
        dataType: "json",
        success: function (obj) {
            if (obj.resultCode == "000") {
            	pieoption.series[0].data = obj.businessInfos;
            	pieoption.legend.data = obj.businessItemNames;
            	pieChart.setOption(pieoption); 
                $(window).resize(pieChart.resize);
            }else{
            	swal("查询失败！", obj.msg, "error");
            }
        },
        error: function (xhr) {
        }
    });
    
    var pieoption = {
        title : {
            text: '系统业务数据',
            subtext: '',
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:[]
        },
        calculable : true,
        series : [
            {
                name:'访问来源',
                type:'pie',
                radius : '55%',
                center: ['70%', '60%'],
                data:[
                    
                ]
            }
        ]
    };
    
    // 柱状图
    var barChart = echarts.init(document.getElementById("echarts-bar-chart"));
    
    $.ajax({
        type: "POST",
        url: "/micromvc/center/monthBusiness",
        data: {},
        dataType: "json",
        success: function (obj) {
            if (obj.resultCode == "000") {
            	baroption.series[0].data = obj.monthBusinessList;
            	baroption.series[1].data = obj.monthFaildList;
            	barChart.setOption(baroption);
    			window.onresize = barChart.resize;
            }else{
            	swal("查询失败！", obj.msg, "error");
            }
        },
        error: function (xhr) {
        }
    });
    
    var baroption = {
        title : {
            text: '每月业务数量'
        },
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['进件数','放弃数']
        },
        grid:{
            x:30,
            x2:40,
            y2:24
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'进件数',
                type:'bar',
                data:[],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name: '平均值'}
                    ]
                }
            },
            {
                name:'放弃数',
                type:'bar',
                data:[],
                markPoint : {
                    data : [
                    	{type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name : '平均值'}
                    ]
                }
            }
        ]
    };
    barChart.setOption(baroption);
	window.onresize = barChart.resize;
});
