<%@page contentType="text/html; charset=UTF-8"%>
<%@page session="false" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>欢迎使用0A系统</title>
	</head>
	<body>
	<h1>LOGIN PAGE</h1>
	<hr>
<%--		<a href="<%=request.getContextPath()%>/dept/list">查看部门列表</a>--%>
	<form action="<%=request.getContextPath()%>/user/login" method="post">
		username:<input type="text" name="username"><br>
		password:<input type="password" name="password"><br>
		<input type="checkbox" name="flag" value="1">十天内免登录<br>
		<input type="submit" value="login">
	</form>
	</body>
</html>