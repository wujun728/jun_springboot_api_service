layUI select下拉框动态赋值和设置选中值

核心代码

<script>
    layui.use(['form', 'layedit', 'laydate'], function () {
        var $ = layui.jquery;
        $.ajax({
            url: '../api/SysType/GetArticleType?parentId=1',//json文件位置
            type: 'get', //请求方式为get
            dataType: 'json', //返回数据格式为json
            success: function (result) { //请求成功完成后要执行的方法
                var select = $("#TypeID");//TypeID为HTML标签ID
                $.each(result, function (index, item) {
                    select.append(new Option( item.TypeName, item.TypeID));// 下拉菜单里添加元素
                });
                layui.form.render("select");
            }
        });
    });</script>
    
设置选中值，今天我设置了值但layUI没有显示我选中的值,原因是没有 layui.form.render("select");这个没有设置.

$("#TypeID").val("选中的值");//设置选中的值  TypeID为HTML标签ID
layui.form.render("select");//layUI设置


layui给select下拉框赋值
		//重新渲染表单函数
		function renderForm() {
			layui.use('form', function() {
				var form = layui.form(); //高版本建议把括号去掉，有的低版本，需要加()
				form.render();
			});
		}


		//赋值部分
		$.ajax({
			url: "",
			type: "post",
			dataType: "json",
			data: {
				"id": id
				},
				success: function(result) {
					$("#partnersName").val(result.partnersName);
					//select赋值 
					$("#partnersType").val(result.partnersType);
					$("#partnersStatus").val(result.partnersStatus);
					renderForm();//表单重新渲染，要不然添加完显示不出来新的
					$("#partnersProprotion").val(result.partnersProprotion);
					$("#partnersAddress").val(result.partnersAddress);
					$("#partnersMan").val(result.partnersMan);
					$("#partnersContact").val(result.partnersContact);
				}
			});