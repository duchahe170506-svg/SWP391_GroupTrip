package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Users;
import util.MailUtil;

public class UserDAO {

    private String lastErrorMessage;

    public String getLastError() {
        return lastErrorMessage;
    }

    private Users mapResultSetToUser(ResultSet rs) throws SQLException {
        Users user = new Users();
        user.setUser_id(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setPassword(rs.getString("password"));
        user.setDate_of_birth(rs.getDate("date_of_birth"));
        user.setGender(rs.getString("gender"));
        user.setAvatar(rs.getString("avatar"));
        user.setAddress(rs.getString("address"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        return user;
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Users getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Users getUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int signup(Users user) {
        String sql = "INSERT INTO Users (name, email, phone, password, date_of_birth, gender, avatar, address, role, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setDate(5, user.getDate_of_birth());
            ps.setString(6, user.getGender());
            ps.setString(7, user.getAvatar());
            ps.setString(8, user.getAddress());
            ps.setString(9, user.getRole() == null ? "User" : user.getRole());
            ps.setString(10, user.getStatus() == null ? "Active" : user.getStatus());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public Users signin(String email, String password) {
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ? AND status = 'Active'";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean updateProfile(Users user) {
        String sql = "UPDATE Users SET name = ?, email = ?, phone = ?, date_of_birth = ?, gender = ?, avatar = ?, address = ?, role = ?, status = ? WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setDate(4, user.getDate_of_birth());
            ps.setString(5, user.getGender());
            ps.setString(6, user.getAvatar());
            ps.setString(7, user.getAddress());
            ps.setString(8, user.getRole());
            ps.setString(9, user.getStatus());
            ps.setInt(10, user.getUser_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updatePasswordByUserId(int userId, String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updatePasswordByEmail(String email, String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE email = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // --- phần reset password giữ nguyên vì không phụ thuộc schema Users ---
    public boolean requestPasswordReset(String email) {
        lastErrorMessage = null;
        Users user = getUserByEmail(email);
        if (user == null) {
            lastErrorMessage = "Email không tồn tại trong hệ thống";
            return false;
        }
        String token = String.format("%06d", (int) (Math.random() * 1_000_000));
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + 10 * 60 * 1000);
        boolean stored = storePasswordResetToken(user.getUser_id(), token, expiresAt);
        if (!stored) {
            lastErrorMessage = "Không thể lưu mã đặt lại mật khẩu (token)";
            return false;
        }
        boolean sent = MailUtil.sendVerificationEmail(email, token);
        if (!sent) {
            lastErrorMessage = "Gửi email thất bại. Vui lòng kiểm tra cấu hình SMTP/App Password";
            return false;
        }
        return true;
    }

    public boolean storePasswordResetToken(int userId, String token, Timestamp expiresAt) {
        String sql = "INSERT INTO PasswordResets (user_id, token, expires_at, created_at) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.setTimestamp(3, expiresAt);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Integer getUserIdByValidToken(String token) {
        String sql = "SELECT user_id FROM PasswordResets WHERE token = ? AND expires_at > NOW() ORDER BY reset_id DESC LIMIT 1";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Integer getUserIdByValidOtp(String email, String otp) {
        String sql = "SELECT pr.user_id FROM PasswordResets pr "
                + "JOIN Users u ON pr.user_id = u.user_id "
                + "WHERE u.email = ? AND pr.token = ? AND pr.expires_at > NOW() "
                + "ORDER BY pr.reset_id DESC LIMIT 1";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, otp);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean resetPassword(String token, String newPassword) {
        Integer userId = getUserIdByValidToken(token);
        if (userId == null) {
            return false;
        }
        boolean updated = updatePasswordByUserId(userId, newPassword);
        if (updated) {
            deleteUsedResetTokens(userId);
        }
        return updated;
    }

    public boolean resetPasswordByOtp(String email, String otp, String newPassword) {
        Integer userId = getUserIdByValidOtp(email, otp);
        if (userId == null) {
            lastErrorMessage = "Mã OTP không hợp lệ hoặc đã hết hạn";
            return false;
        }
        boolean updated = updatePasswordByUserId(userId, newPassword);
        if (updated) {
            deleteUsedResetTokens(userId);
        }
        return updated;
    }

    public void deleteUsedResetTokens(int userId) {
        String sql = "DELETE FROM PasswordResets WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Users> getAllUsers() {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    public List<Users> getUsersByPage(int page, int pageSize) {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY user_id LIMIT ? OFFSET ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int offset = (page - 1) * pageSize;
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    public Users getGroupLeader(int groupId) {
        String sql = "SELECT u.* FROM GroupMembers gm "
                + "JOIN Users u ON gm.user_id = u.user_id "
                + "WHERE gm.group_id = ? AND gm.role = 'Leader' LIMIT 1";
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

    public Map<Integer, String> getUserNamesByIds(Set<Integer> ids) throws SQLException {
        Map<Integer, String> userMap = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            return userMap;
        }

        String placeholders = String.join(",", java.util.Collections.nCopies(ids.size(), "?"));
        String sql = "SELECT user_id, name FROM Users WHERE user_id IN (" + placeholders + ")";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int i = 1;
            for (int id : ids) {
                ps.setInt(i++, id);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userMap.put(rs.getInt("user_id"), rs.getString("name"));
            }
        }
        return userMap;
    }

    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM Users";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Users> getUsersWithStatsAndSearch(String keyword) {
        List<Users> list = new ArrayList<>();
        String sql = """
            SELECT 
                u.*,
                COALESCE(r.report_count,0) AS report_count,
                COALESCE(t.created_trip_count,0) AS created_trip_count,
                COALESCE(j.joined_trip_count,0) AS joined_trip_count
            FROM Users u
            LEFT JOIN (
                SELECT reported_user_id, COUNT(*) AS report_count
                FROM Reports
                WHERE reported_user_id IS NOT NULL
                GROUP BY reported_user_id
            ) r ON u.user_id = r.reported_user_id
            LEFT JOIN (
                SELECT u.user_id, COUNT(t.trip_id) AS created_trip_count
                FROM Users u
                JOIN TravelGroups g ON u.user_id = g.leader_id
                JOIN Trips t ON g.group_id = t.group_id
                GROUP BY u.user_id
            ) t ON u.user_id = t.user_id
            LEFT JOIN (
                SELECT gm.user_id, COUNT(DISTINCT tr.trip_id) AS joined_trip_count
                FROM GroupMembers gm
                JOIN Trips tr ON gm.group_id = tr.group_id
                WHERE gm.status = 'Active'
                GROUP BY gm.user_id
            ) j ON u.user_id = j.user_id
            WHERE u.name LIKE ? OR u.email LIKE ? OR u.phone LIKE ?
            ORDER BY u.user_id ASC
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Users u = new Users();
                    u.setUser_id(rs.getInt("user_id"));
                    u.setName(rs.getString("name"));
                    u.setEmail(rs.getString("email"));
                    u.setPhone(rs.getString("phone"));
                    u.setDate_of_birth(rs.getDate("date_of_birth"));
                    u.setAddress(rs.getString("address"));
                    u.setStatus(rs.getString("status"));
                    u.setReportCount(rs.getInt("report_count"));
                    u.setCreatedTripCount(rs.getInt("created_trip_count"));
                    u.setJoinedTripCount(rs.getInt("joined_trip_count"));
                    list.add(u);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public boolean updateStatus(int userId, String newStatus) {
        String sql = "UPDATE Users SET status=? WHERE user_id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, userId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Users> getAllLeaders() {
        List<Users> leaders = new ArrayList<>();
        String sql = "SELECT user_id, name, email, phone, role, status FROM Users WHERE role = 'Leader'";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Users u = new Users();
                u.setUser_id(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPhone(rs.getString("phone"));
                u.setRole(rs.getString("role"));
                u.setStatus(rs.getString("status"));
                leaders.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaders;
    }
    
    public List<Users> getUsersByTrip(int tripId) throws SQLException {
        List<Users> list = new ArrayList<>();
        String sql = "SELECT u.user_id, u.name, u.email, u.phone, u.avatar, u.role " +
                     "FROM Users u " +
                     "INNER JOIN GroupMembers gm ON u.user_id = gm.user_id " +
                     "INNER JOIN Trips t ON gm.group_id = t.group_id " +
                     "WHERE t.trip_id = ? AND gm.status = 'Active'";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tripId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Users u = new Users();
                    u.setUser_id(rs.getInt("user_id"));
                    u.setName(rs.getString("name"));
                    u.setEmail(rs.getString("email"));
                    u.setPhone(rs.getString("phone"));
                    u.setAvatar(rs.getString("avatar"));
                    u.setRole(rs.getString("role"));
                    list.add(u);
                }
            }
        }
        return list;
    }
}
