package controller;

import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Users;

@WebServlet(name = "SignupServlet", urlPatterns = {"/signup"})
public class SignupServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");

        if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Please fill in all required fields.");
            request.getRequestDispatcher("/views/signup.jsp").forward(request, response);
            return;
        }
        if (!password.equals(confirm)) {
            request.setAttribute("error", "Password confirmation does not match.");
            request.getRequestDispatcher("/views/signup.jsp").forward(request, response);
            return;
        }

        UserDAO dao = new UserDAO();
        if (dao.isEmailExists(email)) {
            request.setAttribute("error", "Email already exists.");
            request.getRequestDispatcher("/views/signup.jsp").forward(request, response);
            return;
        }

        Users user = new Users();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password); // TODO: hash later
        user.setRole("User");
        user.setStatus("Active");

        int userId = dao.signup(user);
        if (userId > 0) {
            response.sendRedirect(request.getContextPath() + "/login?registered=1");
        } else {
            request.setAttribute("error", "Registration failed. Please try again later.");
            request.getRequestDispatcher("/views/signup.jsp").forward(request, response);
        }
    }
}
