package controller;

import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/forgot_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        if (email == null || email.isEmpty()) {
            request.setAttribute("error", "Please enter your email.");
            request.getRequestDispatcher("/views/forgot_password.jsp").forward(request, response);
            return;
        }
        UserDAO dao = new UserDAO();
        boolean ok = dao.requestPasswordReset(email);
        if (ok) {
            response.sendRedirect(request.getContextPath() + "/reset-password?sent=1&email=" + java.net.URLEncoder.encode(email, "UTF-8"));
            return;
        } else {
            String err = dao.getLastError();
            request.setAttribute("error", err != null ? err : "Email not found or an error occurred.");
            request.getRequestDispatcher("/views/forgot_password.jsp").forward(request, response);
        }
    }
}
