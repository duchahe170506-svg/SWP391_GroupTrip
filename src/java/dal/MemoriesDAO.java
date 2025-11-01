package dal;

import model.Memories;
import java.sql.*;
import java.util.*;
import dal.DBConnect;

public class MemoriesDAO extends DBConnect {

    public List<Memories> getMemoriesByGroup(int groupId) throws SQLException {
        List<Memories> list = new ArrayList<>();
        String sql = "SELECT m.*, u.name AS userName FROM Memories m "
                   + "JOIN Users u ON m.user_id = u.user_id "
                   + "WHERE m.group_id = ? ORDER BY m.created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Memories m = new Memories();
                m.setMemoryId(rs.getInt("memory_id"));
                m.setUserId(rs.getInt("user_id"));
                m.setGroupId(rs.getInt("group_id"));
                m.setTitle(rs.getString("title"));
                m.setContent(rs.getString("content"));
                m.setImageUrl(rs.getString("image_url"));
                m.setCreatedAt(rs.getTimestamp("created_at"));
                m.setUserName(rs.getString("userName"));
                list.add(m);
            }
        }
        return list;
    }

    public int addMemory(int userId, int groupId, String title, String content, String imageUrl) throws SQLException {
        String sql = "INSERT INTO Memories(user_id, group_id, title, content, image_url) VALUES(?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setInt(2, groupId);
            ps.setString(3, title);
            ps.setString(4, content);
            ps.setString(5, imageUrl);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    public boolean updateMemory(int memoryId, String title, String content, String imageUrl) throws SQLException {
        String sql = "UPDATE Memories SET title=?, content=?, image_url=? WHERE memory_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setString(3, imageUrl);
            ps.setInt(4, memoryId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteMemory(int memoryId) throws SQLException {
        String sql = "DELETE FROM Memories WHERE memory_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memoryId);
            return ps.executeUpdate() > 0;
        }
    }

    public Memories getMemoryById(int memoryId) throws SQLException {
        String sql = "SELECT m.*, u.name AS userName FROM Memories m JOIN Users u ON m.user_id = u.user_id WHERE m.memory_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memoryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Memories m = new Memories();
                m.setMemoryId(rs.getInt("memory_id"));
                m.setUserId(rs.getInt("user_id"));
                m.setGroupId(rs.getInt("group_id"));
                m.setTitle(rs.getString("title"));
                m.setContent(rs.getString("content"));
                m.setImageUrl(rs.getString("image_url"));
                m.setCreatedAt(rs.getTimestamp("created_at"));
                m.setUserName(rs.getString("userName"));
                return m;
            }
        }
        return null;
    }
    
    public List<Memories> getMemoriesByUser(int userId) {
        List<Memories> list = new ArrayList<>();
        String sql = "SELECT * FROM Memories WHERE user_id=? ORDER BY created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Memories m = new Memories();
                m.setMemoryId(rs.getInt("memory_id"));
                m.setUserId(rs.getInt("user_id"));
                m.setTitle(rs.getString("title"));
                m.setContent(rs.getString("content"));
                m.setImageUrl(rs.getString("image"));
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
