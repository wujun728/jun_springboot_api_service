<%@ page language="java" pageEncoding="UTF-8" %>
<%
String path = request.getContextPath();
%>
<script type="text/javascript" charset="UTF-8">
function openModifyPass(){
	$centerFrame.attr('src','<%=path %>/nh-micro-jsp/user-manager/modifyPassword.jsp');
}
</script>
<form id="logoutform" >
<div style="position: absolute; right: 0px; bottom: 0px; ">
	用户名：${nhUserName}&nbsp;<a href="javascript:void(0)" onclick="openModifyPass()">修改密码</a>
	&nbsp;<a href="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhlogin&groovySubName=execLogOutGo">退出</a>
</div>
</form>
