package qjl.oa.web.action;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import qjl.oa.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet({"/user/login", "/user/exit"})
public class UserServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        if("/user/login".equals(servletPath)){
            doLogin(req, resp);
        }else if("/user/exit".equals(servletPath)){
            doExit(req, resp);
        }
    }
    protected void doExit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if(session!=null){
            session.invalidate();
            resp.sendRedirect(req.getContextPath());
        }
    }


    protected void doLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean success = false;
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count =0;
        try {
            conn = DBUtil.getConnection();
            String sql = "select * from t_user where username = ? and password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if(rs.next()){
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }

        if(success){
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            String f = req.getParameter("flag");
            if("1".equals(f)){
                Cookie cookie1 = new Cookie("username", username);
                Cookie cookie2 = new Cookie("password", password);
                cookie1.setMaxAge(60*60*24*10);
                cookie2.setMaxAge(60*60*24*10);
                cookie1.setPath(req.getContextPath());
                cookie2.setPath(req.getContextPath());
                resp.addCookie(cookie1);
                resp.addCookie(cookie2);
            }
            resp.sendRedirect(req.getContextPath()+"/dept/list");
        }else{
            resp.sendRedirect(req.getContextPath()+"/error.html");
        }
    }
}
