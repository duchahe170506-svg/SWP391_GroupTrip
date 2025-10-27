package dal;

import model.MemoryComments;
import java.sql.*;
import java.util.*;
import dal.DBConnect;

public class MemoryCommentsDAO extends DBConnect {

    public List<MemoryComments> getCommentsByMemory(int memoryId) throws SQLException {
        List<MemoryComments> list = new ArrayList<>();
        String sql = "SELECT mc.*, u.name AS userName FROM MemoryComments mc "
                + "JOIN Users u ON mc.user_id = u.user_id "
                + "WHERE mc.memory_id = ? ORDER BY mc.created_at ASC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MemoryComments c = new MemoryComments();
                c.setCommentId(rs.getInt("comment_id"));
                c.setMemoryId(rs.getInt("memory_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setContent(rs.getString("content"));
                c.setCreatedAt(rs.getTimestamp("created_at"));
                c.setUserName(rs.getString("userName"));
                list.add(c);
            }
        }
        return list;
    }

    public boolean addComment(int memoryId, int userId, String content) throws Exception {
        String sql = "INSERT INTO MemoryComments(memory_id, user_id, content) VALUES(?,?,?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memoryId);
            ps.setInt(2, userId);
            ps.setString(3, content);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateComment(int commentId, int userId, String newContent) throws SQLException {
        String sql = "UPDATE MemoryComments SET content=? WHERE comment_id=? AND user_id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newContent);
            ps.setInt(2, commentId);
            ps.setInt(3, userId); // đảm bảo chỉ user này mới sửa
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteComment(int commentId, int userId) throws SQLException {
    String sql = "DELETE FROM MemoryComments WHERE comment_id=? AND user_id=?";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, commentId);
        ps.setInt(2, userId); // chỉ xóa comment của chính user
        return ps.executeUpdate() > 0;
    }
}

}
