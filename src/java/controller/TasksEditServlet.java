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
import java.util.List;
import model.GroupMembers;
import model.Tasks;
import model.Trips;
import model.Users;

@WebServlet("/group/manage/tasks-edit")
public class TasksEditServlet extends HttpServlet {

    private TaskDAO taskDAO = new TaskDAO();
    private TripDAO tripDAO = new TripDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Users currentUser = (Users) session.getAttribute("currentUser");
        int currentUserId = currentUser.getUser_id();

        String taskIdParam = request.getParameter("task_id");
        if (taskIdParam == null || taskIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks");
            return;
        }
        
        int taskId = Integer.parseInt(taskIdParam);
        Tasks task = taskDAO.getTaskById(taskId);
        
        if (task == null) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks");
            return;
        }
        
        int tripId = task.getTrip_id();
        
        // Lấy thông tin trip để validate deadline
        Trips trip = tripDAO.getTripById(tripId);
        if (trip == null) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks");
            return;
        }
        
        int groupId = trip.getGroupId();
        
        // Check if current user is leader
        boolean isLeader = tripDAO.isUserLeaderOfGroup(currentUserId, groupId);

        if (!isLeader) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks?groupId=" + groupId + "&error=permission");
            return;
        }
        
        GroupMembersDAO gmd = new GroupMembersDAO();
        List<GroupMembers> listmember = gmd.getMembersByGroup(groupId);

        request.setAttribute("task", task);
        request.setAttribute("trip", trip);
        request.setAttribute("tripId", tripId);
        request.setAttribute("groupId", groupId);
        request.setAttribute("listMember", listmember);
        request.setAttribute("isLeader", isLeader);
        request.getRequestDispatcher("/views/task-detail.jsp").forward(request, response);
    }

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
        
        int taskId = Integer.parseInt(request.getParameter("task_id"));
        int tripId = Integer.parseInt(request.getParameter("trip_id"));
        String description = request.getParameter("description");
        String deadline = request.getParameter("deadline");
        String estimatedCost = request.getParameter("estimated_cost");
        String status = request.getParameter("status");
        String assignedToParam = request.getParameter("assigned_to");
        Integer assigned_to = null;
        if (assignedToParam != null && !assignedToParam.trim().isEmpty()) {
            assigned_to = Integer.parseInt(assignedToParam);
        }
        
        // Lấy thông tin trip
        Trips trip = tripDAO.getTripById(tripId);
        if (trip == null) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks");
            return;
        }
        
        int groupId = trip.getGroupId();
        
        // Lấy thông tin task hiện tại
        Tasks currentTask = taskDAO.getTaskById(taskId);
        if (currentTask == null) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks?groupId=" + groupId);
            return;
        }
        
        // Check if current user is leader
        boolean isLeader = tripDAO.isUserLeaderOfGroup(currentUserId, groupId);
        if (!isLeader) {
            request.setAttribute("errorMessage", "Chỉ người tạo chuyến đi mới có quyền sửa task.");
            doGet(request, response);
            return;
        }
        
        // Validate deadline
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date deadlineDate = sdf.parse(deadline);
            
            // Lấy ngày hôm nay (00:00:00)
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            Date today = cal.getTime();
            
            // Kiểm tra deadline không được trước ngày hôm nay (cho phép = hôm nay)
            if (deadlineDate.before(today)) {
                request.setAttribute("errorMessage", "Deadline không được trong quá khứ (phải từ hôm nay trở đi).");
                doGet(request, response);
                return;
            }
            
            // Kiểm tra start_date <= deadline <= end_date
            if (trip != null) {
                if (trip.getStartDate() != null && deadlineDate.before(trip.getStartDate())) {
                    request.setAttribute("errorMessage", "Deadline phải sau ngày bắt đầu chuyến đi (" + sdf.format(trip.getStartDate()) + ").");
                    doGet(request, response);
                    return;
                }
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
        
        boolean success = taskDAO.updateTask(taskId, description, deadline, estimatedCost, status, assigned_to);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks?groupId=" + groupId);
        } else {
            request.setAttribute("errorMessage", "Cập nhật thất bại, vui lòng thử lại.");
            doGet(request, response);
        }
    }
}
