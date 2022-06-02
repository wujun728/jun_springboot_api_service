<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <title>管理系统</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/font-awesome.min.css?v=4.3.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/style.css?v=3.2.0" rel="stylesheet">

</head>
<body class="fixed-sidebar full-height-layout gray-bg">
<div id="wrapper">
    <!--左侧导航开始-->
    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="nav-close"><i class="fa fa-times-circle"></i>
        </div>
        <div class="sidebar-collapse">
            <ul class="nav" id="side-menu">
                <li class="nav-header">
                    <div class="profile-element">
                        <span><img alt="image" width="70" class="img-circle" src="<%=path%>/nh-micro-js/images/face.png"></span>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear"></span>
                               <span class="block m-t-xs"><strong class="font-bold">${nhUserName }</strong></span>
                        </a>
                        <div class="message_icon">
                            <!-- <a href="#">
                                <i class="fa fa-envelope"></i>
                                <i class="message_dot"></i>
                            </a>&nbsp;&nbsp;
                            <a href="#">
                                <i class="fa fa-bell"></i>
                            </a> -->
                        </div>
                    </div>
                    <div class="logo-element"></div>
                </li>

                <c:forEach items="${menuInfo}" var="menu">
                    <c:if test="${menu.children == null}">
                        <li>
                            <a class="J_menuItem" href="<%=basePath%>${menu.url}" name="tabMenuItem">
                                <i class="fa ${menu.icon}"></i>
                                <span class="nav-label">${menu.name}</span>
                            </a>
                        </li>
                    </c:if>
                    <c:if test="${menu.children != null}">
                        <li>
                            <a href="#">
                                <i class="fa ${menu.icon}"></i>
                                <span class="nav-label">${menu.name}</span>
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-second-level">
                        <c:forEach items="${menu.children}" var="subMenu">
                            <c:if test="${subMenu.children == null}">
                                <li>
                                    <a class="J_menuItem" href="<%=basePath%>${subMenu.url}" name="tabMenuItem">${subMenu.name}</a>
                                </li>
                            </c:if>
                            <c:if test="${subMenu.children != null}">
                                <li>
                                    <a href="#">${subMenu.name} <span class="fa arrow"></span></a>
                                    <ul class="nav nav-third-level">
                                        <c:forEach items="${subMenu.children}" var="subMenus">
                                        <li>
                                            <a class="J_menuItem" href="<%=basePath%>${subMenus.url}" name="tabMenuItem">${subMenus.name}</a>
                                        </li>
                                        </c:forEach>
                                    </ul>
                                </li>
                            </c:if>
                        </c:forEach>
                            </ul>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
        </div>
    </nav>
    <!--左侧导航结束-->
    <!--右侧部分开始-->
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0;min-height:0px;">
                <div class="navbar-header">
                </div>
            </nav>
        </div>
        <div class="row content-tabs">
            <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#" style="position: absolute; z-index: 1;">
                <i class="fa fa-bars"></i>
            </a>
            <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i></button>
            <nav class="page-tabs J_menuTabs">
                <div class="page-tabs-content">
                    <a href="javascript:;" class="active J_menuTab" data-id="<%=basePath%>micropage/nh-micro-jsp/layout/center.jsp">首页</a>
                </div>
            </nav>
            <button class="roll-nav roll-right J_tabRight" style="right:140px"><i class="fa fa-forward"></i></button>
            <div class="btn-group roll-nav roll-right" style="right:60px">
                <button class="dropdown J_tabClose"  data-toggle="dropdown">关闭操作<span class="caret"></span></button>
                <ul role="menu" class="dropdown-menu dropdown-menu-right">
                    <li class="J_tabShowActive"><a>定位当前选项卡</a>
                    </li>
                    <li class="divider"></li>
                    <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                    </li>
                    <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                    </li>
                </ul>
            </div>
            <a href="<%=basePath%>micromvc/uc/logoutgo" class="roll-nav roll-right J_tabExit"><i class="fa fa fa-sign-out"></i> 退出</a>
        </div>
        <div class="row J_mainContent" id="content-main">
            <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="<%=basePath%>micropage/nh-micro-jsp/layout/center.jsp"
                    frameborder="0" data-id="<%=basePath%>micropage/nh-micro-jsp/layout/center.jsp" seamless></iframe>
        </div>
        <div class="footer">

        </div>
    </div>
    <!--右侧部分结束-->
</div>
<!-- 全局js -->
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" charset="UTF-8">


    var $centerFrame;

    $(function() {
        $centerFrame=$("#jn_Frame");
        $centerFrame.attr("src","<%=path%>/micropage/nh-micro-jsp/layout/home.jsp");
    });
</script>

<script src="<%=path%>/nh-micro-js/js/bootstrap/js/bootstrap.min.js?v=3.4.0"></script>
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/layer.min.js"></script>

<!-- 自定义js -->
<script src="<%=path%>/nh-micro-js/js/hplus.js?v=4.1.0"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-js/js/contabs.js"></script>

<!-- 第三方插件 -->
<script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/pace/pace.min.js"></script>

</body>
</html>
