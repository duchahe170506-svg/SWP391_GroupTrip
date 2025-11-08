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

    private GroupJoinRequestDAO joinDAO = new GroupJoinRequestDAO();
    private TripDAO tripDAO = new TripDAO();
    private UserDAO userDAO = new UserDAO();
    private MailUtil emailService = new MailUtil();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Users currentUser = (Users) session.getAttribute("currentUser");
        int userId = currentUser.getUser_id();
        int tripId = Integer.parseInt(request.getParameter("id"));

     
        String message = joinDAO.createJoinRequest(tripId, userId);
        request.setAttribute("message", message);

        
        if (message.equals("Yêu cầu tham gia đã được gửi thành công!")) {
            Trips trip = tripDAO.getTripById(tripId);
            Users leader = userDAO.getUserById(trip.getGroupId());
            String subject = "Yêu cầu tham gia chuyến đi";
            String body = "Người dùng " + currentUser.getName() 
                        + " đã gửi yêu cầu tham gia chuyến đi:\n\n"
                        + "Tên chuyến: " + trip.getName() + "\n"
                        + "Địa điểm: " + trip.getLocation() + "\n"
                        + "Thời gian: " + trip.getStartDate() + " đến " + trip.getEndDate() + "\n"
                        + "Ngân sách: " + trip.getBudget() + " VND\n\n"
                        + "Vui lòng xem xét và duyệt yêu cầu.";
            emailService.sendEmail(leader.getEmail(), subject, body);
        }
        
        request.getRequestDispatcher("/trips").forward(request, response);
    }
}
