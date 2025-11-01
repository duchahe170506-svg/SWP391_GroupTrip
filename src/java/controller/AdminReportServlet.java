package controller;

import dal.ReportsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.Reports;
import model.Users;
import java.util.List;

@WebServlet("/admin/reports")
public class AdminReportServlet extends HttpServlet {

    private final ReportsDAO dao = new ReportsDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Users admin = (Users) (session != null ? session.getAttribute("currentUser") : null);

        List<Reports> reports = dao.getAllReports();

        for (Reports r : reports) {
            r.setAttachments(dao.getAttachmentsByReportId(r.getReport_id()));
        }

        req.setAttribute("reports", reports);
        req.getRequestDispatcher("/views/admin_reports.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        int reportId = Integer.parseInt(req.getParameter("report_id"));
        String response = req.getParameter("admin_response");
        String status = req.getParameter("status");

        HttpSession session = req.getSession(false);
        Users admin = (Users) (session != null ? session.getAttribute("currentUser") : null);
        if (admin == null) {
            resp.sendRedirect("login");
            return;
        }

        boolean updated = dao.updateAdminResponse(reportId, response, status, admin.getUser_id());
        if (updated) {
            resp.sendRedirect("reports?msg=updated_success");
        } else {
            resp.sendRedirect("reports?msg=updated_failed");
        }
    }
}
