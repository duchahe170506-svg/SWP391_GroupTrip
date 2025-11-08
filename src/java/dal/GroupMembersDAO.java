package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.GroupMembers;
import model.Users;

public class GroupMembersDAO {

    public boolean addMember(int groupId, int userId, String role) {
        String sql = "INSERT INTO GroupMembers (group_id, user_id, role, status, joined_at) VALUES (?, ?, ?, 'Active', NOW())";
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
        String sql = "SELECT gm.group_id, gm.user_id, gm.role, gm.status, gm.joined_at, gm.removed_at, u.name, u.email "
                + "FROM GroupMembers gm "
                + "JOIN Users u ON gm.user_id = u.user_id "
                + "WHERE gm.group_id = ? AND gm.status = 'Active' "
                + "ORDER BY gm.joined_at DESC";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GroupMembers gm = new GroupMembers(
                            rs.getInt("group_id"),
                            rs.getInt("user_id"),
                            rs.getString("role"),
                            rs.getString("status"),
                            rs.getTimestamp("joined_at"),
                            rs.getTimestamp("removed_at"),
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

    public boolean removeMember(int groupId, int userId, int removedBy, String reason) {
        String update = "UPDATE GroupMembers SET status='Removed', removed_at=NOW() WHERE group_id=? AND user_id=?";
        String insert = "INSERT INTO MemberRemovals(group_id, removed_user_id, removed_by, reason) VALUES(?,?,?,?)";
        try (Connection con = DBConnect.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(update); PreparedStatement ps2 = con.prepareStatement(insert)) {

                ps1.setInt(1, groupId);
                ps1.setInt(2, userId);
                int rows = ps1.executeUpdate();

                if (rows > 0) {
                    ps2.setInt(1, groupId);
                    ps2.setInt(2, userId);
                    ps2.setInt(3, removedBy);
                    ps2.setString(4, reason);
                    ps2.executeUpdate();
                    con.commit();
                    return true;
                }
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean logRemoval(int groupId, int removedUserId, int removedBy, String reason) {
        String sql = "INSERT INTO MemberRemovals (group_id, removed_user_id, removed_by, reason) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, removedUserId);
            ps.setInt(3, removedBy);
            ps.setString(4, reason);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAllRoles(int groupId) {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT role FROM GroupMembers WHERE group_id = ? AND status='Active'";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    roles.add(rs.getString("role"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
    }

    public String getRole(int groupId, int userId) {
        String sql = "SELECT role FROM GroupMembers WHERE group_id = ? AND user_id = ? AND status='Active'";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateRole(int groupId, int userId, String newRole) {
        String sql = "UPDATE GroupMembers SET role=? WHERE group_id=? AND user_id=? AND status='Active'";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newRole);
            ps.setInt(2, groupId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countRole(int groupId, String role) {
        String sql = "SELECT COUNT(*) FROM GroupMembers WHERE group_id=? AND role=? AND status='Active'";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setString(2, role);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countMembers(int groupId) {
        String sql = "SELECT COUNT(*) FROM GroupMembers WHERE group_id=? AND status='Active'";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getUserRole(int groupId, int userId) {
        String sql = "SELECT role FROM GroupMembers WHERE group_id=? AND user_id=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public void addMemberIfNotExists(int groupId, int userId, String role) {
        String check = "SELECT * FROM GroupMembers WHERE group_id=? AND user_id=? AND status='Active'";
        String insert = "INSERT INTO GroupMembers (group_id, user_id, role, status, joined_at) VALUES (?, ?, ?, 'Active', NOW())";
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
        String sql = "SELECT u.* FROM GroupMembers gm "
                + "JOIN Users u ON gm.user_id = u.user_id "
                + "WHERE gm.group_id=? AND gm.role='Leader' AND gm.status='Active' LIMIT 1";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public boolean isGroupCreator(int groupId, int userId) {
        String sql = "SELECT * FROM TravelGroups g "
                + "JOIN GroupMembers gm ON g.group_id=gm.group_id "
                + "WHERE g.group_id=? AND gm.user_id=? AND gm.role='Leader' AND gm.status='Active'";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLeader(int groupId, int userId) {
        String sql = "SELECT * FROM GroupMembers WHERE group_id = ? AND user_id = ? AND role = 'Leader'";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // có tồn tại record => là Leader
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countActiveMembers(int groupId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM GroupMembers WHERE group_id = ? AND status = 'Active'";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean isMember(int groupId, int userId) {
        String sql = "SELECT 1 FROM GroupMembers WHERE group_id=? AND user_id=? LIMIT 1";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // nếu có record, user là thành viên
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean leaveGroup(int groupId, int userId, String reason) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            // 0️⃣ Lấy role hiện tại
            String roleSql = "SELECT role FROM GroupMembers WHERE group_id=? AND user_id=? AND status='Active'";
            String role = null;
            try (PreparedStatement ps = conn.prepareStatement(roleSql)) {
                ps.setInt(1, groupId);
                ps.setInt(2, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        role = rs.getString("role");
                    } else {
                        conn.rollback();
                        return false; // user không còn trong nhóm
                    }
                }
            }

            if ("Leader".equals(role)) {
                // Kiểm tra CoLeader còn tồn tại
                String checkCoLeaderSql = "SELECT COUNT(*) FROM GroupMembers WHERE group_id=? AND role='CoLeader' AND status='Active'";
                int coLeaderCount = 0;
                try (PreparedStatement ps = conn.prepareStatement(checkCoLeaderSql)) {
                    ps.setInt(1, groupId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            coLeaderCount = rs.getInt(1);
                        }
                    }
                }

                if (coLeaderCount == 0) {
                    conn.rollback();
                    return false;
                }

                // Kiểm tra đã có Leader mới chưa
                String checkLeaderSql = "SELECT COUNT(*) FROM GroupMembers WHERE group_id=? AND role='Leader' AND status='Active' AND user_id<>?";
                try (PreparedStatement ps = conn.prepareStatement(checkLeaderSql)) {
                    ps.setInt(1, groupId);
                    ps.setInt(2, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            conn.rollback();
                            return false; // Chưa bầu Leader mới → không thể rời
                        }
                    }
                }
            }

            // 2️⃣ Cập nhật GroupMembers
            String updateSql = "UPDATE GroupMembers SET status='Left', removed_at=NOW() WHERE group_id=? AND user_id=? AND status='Active'";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, groupId);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }

            // 3️⃣ Thêm record vào MemberRemovals
            String insertSql = "INSERT INTO MemberRemovals (group_id, removed_user_id, removed_by, reason) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, groupId);
                ps.setInt(2, userId);
                ps.setInt(3, userId); // tự rời
                ps.setString(4, reason);
                ps.executeUpdate();
            }

            // 4️⃣ Tạo notification cho Leader/CoLeader còn lại
            String notifSql = "INSERT INTO Notifications (user_id, type, message) "
                    + "SELECT user_id,'GENERAL', CONCAT('Thành viên ', ?, ' đã rời nhóm') "
                    + "FROM GroupMembers WHERE group_id=? AND role IN ('Leader','CoLeader') AND status='Active'";
            try (PreparedStatement ps = conn.prepareStatement(notifSql)) {
                ps.setString(1, getUserNameById(userId));
                ps.setInt(2, groupId);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public String getUserNameById(int userId) throws SQLException {
        String sql = "SELECT name FROM Users WHERE user_id=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                } else {
                    return "Người dùng không xác định";
                }
            }
        }
    }

    public boolean updateRoleByRole(int groupId, String oldRole, String newRole) {
        String sql = "UPDATE GroupMembers SET role=? WHERE group_id=? AND role=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newRole);
            ps.setInt(2, groupId);
            ps.setString(3, oldRole);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUserRoleInGroup(int groupId, int userId) {
        String role = null;
        String sql = "SELECT role FROM GroupMembers "
                + "WHERE group_id = ? AND user_id = ? AND status = 'Active'";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                role = rs.getString("role");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;

    }

    public boolean isUserInGroup(int groupId, int userId) {
        String sql = "SELECT * FROM GroupMembers WHERE group_id = ? AND user_id = ? AND status = 'Active'";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
