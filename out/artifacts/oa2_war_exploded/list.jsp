<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.List, qjl.oa.bean.Dept"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset='utf-8'>
		<title>部门列表页面</title>
	</head>
	<body>

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

			<%
				List<Dept> deptList = (List<Dept>)request.getAttribute("deptList");
				int i = 0;
				for(Dept dept:deptList){
			%>
			<tr>
				<td><%=++i%></td>
				<td><%=dept.getDeptno()%></td>
				<td><%=dept.getDname()%></td>
				<td>
					<a href ="javascript:void(0)" onclick="del(<%=dept.getDeptno()%>)">删除</a>
					<a href ="<%=request.getContextPath()%>/dept/detail?f=m&dno=<%=dept.getDeptno()%>">修改</a>
					<a href ="<%=request.getContextPath()%>/dept/detail?f=d&dno=<%=dept.getDeptno()%>">详情</a>
				</td>
			</tr>
			<%
				}
			%>









		</table>
		<hr >
		<a href='<%=request.getContextPath()%>/add.jsp'>新增部门</a>
		
	</body>
</html>