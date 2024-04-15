<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.List, qjl.oa.bean.Dept"%>
<%
	Dept dept = (Dept)request.getAttribute("dept");


%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset='utf-8'>
		<title>部门详情</title>
	</head>
	<body>
	<h3>欢迎<%=session.getAttribute("username")%></h3>
		<h1>部门详情</h1>
		<hr >
		部门编号：<%=dept.getDeptno()%><br>
		部门名称：<%=dept.getDname()%><br>
		部门位置：<%=dept.getLoc()%><br>
	</body>
</html>