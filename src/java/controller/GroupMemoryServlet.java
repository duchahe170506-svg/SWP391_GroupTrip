package controller;

import dal.*;
import model.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import com.google.gson.Gson;

@WebServlet("/group/memories")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 15 // 15MB
)
public class GroupMemoryServlet extends HttpServlet {

    private MemoriesDAO memoryDAO = new MemoriesDAO();
    private MemoryCommentsDAO commentDAO = new MemoryCommentsDAO();
    private MemoryReactionsDAO reactionDAO = new MemoryReactionsDAO();
    private ProfileSharedMemoriesDAO profileShareDAO = new ProfileSharedMemoriesDAO();
    private TripDAO tripDAO = new TripDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String groupIdStr = req.getParameter("groupId");
        if (groupIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/groups");
            return;
        }

        try {
            int groupId = Integer.parseInt(groupIdStr);
            int tripId = groupId;

            List<Memories> memories = memoryDAO.getMemoriesByGroup(groupId);
            Trips trip = tripDAO.getTripById(tripId);

            HttpSession session = req.getSession();
            Users user = (Users) session.getAttribute("currentUser");

            if (user == null) {
                req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
                return;
            }

            int userId = user.getUser_id();

            for (Memories m : memories) {
                m.setReactionsCount(reactionDAO.countReactionsByMemory(m.getMemoryId()));
                m.setComments(commentDAO.getCommentsByMemory(m.getMemoryId()));
                String myReact = reactionDAO.getUserReactionType(m.getMemoryId(), userId);
                m.setUserReaction(myReact);
            }

            req.setAttribute("memories", memories);
            req.setAttribute("groupId", groupId);
            req.setAttribute("trip", trip);
            req.getRequestDispatcher("/views/group-memory.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        Users user = (Users) session.getAttribute("currentUser");

        if (user == null) {
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        int userId = user.getUser_id();

        try {
            switch (action) {
                case "react":
                    handleReact(req, resp, userId);
                    break;
                case "comment":
                    handleComment(req, resp, userId);
                    break;
                case "add":
                    handleAdd(req, resp, userId);
                    break;
                case "edit":
                    handleEdit(req, resp, userId);
                    break;
                case "delete":
                    handleDelete(req, resp);
                    break;
                case "share":
                    handleShare(req, resp, userId);
                    break;
                case "editComment":
                    handleEditComment(req, resp, userId);
                    break;
                case "deleteComment":
                    handleDeleteComment(req, resp, userId);
                    break;
                default:
                    resp.sendError(400, "Unknown action");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(500);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().print(new Gson().toJson(Map.of("success", false, "error", ex.getMessage())));
        }
    }

    private void handleReact(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int memoryId = Integer.parseInt(req.getParameter("memoryId"));
        String type = req.getParameter("type");
        int total = reactionDAO.addOrUpdateReaction(memoryId, userId, type);
        req.getSession().setAttribute("successMessage", "Bạn đã " + type + " memory thành công!");

        int groupId = Integer.parseInt(req.getParameter("groupId"));
        resp.sendRedirect(req.getContextPath() + "/group/memories?groupId=" + groupId);
    }

    private void handleComment(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int memoryId = Integer.parseInt(req.getParameter("memoryId"));
        String content = req.getParameter("content");
        boolean ok = commentDAO.addComment(memoryId, userId, content);

        int groupId = Integer.parseInt(req.getParameter("groupId"));
        if (ok) {
            req.getSession().setAttribute("successMessage", "💬 Bình luận thành công!");
        } else {
            req.getSession().setAttribute("errorMessage", "❌ Bình luận thất bại!");
        }

        resp.sendRedirect(req.getContextPath() + "/group/memories?groupId=" + groupId);
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int groupId = Integer.parseInt(req.getParameter("groupId"));
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        Part filePart = req.getPart("imageFile");
        String imageUrl = null;
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
            String uploadPath = req.getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            filePart.write(uploadPath + File.separator + fileName);
            imageUrl = "uploads/" + fileName;
        }

        int newId = memoryDAO.addMemory(userId, groupId, title, content, imageUrl);
        if (newId > 0) {

            req.getSession().setAttribute("successMessage", "🎉 Đăng kỷ niệm thành công!");
        } else {
            req.getSession().setAttribute("errorMessage", "❌ Đăng thất bại, vui lòng thử lại.");
        }

        resp.sendRedirect(req.getContextPath() + "/group/memories?groupId=" + groupId);
    }

    private void handleEdit(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int groupId = Integer.parseInt(req.getParameter("groupId"));
        int memoryId = Integer.parseInt(req.getParameter("memoryId"));
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String oldImage = req.getParameter("oldImageUrl");

        Part filePart = req.getPart("imageFile");
        String imageUrl = oldImage;

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
            String uploadPath = req.getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            filePart.write(uploadPath + File.separator + fileName);
            imageUrl = "uploads/" + fileName;
        }

        boolean ok = memoryDAO.updateMemory(memoryId, title, content, imageUrl);
        if (ok) {
            req.getSession().setAttribute("successMessage", "✅ Cập nhật kỷ niệm thành công!");
        } else {
            req.getSession().setAttribute("errorMessage", "❌ Cập nhật thất bại, vui lòng thử lại!");
        }

        resp.sendRedirect(req.getContextPath() + "/group/memories?groupId=" + groupId);
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int memoryId = Integer.parseInt(req.getParameter("memoryId"));
        int groupId = Integer.parseInt(req.getParameter("groupId"));
        boolean ok = memoryDAO.deleteMemory(memoryId);
        if (ok) {
            req.getSession().setAttribute("successMessage", "🗑️ Xóa kỷ niệm thành công!");
        } else {
            req.getSession().setAttribute("errorMessage", "❌ Xóa thất bại, vui lòng thử lại!");
        }

        resp.sendRedirect(req.getContextPath() + "/group/memories?groupId=" + groupId);
    }

    private void handleShare(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int groupId = Integer.parseInt(req.getParameter("groupId"));
        int memoryId = Integer.parseInt(req.getParameter("memoryId"));
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String imageUrl = req.getParameter("imageUrl");

        String privacy = req.getParameter("privacy");

        boolean ok = profileShareDAO.addShare(memoryId, userId, title, content, imageUrl, privacy != null ? privacy : "Public");
        if (ok) {
            req.getSession().setAttribute("successMessage", "📤 Chia sẻ kỷ niệm thành công!");
        } else {
            req.getSession().setAttribute("errorMessage", "❌ Không thể chia sẻ, vui lòng thử lại!");
        }

        resp.sendRedirect(req.getContextPath() + "/group/memories?groupId=" + groupId);
    }

    private void handleEditComment(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int commentId = Integer.parseInt(req.getParameter("commentId"));
        int memoryId = Integer.parseInt(req.getParameter("memoryId"));
        int groupId = Integer.parseInt(req.getParameter("groupId"));
        String content = req.getParameter("content");

        boolean ok = commentDAO.updateComment(commentId, userId, content);
        if (ok) {
            req.getSession().setAttribute("successMessage", "✅ Cập nhật bình luận thành công!");
        } else {
            req.getSession().setAttribute("errorMessage", "❌ Không thể cập nhật bình luận!");
        }
        resp.sendRedirect(req.getContextPath() + "/group/memories?groupId=" + groupId);
    }

    private void handleDeleteComment(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int commentId = Integer.parseInt(req.getParameter("commentId"));
        int memoryId = Integer.parseInt(req.getParameter("memoryId"));
        int groupId = Integer.parseInt(req.getParameter("groupId"));

        boolean ok = commentDAO.deleteComment(commentId, userId);
        if (ok) {
            req.getSession().setAttribute("successMessage", "🗑️ Xóa bình luận thành công!");
        } else {
            req.getSession().setAttribute("errorMessage", "❌ Không thể xóa bình luận!");
        }
        resp.sendRedirect(req.getContextPath() + "/group/memories?groupId=" + groupId);
    }

}
