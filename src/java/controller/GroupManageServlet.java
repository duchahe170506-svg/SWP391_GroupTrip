package controller;

import dal.GroupJoinRequestDAO;
import dal.GroupMembersDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Users;
import model.GroupJoinRequests;
import model.GroupMembers;

@WebServlet("/group/manage")
public class GroupManageServlet extends HttpServlet {

    private GroupMembersDAO memberDAO = new GroupMembersDAO();
    private GroupJoinRequestDAO requestDAO = new GroupJoinRequestDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int groupId = Integer.parseInt(req.getParameter("groupId"));
        
        // danh sách thành viên
        List<GroupMembers> members = memberDAO.getMembersByGroup(groupId);

        // đếm số yêu cầu join chưa duyệt
        List<GroupJoinRequests> requests = requestDAO.getRequestsByGroup(groupId);
        long pendingCount = requests.stream().filter(r -> r.getStatus().equals("PENDING")).count();

        req.setAttribute("groupId", groupId);
        req.setAttribute("members", members);
        req.setAttribute("pendingCount", pendingCount);

        req.getRequestDispatcher("/views/group-manage.jsp").forward(req, resp);
    }
}
