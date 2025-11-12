//package controller;
//
//import dal.TripDAO;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.temporal.ChronoUnit;
//
//import model.Trips;
//
//@WebServlet(name = "TripEditServlet", urlPatterns = {"/trip/edit"})
//public class TripEditServlet extends HttpServlet {
//
//    private final TripDAO tripDAO = new TripDAO();
//
//    // ================== Helpers ==================
//
//    private static boolean safeEquals(String a, String b) {
//        return (a == null) ? (b == null) : a.equals(b);
//    }
//
//    /** % thay đổi tuyệt đối giữa old và now theo old (VD: 9% -> 9.0) */
//    private static double percentChange(BigDecimal oldVal, BigDecimal nowVal) {
//        if (oldVal == null || nowVal == null) return Double.POSITIVE_INFINITY;
//        if (oldVal.compareTo(BigDecimal.ZERO) == 0) return Double.POSITIVE_INFINITY;
//        BigDecimal diff = nowVal.subtract(oldVal).abs();
//        BigDecimal pct = diff.multiply(BigDecimal.valueOf(100))
//                .divide(oldVal.abs(), 4, java.math.RoundingMode.HALF_UP);
//        return pct.doubleValue();
//    }
//
//    /** Chuyển java.util.Date hoặc java.sql.Date -> LocalDate an toàn (KHÔNG gọi sql.Date.toInstant) */
//    private static LocalDate toLocalDate(java.util.Date d) {
//        if (d == null) return null;
//        if (d instanceof java.sql.Date) {
//            return ((java.sql.Date) d).toLocalDate(); // không ném UnsupportedOperationException
//        }
//        return Instant.ofEpochMilli(d.getTime())
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate();
//    }
//
//    /** Số ngày còn tới startDate (tính theo lịch, bỏ giờ) */
//    private static long daysLeft(java.util.Date startDate) {
//        LocalDate start = toLocalDate(startDate);
//        if (start == null) return Long.MIN_VALUE; // coi như đã qua
//        LocalDate today = LocalDate.now(ZoneId.systemDefault());
//        return ChronoUnit.DAYS.between(today, start);
//    }
//
//    /** Tính và gán các cờ cho JSP để disable input đúng rule */
//    private void putEditFlags(HttpServletRequest req, long diffDays, int participantCount) {
//
//        boolean allowNameDescImageStatus = true;
//        boolean allowEditDate = false;
//        boolean allowEditLocation = false;
//        boolean allowEditBudget = false;
//        boolean budgetLimit10 = false; // hiển thị nhắc <10% khi được phép
//        boolean allowSubmit = true;
//
//        if (diffDays < 0) {
//            // ĐÃ QUA NGÀY: chặn tất cả
//            allowNameDescImageStatus = false;
//            allowEditDate = false;
//            allowEditLocation = false;
//            allowEditBudget = false;
//            allowSubmit = false; // tắt nút Lưu
//        } else if (participantCount <= 1) {
//            // Chỉ Leader: mở tất cả
//            allowNameDescImageStatus = true;
//            allowEditDate = true;
//            allowEditLocation = true;
//            allowEditBudget = true;
//            allowSubmit = true;
//        } else if (diffDays <= 3) {
//            // ≤ 3 ngày: chặn ngày + ngân sách + địa điểm; cho name/desc/cover/status
//            allowNameDescImageStatus = true;
//            allowEditDate = false;
//            allowEditLocation = false;
//            allowEditBudget = false;
//            allowSubmit = true;
//        } else if (participantCount >= 2 && diffDays >= 15) {
//            // ≥2 người & ≥15 ngày: chặn ngày + địa điểm; cho ngân sách (<10%) + name/desc/cover/status
//            allowNameDescImageStatus = true;
//            allowEditDate = false;
//            allowEditLocation = false;
//            allowEditBudget = true;
//            budgetLimit10 = true;
//            allowSubmit = true;
//        } else {
//            // 4–14 ngày & ≥2 người: chỉ cho name/desc/cover/status
//            allowNameDescImageStatus = true;
//            allowEditDate = false;
//            allowEditLocation = false;
//            allowEditBudget = false;
//            allowSubmit = true;
//        }
//
//        req.setAttribute("allowNameDescImageStatus", allowNameDescImageStatus);
//        req.setAttribute("allowEditDate", allowEditDate);
//        req.setAttribute("allowEditLocation", allowEditLocation);
//        req.setAttribute("allowEditBudget", allowEditBudget);
//        req.setAttribute("budgetLimit10", budgetLimit10);
//        req.setAttribute("allowSubmit", allowSubmit);
//    }
//
//    /** Set tham số hiển thị banner + flags cho JSP */
//    private void putBannerAttrs(HttpServletRequest req, Trips trip, int participantCount) {
//        long diffDays = daysLeft(trip.getStartDate());
//        req.setAttribute("participantCount", participantCount);
//        req.setAttribute("diffDays", diffDays);
//        putEditFlags(req, diffDays, participantCount);
//    }
//
//    // ================== GET ==================
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        String idStr = req.getParameter("id");
//        if (idStr == null || idStr.isEmpty()) {
//            resp.sendRedirect(req.getContextPath() + "/trips");
//            return;
//        }
//
//        try {
//            int id = Integer.parseInt(idStr);
//            Trips trip = tripDAO.getTripById(id);
//            if (trip == null) {
//                resp.sendRedirect(req.getContextPath() + "/trips?notfound=1");
//                return;
//            }
//
//            int participantCount = tripDAO.getParticipantCountByTrip(id);
//            putBannerAttrs(req, trip, participantCount);
//
//            req.setAttribute("trip", trip);
//            req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//
//        } catch (NumberFormatException e) {
//            resp.sendRedirect(req.getContextPath() + "/trips");
//        }
//    }
//
//    // ================== POST ==================
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        req.setCharacterEncoding("UTF-8");
//
//        // ===== Lấy trip hiện tại =====
//        int tripId = Integer.parseInt(req.getParameter("tripId"));
//        Trips oldTrip = tripDAO.getTripById(tripId);
//        if (oldTrip == null) {
//            resp.sendRedirect(req.getContextPath() + "/trips");
//            return;
//        }
//
//        // ===== Lấy input =====
//        String name         = req.getParameter("name");
//        String description  = req.getParameter("description");
//        String location     = req.getParameter("location");
//        String startDateStr = req.getParameter("startDate");
//        String endDateStr   = req.getParameter("endDate");
//        String budgetStr    = req.getParameter("budget");
//        String coverImage   = req.getParameter("coverImage");
//        String status       = req.getParameter("status");
//
//        // ===== Parse ngân sách =====
//        BigDecimal newBudget = null;
//        try {
//            if (budgetStr != null && !budgetStr.trim().isEmpty()) {
//                newBudget = new BigDecimal(budgetStr.trim());
//            }
//        } catch (NumberFormatException e) {
//            req.setAttribute("error", "❌ Ngân sách không hợp lệ.");
//            req.setAttribute("trip", oldTrip);
//            int pc = tripDAO.getParticipantCountByTrip(tripId);
//            putBannerAttrs(req, oldTrip, pc);
//            req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//            return;
//        }
//
//        // ===== Parse ngày =====
//        java.sql.Date newStartDate;
//        java.sql.Date newEndDate;
//        try {
//            newStartDate = java.sql.Date.valueOf(startDateStr);
//            newEndDate   = java.sql.Date.valueOf(endDateStr);
//        } catch (Exception e) {
//            req.setAttribute("error", "❌ Ngày không hợp lệ.");
//            req.setAttribute("trip", oldTrip);
//            int pc = tripDAO.getParticipantCountByTrip(tripId);
//            putBannerAttrs(req, oldTrip, pc);
//            req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//            return;
//        }
//
//        // ===== Thông tin ràng buộc =====
//        int participantCount = tripDAO.getParticipantCountByTrip(tripId);
//        long diffDays = daysLeft(oldTrip.getStartDate()); // số ngày còn tới NGÀY ĐI CŨ (theo lịch)
//        System.out.println("⚙️ TripEdit DEBUG → participants=" + participantCount + ", daysLeft=" + diffDays);
//
//        // ===== Phát hiện thay đổi (null-safe) =====
//        boolean changedName        = !safeEquals(name, oldTrip.getName());
//        boolean changedDesc        = !safeEquals(description, oldTrip.getDescription());
//        boolean changedImage       = !safeEquals(coverImage, oldTrip.getCoverImage());
//        boolean changedStatus      = !safeEquals(status, oldTrip.getStatus());
//        boolean changedLocation    = !safeEquals(location, oldTrip.getLocation());
//        boolean changedStartDate   = !newStartDate.equals(oldTrip.getStartDate());
//        boolean changedEndDate     = !newEndDate.equals(oldTrip.getEndDate());
//
//        BigDecimal oldBudget = oldTrip.getBudget();
//        boolean budgetActuallyChanged;
//        if (oldBudget == null && newBudget == null) {
//            budgetActuallyChanged = false;
//        } else if (oldBudget != null && newBudget != null) {
//            budgetActuallyChanged = oldBudget.compareTo(newBudget) != 0;
//        } else {
//            budgetActuallyChanged = true; // một bên null, một bên có giá trị
//        }
//
//        // ===== Áp quy tắc (chặn thật ở server) =====
//        // 4) ĐÃ QUA NGÀY ĐI → khóa TẤT CẢ
//        if (diffDays < 0) {
//            if (changedName || changedDesc || changedImage || changedStatus
//                    || changedLocation || changedStartDate || changedEndDate || budgetActuallyChanged) {
//                req.setAttribute("error", "❌ Chuyến đi đã qua ngày khởi hành — không sửa được gì.");
//                req.setAttribute("trip", oldTrip);
//                putBannerAttrs(req, oldTrip, participantCount);
//                req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//                return;
//            }
//        }
//        // 1) Mới tạo, chỉ có Leader (≤1 người) → SỬA ĐƯỢC MỌI THỨ
//        else if (participantCount <= 1) {
//            // full access
//        }
//        // 3) Còn ≤ 3 ngày → chặn NGÀY, NGÂN SÁCH và ĐỊA ĐIỂM
//        else if (diffDays <= 3) {
//            if (changedStartDate || changedEndDate) {
//                req.setAttribute("error", "❌ Còn 3 ngày — không được thay đổi ngày đi/kết thúc.");
//                req.setAttribute("trip", oldTrip);
//                putBannerAttrs(req, oldTrip, participantCount);
//                req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//                return;
//            }
//            if (budgetActuallyChanged) {
//                req.setAttribute("error", "❌ Còn 3 ngày — không được thay đổi ngân sách.");
//                req.setAttribute("trip", oldTrip);
//                putBannerAttrs(req, oldTrip, participantCount);
//                req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//                return;
//            }
//            if (changedLocation) {
//                req.setAttribute("error", "❌ Còn 3 ngày — không được thay đổi địa điểm.");
//                req.setAttribute("trip", oldTrip);
//                putBannerAttrs(req, oldTrip, participantCount);
//                req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//                return;
//            }
//        }
//        // 2) Có ≥2 người & còn ≥15 ngày → chỉ name/desc/cover + ngân sách nhẹ (<10%), chặn ngày & địa điểm
//        else if (participantCount >= 2 && diffDays >= 15) {
//            if (changedStartDate || changedEndDate) {
//                req.setAttribute("error", "❌ Đã có người tham gia — không được đổi ngày đi/kết thúc.");
//                req.setAttribute("trip", oldTrip);
//                putBannerAttrs(req, oldTrip, participantCount);
//                req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//                return;
//            }
//            if (changedLocation) {
//                req.setAttribute("error", "❌ Đã có người tham gia — không được đổi địa điểm.");
//                req.setAttribute("trip", oldTrip);
//                putBannerAttrs(req, oldTrip, participantCount);
//                req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//                return;
//            }
//            if (budgetActuallyChanged) {
//                if (oldBudget == null || newBudget == null) {
//                    req.setAttribute("error", "❌ Chỉ được chỉnh ngân sách nhẹ (<10%). Thiếu dữ liệu để so sánh.");
//                    req.setAttribute("trip", oldTrip);
//                    putBannerAttrs(req, oldTrip, participantCount);
//                    req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//                    return;
//                }
//                double pct = percentChange(oldBudget, newBudget);
//                if (pct >= 10.0) {
//                    req.setAttribute("error", String.format(
//                            "❌ Chỉ được chỉnh ngân sách nhẹ (<10%%). Bạn đang thay đổi ~%.2f%%.", pct));
//                    req.setAttribute("trip", oldTrip);
//                    putBannerAttrs(req, oldTrip, participantCount);
//                    req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//                    return;
//                }
//            }
//        }
//        // 4–14 ngày & ≥2 người → chỉ name/desc/cover/status; chặn ngày, ngân sách, địa điểm
//        else {
//            if (changedStartDate || changedEndDate) {
//                req.setAttribute("error", "❌ Không được đổi ngày đi/kết thúc trong giai đoạn này.");
//                req.setAttribute("trip", oldTrip);
//                putBannerAttrs(req, oldTrip, participantCount);
//                req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//                return;
//            }
//            if (changedLocation) {
//                req.setAttribute("error", "❌ Không được đổi địa điểm trong giai đoạn này.");
//                req.setAttribute("trip", oldTrip);
//                putBannerAttrs(req, oldTrip, participantCount);
//                req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//                return;
//            }
//            if (budgetActuallyChanged) {
//                req.setAttribute("error", "❌ Không được đổi ngân sách trong giai đoạn này.");
//                req.setAttribute("trip", oldTrip);
//                putBannerAttrs(req, oldTrip, participantCount);
//                req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//                return;
//            }
//        }
//
//        // ===== Nếu không vi phạm rule → cập nhật =====
//        oldTrip.setName(name);
//        oldTrip.setDescription(description);
//        oldTrip.setCoverImage(coverImage);
//        oldTrip.setStatus(status);
//        oldTrip.setLocation(location);        // nếu có vi phạm đã bị chặn ở trên
//        oldTrip.setStartDate(newStartDate);   // tương tự
//        oldTrip.setEndDate(newEndDate);
//        oldTrip.setBudget(newBudget);         // tương tự
//
//        boolean ok = tripDAO.updateTrip(oldTrip);
//        if (ok) {
//            resp.sendRedirect(req.getContextPath() + "/mytrips?updated=1");
//        } else {
//            req.setAttribute("error", "❌ Cập nhật thất bại, vui lòng thử lại.");
//            req.setAttribute("trip", oldTrip);
//            putBannerAttrs(req, oldTrip, participantCount);
//            req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
//        }
//    }
//}
package controller;

import dal.TripDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import model.Trips;

@WebServlet(name = "TripEditServlet", urlPatterns = {"/trip/edit"})
public class TripEditServlet extends HttpServlet {

    private final TripDAO tripDAO = new TripDAO();

    // ================== Helpers ==================

    private static boolean safeEquals(String a, String b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    /** % thay đổi tuyệt đối giữa old và now theo old (VD: 9% -> 9.0) */
    private static double percentChange(BigDecimal oldVal, BigDecimal nowVal) {
        if (oldVal == null || nowVal == null) return Double.POSITIVE_INFINITY;
        if (oldVal.compareTo(BigDecimal.ZERO) == 0) return Double.POSITIVE_INFINITY;
        BigDecimal diff = nowVal.subtract(oldVal).abs();
        BigDecimal pct = diff.multiply(BigDecimal.valueOf(100))
                .divide(oldVal.abs(), 4, java.math.RoundingMode.HALF_UP);
        return pct.doubleValue();
    }

    /** Chuyển java.util.Date hoặc java.sql.Date -> LocalDate an toàn */
    private static LocalDate toLocalDate(java.util.Date d) {
        if (d == null) return null;
        if (d instanceof java.sql.Date) {
            return ((java.sql.Date) d).toLocalDate();
        }
        return Instant.ofEpochMilli(d.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /** Số ngày còn tới startDate */
    private static long daysLeft(java.util.Date startDate) {
        LocalDate start = toLocalDate(startDate);
        if (start == null) return Long.MIN_VALUE;
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        return ChronoUnit.DAYS.between(today, start);
    }

    /** Tính và gán các cờ cho JSP để disable input đúng rule */
    private void putEditFlags(HttpServletRequest req, long diffDays, int participantCount) {

        boolean allowNameDescImageStatus = true;
        boolean allowEditDate = false;
        boolean allowEditLocation = false;
        boolean allowEditBudget = false;
        boolean allowEditMeeting = false;
        boolean allowEditMinParticipants = false;
        boolean budgetLimit10 = false;
        boolean allowSubmit = true;

        if (diffDays < 0) {
            // ĐÃ QUA NGÀY
            allowNameDescImageStatus = false;
            allowEditDate = false;
            allowEditLocation = false;
            allowEditBudget = false;
            allowEditMeeting = false;
            allowEditMinParticipants = false;
            allowSubmit = false;
        } else if (participantCount <= 1) {
            // Chỉ Leader: mở tất cả
            allowNameDescImageStatus = true;
            allowEditDate = true;
            allowEditLocation = true;
            allowEditBudget = true;
            allowEditMeeting = true;
            allowEditMinParticipants = true;
            allowSubmit = true;
        } else if (diffDays <= 3) {
            // ≤3 ngày
            allowNameDescImageStatus = true;
            allowEditDate = false;
            allowEditLocation = false;
            allowEditBudget = false;
            allowEditMeeting = false;
            allowEditMinParticipants = false;
            allowSubmit = true;
        } else if (participantCount >= 2 && diffDays >= 15) {
            // ≥2 người & ≥15 ngày
            allowNameDescImageStatus = true;
            allowEditDate = false;
            allowEditLocation = false;
            allowEditBudget = true;
            budgetLimit10 = true;
            allowEditMeeting = false;
            allowEditMinParticipants = false;
            allowSubmit = true;
        } else {
            // 4–14 ngày
            allowNameDescImageStatus = true;
            allowEditDate = false;
            allowEditLocation = false;
            allowEditBudget = false;
            allowEditMeeting = false;
            allowEditMinParticipants = false;
            allowSubmit = true;
        }

        req.setAttribute("allowNameDescImageStatus", allowNameDescImageStatus);
        req.setAttribute("allowEditDate", allowEditDate);
        req.setAttribute("allowEditLocation", allowEditLocation);
        req.setAttribute("allowEditBudget", allowEditBudget);
        req.setAttribute("allowEditMeeting", allowEditMeeting);
        req.setAttribute("allowEditMinParticipants", allowEditMinParticipants);
        req.setAttribute("budgetLimit10", budgetLimit10);
        req.setAttribute("allowSubmit", allowSubmit);
    }

    /** Set tham số hiển thị banner + flags cho JSP */
    private void putBannerAttrs(HttpServletRequest req, Trips trip, int participantCount) {
        long diffDays = daysLeft(trip.getStartDate());
        req.setAttribute("participantCount", participantCount);
        req.setAttribute("diffDays", diffDays);
        putEditFlags(req, diffDays, participantCount);
    }

    // ================== GET ==================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/trips");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Trips trip = tripDAO.getTripById(id);
            if (trip == null) {
                resp.sendRedirect(req.getContextPath() + "/trips?notfound=1");
                return;
            }

            int participantCount = tripDAO.getParticipantCountByTrip(id);
            putBannerAttrs(req, trip, participantCount);

            req.setAttribute("trip", trip);
            req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/trips");
        }
    }

    // ================== POST ==================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        int tripId = Integer.parseInt(req.getParameter("tripId"));
        Trips oldTrip = tripDAO.getTripById(tripId);
        if (oldTrip == null) {
            resp.sendRedirect(req.getContextPath() + "/trips");
            return;
        }

        // ===== Lấy input =====
        String name         = req.getParameter("name");
        String description  = req.getParameter("description");
        String location     = req.getParameter("location");
        String meetingPoint = req.getParameter("meeting_point");
        String startDateStr = req.getParameter("startDate");
        String endDateStr   = req.getParameter("endDate");
        String budgetStr    = req.getParameter("budget");
        String coverImage   = req.getParameter("coverImage");
        String status       = req.getParameter("status");
        String minPartStr   = req.getParameter("min_participants");

        // ===== Parse ngân sách =====
        BigDecimal newBudget = null;
        try {
            if (budgetStr != null && !budgetStr.trim().isEmpty()) {
                newBudget = new BigDecimal(budgetStr.trim());
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "❌ Ngân sách không hợp lệ.");
            req.setAttribute("trip", oldTrip);
            int pc = tripDAO.getParticipantCountByTrip(tripId);
            putBannerAttrs(req, oldTrip, pc);
            req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
            return;
        }

        // ===== Parse ngày =====
        java.sql.Date newStartDate;
        java.sql.Date newEndDate;
        try {
            newStartDate = java.sql.Date.valueOf(startDateStr);
            newEndDate   = java.sql.Date.valueOf(endDateStr);
        } catch (Exception e) {
            req.setAttribute("error", "❌ Ngày không hợp lệ.");
            req.setAttribute("trip", oldTrip);
            int pc = tripDAO.getParticipantCountByTrip(tripId);
            putBannerAttrs(req, oldTrip, pc);
            req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
            return;
        }
        
        // ===== Validate ngày =====
        // Kiểm tra startDate >= today
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        
        if (newStartDate.before(new java.sql.Date(today.getTimeInMillis()))) {
            req.setAttribute("error", "❌ Ngày đi phải từ hôm nay trở đi.");
            req.setAttribute("trip", oldTrip);
            int pc = tripDAO.getParticipantCountByTrip(tripId);
            putBannerAttrs(req, oldTrip, pc);
            req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
            return;
        }
        
        // Kiểm tra endDate >= startDate
        if (newEndDate.before(newStartDate)) {
            req.setAttribute("error", "❌ Ngày kết thúc phải sau hoặc bằng ngày đi.");
            req.setAttribute("trip", oldTrip);
            int pc = tripDAO.getParticipantCountByTrip(tripId);
            putBannerAttrs(req, oldTrip, pc);
            req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
            return;
        }

        // ===== Parse số người tối thiểu =====
        int newMinParticipants = oldTrip.getMin_participants();
        try {
            if (minPartStr != null && !minPartStr.trim().isEmpty()) {
                newMinParticipants = Integer.parseInt(minPartStr.trim());
            }
        } catch (NumberFormatException ignore) {
        }

        // ===== Thông tin ràng buộc =====
        int participantCount = tripDAO.getParticipantCountByTrip(tripId);
        long diffDays = daysLeft(oldTrip.getStartDate());
        System.out.println("⚙️ TripEdit DEBUG → participants=" + participantCount + ", daysLeft=" + diffDays);

        // ===== Phát hiện thay đổi =====
        boolean changedName       = !safeEquals(name, oldTrip.getName());
        boolean changedDesc       = !safeEquals(description, oldTrip.getDescription());
        boolean changedImage      = !safeEquals(coverImage, oldTrip.getCoverImage());
        boolean changedStatus     = !safeEquals(status, oldTrip.getStatus());
        boolean changedLocation   = !safeEquals(location, oldTrip.getLocation());
        boolean changedMeeting    = !safeEquals(meetingPoint, oldTrip.getMeeting_point());
        boolean changedStartDate  = !newStartDate.equals(oldTrip.getStartDate());
        boolean changedEndDate    = !newEndDate.equals(oldTrip.getEndDate());
        boolean changedMinPart    = (newMinParticipants != oldTrip.getMin_participants());

        BigDecimal oldBudget = oldTrip.getBudget();
        boolean budgetActuallyChanged;
        if (oldBudget == null && newBudget == null) {
            budgetActuallyChanged = false;
        } else if (oldBudget != null && newBudget != null) {
            budgetActuallyChanged = oldBudget.compareTo(newBudget) != 0;
        } else {
            budgetActuallyChanged = true;
        }

        // ===== Áp quy tắc =====
        if (diffDays < 0) {
            req.setAttribute("error", "❌ Chuyến đi đã qua ngày khởi hành — không sửa được gì.");
            req.setAttribute("trip", oldTrip);
            putBannerAttrs(req, oldTrip, participantCount);
            req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
            return;
        } else if (participantCount <= 1) {
            // full access
        } else if (diffDays <= 3) {
            if (changedStartDate || changedEndDate || changedLocation || budgetActuallyChanged || changedMeeting || changedMinPart) {
                req.setAttribute("error", "❌ Còn 3 ngày — không được thay đổi ngày, địa điểm, ngân sách, địa điểm tập trung hoặc số người tối thiểu.");
                req.setAttribute("trip", oldTrip);
                putBannerAttrs(req, oldTrip, participantCount);
                req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
                return;
            }
        } else if (participantCount >= 2 && diffDays >= 15) {
            if (changedStartDate || changedEndDate || changedLocation || changedMeeting || changedMinPart) {
                req.setAttribute("error", "❌ Đã có người tham gia — không được đổi ngày, địa điểm, địa điểm tập trung hoặc số người tối thiểu.");
                req.setAttribute("trip", oldTrip);
                putBannerAttrs(req, oldTrip, participantCount);
                req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
                return;
            }
            if (budgetActuallyChanged) {
                double pct = percentChange(oldBudget, newBudget);
                if (pct >= 10.0) {
                    req.setAttribute("error", String.format("❌ Chỉ được chỉnh ngân sách nhẹ (<10%%). Bạn đang thay đổi ~%.2f%%.", pct));
                    req.setAttribute("trip", oldTrip);
                    putBannerAttrs(req, oldTrip, participantCount);
                    req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
                    return;
                }
            }
        } else {
            if (changedStartDate || changedEndDate || changedLocation || changedMeeting || changedMinPart || budgetActuallyChanged) {
                req.setAttribute("error", "❌ Không được thay đổi ngày, địa điểm, địa điểm tập trung, số người tối thiểu hoặc ngân sách trong giai đoạn này.");
                req.setAttribute("trip", oldTrip);
                putBannerAttrs(req, oldTrip, participantCount);
                req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
                return;
            }
        }

        // ===== Nếu hợp lệ → cập nhật =====
        oldTrip.setName(name);
        oldTrip.setDescription(description);
        oldTrip.setCoverImage(coverImage);
        oldTrip.setStatus(status);
        oldTrip.setLocation(location);
        oldTrip.setMeeting_point(meetingPoint);
        oldTrip.setStartDate(newStartDate);
        oldTrip.setEndDate(newEndDate);
        oldTrip.setBudget(newBudget);
        oldTrip.setMin_participants(newMinParticipants);

        boolean ok = tripDAO.updateTrip(oldTrip);
        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/mytrips?updated=1");
        } else {
            req.setAttribute("error", "❌ Cập nhật thất bại, vui lòng thử lại.");
            req.setAttribute("trip", oldTrip);
            putBannerAttrs(req, oldTrip, participantCount);
            req.getRequestDispatcher("/views/trip_edit.jsp").forward(req, resp);
        }
    }
}
