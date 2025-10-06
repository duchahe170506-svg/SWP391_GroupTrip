package controller;

import dal.UserDAO;
import model.Users;
import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Users user = (Users) session.getAttribute("currentUser");

        if (user == null) {
            resp.sendRedirect("login");
            return;
        }

        // Lấy lại thông tin mới nhất từ DB để hiển thị
        UserDAO dao = new UserDAO();
        Users refreshed = dao.getUserById(user.getUser_id());
        session.setAttribute("account", refreshed);
        req.setAttribute("user", refreshed);
        req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Users user = (Users) session.getAttribute("account");

        if (user == null) {
            resp.sendRedirect("login");
            return;
        }

        try {
            String name = req.getParameter("name");
            String phone = req.getParameter("phone");
            String dobStr = req.getParameter("date_of_birth");
            String gender = req.getParameter("gender");
            String address = req.getParameter("address");

            Date dob = (dobStr != null && !dobStr.isEmpty()) ? Date.valueOf(dobStr) : null;

            user.setName(name);
            user.setPhone(phone);
            user.setDate_of_birth(dob);
            user.setGender(gender);
            user.setAddress(address);

            UserDAO dao = new UserDAO();
            boolean updated = dao.updateProfile(user);

            if (updated) {
                session.setAttribute("account", user);
                req.setAttribute("user", user);
                req.setAttribute("message", "Cập nhật thông tin thành công!");
            } else {
                req.setAttribute("error", "Không thể cập nhật hồ sơ. Vui lòng thử lại.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Đã xảy ra lỗi trong quá trình cập nhật.");
        }

        req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
    }
}
