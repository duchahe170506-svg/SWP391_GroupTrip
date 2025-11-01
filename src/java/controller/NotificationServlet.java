import dal.NotificationDAO;
import dal.ReportsDAO;
import dal.GroupJoinRequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Notifications;
import model.Reports;
import model.Users;

@WebServlet("/notifications")
public class NotificationServlet extends HttpServlet {

    private NotificationDAO notificationDAO = new NotificationDAO();
    private ReportsDAO reportsDAO = new ReportsDAO();
    private GroupJoinRequestDAO joinDAO = new GroupJoinRequestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        String idStr = request.getParameter("id");
        String msg = null;

        try {
           
            if ("markRead".equals(action) && idStr != null) {
                notificationDAO.markAsRead(Integer.parseInt(idStr));
                response.sendRedirect("notifications");
                return;
            }

         
            if ("markAll".equals(action)) {
                notificationDAO.markAllAsRead(currentUser.getUser_id());
                response.sendRedirect("notifications");
                return;
            }

       
            if ("delete".equals(action) && idStr != null) {
                notificationDAO.deleteNotification(Integer.parseInt(idStr));
                response.sendRedirect("notifications?success=deleted");
                return;
            }

           
            if ("acceptInvite".equals(action) && idStr != null) {
                int requestId = Integer.parseInt(idStr);
                msg = joinDAO.respondToInvite(requestId, currentUser.getUser_id(), "ACCEPT");
            }

        
            if ("rejectInvite".equals(action) && idStr != null) {
                int requestId = Integer.parseInt(idStr);
                msg = joinDAO.respondToInvite(requestId, currentUser.getUser_id(), "REJECT");
            }

        } catch (Exception e) {
            e.printStackTrace();
            msg = "❌ Đã xảy ra lỗi, vui lòng thử lại.";
        }

  
        List<Notifications> list = notificationDAO.getNotificationsByUser(currentUser.getUser_id());
        request.setAttribute("notifications", list);

        if (msg != null) {
            request.setAttribute("toastMessage", msg);
        }

        request.getRequestDispatcher("/views/notifications.jsp").forward(request, response);
    }
}
