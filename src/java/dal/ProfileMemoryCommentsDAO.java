// Package: dal
// File: ProfileMemoryCommentsDAO.java
// Assuming you have a DBConnection class for JDBC connection
package dal;

import model.ProfileMemoryComments;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileMemoryCommentsDAO {

    private Connection connection = DBConnect.getConnection(); // Adjust to your DBConnection

    public List<ProfileMemoryComments> getCommentsByShare(int shareId) {
        List<ProfileMemoryComments> list = new ArrayList<>();
        String sql = "SELECT pmc.*, u.name as userName "
                + // Assuming Users has 'name'
                "FROM ProfileMemoryComments pmc "
                + "JOIN Users u ON pmc.user_id = u.user_id "
                + "WHERE pmc.share_id = ? ORDER BY pmc.created_at ASC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shareId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProfileMemoryComments comment = new ProfileMemoryComments();
                comment.setCommentId(rs.getInt("comment_id"));
                comment.setShareId(rs.getInt("share_id"));
                comment.setUserId(rs.getInt("user_id"));
                comment.setContent(rs.getString("content"));
                comment.setCreatedAt(rs.getTimestamp("created_at"));
                comment.setUserName(rs.getString("userName"));
                list.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addComment(int shareId, int userId, String content) {
        String sql = "INSERT INTO ProfileMemoryComments (share_id, user_id, content) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shareId);
            ps.setInt(2, userId);
            ps.setString(3, content);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateComment(int commentId, int userId, String content) {
        String sql = "UPDATE ProfileMemoryComments SET content=? WHERE comment_id=? AND user_id=?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, content);
            ps.setInt(2, commentId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteComment(int commentId, int userId) {
        String sql = "DELETE FROM ProfileMemoryComments WHERE comment_id=? AND user_id=?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, commentId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
