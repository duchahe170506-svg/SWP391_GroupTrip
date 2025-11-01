// Package: dal
// File: ProfileMemoryReactionsDAO.java
// Assuming you have a DBConnection class for JDBC connection
package dal;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ProfileMemoryReactionsDAO {
    private Connection connection = DBConnect.getConnection(); // Adjust to your DBConnection

    public Map<String, Integer> getReactionCountsByShare(int shareId) {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT type, COUNT(*) as count FROM ProfileMemoryReactions " +
                     "WHERE share_id = ? GROUP BY type";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shareId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("type"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public int countReactionsByShare(int shareId) {
        String sql = "SELECT COUNT(*) FROM ProfileMemoryReactions WHERE share_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shareId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getUserReactionType(int shareId, int userId) {
        String sql = "SELECT type FROM ProfileMemoryReactions WHERE share_id = ? AND user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shareId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addOrUpdateReaction(int shareId, int userId, String type) {
        String sql = "INSERT INTO ProfileMemoryReactions (share_id, user_id, type) " +
                     "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE type = ?, reacted_at = NOW()";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shareId);
            ps.setInt(2, userId);
            ps.setString(3, type);
            ps.setString(4, type);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}