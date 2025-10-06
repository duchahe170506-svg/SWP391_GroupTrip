
import dal.GroupMembersDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Users;
import util.MailUtil;

@WebServlet("/group/remove-member")
public class RemoveMemberServlet extends HttpServlet {

    private GroupMembersDAO memberDAO = new GroupMembersDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userIdParam = req.getParameter("userId");
        if(userIdParam == null || userIdParam.isEmpty()) {
            resp.sendRedirect("manage?groupId=5");
            return;
        }

        int groupId = 5;
        int userId = Integer.parseInt(userIdParam);

        int leaderId = 14; // cứng

        // Chỉ leader mới xóa được
        if(leaderId != 14) {
            resp.sendRedirect("manage?groupId=" + groupId);
            return;
        }

        // Không xóa leader gốc
        if(!memberDAO.isGroupCreator(groupId, userId)) {
            // Lấy thông tin member và leader trước khi xóa
            Users member = userDAO.getUserById(userId);
            Users leader = userDAO.getUserById(leaderId);
            String groupName = memberDAO.getGroupName(groupId);

            // Xóa member
            memberDAO.removeMember(groupId, userId);

            // Gửi mail thông báo cho member
            String subject = "Bạn đã bị xóa khỏi nhóm " + groupName;
            String body = "Xin chào " + member.getName() + ",\n\n"
                    + "Bạn đã bị xóa khỏi nhóm \"" + groupName + "\" bởi " + leader.getName() + ".\n\n"
                    + "Cảm ơn bạn đã tham gia và hy vọng gặp lại trong các chuyến đi khác.\n\n"
                    + "Trân trọng,\nGroupTrip Team";

            MailUtil.sendEmail(member.getEmail(), subject, body);
        }

        resp.sendRedirect("manage?groupId=" + groupId);
    }
}
