/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.GroupJoinRequestDAO;
import dal.GroupMembersDAO;
import dal.TaskDAO;
import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.GroupJoinRequests;
import model.GroupMembers;
import model.Tasks;

/**
 *
 * @author win
 */
@WebServlet(name = "TasksManageServlet", urlPatterns = {"/group/manage/tasks"})
public class TasksManageServlet extends HttpServlet {

    private GroupMembersDAO memberDAO = new GroupMembersDAO();
    private GroupJoinRequestDAO requestDAO = new GroupJoinRequestDAO();
    private UserDAO userDAO = new UserDAO();
    private TaskDAO taskDAO = new TaskDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String groupIdStr = request.getParameter("groupId");
        int groupId = groupIdStr != null ? Integer.parseInt(groupIdStr) : 1;
        int tripId = 1;
        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        List<Tasks> taskList = taskDAO.getAllTasksByTripId(tripId, keyword, status);
        UserDAO ud = new UserDAO();
        for (int i = 0; i < taskList.size(); i++ ){
            String name = "Chưa giao";
            if (taskList.get(i).getAssigned_to() != null){
                name = ud.getUserById(taskList.get(i).getAssigned_to()).getName();
            }
            taskList.get(i).setAssignedUserName(name);
        }
        request.setAttribute("groupId", groupId);
        request.setAttribute("tasks", taskList);
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);
        request.getRequestDispatcher("/views/group-tasks.jsp").forward(request, response);
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
