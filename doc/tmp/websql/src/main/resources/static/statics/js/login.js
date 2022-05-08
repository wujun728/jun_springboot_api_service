layui.config({
    base: "js/"
}).use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;
    //登录按钮事件
    form.on("submit(login)", function (data) {
        //parent.location.href = '/index.html';
        var datas = "userName=" + data.field.username + "&password=" + data.field.password + "&captcha=" + data.field.captcha;
        $.ajax({
            type: "POST",
            url: "/login",
            data: datas,
            success: function (result) {

                if(result.logins=="2"){
                    Msg.error(result.msg);
                    refreshCode();
                }else{
                    Msg.success("登录成功！");
                    location.href="/index";
                }
            }
        });
        return false;
    })
});
function refreshCode(){
    var captcha = document.getElementById("captcha");
    captcha.src = "/login/captcha?t=" + new Date().getTime();
}
