package controller;

import dal.ItineraryDAO;
import dal.TripDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Itinerary;
import model.Trips;
import model.Users;

@WebServlet("/group/manage/timeline")
public class TimelineServlet extends HttpServlet {

    private final TripDAO tripDAO = new TripDAO();
    private final ItineraryDAO itineraryDAO = new ItineraryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Users currentUser = (Users) session.getAttribute("currentUser");
        int currentUserId = currentUser.getUser_id();

        String groupIdParam = request.getParameter("groupId");
        if (groupIdParam == null || groupIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        int groupId = Integer.parseInt(groupIdParam);
        int tripId = tripDAO.getTripIdByGroupId(groupId);
        if (tripId == -1) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Trips trip = tripDAO.getTripById(tripId);
        if (trip == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        List<Itinerary> itineraries = itineraryDAO.getItinerariesByTripId(tripId);
        boolean isLeader = tripDAO.isUserLeaderOfGroup(currentUserId, groupId);

        request.setAttribute("groupId", groupId);
        request.setAttribute("tripId", tripId);
        request.setAttribute("trip", trip);
        request.setAttribute("itineraries", itineraries);
        request.setAttribute("isLeader", isLeader);
        request.getRequestDispatcher("/views/group-timeline.jsp").forward(request, response);
    }
}

