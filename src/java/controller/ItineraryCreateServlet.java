////package controller;
////
////import dal.ItineraryDAO;
////import jakarta.servlet.ServletException;
////import jakarta.servlet.annotation.WebServlet;
////import jakarta.servlet.http.*;
////import java.io.IOException;
////import java.sql.Time;
////import java.text.SimpleDateFormat;
////import java.util.*;
////import model.Itinerary;
////import model.Trips;
////import dal.TripDAO;
////
////@WebServlet(name = "ItineraryCreateServlet", urlPatterns = {"/itinerary/create"})
////public class ItineraryCreateServlet extends HttpServlet {
////
////    private final ItineraryDAO itineraryDAO = new ItineraryDAO();
////    private final TripDAO tripDAO = new TripDAO();
////
////    @Override
////    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
////            throws ServletException, IOException {
////        req.setCharacterEncoding("UTF-8");
////        resp.setCharacterEncoding("UTF-8");
////
////        String tripIdStr = req.getParameter("tripId");
////        if (tripIdStr == null) {
////            resp.sendRedirect(req.getContextPath() + "/trips");
////            return;
////        }
////
////        int tripId = Integer.parseInt(tripIdStr);
////        Trips trip = tripDAO.getTripById(tripId);
////
////        if (trip == null) {
////            resp.sendRedirect(req.getContextPath() + "/trips?notfound=1");
////            return;
////        }
////
////        // ✅ Tính số ngày
////        long diff = trip.getEndDate().getTime() - trip.getStartDate().getTime();
////        int dayCount = (int) (diff / (1000 * 60 * 60 * 24)) + 1;
////
////        req.setAttribute("trip", trip);
////        req.setAttribute("dayCount", dayCount);
////        req.getRequestDispatcher("/views/itinerary_create.jsp").forward(req, resp);
////    }
////
//// @Override
////protected void doPost(HttpServletRequest req, HttpServletResponse resp)
////        throws ServletException, IOException {
////
////    req.setCharacterEncoding("UTF-8");
////    resp.setCharacterEncoding("UTF-8");
////
////    int tripId = Integer.parseInt(req.getParameter("tripId"));
////    int dayCount = Integer.parseInt(req.getParameter("dayCount"));
////
////    List<Itinerary> validItems = new ArrayList<>();
////    List<String> errors = new ArrayList<>();
////    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
////
////    for (int day = 1; day <= dayCount; day++) {
////        Time previousEnd = null;
////
////        // Lặp qua tối đa 20 hoạt động / ngày (để tránh vòng lặp vô hạn)
////        for (int i = 1; i <= 20; i++) {
////            String title = req.getParameter("title" + day + "_" + i);
////            String startStr = req.getParameter("start" + day + "_" + i);
////            String endStr = req.getParameter("end" + day + "_" + i);
////            String desc = req.getParameter("desc" + day + "_" + i);
////
////            // Nếu toàn bộ trống → bỏ qua
////            if ((title == null || title.isEmpty()) &&
////                (startStr == null || startStr.isEmpty()) &&
////                (endStr == null || endStr.isEmpty()) &&
////                (desc == null || desc.isEmpty())) {
////                continue;
////            }
////
////            try {
////                // parse time
////                Time start = new Time(sdf.parse(startStr).getTime());
////                Time end = new Time(sdf.parse(endStr).getTime());
////
////                if (!end.after(start)) {
////                    errors.add("Ngày " + day + " - Hoạt động " + i + ": Giờ kết thúc phải sau giờ bắt đầu.");
////                    continue;
////                }
////
////                if (previousEnd != null && start.getTime() - previousEnd.getTime() < 15 * 60 * 1000) {
////                    errors.add("Ngày " + day + " - Hoạt động " + i + ": Phải cách hoạt động trước ít nhất 15 phút.");
////                    continue;
////                }
////
////                // tạo đối tượng itinerary
////                Itinerary it = new Itinerary();
////                it.setTripId(tripId);
////                it.setDayNumber(day);
////                it.setTitle(title);
////                it.setDescription(desc);
////                it.setStartTime(start);
////                it.setEndTime(end);
////
////                validItems.add(it);
////                previousEnd = end;
////
////            } catch (Exception e) {
////                errors.add("Ngày " + day + " - Hoạt động " + i + ": Dữ liệu thời gian không hợp lệ.");
////            }
////        }
////    }
////
////    if (!errors.isEmpty()) {
////        req.setAttribute("errors", errors);
////        req.setAttribute("tripId", tripId);
////        req.setAttribute("oldData", req.getParameterMap());
////        req.getRequestDispatcher("/views/itinerary_create.jsp").forward(req, resp);
////        return;
////    }
////
////    boolean success = itineraryDAO.insertAll(validItems);
////
////    if (success) {
////        resp.sendRedirect(req.getContextPath() + "/trip/detail?id=" + tripId);
////    } else {
////        req.setAttribute("error", "Không thể lưu lịch trình. Vui lòng thử lại.");
////        req.getRequestDispatcher("/views/itinerary_create.jsp").forward(req, resp);
////    }
////}
////
////
////}
//package controller;
//
//import dal.ItineraryDAO;
//import dal.TripDAO;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//import java.io.IOException;
//import java.sql.Time;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import model.Itinerary;
//import model.Trips;
//
//@WebServlet(name = "ItineraryCreateServlet", urlPatterns = {"/itinerary/create"})
//public class ItineraryCreateServlet extends HttpServlet {
//
//    private final ItineraryDAO itineraryDAO = new ItineraryDAO();
//    private final TripDAO tripDAO = new TripDAO();
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        req.setCharacterEncoding("UTF-8");
//        resp.setCharacterEncoding("UTF-8");
//
//        String tripIdStr = req.getParameter("tripId");
//        if (tripIdStr == null) {
//            resp.sendRedirect(req.getContextPath() + "/trips");
//            return;
//        }
//
//        int tripId = Integer.parseInt(tripIdStr);
//        Trips trip = tripDAO.getTripById(tripId);
//
//        if (trip == null) {
//            resp.sendRedirect(req.getContextPath() + "/trips?notfound=1");
//            return;
//        }
//
//        // ✅ Tính tổng số ngày của chuyến đi
//        long diff = trip.getEndDate().getTime() - trip.getStartDate().getTime();
//        int dayCount = (int) (diff / (1000 * 60 * 60 * 24)) + 1;
//
//        req.setAttribute("trip", trip);
//        req.setAttribute("dayCount", dayCount);
//        req.getRequestDispatcher("/views/itinerary_create.jsp").forward(req, resp);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        req.setCharacterEncoding("UTF-8");
//        resp.setCharacterEncoding("UTF-8");
//
//        int tripId = Integer.parseInt(req.getParameter("tripId"));
//        int dayCount = Integer.parseInt(req.getParameter("dayCount"));
//
//        List<Itinerary> validItems = new ArrayList<>();
//        List<String> errors = new ArrayList<>();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//
//        Map<String, String[]> paramMap = req.getParameterMap();
//
//        for (int day = 1; day <= dayCount; day++) {
//            Time previousEnd = null;
//
//            // ✅ Lấy tất cả chỉ số động cho ngày hiện tại
//            List<Integer> indexes = new ArrayList<>();
//            for (String key : paramMap.keySet()) {
//                if (key.startsWith("title" + day + "_")) {
//                    try {
//                        int idx = Integer.parseInt(key.substring(("title" + day + "_").length()));
//                        indexes.add(idx);
//                    } catch (NumberFormatException ignore) {}
//                }
//            }
//            Collections.sort(indexes);
//
//            for (int i : indexes) {
//                String title = req.getParameter("title" + day + "_" + i);
//                String startStr = req.getParameter("start" + day + "_" + i);
//                String endStr = req.getParameter("end" + day + "_" + i);
//                String desc = req.getParameter("desc" + day + "_" + i);
//
//                // Nếu trống toàn bộ → bỏ qua
//                if ((title == null || title.isBlank()) &&
//                    (startStr == null || startStr.isBlank()) &&
//                    (endStr == null || endStr.isBlank()) &&
//                    (desc == null || desc.isBlank())) {
//                    continue;
//                }
//
//                try {
//                    Time start = new Time(sdf.parse(startStr).getTime());
//                    Time end = new Time(sdf.parse(endStr).getTime());
//
//                    if (!end.after(start)) {
//                        errors.add("Ngày " + day + " - Hoạt động " + i + ": Giờ kết thúc phải sau giờ bắt đầu.");
//                        continue;
//                    }
//
//                    if (previousEnd != null && start.getTime() - previousEnd.getTime() < 15 * 60 * 1000) {
//                        errors.add("Ngày " + day + " - Hoạt động " + i + ": Phải cách hoạt động trước ít nhất 15 phút.");
//                        continue;
//                    }
//
//                    Itinerary it = new Itinerary();
//                    it.setTripId(tripId);
//                    it.setDayNumber(day);
//                    it.setTitle(title);
//                    it.setDescription(desc);
//                    it.setStartTime(start);
//                    it.setEndTime(end);
//
//                    validItems.add(it);
//                    previousEnd = end;
//
//                } catch (Exception e) {
//                    errors.add("Ngày " + day + " - Hoạt động " + i + ": Dữ liệu thời gian không hợp lệ.");
//                }
//            }
//        }
//
//        if (!errors.isEmpty()) {
//            req.setAttribute("errors", errors);
//            req.setAttribute("tripId", tripId);
//            req.setAttribute("oldData", req.getParameterMap());
//            req.getRequestDispatcher("/views/itinerary_create.jsp").forward(req, resp);
//            return;
//        }
//
//        boolean success = itineraryDAO.insertAll(validItems);
//
//        if (success) {
//            resp.sendRedirect(req.getContextPath() + "/trip/detail?id=" + tripId);
//        } else {
//            req.setAttribute("error", "Không thể lưu lịch trình. Vui lòng thử lại.");
//            req.getRequestDispatcher("/views/itinerary_create.jsp").forward(req, resp);
//        }
//    }
//}
package controller;

import dal.ItineraryDAO;
import dal.TripDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import model.Itinerary;
import model.Trips;

@WebServlet(name = "ItineraryCreateServlet", urlPatterns = {"/itinerary/create"})
public class ItineraryCreateServlet extends HttpServlet {

    private final ItineraryDAO itineraryDAO = new ItineraryDAO();
    private final TripDAO tripDAO = new TripDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String tripIdStr = req.getParameter("tripId");
        if (tripIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/trips");
            return;
        }

        int tripId = Integer.parseInt(tripIdStr);
        Trips trip = tripDAO.getTripById(tripId);

        if (trip == null) {
            resp.sendRedirect(req.getContextPath() + "/trips?notfound=1");
            return;
        }

        // ✅ Tính số ngày
        long diff = trip.getEndDate().getTime() - trip.getStartDate().getTime();
        int dayCount = (int) (diff / (1000 * 60 * 60 * 24)) + 1;

        req.setAttribute("trip", trip);
        req.setAttribute("dayCount", dayCount);
        req.getRequestDispatcher("/views/itinerary_create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        int tripId = Integer.parseInt(req.getParameter("tripId"));
        int dayCount = Integer.parseInt(req.getParameter("dayCount"));

        List<Itinerary> validItems = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        for (int day = 1; day <= dayCount; day++) {
            Time previousEnd = null;

            // Kiểm tra tối đa 50 hoạt động/ngày
            for (int i = 1; i <= 50; i++) {
                String title = req.getParameter("title" + day + "_" + i);
                String startStr = req.getParameter("start" + day + "_" + i);
                String endStr = req.getParameter("end" + day + "_" + i);
                String desc = req.getParameter("desc" + day + "_" + i);

                if ((title == null || title.isEmpty()) &&
                    (startStr == null || startStr.isEmpty()) &&
                    (endStr == null || endStr.isEmpty()) &&
                    (desc == null || desc.isEmpty())) {
                    continue;
                }

                try {
                    Time start = new Time(sdf.parse(startStr).getTime());
                    Time end = new Time(sdf.parse(endStr).getTime());

                    if (!end.after(start)) {
                        errors.add("Ngày " + day + " - Hoạt động " + i + ": Giờ kết thúc phải sau giờ bắt đầu.");
                        continue;
                    }

                    if (previousEnd != null && start.getTime() - previousEnd.getTime() < 15 * 60 * 1000) {
                        errors.add("Ngày " + day + " - Hoạt động " + i + ": Phải cách hoạt động trước ít nhất 15 phút.");
                        continue;
                    }

                    Itinerary it = new Itinerary();
                    it.setTripId(tripId);
                    it.setDayNumber(day);
                    it.setTitle(title);
                    it.setDescription(desc);
                    it.setStartTime(start);
                    it.setEndTime(end);

                    validItems.add(it);
                    previousEnd = end;

                } catch (Exception e) {
                    errors.add("Ngày " + day + " - Hoạt động " + i + ": Dữ liệu thời gian không hợp lệ.");
                }
            }
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("tripId", tripId);
            req.setAttribute("oldData", req.getParameterMap());
            req.getRequestDispatcher("/views/itinerary_create.jsp").forward(req, resp);
            return;
        }

        boolean success = itineraryDAO.insertAll(validItems);

        if (success) {
            resp.sendRedirect(req.getContextPath() + "/trip/detail?id=" + tripId);
        } else {
            req.setAttribute("error", "Không thể lưu lịch trình. Vui lòng thử lại.");
            req.getRequestDispatcher("/views/itinerary_create.jsp").forward(req, resp);
        }
    }
}
