/**
 * Created by chenyi on 2018/1/23.
 */
// 判断是否显示锁屏
if(window.sessionStorage.getItem("isLock") == "true"){
    lockPage();
}
//锁屏
function lockScreen(){
    window.sessionStorage.setItem("isLock",true);
    lockPage();
}
function lockPage() {
    layer.open({
        title : false,
        area: ['1980', '1080'],
        type : 1,
        content : '<video class="video-player" preload="auto" autoplay="autoplay" loop="loop" data-height="1080" data-width="1980px" height="1080" width="1980px"> ' +
        '<source src="/statics/login/login1.mp4" type="video/mp4"> </video>' +
        '<div class="lock-content"><div class="admin-header-lock" id="lock-box">'+
        '<div class="admin-header-lock-img"></div>'+
        '<div class="admin-header-lock-name" id="lockUserName"></div>'+
        '<div class="input_btn">'+
        '<input type="password" class="admin-header-lock-input layui-input" autocomplete="off" placeholder="请输入密码解锁.." name="lockPwd" id="lockPwd" />'+
        '<button class="layui-btn" id="unlock" style="background-color: #42bdf1">解锁</button>'+
        '</div>'+
        '</div></div>',
        closeBtn : 0,
        shade : 0.9
    })
    $(".admin-header-lock-input").focus();
}

// 解锁
$("body").on("click","#unlock",function(){
    if($(this).siblings(".admin-header-lock-input").val() == ''){
        Tips.tips("请输入websql解锁！",$("#lockPwd"),1,'#4fcef1');
        $(this).siblings(".admin-header-lock-input").focus();
    }else{
        //验证密码是否正确
        if($(this).siblings(".admin-header-lock-input").val() == "websql"){
            window.sessionStorage.setItem("isLock",false);
            $(this).siblings(".admin-header-lock-input").val('');
            layer.closeAll("page");
        }else{
            Tips.tips("密码错误，请重新输入websql解锁！！",$("#lockPwd"),1,'#4fcef1');
            $(this).siblings(".admin-header-lock-input").val('').focus();
        }
    }
});
$(document).on('keydown', function() {
    if(event.keyCode == 13) {
        $("#unlock").click();
    }
});

//打赏作者
function reward() {
    layer.open({
        title: '',
        type: 1,
        area: ['600px', '448px'], //宽高
        content: 'GITHUB支持下吧'
    });
}

$(document).ready(function () {
    //默认显示菜单
    createMenu("/statics/json/layuiMenu.json");
    setMainHeight();
});
$(window).resize(function () {
    setMainHeight();
});
//设置主内容高度
function setMainHeight() {
    var height = $(parent.window).height();
    $("#main").css("height", height - 154 + "px");
}
//生成菜单
function createMenu(url) {
    $("#menuSearch").val("");
    $.getJSON(url, function (r) {
        //设置菜单缓存
        $t.setStorageItem("menuList", r.menuList);
        //显示菜单
        setMenu(r.menuList);

    });
}
//显示菜单
function setMenu(menuList) {
    $(".layui-nav-tree").html("");
    for (var i = 0; i < menuList.length; i++) {
        var _li;
        if (menuList[i].type === 0) {
            _li = ['<li class="layui-nav-item">',
                '<a class="" href="javascript:;" title="' + menuList[i].name + '" >',
                '<i class="' + menuList[i].icon + '"></i>' + menuList[i].name + '</a>',
                '</li>'].join("");
            //是否有下级菜单
            if (menuList[i].list) {
                var $li = $(_li);
                $li.find("a").after('<dl class="layui-nav-child">');
                for (var j = 0; j < menuList[i].list.length; j++) {
                    $li.find(".layui-nav-child").append(' <dd><a class="cy-page" href="javascript:;" data-url="' + menuList[i].list[j].url + '" title="' + menuList[i].list[j].name + '">' + menuList[i].list[j].name + '</a></dd>');
                }
            }
            _li = $li.prop("outerHTML");
        }
        if (menuList[i].type === 1) {
            _li = '<li class="layui-nav-item"><a class="layui-nav-item cy-page" href="javascript:;" data-url="' + menuList[i].url + '" title="' + menuList[i].name + '"><i class="' + menuList[i].icon + '"></i> ' + menuList[i].name + '</a></li>';
        }
        $(".layui-nav-tree").append(_li);
    }

    layui.use('element', function () {
        var element = layui.element;
        element.render();
    });
}

//左侧菜单收起与显示
$(".toggle-collapse").click(function () {
    var width = $(window).width();
    if ($(this).hasClass("toggle-show")) {
        $(this).removeClass("toggle-show").animate({left: '200px'});
        $(".layui-body,.layui-footer").css("width", parseInt(width) - 200 + "px").animate({left: '200px'});
        $(".layui-side").animate({left: '0px'}).fadeIn("slow");
    } else {
        $(this).addClass("toggle-show").animate({left: '0px'});
        $(".layui-body,.layui-footer").css("width", parseInt(width) + "px").animate({left: '0px'});
        $(".layui-side").animate({left: '-200px'});
    }

});


//菜单搜索
$(" .menu-search-clear").click(function () {
    $("#menuSearch").val("");
    $(".menu-search-clear").hide()
    //显示默认菜单
    setMenu($t.getStorageItem("menuList"))
});

$("#menuSearch").keyup(function () {
    if ($("#menuSearch").val() == "") {
        $(".menu-search-clear").hide();
        //显示默认菜单
        setMenu($t.getStorageItem("menuList"))
    } else {
        $(".menu-search-clear").show();
        var menuList = $t.getStorageItem("menuList");
        //显示搜索结果菜单
        var k = $("#menuSearch").val().trim("");
        if (k == "") return;
        var arr = [];
        var patt = new RegExp(k);
        for (var i = 0; i < menuList.length; i++) {
            if (menuList[i].type === 1) {
                if (patt.test(menuList[i].name) || patt.test(menuList[i].url)) {
                    arr.push({name: menuList[i].name, url: menuList[i].url, icon: menuList[i].icon});
                }
            }
            if (menuList[i].list) {
                for (var j = 0; j < menuList[i].list.length; j++) {
                    if (menuList[i].list[j].type === 1) {
                        if (patt.test(menuList[i].list[j].name) || patt.test(menuList[i].list[j].url)) {
                            arr.push({
                                name: menuList[i].list[j].name,
                                url: menuList[i].list[j].url,
                                icon: menuList[i].list[j].icon
                            });
                        }
                    }

                }
            }
        }
        $(".layui-nav-tree").html("");
        if (arr.length > 0) {
            //渲染查询后的表格
            for (var i = 0; i < arr.length; i++) {
                $('.layui-nav-tree').append(
                    ['<li class="layui-nav-item">',
                        '<a class="layui-nav-item cy-page" href="javascript:;" ',
                        'data-url="' + arr[i].url + '" title="' + arr[i].name + '">',
                        '<i class="fa fa-pencil"></i> ' + arr[i].name + '</a></li>'].join(""));
            }
            layui.use('element', function () {
                var element = layui.element;
                element.render();

            });
        }

    }
});

