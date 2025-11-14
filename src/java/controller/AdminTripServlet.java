package controller;

import dal.GroupMembersDAO;
import dal.NotificationDAO;
import dal.TravelGroupsDAO;
import dal.TripDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import model.GroupMembers;
import model.Groups;
import model.Notifications;
import model.Trips;
import model.Users;

@WebServlet(name = "AdminTripServlet", urlPatterns = {"/admin/trips"})
public class AdminTripServlet extends HttpServlet {

    private TripDAO tripDAO = new TripDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }

        List<Trips> trips = tripDAO.getTripsWithSearch(keyword);
        req.setAttribute("trips", trips);
        req.setAttribute("keyword", keyword);

        req.getRequestDispatcher("/views/admin-trips.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Users admin = (Users) (session != null ? session.getAttribute("currentUser") : null);
        if (admin == null) {
            resp.sendRedirect("login");
            return;
        }

        String action = req.getParameter("action");

        if ("changeStatus".equals(action)) {
            int tripId = Integer.parseInt(req.getParameter("trip_id"));
            int groupId = Integer.parseInt(req.getParameter("trip_id"));
            TripDAO tripDAO = new TripDAO();
            Trips trip = tripDAO.getTripById(tripId);
            String currentStatus = req.getParameter("currentStatus");
            GroupMembersDAO groupMemberDAO = new GroupMembersDAO();
            Users groupMember = groupMemberDAO.getGroupLeader(groupId);

            String newStatus = currentStatus.equals("Active") ? "Blocked" : "Active";
            tripDAO.updateStatus(tripId, newStatus);

            Notifications n = new Notifications();
            n.setSenderId(admin.getUser_id());
            n.setUserId(groupMember.getUser_id());
            n.setType("ADMIN_ANNOUNCEMENT");
            n.setRelatedId(groupId);
            n.setMessage("Chuyến đi \"" + trip.getName() + "\" của bạn đã đổi trạng thái thành "+ newStatus + " bởi " + admin.getName());
            n.setStatus("UNREAD");

            NotificationDAO notificationDAO = new NotificationDAO();
            notificationDAO.createNotification(n);

            req.setAttribute("message", "Đã thay đổi trạng thái trip thành: " + newStatus);
        }

        resp.sendRedirect(req.getContextPath() + "/admin/trips");
    }
}
