// src/main/java/controller/TripDetailServlet.java
package controller;

import dal.TripDAO;
import dal.TripImagesDAO;
import dal.ItineraryDAO;
import model.Trips;
import model.TripImages;
import model.Itinerary;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "TripDetailServlet", urlPatterns = {"/trip/detail"})
public class TripDetailServlet extends HttpServlet {

    // ===== DAO khai báo =====
    private final TripDAO tripDAO = new TripDAO();
    private final TripImagesDAO imagesDAO = new TripImagesDAO();
    private final ItineraryDAO itineraryDAO = new ItineraryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 🔹 Thiết lập UTF-8 tránh lỗi font
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // 🔹 Lấy ID chuyến đi
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/trips");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            // 🔹 Lấy thông tin chuyến đi
            Trips trip = tripDAO.getTripById(id);
            if (trip == null) {
                resp.sendRedirect(req.getContextPath() + "/trips?notfound=1");
                return;
            }

            // 🔹 Lấy ảnh phụ của chuyến đi
            List<TripImages> images = imagesDAO.getImagesByTrip(id);

            // 🔹 Lấy số lượng người tham gia (trừ leader)
            int participantCount = tripDAO.getParticipantCountByTrip(id);

            // 🔹 Lấy danh sách lịch trình (theo ngày và giờ)
            List<Itinerary> itineraries = itineraryDAO.getItinerariesByTripId(id);

            // 🔹 Truyền dữ liệu sang JSP
            req.setAttribute("trip", trip);
            req.setAttribute("images", images);
            req.setAttribute("participantCount", participantCount);
            req.setAttribute("itineraries", itineraries);

            // 🔹 Nếu có tham số 'from=mytrip' thì chuyển cùng
            req.setAttribute("from", req.getParameter("from"));

            // 🔹 Hiển thị giao diện chi tiết
            req.getRequestDispatcher("/views/trip_detail.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/trips");
        }
    }
}
