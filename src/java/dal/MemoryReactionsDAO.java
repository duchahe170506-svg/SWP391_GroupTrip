package dal;

import java.sql.*;
import dal.DBConnect;

public class MemoryReactionsDAO extends DBConnect {

    public int addOrUpdateReaction(int memoryId, int userId, String type) throws Exception {
        String sqlCheck = "SELECT type FROM MemoryReactions WHERE memory_id = ? AND user_id = ?";
        String sqlInsert = "INSERT INTO MemoryReactions(memory_id, user_id, type) VALUES (?, ?, ?)";
        String sqlUpdate = "UPDATE MemoryReactions SET type = ?, reacted_at = NOW() WHERE memory_id = ? AND user_id = ?";
        String sqlCount = "SELECT COUNT(*) FROM MemoryReactions WHERE memory_id = ?";

        try (Connection conn = getConnection()) {
            // Kiểm tra xem user đã react chưa
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, memoryId);
                psCheck.setInt(2, userId);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next()) {
                    // Đã react, update type nếu khác
                    String oldType = rs.getString("type");
                    if (!oldType.equals(type)) {
                        try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                            psUpdate.setString(1, type);
                            psUpdate.setInt(2, memoryId);
                            psUpdate.setInt(3, userId);
                            psUpdate.executeUpdate();
                        }
                    }
                } else {
                    // Chưa react, insert mới
                    try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                        psInsert.setInt(1, memoryId);
                        psInsert.setInt(2, userId);
                        psInsert.setString(3, type);
                        psInsert.executeUpdate();
                    }
                }
            }

            // Trả về tổng số reaction hiện tại của memory
            try (PreparedStatement psCount = conn.prepareStatement(sqlCount)) {
                psCount.setInt(1, memoryId);
                ResultSet rsCount = psCount.executeQuery();
                if (rsCount.next()) {
                    return rsCount.getInt(1);
                }
            }
        }
        return 0;
    }

    // Đếm tổng reaction cho memory
    public int countReactionsByMemory(int memoryId) throws Exception {
        String sql = "SELECT COUNT(*) FROM MemoryReactions WHERE memory_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memoryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Lấy reaction của user cho memory
    public String getUserReaction(int memoryId, int userId) throws Exception {
        String sql = "SELECT type FROM MemoryReactions WHERE memory_id = ? AND user_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memoryId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("type");
            }
        }
        return null;
    }

    public String getUserReactionType(int memoryId, int userId) {
        String sql = "SELECT reaction_type FROM MemoryReactions WHERE memory_id=? AND user_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, memoryId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("reaction_type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
