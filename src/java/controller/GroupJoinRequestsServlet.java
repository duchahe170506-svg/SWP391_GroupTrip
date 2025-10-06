package controller;

import dal.GroupJoinRequestDAO;
import dal.GroupMembersDAO;
import dal.TravelGroupsDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.GroupJoinRequests;
import model.Groups;
import model.Users;
import util.MailUtil;

@WebServlet("/group/join-requests")
public class GroupJoinRequestsServlet extends HttpServlet {

    private GroupJoinRequestDAO requestDAO = new GroupJoinRequestDAO();
    private GroupMembersDAO memberDAO = new GroupMembersDAO();
    private UserDAO userDAO = new UserDAO();
    private TravelGroupsDAO travelGroupsDAO= new TravelGroupsDAO();
    private GroupMembersDAO groupMembersDAO= new GroupMembersDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int groupId = Integer.parseInt(req.getParameter("groupId"));
        List<GroupJoinRequests> requests = requestDAO.getRequestsByGroup(groupId);
        req.setAttribute("requests", requests);
        req.setAttribute("groupId", groupId);
        req.getRequestDispatcher("/views/join-requests.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int requestId = Integer.parseInt(req.getParameter("requestId"));
        int groupId = Integer.parseInt(req.getParameter("groupId"));
        String action = req.getParameter("action");

        int reviewedBy = 14; // fix cứng leader đang login giả định

        GroupJoinRequests request = requestDAO.getRequestById(requestId);
        if (request == null) {
            resp.sendRedirect("join-requests?groupId=" + groupId);
            return;
        }

        int userId = request.getUser_id();
        Users user = userDAO.getUserById(userId);

        Groups group = travelGroupsDAO.getGroupById(groupId);
        Users leader = groupMembersDAO.getGroupLeader(groupId);

        if ("approve".equals(action)) {
            requestDAO.updateStatusByUserAndGroup(userId, groupId, "APPROVED", reviewedBy);
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
        }

        resp.sendRedirect("join-requests?groupId=" + groupId);
    }

}
