package controller;

import dal.ItineraryDAO;
import dal.TripDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Itinerary;
import model.Trips;
import model.Users;

@WebServlet("/group/manage/itinerary-delete")
public class ItineraryDeleteServlet extends HttpServlet {

    private final ItineraryDAO itineraryDAO = new ItineraryDAO();
    private final TripDAO tripDAO = new TripDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Users currentUser = (Users) session.getAttribute("currentUser");
        int itineraryId = parseInt(request.getParameter("itinerary_id"));
        if (itineraryId <= 0) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Itinerary itinerary = itineraryDAO.getItineraryById(itineraryId);
        if (itinerary == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Trips trip = tripDAO.getTripById(itinerary.getTripId());
        if (trip == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        String groupParam = request.getParameter("groupId");
        int groupId = Integer.parseInt(groupParam);
        boolean isLeader = tripDAO.isUserLeaderOfGroup(currentUser.getUser_id(), groupId);
        if (!isLeader) {
            response.sendRedirect(request.getContextPath() + "/group/manage/timeline?groupId=" + groupId + "&error=permission");
            return;
        }

        boolean success = itineraryDAO.deleteItinerary(itineraryId);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/group/manage/timeline?groupId=" + groupId + "&deleteSuccess=1");
        } else {
            response.sendRedirect(request.getContextPath() + "/group/manage/timeline?groupId=" + groupId + "&error=delete");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/home");
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return -1;
        }
    }
}

