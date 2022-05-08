/**
 * Created by 陈熠 on 2017/6/21
 * email   :  228112142@qq.com
 */
(function ($) {
    /* 入口函数 */
    $.fn.editGridTool = function () {
        //当前表格对象
        var $grid = this;
        //获取表格参数
        var pageProps  = $grid.attr("cyProps");
        if (!pageProps) {
            return
        }
        pageProps = pageProps ? pageProps : "";
        //将表格参数转为json
        pageProps = eval("({" + pageProps + "})");
        //如果url不为空 默认试用url的数据
        if(pageProps.url) {
            $.ajax({
                url: pageProps.url,
                async: false,
                type: 'post',
                dataType: "json",
                success: function (R) {
                    pageProps.url="";
                     pageProps.data = R.data||[];

                }
            });
        }
        $(this).editGrid(pageProps);
    };


})(jQuery);
$(document).ready(function () {
    //表格渲染查询
    var tables = $("[cyType='editGrid']");
    for (var i = 0; i < tables.length; i++) {
        $(tables[i]).editGridTool();
    }

});