package controller;

import dal.TripDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import model.Trips;

@WebServlet(name = "TripDeleteServlet", urlPatterns = {"/trip/delete"})
public class TripDeleteServlet extends HttpServlet {

    private final TripDAO tripDAO = new TripDAO();

    /** Tính số ngày còn lại tới ngày khởi hành (để chặn xoá gần ngày đi) */
    private long daysUntilStart(java.util.Date startDate) {
        if (startDate == null) return Long.MIN_VALUE;
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        LocalDate start = new java.sql.Date(startDate.getTime()).toLocalDate();
        return ChronoUnit.DAYS.between(today, start);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/mytrips");
            return;
        }

        try {
            int tripId = Integer.parseInt(idStr);
            Trips trip = tripDAO.getTripById(tripId);

            if (trip == null) {
                resp.sendRedirect(req.getContextPath() + "/mytrips?notfound=1");
                return;
            }

            // ✅ Lấy thêm thông tin hiển thị
            int participants = tripDAO.getParticipantCountByTrip(tripId);
            long daysLeft = daysUntilStart(trip.getStartDate());

            req.setAttribute("trip", trip);
            req.setAttribute("participants", participants);
            req.setAttribute("daysLeft", daysLeft);

            // ✅ Hiển thị trang xác nhận xóa
            req.getRequestDispatcher("/views/trip_delete.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/mytrips");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/mytrips");
            return;
        }

        try {
            int tripId = Integer.parseInt(idStr);
            Trips trip = tripDAO.getTripById(tripId);

            if (trip == null) {
                resp.sendRedirect(req.getContextPath() + "/mytrips?notfound=1");
                return;
            }

            int participants = tripDAO.getParticipantCountByTrip(tripId);
            long daysLeft = daysUntilStart(trip.getStartDate());

            // ✅ Ràng buộc nghiệp vụ
            if (participants > 0) {
                // Có người tham gia -> không cho xoá
                resp.sendRedirect(req.getContextPath() + "/mytrips?cantdelete=1");
                return;
            }

            if (daysLeft <= 3) {
                // Gần ngày đi (≤3 ngày) -> không cho xoá
                resp.sendRedirect(req.getContextPath() + "/mytrips?closetostart=1");
                return;
            }

            // ✅ Tiến hành xoá
            boolean deleted = tripDAO.deleteTrip(tripId);
            if (deleted) {
                resp.sendRedirect(req.getContextPath() + "/mytrips?deleted=1");
            } else {
                resp.sendRedirect(req.getContextPath() + "/mytrips?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/mytrips?error=1");
        }
    }
}
