package controller;

import dal.UserDAO;
import dal.GroupLogDAO;
import dal.TravelGroupsDAO;
import dal.TripDAO;
import model.GroupJoinRequests;
import model.GroupRoleHistory;
import model.MemberRemovals;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Groups;
import model.Trips;

@WebServlet("/group/notifications")
public class GroupNotificationServlet extends HttpServlet {
    private TripDAO tripDAO = new TripDAO();
    private TravelGroupsDAO travelGroupsDAO = new TravelGroupsDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String groupIdParam = request.getParameter("groupId");
        if (groupIdParam == null || groupIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing groupId parameter");
            return;
        }

        int groupId;
        try {
            groupId = Integer.parseInt(groupIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid groupId parameter");
            return;
        }

        Groups group = travelGroupsDAO.getGroupById(groupId);
        Trips trip = (group != null) ? tripDAO.getTripById(group.getGroup_id()) : null;

        GroupLogDAO logDAO = new GroupLogDAO();
        UserDAO userDAO = new UserDAO();

        try {
            List<GroupRoleHistory> roleLogs = logDAO.getRoleHistory(groupId);
            List<GroupJoinRequests> joinLogs = logDAO.getJoinRequestsHistory(groupId);
            List<MemberRemovals> removalLogs = logDAO.getRemovalHistory(groupId);

            Map<Integer, String> userMap = new HashMap<>();
            for (GroupRoleHistory r : roleLogs) {
                userMap.put(r.getUser_id(), userDAO.getUserById(r.getUser_id()).getName());
                userMap.put(r.getChanged_by(), userDAO.getUserById(r.getChanged_by()).getName());
            }
            for (GroupJoinRequests r : joinLogs) {
                userMap.put(r.getUser_id(), userDAO.getUserById(r.getUser_id()).getName());
                if (r.getReviewed_by() != null) userMap.put(r.getReviewed_by(), userDAO.getUserById(r.getReviewed_by()).getName());
                if (r.getInvited_by() != null) userMap.put(r.getInvited_by(), userDAO.getUserById(r.getInvited_by()).getName());
            }
            for (MemberRemovals r : removalLogs) {
                userMap.put(r.getRemoved_user_id(), userDAO.getUserById(r.getRemoved_user_id()).getName());
                userMap.put(r.getRemoved_by(), userDAO.getUserById(r.getRemoved_by()).getName());
            }

        
            List<LogEntry> allLogs = new java.util.ArrayList<>();

            for (GroupRoleHistory r : roleLogs) allLogs.add(new LogEntry("role-change", r.getChanged_at(), r));
            for (GroupJoinRequests r : joinLogs) allLogs.add(new LogEntry("join-invite", r.getRequested_at(), r));
            for (MemberRemovals r : removalLogs) allLogs.add(new LogEntry("removal", r.getRemoved_at(), r));

      
            allLogs.sort((a,b) -> b.getTime().compareTo(a.getTime()));

            request.setAttribute("allLogs", allLogs);
            request.setAttribute("userMap", userMap);
            request.setAttribute("trip", trip);
            request.setAttribute("groupId", groupId);

            request.getRequestDispatcher("/views/group-notification.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Database error while fetching group notifications", e);
        }
    }


    public static class LogEntry {
        private String type;
        private java.util.Date time;
        private Object log;

        public LogEntry(String type, java.util.Date time, Object log) {
            this.type = type;
            this.time = time;
            this.log = log;
        }

        public String getType() { return type; }
        public java.util.Date getTime() { return time; }
        public Object getLog() { return log; }
    }
}

