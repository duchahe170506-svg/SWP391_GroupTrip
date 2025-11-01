package controller;

import dal.GroupMembersDAO;
import dal.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/group/leave")
public class LeaveGroupServlet extends HttpServlet {

    private GroupMembersDAO groupDAO = new GroupMembersDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String groupIdParam = request.getParameter("groupId");
        String userIdParam = request.getParameter("userId");
        String reason = request.getParameter("reason");

        if (groupIdParam == null || userIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        int groupId, userId;
        try {
            groupId = Integer.parseInt(groupIdParam);
            userId = Integer.parseInt(userIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid groupId or userId");
            return;
        }

        if (reason == null || reason.isEmpty()) {
            reason = "Thành viên tự rời nhóm";
        }

        try {
            boolean success = groupDAO.leaveGroup(groupId, userId, reason);

            if (success) {
                request.getSession().setAttribute("successMessage", "Bạn đã rời nhóm thành công.");
                response.sendRedirect(request.getContextPath() + "/trips"); // quay về trang Trips
            } else {
                String errorMsg = "Không thể rời nhóm: ";
                // Kiểm tra role để hiển thị thông báo chi tiết
                String role = groupDAO.getRole(groupId, userId);
                if ("Leader".equals(role)) {
                    errorMsg += "Bạn phải chỉ định một Leader mới trước khi rời nhóm.";
                } else {
                    errorMsg += "Không thể rời nhóm.";
                }
                request.getSession().setAttribute("errorMessage", errorMsg);
                response.sendRedirect(request.getContextPath() + "/group/manage?groupId=" + groupId);
            }
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi khi rời nhóm: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/group/manage?groupId=" + groupId);
        }
    }
}
