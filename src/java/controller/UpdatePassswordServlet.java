package controller;

import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;

public class UpdatePassswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        request.getRequestDispatcher("/views/update_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String currentPassword = request.getParameter("current_password");
        String newPassword = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");

        // Kiểm tra rỗng
        if (currentPassword == null || newPassword == null || confirmPassword == null
                || currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/views/update_password.jsp").forward(request, response);
            return;
        }

        // Kiểm tra password hiện tại
        if (!currentPassword.equals(user.getPassword())) {
            request.setAttribute("error", "Current password is incorrect.");
            request.getRequestDispatcher("/views/update_password.jsp").forward(request, response);
            return;
        }

        // Kiểm tra trùng khớp confirm
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New password and confirmation do not match.");
            request.getRequestDispatcher("/views/update_password.jsp").forward(request, response);
            return;
        }

        // Kiểm tra độ mạnh password
        if (!isValidPassword(newPassword)) {
            request.setAttribute("error", "New password must contain at least 8 characters, one uppercase, one lowercase, one number, and one special character.");
            request.getRequestDispatcher("/views/update_password.jsp").forward(request, response);
            return;
        }

        // Cập nhật DB
        UserDAO dao = new UserDAO();
        boolean updated = dao.updatePasswordByEmail(user.getEmail(), newPassword);

        if (updated) {
            // Cập nhật lại session user
            user.setPassword(newPassword);
            session.setAttribute("currentUser", user);
            request.setAttribute("message", "Password updated successfully!");
        } else {
            request.setAttribute("error", "An error occurred while updating the password. Please try again.");
        }

        request.getRequestDispatcher("/views/update_password.jsp").forward(request, response);
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasNum = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()].*");
        return hasUpper && hasLower && hasNum && hasSpecial;
    }

    @Override
    public String getServletInfo() {
        return "Servlet to handle password updating";
    }
}
