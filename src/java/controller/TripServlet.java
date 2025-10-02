package controller;

import dal.TripDAO;
import model.Trips;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@WebServlet("/trips")
public class TripServlet extends HttpServlet {
    private TripDAO tripDAO = new TripDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                tripDAO.deleteTrip(deleteId);
                response.sendRedirect("trips");
                break;
            case "search":
                int searchId = Integer.parseInt(request.getParameter("id"));
                Trips trip = tripDAO.getTripById(searchId);
                request.setAttribute("trip", trip);
                request.getRequestDispatcher("trip.jsp").forward(request, response);
                break;
            default: // list
                List<Trips> trips = tripDAO.getAllTrips();
                request.setAttribute("trips", trips);
                request.getRequestDispatcher("trip.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            Trips trip = new Trips();
            trip.setGroupId(Integer.parseInt(request.getParameter("groupId")));
            trip.setName(request.getParameter("name"));
            trip.setLocation(request.getParameter("location"));
            trip.setStartDate(Date.valueOf(request.getParameter("startDate")));
            trip.setEndDate(Date.valueOf(request.getParameter("endDate")));
            trip.setBudget(new BigDecimal(request.getParameter("budget")));
            trip.setStatus(request.getParameter("status"));
            tripDAO.addTrip(trip);
            response.sendRedirect("trips");
        } else if ("edit".equals(action)) {
            Trips trip = new Trips();
            trip.setTripId(Integer.parseInt(request.getParameter("tripId")));
            trip.setGroupId(Integer.parseInt(request.getParameter("groupId")));
            trip.setName(request.getParameter("name"));
            trip.setLocation(request.getParameter("location"));
            trip.setStartDate(Date.valueOf(request.getParameter("startDate")));
            trip.setEndDate(Date.valueOf(request.getParameter("endDate")));
            trip.setBudget(new BigDecimal(request.getParameter("budget")));
            trip.setStatus(request.getParameter("status"));
            tripDAO.updateTrip(trip);
            response.sendRedirect("trips");
        }
    }
}