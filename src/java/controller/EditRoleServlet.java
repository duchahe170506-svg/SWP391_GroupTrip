package controller;

import dal.GroupMembersDAO;
import dal.GroupRoleHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import model.GroupRoleHistory;
import model.Users;

@WebServlet("/group/edit-role")
public class EditRoleServlet extends HttpServlet {

    private GroupMembersDAO memberDAO = new GroupMembersDAO();
    private GroupRoleHistoryDAO historyDAO = new GroupRoleHistoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseRedirect = req.getContextPath() + "/group/manage";
        try {
            String gid = req.getParameter("groupId");
            String uid = req.getParameter("userId");
            String action = req.getParameter("action");

            if (gid == null || gid.isBlank() || uid == null || uid.isBlank()) {
                resp.sendRedirect(baseRedirect + "?error=" + URLEncoder.encode("❌ Thiếu tham số", StandardCharsets.UTF_8));
                return;
            }

            int groupId = Integer.parseInt(gid);
            int userId = Integer.parseInt(uid);

            HttpSession session = req.getSession(false);
            int currentUserId = (session != null && session.getAttribute("currentUser") != null)
                    ? ((Users) session.getAttribute("currentUser")).getUser_id()
                    : -1;

            String currentUserRole = memberDAO.getRole(groupId, currentUserId);
            if (currentUserRole == null || !"Leader".equalsIgnoreCase(currentUserRole)) {
                resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&error=" + URLEncoder.encode("❌ Bạn không có quyền chỉnh sửa vai trò", StandardCharsets.UTF_8));
                return;
            }

            String currentRole = memberDAO.getRole(groupId, userId);
            if (currentRole == null) {
                resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&error=" + URLEncoder.encode("❌ Không tìm thấy thành viên trong nhóm", StandardCharsets.UTF_8));
                return;
            }

            String newRole = null;
            String message = "";

            if ("promoteLeader".equalsIgnoreCase(action)) {
                // Hạ tất cả leader hiện tại xuống CoLeader
                memberDAO.updateRoleByRole(groupId, "Leader", "CoLeader");

                boolean promoted = memberDAO.updateRole(groupId, userId, "Leader");
                if (promoted) {
                    GroupRoleHistory history = new GroupRoleHistory();
                    history.setGroup_id(groupId);
                    history.setUser_id(userId);
                    history.setOld_role(currentRole);
                    history.setNew_role("Leader");
                    history.setChanged_by(currentUserId);
                    historyDAO.addHistory(history);

                    resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&success=" + URLEncoder.encode("✅ Đã phong thành Leader mới", StandardCharsets.UTF_8));
                } else {
                    resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&error=" + URLEncoder.encode("❌ Phong Leader thất bại", StandardCharsets.UTF_8));
                }
                return;
            }

          
            if ("toggle".equalsIgnoreCase(action)) {
                if ("Member".equalsIgnoreCase(currentRole)) {
                    newRole = "CoLeader";
                    message = "✅ Đã nâng thành CoLeader";
                } else if ("CoLeader".equalsIgnoreCase(currentRole)) {
                    newRole = "Member";
                    message = "✅ Đã hạ xuống Member";
                } else {
                    resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&error=" + URLEncoder.encode("❌ Không thể đổi role này: " + currentRole, StandardCharsets.UTF_8));
                    return;
                }

                boolean updated = memberDAO.updateRole(groupId, userId, newRole);
                if (updated) {
                    GroupRoleHistory history = new GroupRoleHistory();
                    history.setGroup_id(groupId);
                    history.setUser_id(userId);
                    history.setOld_role(currentRole);
                    history.setNew_role(newRole);
                    history.setChanged_by(currentUserId);
                    historyDAO.addHistory(history);

                    resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&success=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
                } else {
                    resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&error=" + URLEncoder.encode("❌ Cập nhật role thất bại", StandardCharsets.UTF_8));
                }
                return;
            }

            // ======= 3️⃣ Action không hợp lệ =======
            resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&error=" + URLEncoder.encode("❌ Hành động không hợp lệ", StandardCharsets.UTF_8));

        } catch (Exception ex) {
            ex.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/group/manage?error=" + URLEncoder.encode("❌ Lỗi hệ thống: " + ex.getMessage(), StandardCharsets.UTF_8));
        }
    }
}
