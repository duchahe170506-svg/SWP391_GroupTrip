package dal;

import model.MemberRemovals;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRemovalsDAO {

    public boolean addRemoval(int groupId, int removedUserId, int removedBy, String reason) {
        String sql = "INSERT INTO MemberRemovals (group_id, removed_user_id, removed_by, reason) VALUES (?,?,?,?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, removedUserId);
            ps.setInt(3, removedBy);
            ps.setString(4, reason);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<MemberRemovals> getRemovalsByGroup(int groupId) {
        List<MemberRemovals> list = new ArrayList<>();
        String sql = "SELECT mr.*, u1.name AS removedUserName, u2.name AS removedByName " +
                     "FROM MemberRemovals mr " +
                     "JOIN Users u1 ON mr.removed_user_id = u1.user_id " +
                     "JOIN Users u2 ON mr.removed_by = u2.user_id " +
                     "WHERE mr.group_id = ? ORDER BY mr.removed_at DESC";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MemberRemovals mr = new MemberRemovals(
                        rs.getInt("removal_id"),
                        rs.getInt("group_id"),
                        rs.getInt("removed_user_id"),
                        rs.getInt("removed_by"),
                        rs.getString("reason"),
                        rs.getTimestamp("removed_at")
                    );
                    mr.setRemovedUserName(rs.getString("removedUserName"));
                    mr.setRemovedByName(rs.getString("removedByName"));
                    list.add(mr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

