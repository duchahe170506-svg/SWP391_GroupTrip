package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.GroupMembers;
import model.Users;

public class GroupMembersDAO {

    public boolean addMember(int groupId, int userId, String role) {
        String sql = "INSERT INTO GroupMembers (group_id, user_id, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            ps.setString(3, role);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<GroupMembers> getMembersByGroup(int groupId) {
        List<GroupMembers> list = new ArrayList<>();
        String sql = "SELECT gm.group_id, gm.user_id, gm.role, gm.joined_at, u.name, u.email "
                + "FROM GroupMembers gm "
                + "JOIN Users u ON gm.user_id = u.user_id "
                + "WHERE gm.group_id = ? "
                + "ORDER BY gm.joined_at DESC";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GroupMembers gm = new GroupMembers(
                            rs.getInt("group_id"),
                            rs.getInt("user_id"),
                            rs.getString("role"),
                            rs.getTimestamp("joined_at"),
                            rs.getString("name"),
                            rs.getString("email")
                    );
                    list.add(gm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean removeMember(int groupId, int userId) {
        String sql = "DELETE FROM GroupMembers WHERE group_id = ? AND user_id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật vai trò (Leader/Member)
    public boolean updateRole(int groupId, int userId, String newRole) {
        String sql = "UPDATE GroupMembers SET role = ? WHERE group_id = ? AND user_id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newRole);
            ps.setInt(2, groupId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addMemberIfNotExists(int groupId, int userId, String role) {
        String check = "SELECT * FROM GroupMembers WHERE group_id=? AND user_id=?";
        String insert = "INSERT INTO GroupMembers (group_id, user_id, role, joined_at) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement psCheck = conn.prepareStatement(check)) {
            psCheck.setInt(1, groupId);
            psCheck.setInt(2, userId);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (!rs.next()) {
                    try (PreparedStatement psInsert = conn.prepareStatement(insert)) {
                        psInsert.setInt(1, groupId);
                        psInsert.setInt(2, userId);
                        psInsert.setString(3, role);
                        psInsert.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Users getGroupLeader(int groupId) {
        String sql = "SELECT u.* FROM GroupMembers gm " +
                     "JOIN Users u ON gm.user_id = u.user_id " +
                     "WHERE gm.group_id = ? AND gm.role = 'Leader' LIMIT 1";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Users leader = new Users();
                    leader.setUser_id(rs.getInt("user_id"));
                    leader.setName(rs.getString("name"));
                    leader.setEmail(rs.getString("email"));
                    leader.setPassword(rs.getString("password"));
                    return leader;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRole(int groupId, int userId) {
    String sql = "SELECT role FROM GroupMembers WHERE group_id=? AND user_id=?";
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, groupId);
        ps.setInt(2, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("role");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

public boolean isGroupCreator(int groupId, int userId) {
    String sql = "SELECT * FROM TravelGroups g "
               + "JOIN GroupMembers gm ON g.group_id=gm.group_id "
               + "WHERE g.group_id=? AND gm.user_id=? AND gm.role='Leader'";
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, groupId);
        ps.setInt(2, userId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    public String getGroupName(int groupId) {
        String sql = "SELECT name FROM TravelGroups WHERE group_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

}
