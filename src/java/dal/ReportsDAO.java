package dal;

import java.sql.*;
import java.util.*;
import model.ReportAttachments;
import model.Reports;

public class ReportsDAO extends DBConnect {

    public int createReport(Reports report) {
        String sql = """
            INSERT INTO Reports (reporter_id, group_id, reported_user_id, removal_id, title, type, description)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, report.getReporter_id());
            if (report.getGroup_id() > 0) {
                ps.setInt(2, report.getGroup_id());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            if (report.getReported_user_id() > 0) {
                ps.setInt(3, report.getReported_user_id());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (report.getRemoval_id() > 0) {
                ps.setInt(4, report.getRemoval_id());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.setString(5, report.getTitle());
            ps.setString(6, report.getType());
            ps.setString(7, report.getDescription());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Integer getGroupIdByRemovalId(int removalId) {
        String sql = "SELECT group_id FROM MemberRemovals WHERE removal_id = ?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, removalId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("group_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🧷 Thêm file đính kèm cho report
    public void addAttachment(int reportId, String fileName, String filePath) {
        String sql = "INSERT INTO ReportAttachments (report_id, file_name, file_path) VALUES (?, ?, ?)";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reportId);
            ps.setString(2, fileName);
            ps.setString(3, filePath);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 📜 Lấy danh sách report của user hiện tại
    public List<Reports> getReportsByReporterId(int reporterId) {
        List<Reports> list = new ArrayList<>();
        String sql = """
            SELECT r.*, 
                   g.name AS group_name,
                   u1.name AS reporter_name,
                   u2.name AS reported_user_name
            FROM Reports r
            LEFT JOIN TravelGroups g ON r.group_id = g.group_id
            LEFT JOIN Users u1 ON r.reporter_id = u1.user_id
            LEFT JOIN Users u2 ON r.reported_user_id = u2.user_id
            WHERE r.reporter_id = ?
            ORDER BY r.created_at DESC
        """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reporterId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reports r = new Reports();
                r.setReport_id(rs.getInt("report_id"));
                r.setReporter_id(rs.getInt("reporter_id"));
                r.setGroup_id(rs.getInt("group_id"));
                r.setReported_user_id(rs.getInt("reported_user_id"));
                r.setRemoval_id(rs.getInt("removal_id"));
                r.setTitle(rs.getString("title"));
                r.setType(rs.getString("type"));
                r.setDescription(rs.getString("description"));
                r.setStatus(rs.getString("status"));
                r.setAdmin_response(rs.getString("admin_response"));
                r.setReviewed_by(rs.getInt("reviewed_by"));
                r.setCreated_at(rs.getTimestamp("created_at"));
                r.setUpdated_at(rs.getTimestamp("updated_at"));
                r.setGroup_name(rs.getString("group_name"));
                r.setReporter_name(rs.getString("reporter_name"));
                r.setReported_user_name(rs.getString("reported_user_name"));
                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Reports getReportById(int id) {
        String sql = """
            SELECT r.*, 
                   g.name AS group_name,
                   u1.name AS reporter_name,
                   u2.name AS reported_user_name
            FROM Reports r
            LEFT JOIN TravelGroups g ON r.group_id = g.group_id
            LEFT JOIN Users u1 ON r.reporter_id = u1.user_id
            LEFT JOIN Users u2 ON r.reported_user_id = u2.user_id
            WHERE r.report_id = ?
        """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Reports r = new Reports();
                r.setReport_id(rs.getInt("report_id"));
                r.setReporter_id(rs.getInt("reporter_id"));
                r.setGroup_id(rs.getInt("group_id"));
                r.setReported_user_id(rs.getInt("reported_user_id"));
                r.setRemoval_id(rs.getInt("removal_id"));
                r.setTitle(rs.getString("title"));
                r.setType(rs.getString("type"));
                r.setDescription(rs.getString("description"));
                r.setStatus(rs.getString("status"));
                r.setAdmin_response(rs.getString("admin_response"));
                r.setReviewed_by(rs.getInt("reviewed_by"));
                r.setCreated_at(rs.getTimestamp("created_at"));
                r.setUpdated_at(rs.getTimestamp("updated_at"));
                r.setGroup_name(rs.getString("group_name"));
                r.setReporter_name(rs.getString("reporter_name"));
                r.setReported_user_name(rs.getString("reported_user_name"));
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createKickReport(int reporterId, int removalId, String title, String description) {
        String getRemovalInfo = "SELECT group_id, removed_by FROM MemberRemovals WHERE removal_id = ?";
        String insertReport = """
            INSERT INTO Reports (reporter_id, group_id, reported_user_id, removal_id, title, type, description)
            VALUES (?, ?, ?, ?, ?, 'KICK_DISPUTE', ?)
        """;

        try (Connection con = DBConnect.getConnection()) {

            // Lấy thông tin từ MemberRemovals
            int groupId = 0;
            int removedBy = 0;
            try (PreparedStatement ps = con.prepareStatement(getRemovalInfo)) {
                ps.setInt(1, removalId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    groupId = rs.getInt("group_id");
                    removedBy = rs.getInt("removed_by");
                } else {
                    System.out.println("⚠️ Không tìm thấy removal_id: " + removalId);
                    return false;
                }
            }

            // Thêm report
            try (PreparedStatement ps2 = con.prepareStatement(insertReport)) {
                ps2.setInt(1, reporterId);
                ps2.setInt(2, groupId);
                ps2.setInt(3, removedBy);
                ps2.setInt(4, removalId);
                ps2.setString(5, title);
                ps2.setString(6, description);

                return ps2.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Reports> getAllReports() {
        List<Reports> list = new ArrayList<>();
        String sql = """
        SELECT r.*, 
               u1.name AS reporter_name,
               u2.name AS reported_user_name,
               g.name AS group_name
        FROM Reports r
        LEFT JOIN Users u1 ON r.reporter_id = u1.user_id
        LEFT JOIN Users u2 ON r.reported_user_id = u2.user_id
        LEFT JOIN TravelGroups g ON r.group_id = g.group_id
        ORDER BY r.created_at DESC
    """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reports r = new Reports();
                r.setReport_id(rs.getInt("report_id"));
                r.setReporter_id(rs.getInt("reporter_id"));
                r.setGroup_id((Integer) rs.getObject("group_id"));
                r.setReported_user_id((Integer) rs.getObject("reported_user_id"));
                r.setRemoval_id((Integer) rs.getObject("removal_id"));
                r.setTitle(rs.getString("title"));
                r.setType(rs.getString("type"));
                r.setDescription(rs.getString("description"));
                r.setStatus(rs.getString("status"));
                r.setAdmin_response(rs.getString("admin_response"));
                r.setReviewed_by((Integer) rs.getObject("reviewed_by"));
                r.setCreated_at(rs.getTimestamp("created_at"));
                r.setUpdated_at(rs.getTimestamp("updated_at"));
                r.setReporter_name(rs.getString("reporter_name"));
                r.setReported_user_name(rs.getString("reported_user_name"));
                r.setGroup_name(rs.getString("group_name"));
                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAttachmentsByReportId(int reportId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT file_path FROM ReportAttachments WHERE report_id=?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reportId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("file_path"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ReportAttachments> getAllAttachmentsByReportId(int reportId) {
        List<ReportAttachments> list = new ArrayList<>();
        String sql = "SELECT attachment_id, file_name, file_path FROM ReportAttachments WHERE report_id=?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reportId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReportAttachments a = new ReportAttachments();
                a.setAttachmentId(rs.getInt("attachment_id"));
                a.setFileName(rs.getString("file_name"));
                a.setFilePath(rs.getString("file_path"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateAdminResponse(int reportId, String response, String status, int reviewedBy) {
        String sql = "UPDATE Reports SET admin_response=?, status=?, reviewed_by=?, updated_at=NOW() WHERE report_id=?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, response);
            ps.setString(2, status);
            ps.setInt(3, reviewedBy);
            ps.setInt(4, reportId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateReport(Reports r) {
        String sql = "UPDATE Reports SET title=?, description=?, type=? WHERE report_id=?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getTitle());
            ps.setString(2, r.getDescription());
            ps.setString(3, r.getType());
            ps.setInt(4, r.getReport_id());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteReport(int id) {
        String sql = "DELETE FROM Reports WHERE report_id=?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteAttachment(int attachmentId) {
        String sql = "DELETE FROM ReportAttachments WHERE attachment_id=?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, attachmentId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int countReports() {
        String sql = "SELECT COUNT(*) FROM Reports";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<Integer, Integer> countReportsByMonthThisYear() {
        Map<Integer, Integer> result = new HashMap<>();
        String sql = "SELECT MONTH(created_at) AS month, COUNT(*) AS cnt "
                + "FROM Reports "
                + "WHERE YEAR(created_at) = YEAR(CURDATE()) "
                + "GROUP BY MONTH(created_at)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.put(rs.getInt("month"), rs.getInt("cnt"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int countReportsByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM Reports WHERE reporter_id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countReportsByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM Reports WHERE status = ?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
