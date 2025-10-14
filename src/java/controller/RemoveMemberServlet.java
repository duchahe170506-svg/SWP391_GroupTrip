package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import dal.GroupMembersDAO;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/group/remove-member")
public class RemoveMemberServlet extends HttpServlet {
    private GroupMembersDAO memberDAO = new GroupMembersDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int groupId = 8;
            int userId = 13;
            int removedBy = 14;
            String reason = request.getParameter("reason");

            boolean ok = memberDAO.removeMember(groupId, userId, removedBy, reason);

            if (ok) {
                request.setAttribute("success", "Đã xóa thành viên thành công");
            } else {
                request.setAttribute("error", "Không thể xóa thành viên");
            }

            // load lại danh sách members
            request.setAttribute("members", memberDAO.getMembersByGroup(groupId));
            request.setAttribute("groupId", groupId);

            // forward về lại trang quản lý nhóm
            request.getRequestDispatcher("/views/group-manage.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("/views/group-manage.jsp").forward(request, response);
        }
    }
}
