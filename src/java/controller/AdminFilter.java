package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Users;

@WebFilter("/admin/*")   
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);

        Users user = (session != null) ? (Users) session.getAttribute("currentUser") : null;

        if (user == null) {
          
            req.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        if (!"Admin".equals(user.getRole())) {
           
            req.setAttribute("error", "Bạn không có quyền truy cập trang này.");
            req.getRequestDispatcher("/views/403.jsp").forward(request, response);
            return;
        }

        chain.doFilter(request, response); 
    }
}
