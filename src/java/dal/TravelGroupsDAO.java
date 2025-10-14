package dal;

import java.sql.*;

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
}
