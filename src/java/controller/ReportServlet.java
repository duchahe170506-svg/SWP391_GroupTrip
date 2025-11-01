package controller;

import dal.ReportsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import java.util.Collection;
import java.util.List;
import model.ReportAttachments;
import model.Reports;
import model.Users;

@WebServlet("/report")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class ReportServlet extends HttpServlet {

    private final ReportsDAO dao = new ReportsDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Users currentUser = (Users) (session != null ? session.getAttribute("currentUser") : null);
        if (currentUser == null) {
            resp.sendRedirect("login");
            return;
        }

        List<Reports> reports = dao.getReportsByReporterId(currentUser.getUser_id());

        for (Reports r : reports) {
            r.setAttachments(dao.getAttachmentsByReportId(r.getReport_id()));
        }

        req.setAttribute("reports", reports);
        req.getRequestDispatcher("/views/profile_reports.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        Users currentUser = (Users) (session != null ? session.getAttribute("currentUser") : null);

        if (currentUser == null) {
            resp.sendRedirect("login");
            return;
        }

        String action = req.getParameter("action");

        if ("edit".equals(action)) {
            handleEdit(req, resp, currentUser);
        } else if ("delete".equals(action)) {
            handleDelete(req, resp, currentUser);
        } else {
            handleCreate(req, resp, currentUser);
        }
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp, Users currentUser)
            throws IOException, ServletException {
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String type = req.getParameter("type");

        String reportedUserIdStr = req.getParameter("reportedUserId");
        String removalIdStr = req.getParameter("removalId");

        int groupId = dao.getGroupIdByRemovalId(Integer.parseInt(removalIdStr));

        Reports report = new Reports();
        report.setReporter_id(currentUser.getUser_id());
        report.setTitle(title);
        report.setDescription(description);
        report.setType(type);
        report.setGroup_id(groupId);

        if (reportedUserIdStr != null && !reportedUserIdStr.isBlank()) {
            report.setReported_user_id(Integer.parseInt(reportedUserIdStr));
        }
        if (removalIdStr != null && !removalIdStr.isBlank()) {
            report.setRemoval_id(Integer.parseInt(removalIdStr));
        }

        int reportId = dao.createReport(report);
        if (reportId <= 0) {
            resp.sendRedirect("report?msg=failed");
            return;
        }

        uploadAttachments(req, reportId);
        resp.sendRedirect("report?msg=report_submitted");
    }

    private void handleEdit(HttpServletRequest req, HttpServletResponse resp, Users currentUser)
            throws IOException, ServletException {

        int reportId = Integer.parseInt(req.getParameter("reportId"));
        Reports old = dao.getReportById(reportId);

        if (old == null || old.getReporter_id() != currentUser.getUser_id() || !"PENDING".equals(old.getStatus())) {
            resp.sendRedirect("report?msg=unauthorized");
            return;
        }

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String type = req.getParameter("type");

        old.setTitle(title);
        old.setDescription(description);
        old.setType(type);
        dao.updateReport(old);

        String deleteOld = req.getParameter("deleteOldAttachments");
        if ("true".equals(deleteOld)) {
            List<ReportAttachments> oldAttachments = dao.getAllAttachmentsByReportId(reportId);
            for (ReportAttachments a : oldAttachments) {
                File file = new File(getServletContext().getRealPath("/") + a.getFilePath());
                if (file.exists()) {
                    file.delete();
                }
                dao.deleteAttachment(a.getAttachmentId());
            }
        }

        uploadAttachments(req, reportId);

        resp.sendRedirect("report?msg=report_updated");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp, Users currentUser)
            throws IOException {
        int reportId = Integer.parseInt(req.getParameter("reportId"));
        Reports r = dao.getReportById(reportId);

        if (r == null || r.getReporter_id() != currentUser.getUser_id() || !"PENDING".equals(r.getStatus())) {
            resp.sendRedirect("report?msg=unauthorized");
            return;
        }

        dao.deleteReport(reportId);
        resp.sendRedirect("report?msg=report_deleted");
    }

    private void uploadAttachments(HttpServletRequest req, int reportId)
            throws IOException, ServletException {
        String uploadPath = getServletContext().getRealPath("/") + "uploads/reports";
        Files.createDirectories(Paths.get(uploadPath));

        for (Part part : req.getParts()) {
            if ("attachments".equals(part.getName()) && part.getSize() > 0) {
                String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                String uniqueName = System.currentTimeMillis() + "_" + fileName;
                String savePath = uploadPath + File.separator + uniqueName;
                part.write(savePath);
                dao.addAttachment(reportId, uniqueName, "uploads/reports/" + uniqueName);
            }
        }
    }

}
