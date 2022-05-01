/**
 * Created by chenyi on 2017/12/20.
*/
// tab监听事件
layui.use('element', function () {
    var element = layui.element;
    //tabs切换
    element.on('tab(tabs)', function (data) {
        console.log(data);
    });
    //选项卡删除
    element.on('tabDelete(tabs)', function (data) {
        var deleteUrl = $($(data.elem.context).parent()).attr("data-url");
        $("#main").find("iframe[src='" + deleteUrl + "']").remove();
        var url = $(".layui-tab-title .layui-this").attr("data-url");
        $("#main").find("iframe[src='" + url + "']").addClass("cy-show");
        $(".main-tab").find("i").remove();

        var _lis = $(this).parents("#navTab").find("li");
        var n = 0;
        for (var i = 0; i < _lis.length; i++) {
            n += $(_lis[i]).width() + 15;
        }
        //获取右侧区域宽度
        var _width = $(".layui-body").width();
        if (_width > n) {
            $(".layui-tab-title").css("left", "20px");
        }

    });


});

$(function () {
    //选中左侧菜单
    $(".layui-nav").on("click", "a.cy-page", function () {
        var $this = $(this);
        var name = $this.text() || $this.attr("title");
        var url = unescape($this.attr("data-url")) || "";
        if ($("#navTab").find("li[data-url='" + url + "']").length === 0  ) {
            var index = Loading.open(1,false);
            //如果不存在
            $("#navTab").find("li").removeClass("layui-this");
            //新增tab页
            var _li = '<li class="layui-this" data-url="' + url + '">'+name+  '</li>';
            $("#navTab").find("ul").append(_li);

            $("#main").find("iframe").removeClass("cy-show");
            //打开iframe
            var iframe = $('<iframe class="cy-show" scrolling="yes" frameborder="0" ></iframe>');
            $(iframe).attr("src", url);
            $("#main").append(iframe);
            $(iframe).load(function() {
                Loading.close(index);
            });
            //tab过多时
            var _lis = $(".layui-tab-title").find("li");
            var n = 0;
            for (var i = 0; i < _lis.length; i++) {
                n += $(_lis[i]).width()+15;
            }

            var _width = $(".layui-body").width();
            if (n > parseInt(_width)-50) {
                $(".layui-tab-title").css("left", $(".layui-body").width()-n-70);
            }


        }else{
            $("#navTab").find("li").removeClass("layui-this");
            $("#navTab").find("li[data-url='" + url + "']").addClass("layui-this");
            $("#main").find("iframe").removeClass("cy-show");
            $("#main").find("iframe[src='"+url+"']").addClass("cy-show");
        }

        layui.use('element', function(){
            var element = layui.element;
            element.render();

        });

    });

    //选中tabs
    $("#navTab").on("click", "li", function () {
        var url=$(this).attr("data-url");
        $("#main").find("iframe").removeClass("cy-show");
        $("#main").find("iframe[src='"+url+"']").addClass("cy-show");
        $(".layui-nav-tree").find(".layui-nav-item,dd").removeClass("layui-this");
        $(".layui-nav-tree").find(".cy-page[data-url='"+url+"']").parent().addClass("layui-this");
        layui.use('element', function(){
            var element = layui.element;
            element.render();

        });
    });


    //tab左移
    $(".layui-tab-left").on("click", function () {
        var _left= $(".layui-tab-title").css("left");
        _left = parseInt(_left.substr(0, _left.length - 2));
        var _lis = $(".layui-tab-title").find("li");
        var n = 0;
        for (var i = 0; i < _lis.length; i++) {
            n += $(_lis[i]).width()+15;
        }
        var abs = Math.abs(_left);
        //获取右侧区域宽度
        var _width = $(".layui-body").width();
        if (n-abs < _width) {
            Msg.info("拽不动了");
        } else {
            $(".layui-tab-title").css("left", _left - 100);

        }


    });
    //tab右移
    $(".layui-tab-right").on("click", function () {
        var _left= $(".layui-tab-title").css("left");
        _left = parseInt(_left.substr(0, _left.length - 2));
        if (_left < 0) {
            $(".layui-tab-title").css("left", _left + 100);
        }
        else {
            Msg.info("拽不动了");
            $(".layui-tab-title").css("left", 20);
        }
    });


    $(".tabsPage").on("click", "li", function () {
        $(this).parents("ul").find("li").removeClass("selected");
        $(this).addClass("selected")
    });
    $(".tabsPage").on("mouseover", "a.close", function () {
        $(this).addClass("hover");
    });
    $(".tabsPageb").on("mouseout", "a.close", function () {
        $(this).removeClass("hover");
    });
});

//右键菜单
$(document).ready(function(){
    context.init({preventDoubleContext: false});
    context.settings({compress: true});
    context.attach('.layui-tab-title li', [//attach为绑定的dom对象，可以使用类名或id，例如'.classname'
        {text: '刷新当前页面', action: function(e){
            e.preventDefault();
            var dataUrl=$(this).parents("ul").attr("data-url");
            var $iframe=$("#main iframe[src='"+dataUrl+"']");
            $($iframe[0]).attr("src",dataUrl);

        }},
        {text: '关闭当前页面', action: function(e){
            e.preventDefault();
            var dataUrl=$(this).parents("ul").attr("data-url");
            $(".layui-tab-title li[data-url='"+dataUrl+"']").find(".layui-tab-close").click();
        }},
        {text: '关闭其他页面', action: function(e){
            e.preventDefault();
            var dataUrl=$(this).parents("ul").attr("data-url");
            $(".layui-tab-title li[data-url='"+dataUrl+"']").prevAll("li:not(.main-tab)").find(".layui-tab-close").click();
            $(".layui-tab-title li[data-url='"+dataUrl+"']").nextAll("li:not(.main-tab)").find(".layui-tab-close").click();

        }},
        {text: '关闭所有页面', action: function(e){
            var dataUrl=$(this).parents("ul").attr("data-url");
            $(".layui-tab-title li[data-url='"+dataUrl+"']").parents("ul").find("li:not(.main-tab)").find(".layui-tab-close").click();
        }},
    ]);
});
