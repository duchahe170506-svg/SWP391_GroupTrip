/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

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
import model.Tasks;
import model.Trips;
import model.Users;

/**
 *
 * @author win
 */
@WebServlet(name = "TasksDeleteServlet", urlPatterns = {"/group/manage/tasks-delete"})
public class TasksDeleteServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TasksDeleteServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TasksDeleteServlet at " + request.getContextPath() + "</h1>");
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
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            int task_id = Integer.parseInt(request.getParameter("task_id"));
            int group_id = Integer.parseInt(request.getParameter("group_id"));

            TaskDAO task_dao = new TaskDAO();
            boolean status = task_dao.removeTask(task_id);

            if (status) {
                // Trả JSON báo thành công
                out.print("{\"success\": true, \"message\": \"Xóa thành công!\"}");
            } else {
                // Trả JSON báo thất bại (ví dụ task không tồn tại)
                out.print("{\"success\": false, \"message\": \"Không thể xóa task này.\"}");
            }

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"success\": false, \"message\": \"Lỗi hệ thống: " + e.getMessage() + "\"}");
            }
        }
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
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Kiểm tra đăng nhập
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("currentUser") == null) {
                out.print("{\"success\": false, \"message\": \"Bạn cần đăng nhập để thực hiện thao tác này\"}");
                return;
            }
            
            Users currentUser = (Users) session.getAttribute("currentUser");
            int currentUserId = currentUser.getUser_id();
            
            String taskIdParam = request.getParameter("task_id");

            if (taskIdParam == null || taskIdParam.isEmpty()) {
                out.print("{\"success\": false, \"message\": \"Thiếu task_id\"}");
                return;
            }

            int task_id = Integer.parseInt(taskIdParam);
            TaskDAO task_dao = new TaskDAO();
            TripDAO trip_dao = new TripDAO();
            
            // Lấy thông tin task để kiểm tra
            Tasks task = task_dao.getTaskById(task_id);
            if (task == null) {
                out.print("{\"success\": false, \"message\": \"Task không tồn tại\"}");
                return;
            }
            
            // Kiểm tra xem user có phải là leader của trip không
            int leaderId = trip_dao.getLeaderIdByTrip(task.getTrip_id());
            boolean isLeader = (currentUserId == leaderId);
            if (!isLeader) {
                out.print("{\"success\": false, \"message\": \"Chỉ người tạo chuyến đi mới có quyền xóa task\"}");
                return;
            }
            
            // Kiểm tra status của task
            if ("Completed".equals(task.getStatus())) {
                out.print("{\"success\": false, \"message\": \"Không thể xóa task đã hoàn thành\"}");
                return;
            }
            
            // Thực hiện xóa
            boolean success = task_dao.removeTask(task_id);

            if (success) {
                out.print("{\"success\": true}");
            } else {
                out.print("{\"success\": false, \"message\": \"Không thể xóa task\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        } finally {
            out.close();
        }
    }
}
