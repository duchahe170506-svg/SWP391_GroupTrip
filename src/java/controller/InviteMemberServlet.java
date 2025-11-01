package controller;

import dal.GroupJoinRequestDAO;
import dal.UserDAO;
import dal.TravelGroupsDAO;
import dal.GroupMembersDAO;
import dal.NotificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import model.Groups;
import model.Notifications;
import model.Users;
import util.MailUtil;

@WebServlet("/group/invite")
public class InviteMemberServlet extends HttpServlet {

    private GroupJoinRequestDAO requestDAO = new GroupJoinRequestDAO();
    private UserDAO userDAO = new UserDAO();
    private TravelGroupsDAO groupDAO = new TravelGroupsDAO();
    private GroupMembersDAO memberDAO = new GroupMembersDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Users currentUser = (session != null) ? (Users) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int invitedBy = currentUser.getUser_id();

        String email = request.getParameter("email");
        String groupIdStr = request.getParameter("groupId");

        if (email == null || groupIdStr == null || email.trim().isEmpty() || groupIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/group/manage?groupId=" + groupIdStr
                    + "&inviteError=" + URLEncoder.encode("Thiếu thông tin bắt buộc!", "UTF-8"));
            return;
        }

        int groupId = Integer.parseInt(groupIdStr);

        String result = requestDAO.sendInvite(groupId, email.trim(), invitedBy);

        if (result.startsWith("✅")) {
            Users invitedUser = userDAO.getUserByEmail(email.trim());
            Groups group = groupDAO.getGroupById(groupId);
            Users leader = memberDAO.getGroupLeader(groupId);

            if (invitedUser != null && group != null && leader != null) {
                String subject = "📩 Lời mời tham gia nhóm du lịch";
                String body = "Xin chào " + invitedUser.getName() + ",\n\n"
                        + "Bạn vừa được mời tham gia nhóm \"" + group.getName() + "\" bởi "
                        + leader.getName() + ".\n\n"
                        + "Mô tả nhóm: " + group.getDescription() + "\n"
                        + "Hãy đăng nhập để xem chi tiết lời mời và chấp nhận nếu bạn muốn tham gia.\n\n"
                        + "Trân trọng,\nTravelGroup Team";

                MailUtil.sendEmail(invitedUser.getEmail(), subject, body);

                Notifications n = new Notifications();
                n.setSenderId(currentUser.getUser_id());
                n.setUserId(invitedUser.getUser_id());
                n.setType("INVITE");
                n.setRelatedId(group.getGroup_id()); 
                n.setMessage("Bạn đã được mời tham gia nhóm \"" + group.getName() + "\" bởi " + currentUser.getName());
                n.setStatus("UNREAD");

                NotificationDAO notificationDAO = new NotificationDAO();
                notificationDAO.createNotification(n);
            }

            response.sendRedirect(request.getContextPath() + "/group/manage?groupId=" + groupId
                    + "&inviteSuccess=" + URLEncoder.encode(result, "UTF-8"));
        } else {
            response.sendRedirect(request.getContextPath() + "/group/manage?groupId=" + groupId
                    + "&inviteError=" + URLEncoder.encode(result, "UTF-8"));
        }
    }
}
