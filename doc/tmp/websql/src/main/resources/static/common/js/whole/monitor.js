/**
 * Created by 陈熠 on 2017/6/21
 * email   :  228112142@qq.com
 * 公用模块
 */
//文件操作
$(function () {
    //操作栏显示
    $(".layui-upload-list").on("mouseover",".file-div",function () {
        $(this).find(".file-delete").css("height","30px");
    });
    $(".layui-upload-list").on("mouseout",".file-div",function () {
        $(this).find(".file-delete").css("height","0px");
    });
    //文件删除
    $(".layui-upload-list").on("click",".delete-one .fa-trash-o",function () {
        $(this).parents(".file-div").find("input").val("");
        $(this).parents(".file-div").find("img").attr("src","/statics/img/noImg.png");
    });
    $(".layui-upload-list").on("click",".delete-list .fa-trash-o",function () {
        $(this).parents(".file-div").remove();
    });

    //左转
    $(".layui-upload-list").on("click",".fa-undo",function () {
        var currentRetate= $(this).parents(".file-div").find("img").attr("currentRetate")||0;
        currentRetate=currentRetate-90;
        $(this).parents(".file-div").find("img").css("transform","rotate("+currentRetate+"deg)").attr("currentRetate",currentRetate);

    });
    //右转
    $(".layui-upload-list").on("click",".fa-repeat",function () {
        var currentRetate= $(this).parents(".file-div").find("img").attr("currentRetate")||0;
        currentRetate=parseInt(currentRetate)+90;
        $(this).parents(".file-div").find("img").css("transform","rotate("+currentRetate+"deg)").attr("currentRetate",currentRetate);

    });

    //图片点击放大
    $(".layui-upload-list").on("click","img",function () {
        var url=$(this).attr("src");
        var img = new Image();
        img.src = url;
        img.onerror = function(){
            Msg.error("找不到图片");
            return false;
        };
        var height=0;
        var width=0;
        if(img.complete){
            height=img.height;
            width=img.width;
        }else{
            img.onload = function(){
                height=img.height;
                width=img.width;
                img.onload=null;//避免重复加载
            }
        }
        parent.layer.open({
            type: 1,                   //类型 1页面 2iframe
            title:"",
            shadeClose: false,         //是否关闭遮罩
            shade: [0.3, '#000'],      //遮罩
            maxmin: false,              //开启最大化最小化按钮
            area: [ width+"px",height+"px"],
            content: '<img src="'+url+'">'
        });
    });


});
