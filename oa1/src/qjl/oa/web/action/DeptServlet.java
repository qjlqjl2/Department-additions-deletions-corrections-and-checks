package qjl.oa.web.action;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import qjl.oa.DBUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet({"/dept/list","/dept/detail","/dept/delete", "/dept/save", "/dept/edit", "/dept/modify"})
//@WebServlet("/dept/*")
public class DeptServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        if("/dept/list".equals(servletPath)){
            doList(req, resp);
        }else if("/dept/detail".equals(servletPath)){
            doDetail(req, resp);
        }else if("/dept/delete".equals(servletPath)){
            doDel(req, resp);
        }else if("/dept/save".equals(servletPath)){
            doSave(req, resp);
        }else if("/dept/edit".equals(servletPath)){
            doEdit(req, resp);
        }else if("/dept/modify".equals(servletPath)){
            doModify(req, resp);
        }
    }

    private void doModify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String deptno = req.getParameter("deptno");
        String dname = req.getParameter("dname");
        String loc = req.getParameter("loc");
        Connection conn = null;
        PreparedStatement ps = null;
        int count =0;
        try {
            conn = DBUtil.getConnection();
            String sql = "update dept set dname = ?, loc = ? where deptno = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, dname);
            ps.setString(2, loc);
            ps.setString(3, deptno);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, null);
        }

        if(count==1){
            resp.sendRedirect(req.getContextPath()+"/dept/list");
        }else{
            resp.sendRedirect(req.getContextPath()+"/error.html");
        }
    }

    private void doEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.print("<!DOCTYPE html>");
        out.print("<html>");
        out.print("	<head>");
        out.print("		<meta charset='utf-8'>");
        out.print("		<title>修改部门</title>");
        out.print("	</head>");
        out.print("	<body>");
        out.print("		<h1>修改部门</h1>");
        out.print("		<hr >");
        out.print("		<form action='"+contextPath+"/dept/modify' method='post'>");



        String deptno = req.getParameter("deptno");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "select dname, loc from dept where deptno = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, deptno);
            rs = ps.executeQuery();
            if(rs.next()){
                String dname = rs.getString("dname");
                String loc = rs.getString("loc");
                out.print("                部门编号<input type = 'text' name = 'deptno' value='"+deptno+"' readonly/><br>");
                out.print("                部门名称<input type = 'text' name = 'dname' value='"+dname+"'/><br>");
                out.print("                部门位置<input type = 'text' name = 'loc' value='"+loc+"'/><br>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }
        out.print("			<input type = 'submit' value='修改'/><br>");
        out.print("		</form>");
        out.print("	</body>");
        out.print("</html>");
    }

    private void doSave(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String deptno = req.getParameter("deptno");
        String dname = req.getParameter("dname");
        String loc = req.getParameter("loc");

        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "insert into dept(deptno, dname, loc) values(?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, deptno);
            ps.setString(2, dname);
            ps.setString(3, loc);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, null);
        }
        if(count==1){
            //req.getRequestDispatcher("/dept/list").forward(req, resp);
            resp.sendRedirect(req.getContextPath()+"/dept/list");
        }else{
            //req.getRequestDispatcher("/error.html").forward(req, resp);
            resp.sendRedirect(req.getContextPath()+"/error.html");
        }
    }

    private void doDel(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String deptno = req.getParameter("deptno");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count =0;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            String sql = "delete from dept where deptno = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, deptno);
            count = ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            if(conn!=null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    e.printStackTrace();
                }
            }
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }

        if(count==1){
            resp.sendRedirect(req.getContextPath()+"/dept/list");
        }else{
            resp.sendRedirect(req.getContextPath()+"/error.html");
        }
    }

    private void doDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String deptno = req.getParameter("deptno");
        out.print("<!DOCTYPE html>");
        out.print("<html>");
        out.print("	<head>");
        out.print("		<meta charset='utf-8'>");
        out.print("		<title>部门详情</title>");
        out.print("	</head>");
        out.print("	<body>");
        out.print("		<h1>部门详情</h1>");
        out.print("		<hr >");


        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "select dname, loc from dept where deptno = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, deptno);
            rs = ps.executeQuery();
            if(rs.next()){
                String dname = rs.getString("dname");
                String loc = rs.getString("loc");
                out.print("部门编号："+deptno+"<br>");
                out.print("部门名称："+dname+"<br>");
                out.print("部门位置："+loc+"<br>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }
        out.print("     <input type = 'button' value = '后退' onclick = 'window.history.back()'/>");
        out.print("	</body>");
        out.print("</html>");
    }

    private void doList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.print("<!DOCTYPE html>");
        out.print("<html>");
        out.print("	<head>");
        out.print("		<meta charset='utf-8'>");
        out.print("		<title>部门列表页面</title>");
        out.print("	</head>");
        out.print("	<body>");
        out.print("<script type = 'text/javascript'>");
        out.print("    function del(dno){");
        out.print("        if(window.confirm('确定删除吗')){");
        out.print("            document.location.href='"+contextPath+"/dept/delete?deptno='+dno;");
        out.print("        }");
        out.print("    }");
        out.print("</script>");
        out.print("		<h1 align='center'>部门列表</h1>");
        out.print("		<hr >");
        out.print("		<table border='1px' align='center' width = '50%'>");
        out.print("			<tr>");
        out.print("				<th>序号</th>");
        out.print("				<th>部门编号</th>");
        out.print("				<th>部门名称</th>");
        out.print("				<th>操作</th>");
        out.print("			</tr>");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println("c");
            conn = DBUtil.getConnection();
            System.out.println("b");
            String sql = "select deptno as a, dname, loc from dept";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int i = 0;
            System.out.println("a");
            while(rs.next()){
                String deptno = rs.getString("a");
                String dname = rs.getString("dname");
                String loc = rs.getString("loc");

                out.print("			<tr>");
                out.print("				<td>"+(++i)+"</td>");
                out.print("				<td>"+deptno+"</td>");
                out.print("				<td>"+dname+"</td>");
                out.print("				<td>");
                out.print("					<a href ='javascript:void(0)' onclick = 'del("+deptno+")'>删除</a>");
                out.print("					<a href ='"+contextPath+"/dept/edit?deptno="+deptno+"'>修改</a>");
                out.print("					<a href ='"+contextPath+"/dept/detail?deptno="+deptno+"'>详情</a>");
                out.print("				</td>");
                out.print("			</tr>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }

        out.print("		</table>");
        out.print("		<hr >");
        out.print("		<a href='"+contextPath+"/add.html'>新增部门</a>");
        out.print("	</body>");
        out.print("</html>");
    }
}
