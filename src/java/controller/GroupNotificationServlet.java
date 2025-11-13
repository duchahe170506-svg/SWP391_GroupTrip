package controller;

import dal.*;
import model.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/group/notifications")
public class GroupNotificationServlet extends HttpServlet {

    private TripDAO tripDAO = new TripDAO();
    private TravelGroupsDAO travelGroupsDAO = new TravelGroupsDAO();
    private GroupLogDAO logDAO = new GroupLogDAO();
    private UserDAO userDAO = new UserDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

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
        boolean isLeader = travelGroupsDAO.isLeader(currentUser.getUser_id(), groupId);

        try {
            
            List<GroupRoleHistory> roleLogs = logDAO.getRoleHistory(groupId);
            List<GroupJoinRequests> joinLogs = logDAO.getJoinRequestsHistory(groupId);
            List<MemberRemovals> removalLogs = logDAO.getRemovalHistory(groupId);
            List<Map<String, Object>> announcementLogs = logDAO.getAnnouncementLogs(groupId);

            Set<Integer> userIds = new HashSet<>();
            for (GroupRoleHistory r : roleLogs) {
                userIds.add(r.getUser_id());
                userIds.add(r.getChanged_by());
            }
            for (GroupJoinRequests r : joinLogs) {
                userIds.add(r.getUser_id());
                if (r.getReviewed_by() != null) {
                    userIds.add(r.getReviewed_by());
                }
                if (r.getInvited_by() != null) {
                    userIds.add(r.getInvited_by());
                }
            }
            for (MemberRemovals r : removalLogs) {
                userIds.add(r.getRemoved_user_id());
                userIds.add(r.getRemoved_by());
            }

            Map<Integer, String> userMap = userDAO.getUserNamesByIds(userIds);

            List<Map<String, Object>> allLogs = new ArrayList<>();
            for (GroupRoleHistory r : roleLogs) {
                if (r.getChanged_at() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "role-change");
                    map.put("time", r.getChanged_at());
                    map.put("log", r);
                    allLogs.add(map);
                }
            }
            for (GroupJoinRequests r : joinLogs) {
                if (r.getRequested_at() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "join-invite");
                    map.put("time", r.getRequested_at());
                    map.put("log", r);
                    allLogs.add(map);
                }
            }
            for (MemberRemovals r : removalLogs) {
                if (r.getRemoved_at() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "removal");
                    map.put("time", r.getRemoved_at());
                    map.put("log", r);
                    allLogs.add(map);
                }
            }
            Set<String> seenAnnouncements = new HashSet<>();
            for (Map<String, Object> aLog : announcementLogs) {
                String key = aLog.get("message") + "|" + aLog.get("time"); // key duy nhất
                if (!seenAnnouncements.contains(key)) {
                    aLog.put("type", "announcement");
                    allLogs.add(aLog);
                    seenAnnouncements.add(key);
                }
            }

            allLogs.sort((a, b) -> ((Date) b.get("time")).compareTo((Date) a.get("time")));

            request.setAttribute("allLogs", allLogs);
            request.setAttribute("userMap", userMap);
            request.setAttribute("trip", trip);
            request.setAttribute("groupId", groupId);
            request.setAttribute("isLeader", isLeader);
            request.setAttribute("currentUser", currentUser);

            request.getRequestDispatcher("/views/group-notification.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Database error while fetching group notifications", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int groupId = Integer.parseInt(request.getParameter("groupId"));
        String message = request.getParameter("message");

        if (!travelGroupsDAO.isLeader(currentUser.getUser_id(), groupId)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to send announcements.");
            return;
        }

        List<Integer> activeMembers = travelGroupsDAO.getActiveMemberIds(groupId);
        activeMembers.remove(Integer.valueOf(currentUser.getUser_id()));
        notificationDAO.createBulkNotifications(currentUser.getUser_id(), activeMembers, "GROUP_ANNOUNCEMENT", groupId, message);

        doGet(request, response);
    }
}
