package controller;

import dal.GroupMembersDAO;
import dal.TaskDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.GroupMembers;
import model.Tasks;

@WebServlet("/group/manage/tasks-edit")
public class TasksEditServlet extends HttpServlet {

    private TaskDAO taskDAO = new TaskDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String taskIdParam = request.getParameter("task_id");
        int groupId = Integer.parseInt(request.getParameter("group_id"));
        if (taskIdParam == null || taskIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks");
            return;
        }
        GroupMembersDAO gmd = new GroupMembersDAO();
        List<GroupMembers> listmember = gmd.getMembersByGroup(groupId);
        int taskId = Integer.parseInt(taskIdParam);
        Tasks task = taskDAO.getTaskById(taskId);

        request.setAttribute("task", task);
        request.setAttribute("groupId", groupId);
        request.setAttribute("listMember", listmember);
        request.getRequestDispatcher("/views/task-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int taskId = Integer.parseInt(request.getParameter("task_id"));
        int groupId = Integer.parseInt(request.getParameter("group_id"));
        String description = request.getParameter("description");
        String deadline = request.getParameter("deadline");
        String estimatedCost = request.getParameter("estimated_cost");
        String status = request.getParameter("status");
        String assignedToParam = request.getParameter("assigned_to");
        Integer assigned_to = null;
        if (assignedToParam != null && !assignedToParam.trim().isEmpty()) {
            assigned_to = Integer.parseInt(assignedToParam);
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
