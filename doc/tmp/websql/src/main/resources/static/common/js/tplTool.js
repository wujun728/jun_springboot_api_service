/**
 * Created by 陈熠 on 2017/6/21
 * email   :  228112142@qq.com
 */
(function ($) {
    /* 入口函数 */
    $.fn.tplTool = function () {
        //当前对象
        var $grid = this;
        //获取参数
        cyProps = $grid.attr("cyProps");
        if (!cyProps) {
            return
        }
        cyProps = cyProps ? cyProps : "";
        //将参数转为json
        cyProps = eval("({" + cyProps + "})");
        //从后台获取数据
        var url = cyProps.url;
   
        var R = "";
        //如果是从后台获取数据
        if (url != undefined && url != "") {
            R = tplTool.getDataByUrl(cyProps.url);
            tplTool.renderData(R, $grid, cyProps);
        }
     
       
    };
    /*默认配置*/
    var cyProps = {};
    /*方法对象*/
    var tplTool = {
        /**获取数据 by chenyi 2017/6/21*/
        getDataByUrl: function (url) {
            var data;
            $.ajax({
                url: url,
                async: false,
                type: 'post',
                dataType: "json",
                success: function (R) {
                    if (R.code == 0) {
                        data = R;
                    } else {
                        data = {};
                        alert(R.msg);
                    }
                }
            });
            return data;
        },
        /**组件数据渲染 by chenyi 2017/7/20*/
        renderData: function (R, $grid, cyProps) {
            var _grid = $grid;
            layui.use('laytpl', function () {
                var laytpl = layui.laytpl;
                var getTpl = $(_grid).find("script").html();
                laytpl(getTpl).render(R, function (html) {
                    $(_grid).html(html);
                });
            });
        }
    }

})(jQuery);
$(document).ready(function () {
    var tpls = $("[cyType='tplTool']");
    for (var i = 0; i < tpls.length; i++) {
        $(tpls[i]).tplTool();
    }
});