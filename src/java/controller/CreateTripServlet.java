// src/main/java/controller/CreateTripServlet.java
package controller;

import dal.TripDAO;
import dal.TravelGroupsDAO;
import dal.GroupMembersDAO;
import dal.TripImagesDAO;
import model.Trips;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import model.Users;

@WebServlet(name = "CreateTripServlet", urlPatterns = {"/trip/create"})
public class CreateTripServlet extends HttpServlet {

    private final TripDAO tripDAO = new TripDAO();
    private final TravelGroupsDAO groupsDAO = new TravelGroupsDAO();
    private final GroupMembersDAO membersDAO = new GroupMembersDAO();
    private final TripImagesDAO imagesDAO = new TripImagesDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        req.getRequestDispatcher("/views/create_trip.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // -------- Lấy dữ liệu form --------
        String name = trim(req.getParameter("name"));
        String description = trim(req.getParameter("description"));
        String location = trim(req.getParameter("location"));
        String meetingPoint = trim(req.getParameter("meeting_point")); // 🆕
        String startDateStr = trim(req.getParameter("startDate"));
        String endDateStr = trim(req.getParameter("endDate"));
        String budgetStr = trim(req.getParameter("budget"));
        String coverImage = trim(req.getParameter("coverImage"));
        String tripType = trim(req.getParameter("tripType"));
        String status = trim(req.getParameter("status"));
        if (status.isEmpty()) {
            status = "Active";
        }

        String maxPartStr = trim(req.getParameter("maxParticipants"));
        String minPartStr = trim(req.getParameter("minParticipants")); // 🆕

        // Ảnh phụ
        String img1 = trim(req.getParameter("image1"));
        String img2 = trim(req.getParameter("image2"));
        String img3 = trim(req.getParameter("image3"));

        List<String> errors = new ArrayList<>();

        HttpSession session = req.getSession();
        Users user = (Users) session.getAttribute("currentUser");
        Integer leaderId = user.getUser_id();

        // -------- Validate --------
        if (name.isEmpty()) {
            errors.add("Tên chuyến đi không được để trống.");
        }
        if (location.isEmpty()) {
            errors.add("Địa điểm không được để trống.");
        }
        if (meetingPoint.isEmpty()) {
            errors.add("Địa điểm tập trung không được để trống."); // 🆕
        }
        Date startDate = parseDate(startDateStr, "Ngày đi không hợp lệ (yyyy-MM-dd).", errors);
        Date endDate = parseDate(endDateStr, "Ngày kết thúc không hợp lệ (yyyy-MM-dd).", errors);
        
        // Validate ngày đi phải >= hôm nay
        if (startDate != null) {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            
            if (startDate.before(today.getTime())) {
                errors.add("Ngày đi phải từ hôm nay trở đi.");
            }
        }

        BigDecimal budget = null;
        if (!budgetStr.isEmpty()) {
            try {
                budget = new BigDecimal(budgetStr);
                if (budget.compareTo(BigDecimal.ZERO) < 0) {
                    errors.add("Ngân sách phải ≥ 0.");
                }
            } catch (NumberFormatException e) {
                errors.add("Ngân sách không hợp lệ.");
            }
        }

        Integer maxParticipants = null;
        if (!maxPartStr.isEmpty()) {
            try {
                maxParticipants = Integer.parseInt(maxPartStr);
                if (maxParticipants <= 0) {
                    errors.add("Số người tham gia tối đa phải > 0.");
                }
            } catch (NumberFormatException e) {
                errors.add("Số người tham gia tối đa không hợp lệ.");
            }
        }

        Integer minParticipants = null;
        if (!minPartStr.isEmpty()) {
            try {
                minParticipants = Integer.parseInt(minPartStr);
                if (minParticipants <= 0) {
                    errors.add("Số người tối thiểu phải > 0.");
                }
                if (maxParticipants != null && minParticipants > maxParticipants) {
                    errors.add("Số người tối thiểu không được lớn hơn số người tối đa.");
                }
            } catch (NumberFormatException e) {
                errors.add("Số người tối thiểu không hợp lệ.");
            }
        } else {
            minParticipants = 1; // default
        }

        if (startDate != null && endDate != null && startDate.after(endDate)) {
            errors.add("Ngày đi phải trước hoặc bằng ngày kết thúc.");
        }
        if (tripType.isEmpty()) {
            errors.add("Vui lòng chọn loại chuyến đi.");
        }

        // --- Nếu lỗi -> trả lại form ---
        if (!errors.isEmpty()) {
            pushBackForm(req, name, description, location, meetingPoint, startDateStr, endDateStr,
                    budgetStr, coverImage, tripType, status, img1, img2, img3, maxPartStr, minPartStr, errors);
            req.getRequestDispatcher("/views/create_trip.jsp").forward(req, resp);
            return;
        }

        // ================== FLOW ==================
        // 1) Tạo group mới
        String groupName = name;
        int newGroupId = groupsDAO.insertGroup(groupName, description, leaderId);
        if (newGroupId <= 0) {
            errors.add("Không tạo được nhóm (group) mới.");
            pushBackForm(req, name, description, location, meetingPoint, startDateStr, endDateStr,
                    budgetStr, coverImage, tripType, status, img1, img2, img3, maxPartStr, minPartStr, errors);
            req.getRequestDispatcher("/views/create_trip.jsp").forward(req, resp);
            return;
        }

        // 2) Thêm leader vào GroupMembers
        if (!membersDAO.addMember(newGroupId, leaderId, "Leader")) {
            errors.add("Không thêm được Leader vào nhóm.");
            pushBackForm(req, name, description, location, meetingPoint, startDateStr, endDateStr,
                    budgetStr, coverImage, tripType, status, img1, img2, img3, maxPartStr, minPartStr, errors);
            req.getRequestDispatcher("/views/create_trip.jsp").forward(req, resp);
            return;
        }

        // 3) Tạo Trip
        Trips trip = new Trips();
        trip.setGroupId(newGroupId);
        trip.setName(name);
        trip.setDescription(description);
        trip.setLocation(location);
        trip.setMeeting_point(meetingPoint);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        trip.setBudget(budget);
        trip.setCoverImage(coverImage);
        trip.setTripType(tripType);
        trip.setStatus(status);
        trip.setMax_participants(maxParticipants);
        trip.setMin_participants(minParticipants);

        int newTripId = tripDAO.insertTrip(trip);

        if (newTripId > 0) {
            // 4) Thêm ảnh phụ
            if (!img1.isEmpty()) {
                imagesDAO.insertImage(newTripId, img1);
            }
            if (!img2.isEmpty()) {
                imagesDAO.insertImage(newTripId, img2);
            }
            if (!img3.isEmpty()) {
                imagesDAO.insertImage(newTripId, img3);
            }

            // ✅ Redirect sang trang thêm lịch trình
            resp.sendRedirect(req.getContextPath() + "/itinerary/create?tripId=" + newTripId);
            return;
        } else {
            errors.add("Tạo chuyến đi thất bại. Vui lòng thử lại.");
            pushBackForm(req, name, description, location, meetingPoint, startDateStr, endDateStr,
                    budgetStr, coverImage, tripType, status, img1, img2, img3, maxPartStr, minPartStr, errors);
            req.getRequestDispatcher("/views/create_trip.jsp").forward(req, resp);
        }
    }

    /* ================= Helpers ================= */
    private String trim(String s) {
        return (s == null) ? "" : s.trim();
    }

    private Date parseDate(String s, String errMsg, List<String> errors) {
        if (s.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            return sdf.parse(s);
        } catch (ParseException e) {
            errors.add(errMsg);
            return null;
        }
    }

    private Integer getCurrentUserId(HttpSession session) {
        Object uid = session.getAttribute("userId");
        if (uid instanceof Integer) {
            return (Integer) uid;
        }
        Object userObj = session.getAttribute("user");
        if (userObj != null) {
            try {
                Object val = userObj.getClass().getMethod("getUserId").invoke(userObj);
                if (val != null) {
                    return Integer.parseInt(val.toString());
                }
            } catch (Exception ignore) {
            }
        }
        return null;
    }

    private void pushBackForm(HttpServletRequest req, String name, String description, String location,
            String meetingPoint, String startDate, String endDate, String budget, String coverImage,
            String tripType, String status, String img1, String img2, String img3,
            String maxParticipants, String minParticipants, List<String> errors) {
        req.setAttribute("errors", errors);
        req.setAttribute("form_name", name);
        req.setAttribute("form_description", description);
        req.setAttribute("form_location", location);
        req.setAttribute("form_meeting_point", meetingPoint);
        req.setAttribute("form_startDate", startDate);
        req.setAttribute("form_endDate", endDate);
        req.setAttribute("form_budget", budget);
        req.setAttribute("form_coverImage", coverImage);
        req.setAttribute("form_tripType", tripType);
        req.setAttribute("form_status", status);
        req.setAttribute("form_image1", img1);
        req.setAttribute("form_image2", img2);
        req.setAttribute("form_image3", img3);
        req.setAttribute("form_maxParticipants", maxParticipants);
        req.setAttribute("form_minParticipants", minParticipants);
    }
}
