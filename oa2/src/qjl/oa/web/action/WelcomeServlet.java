package qjl.oa.web.action;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import qjl.oa.DBUtil;
import qjl.oa.bean.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        String username = null;
        String password = null;
        boolean success = false;
        if(cookies!=null){
            for(Cookie cookie : cookies){
                String name = cookie.getName();
                if("username".equals(name)){
                    username = cookie.getValue();
                }else if("password".equals(name)){
                    password = cookie.getValue();
                }
            }
        }
        if(username!=null&&password!=null){
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
                User user = new User(username, password);
                session.setAttribute("user", user);
                resp.sendRedirect(req.getContextPath()+"/dept/list");
            }else{
                resp.sendRedirect(req.getContextPath()+"/index.jsp");
            }

        }else{
            resp.sendRedirect(req.getContextPath()+"/index.jsp");
        }
    }
}
