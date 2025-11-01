/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.GroupRoleHistory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author quang
 */
public class GroupRoleHistoryDAO extends DBConnect {

    public boolean addHistory(GroupRoleHistory history) {
        String sql = "INSERT INTO GroupRoleHistory (group_id, user_id, old_role, new_role, changed_by, changed_at) "
                + "VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, history.getGroup_id());
            ps.setInt(2, history.getUser_id());
            ps.setString(3, history.getOld_role());
            ps.setString(4, history.getNew_role());
            ps.setInt(5, history.getChanged_by());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2️⃣ Lấy lịch sử đổi role theo group
    public List<GroupRoleHistory> getHistoryByGroup(int groupId) {
        List<GroupRoleHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM GroupRoleHistory WHERE group_id = ? ORDER BY changed_at DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3️⃣ Lấy lịch sử đổi role theo user
    public List<GroupRoleHistory> getHistoryByUser(int userId) {
        List<GroupRoleHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM GroupRoleHistory WHERE user_id = ? ORDER BY changed_at DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Map ResultSet sang model
    private GroupRoleHistory mapResultSet(ResultSet rs) throws SQLException {
        GroupRoleHistory history = new GroupRoleHistory();
        history.setHistory_id(rs.getInt("history_id"));
        history.setGroup_id(rs.getInt("group_id"));
        history.setUser_id(rs.getInt("user_id"));
        history.setOld_role(rs.getString("old_role"));
        history.setNew_role(rs.getString("new_role"));
        history.setChanged_by(rs.getInt("changed_by"));
        history.setChanged_at(rs.getTimestamp("changed_at"));
        return history;
    }

    public boolean changeMemberRole(int groupId, int userId, String oldRole, String newRole, int changedBy) {
        GroupRoleHistoryDAO roleDao = new GroupRoleHistoryDAO();
        NotificationDAO notiDao = new NotificationDAO();
        boolean success = roleDao.addHistory(new GroupRoleHistory(0, groupId, userId, oldRole, newRole, changedBy, null));
        if (success) {
            String message = "Your role in group #" + groupId + " has been changed from " + oldRole + " to " + newRole + ".";
            notiDao.createNotification(changedBy, userId, "ROLE_CHANGE", groupId, message);
        }
        return success;
    }

}
