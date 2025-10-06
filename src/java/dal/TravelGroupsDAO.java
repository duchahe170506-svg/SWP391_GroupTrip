package dal;

import java.sql.*;
import model.Groups;

public class TravelGroupsDAO {

    /** Tạo group mới và trả về group_id */
    public int insertGroup(String name, String description, int leaderId) {
        String sql = "INSERT INTO TravelGroups (name, description, leader_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, leaderId);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Groups getGroupById(int groupId) {
        String sql = "SELECT * FROM TravelGroups WHERE group_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Groups group = new Groups();
                    group.setGroup_id(rs.getInt("group_id"));
                    group.setName(rs.getString("name"));
                    group.setDescription(rs.getString("description"));
                    group.setCreated_at(rs.getTimestamp("created_at"));
                    return group;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
