package qjl.oa.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession(false);
        if("/index.jsp".equals(servletPath)||"/welcome".equals(servletPath)||"/user/login".equals(servletPath)||
                "/user/exit".equals(servletPath)||
                (session!=null&&session.getAttribute("user")!=null)){
            filterChain.doFilter(request, response);
        }else{
            response.sendRedirect(request.getContextPath()+"/index.jsp");
        }
    }
}
