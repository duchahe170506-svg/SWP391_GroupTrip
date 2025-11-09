package controller;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="AdminUserServlet", urlPatterns={"/admin/users"})
public class AdminUserServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        if (keyword == null) keyword = "";

        req.setAttribute("users", userDAO.getUsersWithStatsAndSearch(keyword));
        req.setAttribute("keyword", keyword);

        req.getRequestDispatcher("/views/admin-users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("changeStatus".equals(action)) {
            try {
                int userId = Integer.parseInt(req.getParameter("user_id"));
                String currentStatus = req.getParameter("currentStatus");
                String newStatus = "Active".equalsIgnoreCase(currentStatus) ? "Blocked" : "Active";

                boolean success = userDAO.updateStatus(userId, newStatus);
                if (success) {
                    req.setAttribute("message", "Đã thay đổi trạng thái thành công!");
                } else {
                    req.setAttribute("message", "Không thể thay đổi trạng thái người dùng!");
                }
            } catch (NumberFormatException e) {
                req.setAttribute("message", "ID người dùng không hợp lệ!");
            }
        }

        doGet(req, resp);
    }
}
