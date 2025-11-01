package controller;

import com.google.gson.Gson;
import dal.ProfileSharedMemoriesDAO;
import dal.ProfileMemoryCommentsDAO;
import dal.ProfileMemoryReactionsDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import model.ProfileSharedMemories;
import model.ProfileMemoryComments;
import model.Users;

@WebServlet("/profile-memories")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 15 // 15MB
)
public class ProfileMemoryServlet extends HttpServlet {

    private final ProfileSharedMemoriesDAO sharedDAO = new ProfileSharedMemoriesDAO();
    private final ProfileMemoryCommentsDAO commentDAO = new ProfileMemoryCommentsDAO();
    private final ProfileMemoryReactionsDAO reactionDAO = new ProfileMemoryReactionsDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Users currentUser = (Users) req.getSession().getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        int userId = currentUser.getUser_id();

        List<ProfileSharedMemories> sharedList = sharedDAO.getSharedMemoriesByUser(userId);

        for (ProfileSharedMemories share : sharedList) {
            int shareId = share.getShareId();

            // Lấy bình luận
            share.setComments(commentDAO.getCommentsByShare(shareId));

            // Lấy map đếm từng loại cảm xúc (Like, Love, ...)
            Map<String, Integer> reactionsMap = reactionDAO.getReactionCountsByShare(shareId);
            share.setReactionsCountMap(reactionsMap);

            // Tính tổng cảm xúc từ map
            int totalReactions = reactionsMap.values().stream().mapToInt(Integer::intValue).sum();
            share.setReactionsCount(totalReactions);

            // Lấy cảm xúc của người dùng hiện tại
            share.setUserReaction(reactionDAO.getUserReactionType(shareId, userId));
        }

        req.setAttribute("shares", sharedList);
        req.getRequestDispatcher("/views/profile-memories.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Users currentUser = (Users) req.getSession().getAttribute("currentUser");
        if (currentUser == null) {
            sendJsonError(resp, "Chưa đăng nhập");
            return;
        }

        int userId = currentUser.getUser_id();
        String action = req.getParameter("action");
        resp.setContentType("application/json;charset=UTF-8");

        if ("comment".equals(action)) {
            try {
                int shareId = Integer.parseInt(req.getParameter("shareId"));
                String content = req.getParameter("content");
                if (content == null || content.trim().isEmpty()) {
                    sendJsonError(resp, "Nội dung bình luận không được để trống");
                    return;
                }

                boolean success = commentDAO.addComment(shareId, userId, content.trim());
                if (success) {
                    resp.getWriter().print(gson.toJson(Map.of(
                            "success", true,
                            "userName", currentUser.getName(),
                            "content", content.trim()
                    )));
                } else {
                    sendJsonError(resp, "Không thể thêm bình luận");
                }
            } catch (Exception e) {
                sendJsonError(resp, "Dữ liệu không hợp lệ");
            }
            return;
        }

        if ("react".equals(action)) {
            try {
                int shareId = Integer.parseInt(req.getParameter("shareId"));
                String type = req.getParameter("type");

                // Kiểm tra loại cảm xúc hợp lệ
                if (!List.of("Like", "Love", "Haha", "Wow", "Sad", "Angry").contains(type)) {
                    sendJsonError(resp, "Loại cảm xúc không hợp lệ");
                    return;
                }

                reactionDAO.addOrUpdateReaction(shareId, userId, type);
                Map<String, Integer> counts = reactionDAO.getReactionCountsByShare(shareId);

                resp.getWriter().print(gson.toJson(Map.of(
                        "success", true,
                        "reactions", counts,
                        "userReaction", type
                )));
            } catch (Exception e) {
                sendJsonError(resp, "Dữ liệu không hợp lệ");
            }
            return;
        }

        if ("editShare".equals(action)) {
            try {
                int shareId = Integer.parseInt(req.getParameter("shareId"));
                String title = req.getParameter("title");
                String content = req.getParameter("content");
                String privacy = req.getParameter("privacy");
                Part imagePart = req.getPart("imageFile");
                String oldImageUrl = req.getParameter("oldImageUrl"); // lấy ảnh cũ

                if (title == null || title.trim().isEmpty()) {
                    sendJsonError(resp, "Tiêu đề không được trống");
                    return;
                }

                String imageUrl = null;
                if (imagePart != null && imagePart.getSize() > 0) {
                    // nếu chọn ảnh mới → lưu lại
                    String fileName = System.currentTimeMillis() + "_" + imagePart.getSubmittedFileName();
                    String uploadPath = req.getServletContext().getRealPath("/uploads");
                    java.nio.file.Files.createDirectories(java.nio.file.Paths.get(uploadPath));
                    imagePart.write(uploadPath + "/" + fileName);
                    imageUrl = "uploads/" + fileName;
                } else {
                    // nếu không chọn ảnh → dùng lại ảnh cũ
                    imageUrl = oldImageUrl;
                }

                boolean success = sharedDAO.updateShare(
                        title.trim(), content.trim(), imageUrl, privacy, shareId, userId
                );

                if (success) {
                    resp.getWriter().print(gson.toJson(Map.of("success", true)));
                } else {
                    sendJsonError(resp, "Không thể cập nhật bài viết");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendJsonError(resp, "Dữ liệu không hợp lệ");
            }
            return;
        }

        if ("deleteShare".equals(action)) {
            try {
                int shareId = Integer.parseInt(req.getParameter("shareId"));
                boolean success = sharedDAO.deleteShare(shareId, userId);
                if (success) {
                    resp.getWriter().print(gson.toJson(Map.of("success", true)));
                } else {
                    sendJsonError(resp, "Không thể xóa bài viết");
                }
            } catch (Exception e) {
                sendJsonError(resp, "Dữ liệu không hợp lệ");
            }
            return;
        }

        if ("editComment".equals(action)) {
            try {
                int commentId = Integer.parseInt(req.getParameter("commentId"));
                String content = req.getParameter("content");

                if (content == null || content.trim().isEmpty()) {
                    sendJsonError(resp, "Nội dung bình luận không được để trống");
                    return;
                }

                boolean success = commentDAO.updateComment(commentId, userId, content.trim());
                if (success) {
                    resp.getWriter().print(gson.toJson(Map.of("success", true)));
                } else {
                    sendJsonError(resp, "Không thể sửa bình luận");
                }
            } catch (Exception e) {
                sendJsonError(resp, "Dữ liệu không hợp lệ");
            }
            return;
        }

        if ("deleteComment".equals(action)) {
            try {
                int commentId = Integer.parseInt(req.getParameter("commentId"));
                boolean success = commentDAO.deleteComment(commentId, userId);
                if (success) {
                    resp.getWriter().print(gson.toJson(Map.of("success", true)));
                } else {
                    sendJsonError(resp, "Không thể xóa bình luận");
                }
            } catch (Exception e) {
                sendJsonError(resp, "Dữ liệu không hợp lệ");
            }
            return;
        }

        if ("submit".equals(action)) {
            try {
                String title = req.getParameter("title");
                String content = req.getParameter("content");
                String privacy = req.getParameter("privacy");
                Part imagePart = req.getPart("imageFile");

                if (title == null || title.trim().isEmpty()) {
                    sendJsonError(resp, "Tiêu đề không được để trống");
                    return;
                }

                String imageUrl = null;
                if (imagePart != null && imagePart.getSize() > 0) {
                    String fileName = System.currentTimeMillis() + "_" + imagePart.getSubmittedFileName();
                    String uploadPath = req.getServletContext().getRealPath("/uploads");
                    java.nio.file.Files.createDirectories(java.nio.file.Paths.get(uploadPath));
                    imagePart.write(uploadPath + "/" + fileName);
                    imageUrl = "uploads/" + fileName;
                }

                boolean success = sharedDAO.createShare(userId, title.trim(), content.trim(), imageUrl, privacy);
                if (success) {
                    resp.getWriter().print(gson.toJson(Map.of("success", true)));
                } else {
                    sendJsonError(resp, "Không thể đăng kỷ niệm");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendJsonError(resp, "Dữ liệu không hợp lệ");
            }
            return;
        }

        sendJsonError(resp, "Hành động không hợp lệ");
    }

    private void sendJsonError(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print(gson.toJson(Map.of("success", false, "error", message)));
    }
}
