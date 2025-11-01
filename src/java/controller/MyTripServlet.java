package controller;

import dal.TripDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import model.Trips;
import model.Users;

@WebServlet(name = "MyTripServlet", urlPatterns = {"/mytrips"})
public class MyTripServlet extends HttpServlet {

    private final TripDAO tripDAO = new TripDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }


        Users currentUser = (Users) session.getAttribute("currentUser");
        int userId = currentUser.getUser_id(); // userId thực tế
    
        List<Trips> joinedTrips = tripDAO.getTripsByMember(userId);

      
        List<Trips> leaderTrips = tripDAO.getTripsByLeader(userId);

        req.setAttribute("joinedTrips", joinedTrips);
        req.setAttribute("leaderTrips", leaderTrips);

        req.getRequestDispatcher("/views/mytrip.jsp").forward(req, resp);
    }
}
