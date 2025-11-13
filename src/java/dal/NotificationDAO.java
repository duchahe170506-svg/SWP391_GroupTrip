package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Notifications;

public class NotificationDAO extends DBConnect {

    private Notifications mapResultSet(ResultSet rs) throws SQLException {
        Notifications n = new Notifications();
        n.setNotificationId(rs.getInt("notification_id"));
        int sId = rs.getInt("sender_id");
        n.setSenderId(rs.wasNull() ? null : sId);
        n.setUserId(rs.getInt("user_id"));
        n.setType(rs.getString("type"));
        int rId = rs.getInt("related_id");
        n.setRelatedId(rs.wasNull() ? null : rId);
        n.setMessage(rs.getString("message"));
        n.setStatus(rs.getString("status"));
        n.setCreatedAt(rs.getTimestamp("created_at"));
        return n;
    }

    public boolean createNotification(Integer senderId, int userId, String type, Integer relatedId, String message) {
        String sql = "INSERT INTO Notifications (sender_id, user_id, type, related_id, message) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (senderId != null) {
                ps.setInt(1, senderId);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setInt(2, userId);
            ps.setString(3, type);
            if (relatedId != null) {
                ps.setInt(4, relatedId);
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setString(5, message);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error creating notification: " + e.getMessage());
        }
        return false;
    }

    public void createNotification(Notifications n) {
        String sqlCheck = "SELECT COUNT(*) FROM Notifications WHERE sender_id = ? AND user_id = ? AND type = ? AND related_id = ? AND message = ?";
        String sqlInsert = "INSERT INTO Notifications(sender_id, user_id, type, related_id, message, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
          
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setObject(1, n.getSenderId());
                psCheck.setInt(2, n.getUserId());
                psCheck.setString(3, n.getType());
                psCheck.setObject(4, n.getRelatedId());
                psCheck.setString(5, n.getMessage());
                ResultSet rs = psCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return;
                }
            }

            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setObject(1, n.getSenderId());
                psInsert.setInt(2, n.getUserId());
                psInsert.setString(3, n.getType());
                psInsert.setObject(4, n.getRelatedId());
                psInsert.setString(5, n.getMessage());
                psInsert.setString(6, n.getStatus() != null ? n.getStatus() : "UNREAD");
                psInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteNotification(int notificationId) {
        String sql = "DELETE FROM Notifications WHERE notification_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notificationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting notification: " + e.getMessage());
        }
        return false;
    }

    public boolean createBulkNotifications(Integer senderId, List<Integer> userIds, String type, Integer relatedId, String message) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        String sql = "INSERT INTO Notifications (sender_id, user_id, type, related_id, message) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int userId : userIds) {
                if (senderId != null) {
                    ps.setInt(1, senderId);
                } else {
                    ps.setNull(1, Types.INTEGER);
                }
                ps.setInt(2, userId);
                ps.setString(3, type);
                if (relatedId != null) {
                    ps.setInt(4, relatedId);
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                ps.setString(5, message);
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            System.out.println("Error creating bulk notifications: " + e.getMessage());
        }
        return false;
    }

    public List<Notifications> getNotificationsByUser(int userId) {
        List<Notifications> list = new ArrayList<>();
        String sql = """
        SELECT n.*, 
               u.name AS sender_name,
               t.name AS trip_name,
               r.request_id AS related_request_id,
               r.status AS request_status 
        FROM Notifications n
        LEFT JOIN Users u ON n.sender_id = u.user_id
        LEFT JOIN Trips t ON n.related_id = t.trip_id
        LEFT JOIN GroupJoinRequests r ON r.group_id = n.related_id AND r.user_id = n.user_id
        WHERE n.user_id = ?
        ORDER BY n.created_at DESC
    """;

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notifications noti = new Notifications();
                noti.setNotificationId(rs.getInt("notification_id"));
                noti.setSenderId((Integer) rs.getObject("sender_id"));
                noti.setUserId(rs.getInt("user_id"));
                noti.setType(rs.getString("type"));
                noti.setRelatedId((Integer) rs.getObject("related_id"));
                noti.setRelatedRequestId((Integer) rs.getObject("related_request_id"));
                noti.setMessage(rs.getString("message"));
                noti.setStatus(rs.getString("status"));
                noti.setCreatedAt(rs.getTimestamp("created_at"));
                noti.setSenderName(rs.getString("sender_name"));
                noti.setTripName(rs.getString("trip_name"));
                noti.setRequestStatus(rs.getString("request_status"));

                String label;
                switch (noti.getType()) {
                    case "INVITE" ->
                        label = "Lời mời tham gia nhóm";
                    case "JOIN_RESPONSE" ->
                        label = "Phản hồi yêu cầu tham gia";
                    case "KICKED" ->
                        label = "Thông báo bị xóa khỏi nhóm";
                    case "REPORT_RESPONSE" ->
                        label = "Kết quả báo cáo";
                    case "GENERAL" ->
                        label = "Thông báo chung";
                    case "ADMIN_ANNOUNCEMENT" ->
                        label = "Thông báo từ quản trị viên";
                    case "USER_REPORT" ->
                        label = "Báo cáo người dùng";
                    case "SYSTEM_ALERT" ->
                        label = "Cảnh báo hệ thống";
                    case "GROUP_ANNOUNCEMENT" ->
                        label = "Thông báo nhóm";
                    default ->
                        label = "Khác";
                }
                noti.setTypeLabel(label);

                list.add(noti);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching notifications: " + e.getMessage());
        }

        return list;
    }

    public List<Notifications> getNotificationsByUserAndType(int userId, String type) {
        List<Notifications> list = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE user_id = ? AND type = ? ORDER BY created_at DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching notifications by type: " + e.getMessage());
        }
        return list;
    }

    public List<Notifications> getRecentNotifications(int userId, int limit) {
        List<Notifications> list = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE user_id = ? ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching recent notifications: " + e.getMessage());
        }
        return list;
    }

    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE Notifications SET status = 'READ' WHERE notification_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notificationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error marking notification as read: " + e.getMessage());
        }
        return false;
    }

    public boolean markAllAsRead(int userId) {
        String sql = "UPDATE Notifications SET status = 'READ' WHERE user_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error marking all notifications as read: " + e.getMessage());
        }
        return false;
    }

    public int countUnread(int userId) {
        String sql = "SELECT COUNT(*) FROM Notifications WHERE user_id = ? AND status = 'UNREAD'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error counting unread notifications: " + e.getMessage());
        }
        return 0;
    }

    public boolean updateStatus(int notificationId, String newStatus) {
        String sql = "UPDATE Notifications SET status = ? WHERE notification_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, notificationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating notification status: " + e.getMessage());
        }
        return false;
    }
}
