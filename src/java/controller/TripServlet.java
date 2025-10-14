
// Nếu dùng Tomcat 10+ (jakarta), giữ nguyên import dưới.
// Nếu Tomcat 9, đổi tất cả 'jakarta.' thành 'javax.' và sửa dependency cho phù hợp.
package controller;

import dal.TripDAO;
import model.Trips;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "TripServlet", urlPatterns = {"/trips"})
public class TripServlet extends HttpServlet {

    private final TripDAO tripDAO = new TripDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // --- Lấy filter từ query string ---
        String budget    = param(request, "budget");      // "", "0-3000000", "3000000-7000000", "7000000+"
        String departOn  = param(request, "departOn");    // yyyy-MM-dd
        String from      = param(request, "from");        // yyyy-MM-dd
        String to        = param(request, "to");          // yyyy-MM-dd
        String tripType  = param(request, "tripType");    // loại chuyến đi
        String meeting   = param(request, "meeting_point"); // 🆕 tìm theo địa điểm tập trung (tuỳ chọn)
        String minPStr   = param(request, "min_participants"); // 🆕 lọc theo số người tối thiểu

        // --- Parse dữ liệu lọc ---
        BigDecimal minB = null, maxB = null;
        if (!budget.isEmpty()) {
            try {
                if (budget.endsWith("+")) {
                    minB = new BigDecimal(budget.replace("+", "").trim());
                } else if (budget.contains("-")) {
                    String[] p = budget.split("-");
                    minB = new BigDecimal(p[0].trim());
                    maxB = new BigDecimal(p[1].trim());
                }
            } catch (NumberFormatException ignore) { /* bỏ qua nếu người dùng nhập sai */ }
        }

        Date departDate = parseDate(departOn);
        Date fromDate   = parseDate(from);
        Date toDate     = parseDate(to);

        Integer minParticipants = null;
        try {
            if (!minPStr.isEmpty()) {
                minParticipants = Integer.parseInt(minPStr);
            }
        } catch (NumberFormatException ignore) { /* bỏ qua nếu người dùng nhập sai */ }

        // --- Lấy danh sách ---
        List<Trips> trips;
        boolean hasFilter = (
                minB != null || maxB != null ||
                departDate != null || fromDate != null || toDate != null ||
                !tripType.isEmpty() || !meeting.isEmpty() || minParticipants != null
        );

        if (hasFilter) {
            // ✅ Tìm theo điều kiện + meeting_point + min_participants
            trips = filterTrips(minB, maxB, departDate, fromDate, toDate, tripType, meeting, minParticipants);
        } else {
            trips = tripDAO.getAllTrips();
        }

        // --- Đếm số người tham gia ---
        Map<Integer, Integer> participantMap = new HashMap<>();
        for (Trips t : trips) {
            participantMap.put(t.getTripId(), tripDAO.getParticipantCountByTrip(t.getTripId()));
        }

        // --- Gửi dữ liệu sang JSP ---
        request.setAttribute("trips", trips);
        request.setAttribute("participantMap", participantMap);
        request.setAttribute("budget", budget);
        request.setAttribute("departOn", departOn);
        request.setAttribute("from", from);
        request.setAttribute("to", to);
        request.setAttribute("tripType", tripType);
        request.setAttribute("meeting_point", meeting);
        request.setAttribute("min_participants", minPStr);

        request.getRequestDispatcher("/views/trip.jsp").forward(request, response);
    }

    /**
     * Lọc chuyến đi theo các tham số — mở rộng thêm meeting_point + min_participants
     */
    private List<Trips> filterTrips(BigDecimal minB, BigDecimal maxB,
                                   Date departDate, Date fromDate, Date toDate,
                                   String tripType, String meetingPoint, Integer minParticipants) {

        // 🔹 Gọi hàm searchTrips hiện tại trong TripDAO
        List<Trips> base = tripDAO.searchTrips(minB, maxB, departDate, fromDate, toDate, tripType);

        // 🔹 Lọc thêm bằng Java cho 2 trường mới
        List<Trips> result = new ArrayList<>();
        for (Trips t : base) {
            boolean ok = true;
            if (meetingPoint != null && !meetingPoint.isEmpty()) {
                if (t.getMeeting_point() == null || !t.getMeeting_point().toLowerCase().contains(meetingPoint.toLowerCase())) {
                    ok = false;
                }
            }
            if (minParticipants != null && t.getMin_participants() < minParticipants) {
                ok = false;
            }
            if (ok) result.add(t);
        }
        return result;
    }

    private String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }

    private Date parseDate(String s) {
        if (s == null || s.isEmpty()) return null;
        try { return sdf.parse(s); } catch (Exception e) { return null; }
    }
}

