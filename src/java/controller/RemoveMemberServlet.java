package controller;

import dal.GroupMembersDAO;
import dal.NotificationDAO;
import dal.TravelGroupsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import model.Groups;
import model.Notifications;
import model.Users;

@WebServlet("/group/remove-member")
public class RemoveMemberServlet extends HttpServlet {

    private GroupMembersDAO memberDAO = new GroupMembersDAO();
    private TravelGroupsDAO travelGroupsDAO = new TravelGroupsDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            String msg = URLEncoder.encode("❌ Bạn cần đăng nhập để thực hiện hành động này", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/login?error=" + msg);
            return;
        }
        Users currentUser = (Users) session.getAttribute("currentUser");
        if (currentUser == null) {
            String msg = URLEncoder.encode("❌ Phiên đăng nhập hết hạn", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/login?error=" + msg);
            return;
        }

        String groupIdStr = request.getParameter("groupId");
        String userIdStr = request.getParameter("userId");
        String reason = request.getParameter("reason");

        int groupId = 0;
        try {
            groupId = Integer.parseInt(groupIdStr);
        } catch (Exception e) {
            String msg = URLEncoder.encode("❌ Tham số groupId không hợp lệ", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/group/manage?error=" + msg);
            return;
        }

        if (userIdStr == null || reason == null || reason.isEmpty()) {
            String msg = URLEncoder.encode("❌ Thiếu thông tin hoặc lý do xóa", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/group/manage?groupId=" + groupId + "&error=" + msg);
            return;
        }

        int userId = Integer.parseInt(userIdStr);
        Groups group = travelGroupsDAO.getGroupById(groupId);

        try {

            String roleToRemove = memberDAO.getRole(groupId, userId);
            if (roleToRemove == null) {
                String msg = URLEncoder.encode("❌ Không tìm thấy thành viên trong nhóm", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/group/manage?groupId=" + groupId + "&error=" + msg);
                return;
            }

            String currentUserRole = memberDAO.getRole(groupId, currentUser.getUser_id());
            if (!"Leader".equalsIgnoreCase(currentUserRole)) {
                String msg = URLEncoder.encode("❌ Bạn không có quyền xóa thành viên", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/group/manage?groupId=" + groupId + "&error=" + msg);
                return;
            }

            if (currentUser.getUser_id() == userId) {
                String msg = URLEncoder.encode("❌ Leader không được xóa chính mình", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/group/manage?groupId=" + groupId + "&error=" + msg);
                return;
            }

            boolean removed = memberDAO.removeMember(groupId, userId, currentUser.getUser_id(), reason);
            if (removed) {

                Notifications notification = new Notifications();
                notification.setSenderId(currentUser.getUser_id());
                notification.setUserId(userId);
                notification.setType("KICKED");
                notification.setRelatedId(groupId);
                notification.setMessage("Bạn bị kick khỏi chuyến đi \"" + group.getName() + "\" " );
                notification.setStatus("UNREAD");
                notification.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                NotificationDAO notificationDAO = new NotificationDAO();
                notificationDAO.createNotification(notification);

                String msg = URLEncoder.encode("✅ Đã xóa thành viên thành công", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/group/manage?groupId=" + groupId + "&success=" + msg);
            } else {
                String msg = URLEncoder.encode("❌ Không thể xóa thành viên", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/group/manage?groupId=" + groupId + "&error=" + msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String msg = URLEncoder.encode("❌ Lỗi hệ thống: " + e.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/group/manage?groupId=" + groupId + "&error=" + msg);
        }
    }
}
