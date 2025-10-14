package controller;

import dal.GroupMembersDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/group/edit-role")
public class EditRoleServlet extends HttpServlet {

    private GroupMembersDAO memberDAO = new GroupMembersDAO();
    private static final int ROOT_LEADER_ID = 14; // cố định leader gốc

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String gid = req.getParameter("groupId");
            String uid = req.getParameter("userId");

            String baseRedirect = req.getContextPath() + "/group/manage";

            if (gid == null || gid.isBlank()) {
                String msg = URLEncoder.encode("❌ Thiếu tham số groupId", StandardCharsets.UTF_8.name());
                resp.sendRedirect(baseRedirect + "?error=" + msg);
                return;
            }
            if (uid == null || uid.isBlank()) {
                String msg = URLEncoder.encode("❌ Thiếu tham số userId", StandardCharsets.UTF_8.name());
                resp.sendRedirect(baseRedirect + "?groupId=" + gid + "&error=" + msg);
                return;
            }

            int groupId = Integer.parseInt(gid);
            int userId = Integer.parseInt(uid);

            String currentRole = memberDAO.getRole(groupId, userId);
            if (currentRole == null) {
                String msg = URLEncoder.encode("❌ Không tìm thấy thành viên trong nhóm", StandardCharsets.UTF_8.name());
                resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&error=" + msg);
                return;
            }

            int leaderCount = memberDAO.countRole(groupId, "Leader");
            int memberCount = memberDAO.countRole(groupId, "Member");
            int totalCount  = memberDAO.countMembers(groupId);

            // Leader gốc hạ xuống
            if (userId == ROOT_LEADER_ID && "Leader".equalsIgnoreCase(currentRole)) {
                if (leaderCount <= 1) {
                    String msg = URLEncoder.encode("❌ Phải có ít nhất 1 leader khác trước khi hạ leader gốc", StandardCharsets.UTF_8.name());
                    resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&error=" + msg);
                    return;
                }
                memberDAO.updateRole(groupId, userId, "Member");
                String msg = URLEncoder.encode("✅ Đã hạ leader gốc xuống Member", StandardCharsets.UTF_8.name());
                resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&success=" + msg);
                return;
            }

            // Member -> Leader
            if ("Member".equalsIgnoreCase(currentRole)) {
                if (memberCount <= 1 && totalCount > 1) {
                    String msg = URLEncoder.encode("❌ Nhóm phải còn ít nhất 1 Member, không thể nâng tất cả lên Leader!", StandardCharsets.UTF_8.name());
                    resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&error=" + msg);
                    return;
                }
                memberDAO.updateRole(groupId, userId, "Leader");
                String msg = URLEncoder.encode("✅ Đã nâng thành Leader", StandardCharsets.UTF_8.name());
                resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&success=" + msg);
                return;
            }

            // Leader -> Member
            if ("Leader".equalsIgnoreCase(currentRole)) {
                if (leaderCount <= 1) {
                    String msg = URLEncoder.encode("❌ Nhóm phải có ít nhất 1 leader", StandardCharsets.UTF_8.name());
                    resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&error=" + msg);
                    return;
                }
//                if (memberCount <= 1 && totalCount > 1) {
//                    String msg = URLEncoder.encode("❌ Nhóm phải có ít nhất 1 member", StandardCharsets.UTF_8.name());
//                    resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&error=" + msg);
//                    return;
//                }
                memberDAO.updateRole(groupId, userId, "Member");
                String msg = URLEncoder.encode("✅ Đã hạ xuống Member", StandardCharsets.UTF_8.name());
                resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&success=" + msg);
                return;
            }

            String msg = URLEncoder.encode("❌ Role hiện tại không được hỗ trợ: " + currentRole, StandardCharsets.UTF_8.name());
            resp.sendRedirect(baseRedirect + "?groupId=" + groupId + "&error=" + msg);

        } catch (Exception ex) {
            ex.printStackTrace();
            String msg = URLEncoder.encode("❌ Lỗi hệ thống: " + ex.getMessage(), StandardCharsets.UTF_8.name());
            resp.sendRedirect(req.getContextPath() + "/group/manage?error=" + msg);
        }
    }
}
