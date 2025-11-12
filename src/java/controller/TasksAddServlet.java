/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.GroupMembersDAO;
import dal.TaskDAO;
import dal.TripDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import model.GroupMembers;
import model.Tasks;
import model.Trips;
import model.Users;

/**
 *
 * @author win
 */
@WebServlet(name = "TasksAddServlet", urlPatterns = {"/group/manage/tasks-add"})
public class TasksAddServlet extends HttpServlet {

    private TaskDAO taskDAO = new TaskDAO();
    private TripDAO tripDAO = new TripDAO();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TasksAddServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TasksAddServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Users currentUser = (Users) session.getAttribute("currentUser");
        int currentUserId = currentUser.getUser_id();
        
        String groupParam = request.getParameter("groupId");
        if (groupParam == null || groupParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks");
            return;
        }
        int groupId = Integer.parseInt(groupParam);
        
        // Check if current user is leader of this trip
        boolean isLeader = tripDAO.isUserLeaderOfGroup(currentUserId, groupId);
        
        if (!isLeader) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks?groupId=" + groupId + "&error=permission");
            return;
        }
        
        // Lấy tripId từ groupId
        int tripId = tripDAO.getTripIdByGroupId(groupId);
        if (tripId == -1) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks?groupId=" + groupId + "&error=notfound");
            return;
        }
        
        // Get trip info to validate deadline
        Trips trip = tripDAO.getTripById(tripId);
        
        GroupMembersDAO gmd = new GroupMembersDAO();
        List<GroupMembers> listmember = gmd.getMembersByGroup(groupId);
        request.setAttribute("groupId", groupId);
        request.setAttribute("tripId", tripId);
        request.setAttribute("listMember", listmember);
        request.setAttribute("trip", trip);
        request.getRequestDispatcher("/views/task-add.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Users currentUser = (Users) session.getAttribute("currentUser");
        int currentUserId = currentUser.getUser_id();
        
        int groupId = Integer.parseInt(request.getParameter("groupId"));
        String description = request.getParameter("description");
        String deadline = request.getParameter("deadline");
        String estimatedCost = request.getParameter("estimated_cost");
        String status = request.getParameter("status");
        String assigned_to = request.getParameter("assigned_to");
        
        // Check if current user is leader
        boolean isLeader = tripDAO.isUserLeaderOfGroup(currentUserId, groupId);
        
        if (!isLeader) {
            request.setAttribute("errorMessage", "Chỉ người tạo chuyến đi mới có quyền thêm task.");
            doGet(request, response);
            return;
        }
        
        // Lấy trip_id từ group_id
        int tripId = tripDAO.getTripIdByGroupId(groupId);
        if (tripId == -1) {
            request.setAttribute("errorMessage", "Không tìm thấy chuyến đi cho nhóm này.");
            doGet(request, response);
            return;
        }
        
        // Lấy thông tin trip để validate deadline
        Trips trip = tripDAO.getTripById(tripId);
        
        // Validate deadline
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date deadlineDate = sdf.parse(deadline);
            
            // Lấy ngày hôm nay (00:00:00)
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date today = cal.getTime();
            
            // Kiểm tra deadline không được trước ngày hôm nay (cho phép = hôm nay)
            if (deadlineDate.before(today)) {
                request.setAttribute("errorMessage", "Deadline không được trong quá khứ (phải từ hôm nay trở đi).");
                doGet(request, response);
                return;
            }
            
            // Kiểm tra start_date <= deadline <= end_date
            if (trip != null) {
                if (trip.getEndDate() != null && deadlineDate.after(trip.getEndDate())) {
                    request.setAttribute("errorMessage", "Deadline phải trước ngày kết thúc chuyến đi (" + sdf.format(trip.getEndDate()) + ").");
                    doGet(request, response);
                    return;
                }
            }
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ.");
            doGet(request, response);
            return;
        }

        boolean success = taskDAO.insertTask(tripId, description, deadline, estimatedCost, status, assigned_to);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks?groupId=" + groupId + "&updateSuccess=1");
        } else {
            request.setAttribute("errorMessage", "Cập nhật thất bại, vui lòng thử lại.");
            doGet(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
