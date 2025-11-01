package dal;

import static dal.DBConnect.getConnection;
import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import model.GroupJoinRequests;
import model.Users;

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
        String sql = """
        SELECT COUNT(*) 
        FROM GroupJoinRequests 
        WHERE group_id = ? 
          AND status = 'PENDING' 
          AND (invited_by = 0 OR invited_by IS NULL)
    """;
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

    public List<GroupJoinRequests> getUserRequests(int groupId) {
        List<GroupJoinRequests> list = new ArrayList<>();
        String sql = "SELECT * FROM GroupJoinRequests WHERE group_id=? AND (invited_by IS NULL OR invited_by=0)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GroupJoinRequests req = new GroupJoinRequests();
                    req.setRequest_id(rs.getInt("request_id"));
                    req.setGroup_id(rs.getInt("group_id"));
                    req.setUser_id(rs.getInt("user_id"));
                    req.setStatus(rs.getString("status"));
                    req.setRequested_at(rs.getTimestamp("requested_at"));
                    req.setReviewed_at(rs.getTimestamp("reviewed_at"));
                    req.setReviewed_by(rs.getInt("reviewed_by"));
                    req.setInvited_by(rs.getInt("invited_by"));
                    list.add(req);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<GroupJoinRequests> getLeaderInvites(int groupId) {
        List<GroupJoinRequests> list = new ArrayList<>();
        String sql = "SELECT * FROM GroupJoinRequests WHERE group_id=? AND invited_by IS NOT NULL AND invited_by <> 0";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GroupJoinRequests req = new GroupJoinRequests();
                    req.setRequest_id(rs.getInt("request_id"));
                    req.setGroup_id(rs.getInt("group_id"));
                    req.setUser_id(rs.getInt("user_id"));
                    req.setStatus(rs.getString("status"));
                    req.setRequested_at(rs.getTimestamp("requested_at"));
                    req.setInvited_by(rs.getInt("invited_by"));
                    list.add(req);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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

    public String sendInvite(int groupId, String email, int invitedBy) {
        UserDAO userDAO = new UserDAO();
        GroupMembersDAO memberDAO = new GroupMembersDAO();

        Users invitedUser = userDAO.getUserByEmail(email);
        if (invitedUser == null) {
            return "❌ Người dùng không tồn tại";
        }

        int userId = invitedUser.getUser_id();

        if (memberDAO.isMember(groupId, userId)) {
            return "❌ Người dùng này đã là thành viên nhóm";
        }

        GroupJoinRequests existing = getRequestByUserAndGroup(userId, groupId);
        if (existing != null) {
            String status = existing.getStatus();

            if ("INVITED".equalsIgnoreCase(status) || "PENDING".equalsIgnoreCase(status)) {
                return "❌ Người dùng này đã có lời mời hoặc yêu cầu đang chờ xử lý";
            }

            if ("CANCELLED".equalsIgnoreCase(status)
                    || "REJECTED".equalsIgnoreCase(status)
                    || "EXPIRED".equalsIgnoreCase(status)) {
                deleteRequest(existing.getRequest_id());
            }
        }

        String sql = """
        INSERT INTO GroupJoinRequests(group_id, user_id, status, invited_by)
        VALUES (?, ?, 'INVITED', ?)
    """;

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            ps.setInt(3, invitedBy);
            int inserted = ps.executeUpdate();
            if (inserted > 0) {
                return "✅ Gửi lời mời thành công cho " + invitedUser.getName();
            } else {
                return "❌ Gửi lời mời thất bại";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Lỗi hệ thống, vui lòng thử lại";
        }
    }

    public boolean deleteRequest(int requestId) {
        String sql = "DELETE FROM GroupJoinRequests WHERE request_id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public GroupJoinRequests getRequestByUserAndGroup(int userId, int groupId) {
        String sql = "SELECT * FROM GroupJoinRequests WHERE user_id=? AND group_id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, groupId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                GroupJoinRequests r = new GroupJoinRequests();
                r.setRequest_id(rs.getInt("request_id"));
                r.setGroup_id(rs.getInt("group_id"));
                r.setUser_id(rs.getInt("user_id"));
                r.setStatus(rs.getString("status"));
                r.setRequested_at(rs.getTimestamp("requested_at"));
                r.setReviewed_at(rs.getTimestamp("reviewed_at"));
                r.setReviewed_by(rs.getInt("reviewed_by"));
                r.setInvited_by(rs.getInt("invited_by"));
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GroupJoinRequests getRequestById(int requestId) {
        String sql = "SELECT * FROM GroupJoinRequests WHERE request_id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                GroupJoinRequests r = new GroupJoinRequests();
                r.setRequest_id(rs.getInt("request_id"));
                r.setGroup_id(rs.getInt("group_id"));
                r.setUser_id(rs.getInt("user_id"));
                r.setStatus(rs.getString("status"));
                r.setRequested_at(rs.getTimestamp("requested_at"));
                r.setReviewed_at(rs.getTimestamp("reviewed_at"));
                r.setReviewed_by((Integer) rs.getObject("reviewed_by"));
                r.setInvited_by((Integer) rs.getObject("invited_by"));
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean cancelInvite(int requestId, int actorId) {
        String sql = """
            UPDATE GroupJoinRequests
            SET status='CANCELLED', reviewed_at=NOW(), reviewed_by=?
            WHERE request_id=? AND status='INVITED' AND invited_by=? AND reviewed_by IS NULL
        """;
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, actorId);
            ps.setInt(2, requestId);
            ps.setInt(3, actorId);
            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String respondToInvite(int requestId, int userId, String action) {
        String sqlGet = """
        SELECT gjr.group_id, gjr.invited_by, t.name AS trip_name, t.start_date, t.end_date, t.max_participants
        FROM GroupJoinRequests gjr
        JOIN Trips t ON gjr.group_id = t.group_id
        WHERE gjr.request_id = ? AND gjr.user_id = ?
    """;

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sqlGet)) {

            ps.setInt(1, requestId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return "❌ Lời mời không tồn tại hoặc không thuộc về bạn";
            }

            int groupId = rs.getInt("group_id");
            int inviterId = rs.getInt("invited_by");
            String tripName = rs.getString("trip_name");
            Date start = rs.getDate("start_date");
            Date end = rs.getDate("end_date");
            int maxParticipants = rs.getInt("max_participants");

            // Lấy tên người dùng hiện tại
            String userName = null;
            try (PreparedStatement psUser = conn.prepareStatement("SELECT name FROM Users WHERE user_id=?")) {
                psUser.setInt(1, userId);
                ResultSet rsUser = psUser.executeQuery();
                if (rsUser.next()) {
                    userName = rsUser.getString("name");
                } else {
                    userName = "Người dùng";
                }
            }

            if (action.equalsIgnoreCase("REJECT")) {
                try (PreparedStatement psReject = conn.prepareStatement(
                        "UPDATE GroupJoinRequests SET status='REJECTED', reviewed_at=NOW(), reviewed_by=? WHERE request_id=?")) {
                    psReject.setInt(1, userId);
                    psReject.setInt(2, requestId);
                    psReject.executeUpdate();
                }

                try (PreparedStatement psNoti = conn.prepareStatement(
                        "INSERT INTO Notifications (sender_id, user_id, type, related_id, message) VALUES (?, ?, 'JOIN_RESPONSE', ?, ?)")) {
                    psNoti.setInt(1, userId);
                    psNoti.setInt(2, inviterId);
                    psNoti.setInt(3, groupId);
                    psNoti.setString(4, "❌ " + userName + " đã từ chối lời mời tham gia chuyến đi '" + tripName + "'.");
                    psNoti.executeUpdate();
                }

                return "❌ Bạn đã từ chối lời mời.";
            }

            // Kiểm tra số lượng thành viên
            try (PreparedStatement psCount = conn.prepareStatement(
                    "SELECT COUNT(*) FROM GroupMembers WHERE group_id=? AND status='Active'")) {
                psCount.setInt(1, groupId);
                ResultSet rsc = psCount.executeQuery();
                if (rsc.next() && rsc.getInt(1) >= maxParticipants) {
                    return "⚠️ Nhóm đã đủ số lượng thành viên, không thể tham gia.";
                }
            }

            // Kiểm tra trùng thời gian
            try (PreparedStatement psOverlap = conn.prepareStatement(
                    "SELECT COUNT(*) FROM Trips t "
                    + "JOIN GroupMembers gm ON t.group_id = gm.group_id "
                    + "WHERE gm.user_id=? AND gm.status='Active' AND NOT (t.end_date < ? OR t.start_date > ?)")) {
                psOverlap.setInt(1, userId);
                psOverlap.setDate(2, start);
                psOverlap.setDate(3, end);
                ResultSet rso = psOverlap.executeQuery();
                if (rso.next() && rso.getInt(1) > 0) {
                    return "⚠️ Thời gian chuyến đi này trùng với một chuyến khác bạn đã tham gia.";
                }
            }

            conn.setAutoCommit(false);
            try (PreparedStatement psAccept = conn.prepareStatement(
                    "UPDATE GroupJoinRequests SET status='ACCEPTED', reviewed_at=NOW(), reviewed_by=? WHERE request_id=?"); PreparedStatement psMember = conn.prepareStatement(
                            "INSERT INTO GroupMembers (group_id, user_id, role, joined_at) VALUES (?, ?, 'Member', NOW())"); PreparedStatement psNoti = conn.prepareStatement(
                            "INSERT INTO Notifications (sender_id, user_id, type, related_id, message) VALUES (?, ?, 'JOIN_RESPONSE', ?, ?)")) {

                psAccept.setInt(1, userId);
                psAccept.setInt(2, requestId);
                psAccept.executeUpdate();

                psMember.setInt(1, groupId);
                psMember.setInt(2, userId);
                psMember.executeUpdate();

                psNoti.setInt(1, userId);
                psNoti.setInt(2, inviterId);
                psNoti.setInt(3, groupId);
                psNoti.setString(4, "✅ " + userName + " đã chấp nhận tham gia chuyến đi '" + tripName + "'.");
                psNoti.executeUpdate();

                conn.commit();
                return "✅ Bạn đã chấp nhận và tham gia nhóm thành công!";
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Lỗi hệ thống, vui lòng thử lại.";
        }
    }
}
