package controller;

import dal.TripDAO;
import model.Trips;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/createtrip")
public class CreateTripServlet extends HttpServlet {

    private TripDAO dao;

    @Override
    public void init() {
        dao = new TripDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hiển thị form tạo chuyến đi
        request.getRequestDispatcher("/createtrip.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8"); // tránh lỗi tiếng Việt

            // ====== 1. Lấy dữ liệu từ form ======
            String name = request.getParameter("name");
            String location = request.getParameter("location");
            Date start_date = parseDate(request.getParameter("start_date"));
            Date end_date = parseDate(request.getParameter("end_date"));
            BigDecimal budget = new BigDecimal(request.getParameter("budget"));
            String status = request.getParameter("status");
            Timestamp created_at = new Timestamp(System.currentTimeMillis());

            // ====== 2. Tạo TravelGroup mới (leader_id = 1 tạm thời) ======
            int leaderId = 1; // ⚠️ tạm thời fix cứng user_id = 1 (sau này thay bằng user login)
            String groupName = name + " Group";
            String description = "Nhóm được tạo tự động khi tạo chuyến đi: " + name;

            int group_id = dao.createGroupAndGetId(groupName, description, leaderId);

            if (group_id <= 0) {
                throw new Exception("Không thể tạo TravelGroup mới trong DB!");
            }

            // ====== 3. Tạo Trip mới ======
            Trips trip = new Trips(
                    0,              // trip_id tự tăng
                    group_id,       // group_id vừa tạo
                    name,
                    location,
                    start_date,
                    end_date,
                    budget,
                    status,
                    created_at
            );

            boolean success = dao.insertTrip(trip);

            // ====== 4. Xử lý kết quả ======
            HttpSession session = request.getSession();
            if (success) {
                session.setAttribute("successMessage", "✅ Tạo chuyến đi thành công!");
            } else {
                session.setAttribute("errorMessage", "❌ Không thể tạo chuyến đi. Vui lòng thử lại!");
            }

            // Chuyển hướng về danh sách chuyến đi
            response.sendRedirect("trips");

        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi → quay lại form kèm thông báo
            request.setAttribute("errorMessage", "Lỗi khi tạo chuyến đi: " + e.getMessage());
            request.getRequestDispatcher("/createtrip.jsp").forward(request, response);
        }
    }

    // 👉 Hàm parse ngày hỗ trợ cả yyyy-MM-dd và dd/MM/yyyy
    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return Date.valueOf(dateStr); // yyyy-MM-dd (chuẩn HTML input type=date)
        } catch (IllegalArgumentException e) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                java.util.Date utilDate = sdf.parse(dateStr);
                return new Date(utilDate.getTime());
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
}