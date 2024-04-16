<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.List, qjl.oa.bean.Dept"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset='utf-8'>
		<title>部门列表页面</title>
		<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
	</head>
	<body>
	<h3>欢迎$(username)</h3>
	<a href="/user/exit">[退出系统]</a>

	<script type="text/javascript">
		function del(dno){
			var ok = window.confirm("删除了不可恢复");
			if(ok){
				document.location.href = "<%=request.getContextPath()%>/dept/delete?deptno="+dno;
			}
		}
	</script>


		<h1 align='center'>部门列表</h1>
		<hr >
		<table border='1px' align='center' width = '50%'>
			<tr>
				<th>序号</th>
				<th>部门编号</th>
				<th>部门名称</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${deptList}" varStatus="deptStatus" var="dept">
				<tr>
					<td>$(deptStatus.count)</td>
					<td>$(dept.deptno)</td>
					<td>$(dept.dname)</td>
					<td>
						<a href ="javascript:void(0)" onclick="del(${dept.deptno})">删除</a>
						<a href ="/dept/detail?f=m&dno=${dept.deptno}">修改</a>
						<a href ="/dept/detail?f=d&dno=${dept.deptno}">详情</a>
					</td>
				</tr>

			</c:forEach>
		</table>
		<hr >
		<a href='/add.jsp'>新增部门</a>
		
	</body>
</html>