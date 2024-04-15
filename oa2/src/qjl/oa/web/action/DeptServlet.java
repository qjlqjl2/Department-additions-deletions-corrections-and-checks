package qjl.oa.web.action;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import qjl.oa.DBUtil;
import qjl.oa.bean.Dept;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet({"/dept/list","/dept/detail","/dept/delete", "/dept/save", "/dept/modify"})
//@WebServlet("/dept/*")
public class DeptServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if(session!=null&&session.getAttribute("username")!=null){
            String servletPath = req.getServletPath();
            if("/dept/list".equals(servletPath)){
                doList(req, resp);
            }else if("/dept/detail".equals(servletPath)){
                doDetail(req, resp);
            }else if("/dept/delete".equals(servletPath)){
                doDel(req, resp);
            }else if("/dept/save".equals(servletPath)){
                doSave(req, resp);
            }else if("/dept/modify".equals(servletPath)){
                doModify(req, resp);
            }
        }else{
            resp.sendRedirect(req.getContextPath()+"/index.jsp");
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
        Dept dept = new Dept();
        String deptno = req.getParameter("dno");
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

                dept.setDeptno(deptno);
                dept.setDname(dname);
                dept.setLoc(loc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }

        req.setAttribute("dept", dept);

        String f = req.getParameter("f");
        if("m".equals(f)){
            req.getRequestDispatcher("/edit.jsp").forward(req,resp);
        }else if("d".equals(f)){
            req.getRequestDispatcher("/detail.jsp").forward(req,resp);
        }



    }

    private void doList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Dept>depts = new ArrayList();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "select deptno as a, dname, loc from dept";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                String deptno = rs.getString("a");
                String dname = rs.getString("dname");
                String loc = rs.getString("loc");

                Dept dept = new Dept();
                dept.setDeptno(deptno);
                dept.setDname(dname);
                dept.setLoc(loc);

                depts.add(dept);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }
req.setAttribute("deptList", depts);

        req.getRequestDispatcher("/list.jsp").forward(req,resp);
    }
}
