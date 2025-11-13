package controller;

import dal.GroupMembersDAO;
import dal.TripDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Trips;
import model.Users;

@WebFilter({"/group/*"}) 
public class GroupAccessFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        Users currentUser = (session != null) ? (Users) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String groupIdParam = req.getParameter("groupId");
        if (groupIdParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số group_id");
            return;
        }

        int groupId = Integer.parseInt(groupIdParam);
        int userId = currentUser.getUser_id();

        GroupMembersDAO dao = new GroupMembersDAO();
        TripDAO tripDAO = new TripDAO();

      
        boolean isMember = dao.isUserInGroup(groupId, userId);

        if (!isMember && !"Admin".equals(currentUser.getRole())) {
          
            req.setAttribute("error", "Bạn không thuộc nhóm này nên không thể truy cập.");
            req.getRequestDispatcher("/views/403.jsp").forward(req, resp);
            return;
        }

        Trips trip = tripDAO.getTripById(groupId); 

        if (trip != null && "Blocked".equalsIgnoreCase(trip.getStatus())
                && !"Admin".equals(currentUser.getRole())) {
            req.setAttribute("error", "Chuyến đi này đã bị khóa. Chỉ Admin có quyền truy cập.");
            req.getRequestDispatcher("/views/403.jsp").forward(req, resp);
            return;
        }
       
        chain.doFilter(request, response);
    }
}
