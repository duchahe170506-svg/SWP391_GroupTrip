package controller;

import dal.TripDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import model.Trips;
import model.Users;

@WebServlet(name="AdminTripServlet", urlPatterns={"/admin/trips"})
public class AdminTripServlet extends HttpServlet {

    private TripDAO tripDAO = new TripDAO();
    private UserDAO userDAO = new UserDAO(); 

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        if (keyword == null) keyword = "";

        List<Trips> trips = tripDAO.getTripsWithSearch(keyword);
        req.setAttribute("trips", trips);
        req.setAttribute("keyword", keyword);

        req.getRequestDispatcher("/views/admin-trips.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("changeStatus".equals(action)) {
            int tripId = Integer.parseInt(req.getParameter("trip_id"));
            String currentStatus = req.getParameter("currentStatus");

            
            String newStatus = currentStatus.equals("Active") ? "Blocked" : "Active";
            tripDAO.updateStatus(tripId, newStatus);

            req.setAttribute("message", "Đã thay đổi trạng thái trip thành: " + newStatus);
        }

        resp.sendRedirect(req.getContextPath() + "/admin/trips");
    }
}
