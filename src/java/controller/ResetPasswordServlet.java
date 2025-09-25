package controller;

import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/reset-password"})
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        String email = request.getParameter("email");
        request.setAttribute("token", token);
        request.setAttribute("email", email);
        request.setAttribute("stage", "otp");
        request.getRequestDispatcher("/views/reset_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String stage = request.getParameter("stage");
        String email = request.getParameter("email");
        String otp = request.getParameter("otp");
        UserDAO dao = new UserDAO();

        if (stage == null || stage.equals("otp")) {
            if (otp == null || !otp.matches("\\d{6}")) {
                request.setAttribute("error", "Please enter a valid 6-digit OTP.");
                request.setAttribute("email", email);
                request.setAttribute("stage", "otp");
                request.getRequestDispatcher("/views/reset_password.jsp").forward(request, response);
                return;
            }
            Integer userId = dao.getUserIdByValidOtp(email, otp);
            if (userId == null) {
                request.setAttribute("error", "OTP is invalid or has expired.");
                request.setAttribute("email", email);
                request.setAttribute("stage", "otp");
                request.getRequestDispatcher("/views/reset_password.jsp").forward(request, response);
                return;
            }
            request.setAttribute("email", email);
            request.setAttribute("otp", otp);
            request.setAttribute("stage", "password");
            request.getRequestDispatcher("/views/reset_password.jsp").forward(request, response);
            return;
        }

        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");
        if (password == null || password.isEmpty() || !password.equals(confirm)) {
            request.setAttribute("error", "Password is invalid or confirmation does not match.");
            request.setAttribute("email", email);
            request.setAttribute("otp", otp);
            request.setAttribute("stage", "password");
            request.getRequestDispatcher("/views/reset_password.jsp").forward(request, response);
            return;
        }
        boolean ok = dao.resetPasswordByOtp(email, otp, password);
        if (ok) {
            response.sendRedirect(request.getContextPath() + "/login?reset=1");
        } else {
            request.setAttribute("error", dao.getLastError() != null ? dao.getLastError() : "OTP is invalid or has expired.");
            request.setAttribute("email", email);
            request.setAttribute("otp", otp);
            request.setAttribute("stage", "password");
            request.getRequestDispatcher("/views/reset_password.jsp").forward(request, response);
        }
    }
}
