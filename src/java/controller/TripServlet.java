package controller;

import dal.TripDAO;
import model.Trips;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/trips")
public class TripServlet extends HttpServlet {
    private TripDAO tripDAO;

    @Override
    public void init() {
        tripDAO = new TripDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy filter từ request
        String budgetStr = request.getParameter("budget");
        String startStr = request.getParameter("start_date");
        String endStr = request.getParameter("end_date");

        Double budget = (budgetStr != null && !budgetStr.isEmpty()) ? Double.parseDouble(budgetStr) : null;
        Date start = (startStr != null && !startStr.isEmpty()) ? Date.valueOf(startStr) : null;
        Date end = (endStr != null && !endStr.isEmpty()) ? Date.valueOf(endStr) : null;

        // Lấy list trip
        List<Trips> trips;
        if (budget != null || start != null || end != null) {
            trips = tripDAO.searchTrips(budget, start, end);
        } else {
            trips = tripDAO.getAllTrips();
        }

        // ✅ Lấy thông báo từ session (sau redirect từ POST)
        HttpSession session = request.getSession(false);
        if (session != null) {
            String successMessage = (String) session.getAttribute("successMessage");
            String errorMessage = (String) session.getAttribute("errorMessage");

            if (successMessage != null) {
                request.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
            }
            if (errorMessage != null) {
                request.setAttribute("errorMessage", errorMessage);
                session.removeAttribute("errorMessage");
            }
        }

        request.setAttribute("trips", trips);
        RequestDispatcher rd = request.getRequestDispatcher("/views/trips.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ✅ Xử lý tạo chuyến đi
        try {
            int group_id = Integer.parseInt(request.getParameter("group_id"));
            String name = request.getParameter("name");
            String location = request.getParameter("location");
            Date start_date = Date.valueOf(request.getParameter("start_date"));
            Date end_date = Date.valueOf(request.getParameter("end_date"));
            BigDecimal budget = new BigDecimal(request.getParameter("budget"));
            String status = request.getParameter("status");
            Timestamp created_at = new Timestamp(System.currentTimeMillis());

            Trips trip = new Trips(0, group_id, name, location, start_date, end_date, budget, status, created_at);

            boolean success = tripDAO.insertTrip(trip);

            HttpSession session = request.getSession();
            if (success) {
                session.setAttribute("successMessage", "✅ Tạo chuyến đi thành công!");
            } else {
                session.setAttribute("errorMessage", "❌ Không thể tạo chuyến đi!");
            }

            response.sendRedirect("trips");

        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "❌ Lỗi khi tạo chuyến đi: " + e.getMessage());
            response.sendRedirect("trips");
        }
    }
}