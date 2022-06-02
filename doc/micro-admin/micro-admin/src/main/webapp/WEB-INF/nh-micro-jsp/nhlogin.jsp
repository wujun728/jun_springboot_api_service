<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>业务管理系统-登录</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/font-awesome.css?v=4.3.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/animate.css" rel="stylesheet">  
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/style.css?v=3.2.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/login.css" rel="stylesheet">
     <!-- 全局js -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/jquery-2.1.1.min.js"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/bootstrap.min.js?v=3.4.0"></script>
    <!-- iCheck -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/iCheck/icheck.min.js"></script>
    <!--login-->
 	<script src="<%=path%>/nh-micro-js/js/login.js"></script>
    <script type="text/javascript">
    if(window.top !== window.self){ window.top.location = window.location;}
  	//重载验证码
    function reloadVerifyCode(){
  		var timenow = new Date().getTime();
        document.getElementById("scode").src="<%=path%>/servlet/imgCheckCode?d="+timenow;
    }
    </script>
</head>

<body class="bt_login_bgcolor signin">
    <div class="middle-box text-center loginscreen  animated fadeInDown"> 
        <div class="bt_login_boxs">
            <div class="title clearfix">
            	<img src="<%=path%>/nh-micro-js/js/bootstrap/img/login/logo.png" alt=""/>
            	<span>业务管理系统</span>
            </div>
            <form class="m-t my-form" role="form" action="<%=basePath%>micromvc/uc/logingo" method="post">
            	<div class="width_80">
            	    <p id="J-error"><i class="fa fa-info-circle hide"></i><span>${nhloginMsg}</span></p>
	                <div class="form-group bt_login_inputs">
	                	<i class="user"></i>
	                    <input id="J-username" type="text" name="username" class="form-control" placeholder="请输入用户名">
	                </div> 
	                <div class="form-group bt_login_inputs">
	                	<i class="lock"></i>
	                    <input id="J-password" type="password" name="password" class="form-control" placeholder="请输入密码">
	                </div>
	                 <div class="form-group bt_login_inputs code-input clearfix">
	                	<i class="code"></i>
	                    <input id="J-code" type="text" name="imgcode" class="form-control" placeholder="请输入验证码<%=path%>" maxlength="4">
	                    <span><img src="<%=path%>/servlet/imgCheckCode" alt="" id="scode" onclick="reloadVerifyCode();"/></span>
	                    <label onclick="reloadVerifyCode();">换一换</label>
	                </div>
	                <label class="i-checks"><input type="checkbox" id="remb_me">&nbsp;记住用户名</label>
	                <div class="clear"></div>
	                <button id="J-submit" type="submit" class="btn btn-primary block full-width bt_login_bnt">登 录</button>   
                <!--<p class="text-muted text-center"> <a href="login.html#"><small>忘记密码了？</small></a> | <a href="register.html">注册一个新账号</a>
                </p>-->
            </form>
        </div>
    </div>
    <!-- <footer>COPYRIGHT© 集团 京ICP备15054839号-2</footer> -->
</body>
</html>
