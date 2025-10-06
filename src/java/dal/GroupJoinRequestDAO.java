package dal;

import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import model.GroupJoinRequests;

public class GroupJoinRequestDAO extends DBConnect {

    public int countRequestByUser(int tripId, int userId) {
        String sql = "SELECT COUNT(*) FROM GroupJoinRequests gjr "
                + "JOIN Trips t ON gjr.group_id = t.group_id "
                + "WHERE t.trip_id=? AND gjr.user_id=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void createRequest(int tripId, int userId) {
        String sql = "INSERT INTO GroupJoinRequests(group_id, user_id) "
                + "SELECT group_id, ? FROM Trips WHERE trip_id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, tripId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRequestStatus(int requestId, String status, int reviewedBy) {
        String sql = "UPDATE GroupJoinRequests SET status=?, reviewed_at=NOW(), reviewed_by=? WHERE request_id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, reviewedBy);
            ps.setInt(3, requestId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<GroupJoinRequests> getRequestsByGroup(int groupId) {
        List<GroupJoinRequests> list = new ArrayList<>();
        String sql = "SELECT * FROM GroupJoinRequests WHERE group_id = ? ORDER BY requested_at DESC";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                GroupJoinRequests r = new GroupJoinRequests();
                r.setRequest_id(rs.getInt("request_id"));
                r.setGroup_id(rs.getInt("group_id"));
                r.setUser_id(rs.getInt("user_id"));
                r.setStatus(rs.getString("status"));
                r.setRequested_at(rs.getTimestamp("requested_at"));
                r.setReviewed_at(rs.getTimestamp("reviewed_at"));
                r.setReviewed_by(rs.getInt("reviewed_by"));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public GroupJoinRequests getRequestById(int requestId) {
        String sql = "SELECT * FROM GroupJoinRequests WHERE request_id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GroupJoinRequests(
                            rs.getInt("request_id"),
                            rs.getInt("group_id"),
                            rs.getInt("user_id"),
                            rs.getString("status"),
                            rs.getTimestamp("requested_at"),
                            rs.getTimestamp("reviewed_at"),
                            rs.getInt("reviewed_by")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật trạng thái cho tất cả request của 1 user trong group
    public void updateStatusByUserAndGroup(int userId, int groupId, String status, int reviewedBy) {
        String sql = "UPDATE GroupJoinRequests SET status=?, reviewed_at=NOW(), reviewed_by=? WHERE user_id=? AND group_id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, reviewedBy);
            ps.setInt(3, userId);
            ps.setInt(4, groupId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Đếm số yêu cầu pending trong group
    public int countPendingRequests(int groupId) {
        String sql = "SELECT COUNT(*) FROM GroupJoinRequests WHERE group_id=? AND status='PENDING'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean approveRequestAndAddMember(int requestId, int groupId, int userId, int leaderId) {
        String updateSql = "UPDATE GroupJoinRequests SET status = 'approved', reviewed_at = NOW(), reviewed_by = ? WHERE request_id = ?";
        String insertSql = "INSERT INTO GroupMembers (group_id, user_id, role, joined_at) VALUES (?, ?, 'Member', NOW())";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(updateSql); PreparedStatement ps2 = conn.prepareStatement(insertSql)) {

                ps1.setInt(1, leaderId);
                ps1.setInt(2, requestId);
                ps1.executeUpdate();

                ps2.setInt(1, groupId);
                ps2.setInt(2, userId);
                ps2.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
