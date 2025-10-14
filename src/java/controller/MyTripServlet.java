package controller;

import dal.TripDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import model.Trips;

@WebServlet(name = "MyTripServlet", urlPatterns = {"/mytrips"})
public class MyTripServlet extends HttpServlet {

    private final TripDAO tripDAO = new TripDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
//        Integer userId = (Integer) session.getAttribute("userId");
//
//        if (userId == null) {
//            resp.sendRedirect(req.getContextPath() + "/login");
//            return;
//        }
        int userId = 1;
        // Danh sách chuyến đi đã tham gia
        List<Trips> joinedTrips = tripDAO.getTripsByMember(userId);

        // Danh sách chuyến đi mà mình là leader
        List<Trips> leaderTrips = tripDAO.getTripsByLeader(userId);

        req.setAttribute("joinedTrips", joinedTrips);
        req.setAttribute("leaderTrips", leaderTrips);

        req.getRequestDispatcher("/views/mytrip.jsp").forward(req, resp);
    }
}
