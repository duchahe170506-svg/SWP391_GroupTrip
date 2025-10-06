package controller;

import dal.GroupMembersDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/group/edit-role")
public class EditRoleServlet extends HttpServlet {

    private GroupMembersDAO memberDAO = new GroupMembersDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int groupId = 5;
        int userId = Integer.parseInt(req.getParameter("userId"));

        // Fix cứng leader ID = 14
        int leaderId = 14;

        // Chỉ leader mới có quyền đổi role
        if (leaderId != 14) {
            resp.sendRedirect("manage?groupId=" + groupId);
            return;
        }

        // Lấy role hiện tại
        String currentRole = memberDAO.getRole(groupId, userId);
        String newRole = "Member".equals(currentRole) ? "Leader" : "Member";

        // Không cho đổi vai trò của leader gốc
        if (!"Leader".equals(currentRole)) {
            memberDAO.updateRole(groupId, userId, newRole);
        }

        resp.sendRedirect("manage?groupId=" + groupId);
    }
}
