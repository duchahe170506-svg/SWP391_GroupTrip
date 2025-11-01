package controller;

import dal.GroupJoinRequestDAO;
import dal.GroupMembersDAO;
import dal.NotificationDAO;
import dal.TravelGroupsDAO;
import dal.TripDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import model.GroupJoinRequests;
import model.GroupMembers;
import model.Groups;
import model.Notifications;
import model.Trips;
import model.Users;
import util.MailUtil;

@WebServlet("/group/manage")
public class GroupManageServlet extends HttpServlet {

    private GroupMembersDAO memberDAO = new GroupMembersDAO();
    private GroupJoinRequestDAO requestDAO = new GroupJoinRequestDAO();
    private UserDAO userDAO = new UserDAO();
    private TravelGroupsDAO travelGroupsDAO = new TravelGroupsDAO();
    private TripDAO tripDAO = new TripDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int groupId = Integer.parseInt(req.getParameter("groupId"));

        Groups group = travelGroupsDAO.getGroupById(groupId);
        Trips trip = (group != null) ? tripDAO.getTripById(group.getGroup_id()) : null;

        List<GroupMembers> members = memberDAO.getMembersByGroup(groupId);
        List<GroupJoinRequests> userRequests = requestDAO.getUserRequests(groupId);
        List<GroupJoinRequests> leaderInvites = requestDAO.getLeaderInvites(groupId);

        Users leader = memberDAO.getGroupLeader(groupId);
        Integer leaderId = (leader != null) ? leader.getUser_id() : null;
        req.setAttribute("leaderId", leaderId);

        req.setAttribute("groupId", groupId);
        req.setAttribute("members", members);
        req.setAttribute("trip", trip);
        req.setAttribute("userRequests", userRequests);
        req.setAttribute("leaderInvites", leaderInvites);

        List<GroupJoinRequests> allRequests = new ArrayList<>();
        allRequests.addAll(userRequests);
        allRequests.addAll(leaderInvites);

        List<Users> userList = new ArrayList<>();
        Set<Integer> seenUserIds = new HashSet<>();
        for (GroupJoinRequests r : allRequests) {
            int[] ids = {
                r.getUser_id(),
                r.getInvited_by() != null ? r.getInvited_by() : 0,
                r.getReviewed_by() != null ? r.getReviewed_by() : 0
            };
            for (int id : ids) {
                if (id > 0 && !seenUserIds.contains(id)) {
                    Users u = userDAO.getUserById(id);
                    if (u != null) {
                        userList.add(u);
                        seenUserIds.add(id);
                    }
                }
            }
        }

        Map<Integer, String> userMap = new HashMap<>();
        Map<Integer, String> emailMap = new HashMap<>();

        for (Users u : userList) {
            userMap.put(u.getUser_id(), u.getName());
            emailMap.put(u.getUser_id(), u.getEmail());
        }

        req.setAttribute("userMap", userMap);
        req.setAttribute("emailMap", emailMap);
        req.setAttribute("userList", userList);

        req.getRequestDispatcher("/views/group-manage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int requestId = Integer.parseInt(req.getParameter("requestId"));
        int groupId = Integer.parseInt(req.getParameter("groupId"));
        String action = req.getParameter("action");

        HttpSession session = req.getSession(false);
        Users currentUser = (Users) session.getAttribute("currentUser");
        int reviewedBy = (currentUser != null) ? currentUser.getUser_id() : 0;

        GroupJoinRequests request = requestDAO.getRequestById(requestId);
        if (request == null) {
            resp.sendRedirect("manage?groupId=" + groupId + "&showRequests=true");
            return;
        }

        int userId = request.getUser_id();
        Users user = userDAO.getUserById(userId);
        Groups group = travelGroupsDAO.getGroupById(groupId);
        Users leader = memberDAO.getGroupLeader(groupId);

        if ("approve".equals(action)) {
            Trips trip = tripDAO.getTripById(groupId);
            int maxParticipants = trip.getMax_participants();
            int currentMembers = memberDAO.countActiveMembers(groupId);

            if (currentMembers >= maxParticipants) {
                // Không thể thêm, gửi thông báo lỗi
                String msg = URLEncoder.encode("❌ Nhóm đã đủ số lượng tối đa của chuyến đi.", StandardCharsets.UTF_8.name());
                resp.sendRedirect("manage?groupId=" + groupId + "&error=" + msg + "&showRequests=true");
                return;
            }

            requestDAO.updateStatusByUserAndGroup(userId, groupId, "ACCEPTED", reviewedBy);
            memberDAO.addMemberIfNotExists(groupId, userId, "Member");

            if (user != null && group != null) {
                String leaderName = (leader != null) ? leader.getName() : "Leader";
                String subject = "Yêu cầu tham gia nhóm đã được chấp nhận";
                String body = "Xin chào " + user.getName() + ",\n\n"
                        + "Yêu cầu tham gia nhóm \"" + group.getName() + "\" của bạn đã được CHẤP NHẬN.\n\n"
                        + "Thông tin nhóm:\n"
                        + "- Tên nhóm: " + group.getName() + "\n"
                        + "- Mô tả: " + group.getDescription() + "\n"
                        + "- Ngày tạo: " + group.getCreated_at() + "\n"
                        + "- Leader: " + leaderName + "\n\n"
                        + "Chúc bạn có trải nghiệm vui vẻ!";
                MailUtil.sendEmail(user.getEmail(), subject, body);
            }

        } else if ("reject".equals(action)) {
            requestDAO.updateStatusByUserAndGroup(userId, groupId, "REJECTED", reviewedBy);

            if (user != null && group != null) {
                String leaderName = (leader != null) ? leader.getName() : "Leader";
                String subject = "Yêu cầu tham gia nhóm đã bị từ chối";
                String body = "Xin chào " + user.getName() + ",\n\n"
                        + "Yêu cầu tham gia nhóm \"" + group.getName() + "\" đã bị TỪ CHỐI bởi leader " + leaderName + ".\n\n"
                        + "Thông tin nhóm:\n"
                        + "- Tên nhóm: " + group.getName() + "\n"
                        + "- Mô tả: " + group.getDescription() + "\n"
                        + "- Ngày tạo: " + group.getCreated_at() + "\n"
                        + "- Leader: " + leaderName + "\n\n"
                        + "Bạn có thể thử tham gia lại sau.";
                MailUtil.sendEmail(user.getEmail(), subject, body);
            }
        } else if ("cancel".equals(action)) {
            int actorId = currentUser.getUser_id();
            String message = "";

            if (!"INVITED".equals(request.getStatus())) {
                message = "❌ Lời mời này không thể thu hồi.";
            } else if (request.getInvited_by() == null) {
                message = "❌ Bạn không thể thu hồi lời mời này.";
            } else if (Objects.equals(request.getInvited_by(), actorId)) {
                boolean success = requestDAO.cancelInvite(requestId, actorId);
                if (success) {
                    message = "✅ Thu hồi lời mời thành công.";

                    if (user != null && group != null) {
                        String subject = "Lời mời tham gia nhóm đã bị thu hồi";
                        String body = "Xin chào " + user.getName() + ",\n\n"
                                + "Lời mời tham gia nhóm \"" + group.getName() + "\" đã bị thu hồi bởi người gửi.\n\n"
                                + "Bạn sẽ không thể chấp nhận lời mời này nữa.";
                        MailUtil.sendEmail(user.getEmail(), subject, body);

                        Notifications notification = new Notifications();
                        notification.setSenderId(actorId);
                        notification.setUserId(user.getUser_id());
                        notification.setType("INVITE_CANCELLED"); 
                        notification.setRelatedId(group.getGroup_id()); 
                        notification.setMessage("Lời mời tham gia nhóm \"" + group.getName() + "\" đã bị thu hồi bởi " + currentUser.getName());
                        notification.setStatus("UNREAD");
                        notification.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                        NotificationDAO notificationDAO = new NotificationDAO();
                        notificationDAO.createNotification(notification);
                    }
                } else {
                    message = "❌ Thu hồi thất bại, vui lòng thử lại.";
                }
            } else {
                message = "❌ Bạn không phải là người gửi, không thể thu hồi.";
            }

            String encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.name());
            resp.sendRedirect("manage?groupId=" + groupId + "&showRequests=true&inviteError=" + encodedMsg);
            return;
        }

        resp.sendRedirect("manage?groupId=" + groupId + "&showRequests=true");
    }
}
