$(function(){
    init(); //初始化
    checkLogin(); //校验
});
function init(){
	$('.i-checks').iCheck({
	    checkboxClass: 'icheckbox_square-green',
	    radioClass: 'iradio_square-green',
	});
	$("input").bind("keydown",function(){
		$("#J-error").find('i').addClass('hide');
		$("#J-error span").html('');
		$(this).removeClass("bd-error");
	});
	
	//判断是否存在过用户
    var storage = window.localStorage;
    if("yes" == storage["isstorename"]){
        $("#remb_me").iCheck('check');
        $("#J-username").val(storage["J-username"]);
        $("#J-password").focus();
    }else{
    	$("#J-username").focus();
    }
    
 }
function removeError(){
	$("#J-error").find('i').addClass('hide');
	$("#J-error span").html('');
}
function checkLogin(){
	$("#J-submit").bind("click",function(){
		if(!$.trim($("#J-username").val())){
		  $("#J-error").find('i').removeClass('hide');
		  $("#J-error span").html('用户名不能为空，请重新输入！');
		  $("#J-username").addClass('bd-error');
		  return false;
	   }else{
	   	  removeError();
	   }
	   if(!$.trim($("#J-password").val())){
		  $("#J-error").find('i').removeClass('hide');
		  $("#J-error span").html('密码不能为空，请重新输入！');
		  $("#J-password").addClass('bd-error');
		  return false;
	   }else{
	   	  removeError();
	   }
	   if(!$.trim($("#J-code").val())){
		  $("#J-error").find('i').removeClass('hide');
		  $("#J-error span").html('验证码不能为空，请重新输入！');
		  $("#J-code").addClass('bd-error');
		  return false;
	   }else{
	   	  removeError();
	   }
	   var storage = window.localStorage;	// 读取本地存储
       if($("#remb_me").is(':checked')){	// 判断是否勾选记住用户名
           //存储到loaclStage
           storage["J-username"] = $("#J-username").val();
           storage["isstorename"] =  "yes"; 
       }else{
    	   // 清除
           storage["J-username"] = "";
           storage["isstorename"] =  "no"; 
       }
	});
}
