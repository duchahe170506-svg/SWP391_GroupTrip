package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        user.setCreated_at(rs.getTimestamp("created_at"));
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
        String sql = "SELECT user_id, name, email, password, role, status, created_at FROM Users WHERE email = ?";
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
        String sql = "SELECT user_id, name, email, password, role, status, created_at FROM Users WHERE user_id = ?";
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
        String sql = "INSERT INTO Users (name, email, password, role, status, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole() == null ? "User" : user.getRole());
            ps.setString(5, user.getStatus() == null ? "Active" : user.getStatus());
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
        String sql = "SELECT user_id, name, email, password, role, status, created_at "
                + "FROM Users WHERE email = ? AND password = ? AND status = 'Active'";
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
        String sql = "UPDATE Users SET name = ?, email = ?, role = ?, status = ? WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getStatus());
            ps.setInt(5, user.getUser_id());
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

    public boolean requestPasswordReset(String email) {
        lastErrorMessage = null;
        Users user = getUserByEmail(email);
        if (user == null) {
            lastErrorMessage = "Email không tồn tại trong hệ thống";
            return false;
        }
        // Generate 6-digit OTP
        String token = String.format("%06d", (int) (Math.random() * 1_000_000));
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + 10 * 60 * 1000); // 10 minutes
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
                Users u = mapResultSetToUser(rs);
                System.out.println("DEBUG USER: " + u.getUser_id() + " - " + u.getName());
                users.add(u);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }
    

    public List<Users> getUsersByPage(int page, int pageSize) {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT user_id, name, email, password, role, status, created_at "
                + "FROM Users ORDER BY user_id LIMIT ? OFFSET ?";
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
}
