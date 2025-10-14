package dal;

import java.sql.*;

public class GroupMembersDAO {

    public boolean addMember(int groupId, int userId, String role) {
        String sql = "INSERT INTO GroupMembers (group_id, user_id, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            ps.setString(3, role);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
