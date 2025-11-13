package controller;

import dal.ItineraryDAO;
import dal.TripDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Time;
import model.Itinerary;
import model.Trips;
import model.Users;

@WebServlet("/group/manage/itinerary-edit")
public class ItineraryEditServlet extends HttpServlet {

    private final ItineraryDAO itineraryDAO = new ItineraryDAO();
    private final TripDAO tripDAO = new TripDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Users currentUser = (Users) session.getAttribute("currentUser");
        int itineraryId = parseInt(request.getParameter("itinerary_id"));
        if (itineraryId <= 0) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Itinerary itinerary = itineraryDAO.getItineraryById(itineraryId);
        if (itinerary == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Trips trip = tripDAO.getTripById(itinerary.getTripId());
        if (trip == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        String groupParam = request.getParameter("groupId");
        int groupId = Integer.parseInt(groupParam);
        boolean isLeader = tripDAO.isUserLeaderOfGroup(currentUser.getUser_id(), groupId);
        if (!isLeader) {
            response.sendRedirect(request.getContextPath() + "/group/manage/timeline?groupId=" + groupId + "&error=permission");
            return;
        }

        request.setAttribute("groupId", groupId);
        request.setAttribute("trip", trip);
        request.setAttribute("itinerary", itinerary);
        request.getRequestDispatcher("/views/itinerary-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Users currentUser = (Users) session.getAttribute("currentUser");
        int itineraryId = parseInt(request.getParameter("itinerary_id"));
        if (itineraryId <= 0) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Itinerary itinerary = itineraryDAO.getItineraryById(itineraryId);
        if (itinerary == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Trips trip = tripDAO.getTripById(itinerary.getTripId());
        if (trip == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        String groupParam = request.getParameter("groupId");
        int groupId = Integer.parseInt(groupParam);
        boolean isLeader = tripDAO.isUserLeaderOfGroup(currentUser.getUser_id(), groupId);
        if (!isLeader) {
            response.sendRedirect(request.getContextPath() + "/group/manage/timeline?groupId=" + groupId + "&error=permission");
            return;
        }

        try {
            int dayNumber = parseInt(request.getParameter("day_number"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String startTimeStr = request.getParameter("start_time");
            String endTimeStr = request.getParameter("end_time");

            if (dayNumber <= 0 || startTimeStr == null || endTimeStr == null) {
                request.setAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin hợp lệ.");
                doGet(request, response);
                return;
            }

            Time startTime = parseTime(startTimeStr);
            Time endTime = parseTime(endTimeStr);
            if (startTime == null || endTime == null || !endTime.after(startTime)) {
                request.setAttribute("errorMessage", "Thời gian bắt đầu phải trước thời gian kết thúc.");
                doGet(request, response);
                return;
            }

            itinerary.setDayNumber(dayNumber);
            itinerary.setTitle(title);
            itinerary.setDescription(description);
            itinerary.setStartTime(startTime);
            itinerary.setEndTime(endTime);

            boolean success = itineraryDAO.updateItinerary(itinerary);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/group/manage/timeline?groupId=" + groupId + "&updateSuccess=1");
            } else {
                request.setAttribute("errorMessage", "Cập nhật thất bại, vui lòng thử lại.");
                doGet(request, response);
            }
        } catch (Exception ex) {
            request.setAttribute("errorMessage", "Có lỗi xảy ra, vui lòng thử lại.");
            doGet(request, response);
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return -1;
        }
    }

    private Time parseTime(String value) {
        try {
            if (value == null) {
                return null;
            }
            String normalized = value.length() == 5 ? value + ":00" : value;
            return Time.valueOf(normalized);
        } catch (Exception ex) {
            return null;
        }
    }
}

