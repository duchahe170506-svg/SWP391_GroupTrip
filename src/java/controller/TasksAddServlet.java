/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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

/**
 *
 * @author win
 */
@WebServlet(name = "TasksAddServlet", urlPatterns = {"/group/manage/tasks-add"})
public class TasksAddServlet extends HttpServlet {

    private TaskDAO taskDAO = new TaskDAO();
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
        String groupParam = request.getParameter("group_id");
        if (groupParam == null || groupParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks");
            return;
        }
        int groupId = Integer.parseInt(groupParam);
        GroupMembersDAO gmd = new GroupMembersDAO();
        List<GroupMembers> listmember = gmd.getMembersByGroup(groupId);
        request.setAttribute("groupId", groupId);
        request.setAttribute("listMember", listmember);
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
        int groupId = Integer.parseInt(request.getParameter("group_id"));
        String description = request.getParameter("description");
        String deadline = request.getParameter("deadline");
        String estimatedCost = request.getParameter("estimated_cost");
        String status = request.getParameter("status");
        String assigned_to = request.getParameter("assigned_to");

        boolean success = taskDAO.insertTask(groupId, description, deadline, estimatedCost, status, assigned_to);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/group/manage/tasks?updateSuccess=1");
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
