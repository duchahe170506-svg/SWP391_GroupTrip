
import dal.GroupJoinRequestDAO;
import dal.TripDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Trips;
import model.Users;
import util.MailUtil;

@WebServlet("/trip/join")
public class JoinTripServlet extends HttpServlet {

    private TripDAO tripDAO = new TripDAO();
    private GroupJoinRequestDAO joinDAO = new GroupJoinRequestDAO();
    private UserDAO userDAO = new UserDAO();
    private MailUtil emailService = new MailUtil();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int tripId = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }


        Users currentUser = (Users) session.getAttribute("currentUser");
        int userId = currentUser.getUser_id();

      
        int count = joinDAO.countRequestByUser(tripId, userId);
        if (count >= 1) {
            request.setAttribute("message", "❌ Yêu cầu đã được gửi đi.");
            request.getRequestDispatcher("/trips").forward(request, response);
            return;
        }

        // Tạo request mới
        joinDAO.createRequest(tripId, userId);

        // Lấy leader và sender
        int leaderId = tripDAO.getLeaderIdByTrip(tripId);
        Users leader = userDAO.getUserById(leaderId);
        Users sender = userDAO.getUserById(userId);

        // Lấy thông tin chuyến đi
        Trips trip = tripDAO.getTripById(tripId);

        // Gửi mail với tên & địa điểm chuyến đi
        String subject = "Yêu cầu tham gia chuyến đi";
        String body = "Người dùng " + sender.getName()
                + " đã gửi yêu cầu tham gia chuyến đi:\n\n"
                + "Tên chuyến: " + trip.getName() + "\n"
                + "Địa điểm: " + trip.getLocation() + "\n"
                + "Thời gian: " + trip.getStartDate() + " đến " + trip.getEndDate() + "\n"
                + "Ngân sách dự kiến: " + trip.getBudget() + " VND\n"
                + "Loại hình: " + trip.getTripType() + "\n"
                + "Trạng thái hiện tại: " + trip.getStatus() + "\n\n"
                + "Vui lòng xem xét và duyệt yêu cầu.";

        emailService.sendEmail(leader.getEmail(), subject, body);

        // Thêm message hiển thị trên trang
        request.setAttribute("message", "✅ Yêu cầu tham gia chuyến đi \""
                + trip.getName() + "\" đã được gửi thành công.");
        request.getRequestDispatcher("/trips").forward(request, response);
    }
}
