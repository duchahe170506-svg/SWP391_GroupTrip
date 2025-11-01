// Package: dal
// File: ProfileSharedMemoriesDAO.java
package dal;

import model.ProfileSharedMemories;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileSharedMemoriesDAO {

    public List<ProfileSharedMemories> getSharedMemoriesByUser(int userId) {
    List<ProfileSharedMemories> list = new ArrayList<>();
    String sql = """
        SELECT 
            psm.share_id,
            psm.memory_id,
            psm.shared_by,
            psm.title,
            psm.content AS shared_content,
            psm.image_url AS shared_image,
            psm.privacy,
            psm.shared_at,
            m.content AS memory_content,
            m.image_url AS memory_image,
            t.name AS trip_name
        FROM ProfileSharedMemories psm
        LEFT JOIN Memories m ON psm.memory_id = m.memory_id
        LEFT JOIN TravelGroups g ON m.group_id = g.group_id
        LEFT JOIN Trips t ON g.group_id = t.group_id
        WHERE psm.shared_by = ?
        ORDER BY psm.shared_at DESC
    """;

    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ProfileSharedMemories share = new ProfileSharedMemories();
            share.setShareId(rs.getInt("share_id"));
            share.setMemoryId(rs.getInt("memory_id"));
            share.setSharedBy(rs.getInt("shared_by"));
            share.setTitle(rs.getString("title"));
            share.setPrivacy(rs.getString("privacy"));
            share.setSharedAt(rs.getTimestamp("shared_at"));

            
            String content = rs.getString("memory_content");
            if (content == null || content.isEmpty()) {
                content = rs.getString("shared_content");
            }
            share.setContent(content);

           
            String image = rs.getString("memory_image");
            if (image == null || image.isEmpty()) {
                image = rs.getString("shared_image");
            }
            share.setImageUrl(image);

            share.setTripName(rs.getString("trip_name"));
            list.add(share);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}



    public boolean addPersonalPost(int userId, String content, String imageUrl, String privacy) {
        String sql = "INSERT INTO ProfileSharedMemories (memory_id, shared_by, content, image_url, privacy) "
                + "VALUES (NULL, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, content);
            ps.setString(3, imageUrl);
            ps.setString(4, privacy);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean shareMemoryFromGroup(int memoryId, int userId, String privacy) {
        String sql = "INSERT INTO ProfileSharedMemories (memory_id, shared_by, privacy) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memoryId);
            ps.setInt(2, userId);
            ps.setString(3, privacy);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addShare(int memoryId, int userId, String title, String content, String imageUrl, String privacy) {
        String sql = "INSERT INTO ProfileSharedMemories (memory_id, shared_by, title, content, image_url, privacy) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memoryId);
            ps.setInt(2, userId);
            ps.setString(3, title);
            ps.setString(4, content);
            ps.setString(5, imageUrl);
            ps.setString(6, privacy);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createShare(int userId, String title, String content, String imageUrl, String privacy) {
        String sql = "INSERT INTO ProfileSharedMemories (shared_by, title, content, image_url, type, privacy) "
                + "VALUES (?, ?, ?, ?, 'PersonalPost', ?)";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, content);
            ps.setString(4, imageUrl);
            ps.setString(5, privacy);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateShare(String title, String content, String imageUrl, String privacy, int shareId, int userId) {
        String sql = "UPDATE ProfileSharedMemories "
                + "SET title = ?, content = ?, image_url = ?, privacy = ? "
                + "WHERE share_id = ? AND shared_by = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, content);
            ps.setString(3, imageUrl);
            ps.setString(4, privacy);
            ps.setInt(5, shareId);
            ps.setInt(6, userId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteShare(int shareId, int userId) {
        String sql = "DELETE FROM ProfileSharedMemories WHERE share_id = ? AND shared_by = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shareId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
