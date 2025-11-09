package controller;

import dal.ReportsDAO;
import dal.TripDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import model.Reports;
import model.Users;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();
    private TripDAO tripDAO = new TripDAO();
    private ReportsDAO reportDAO = new ReportsDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Users
        req.setAttribute("totalUsers", userDAO.countUsers());
        req.setAttribute("users", userDAO.getAllUsers());

        // Trips
        Map<Integer, Integer> tripsPerMonth = tripDAO.countTripsByMonthThisYear();
        int totalTripsThisYear = tripsPerMonth.values().stream().mapToInt(Integer::intValue).sum();
        req.setAttribute("tripsPerMonth", tripsPerMonth);
        req.setAttribute("totalTripsThisYear", totalTripsThisYear);
        req.setAttribute("allTrips", tripDAO.getAllTrips());

        // Reports
        req.setAttribute("pendingReports", reportDAO.countReportsByStatus("PENDING"));
        req.setAttribute("inProgressReports", reportDAO.countReportsByStatus("IN_PROGRESS"));
        req.setAttribute("resolvedReports", reportDAO.countReportsByStatus("RESOLVED"));
        req.setAttribute("rejectedReports", reportDAO.countReportsByStatus("REJECTED"));

        List<Reports> reports = reportDAO.getAllReports();
        for (Reports r : reports) {
            r.setAttachments(reportDAO.getAttachmentsByReportId(r.getReport_id()));
        }
        req.setAttribute("reports", reports);

        req.getRequestDispatcher("/views/admin-dashboard.jsp").forward(req, resp);
    }

}
