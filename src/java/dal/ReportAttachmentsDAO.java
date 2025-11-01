package dal;

import java.sql.*;
import java.util.*;
import model.ReportAttachments;
import dal.DBConnect;

public class ReportAttachmentsDAO extends DBConnect {

    public void addAttachment(int reportId, String fileName, String filePath) {
        String sql = "INSERT INTO ReportAttachments (report_id, file_name, file_path) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reportId);
            ps.setString(2, fileName);
            ps.setString(3, filePath);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error addAttachment: " + e.getMessage());
        }
    }

    public List<ReportAttachments> getAttachmentsByReport(int reportId) {
        List<ReportAttachments> list = new ArrayList<>();
        String sql = "SELECT * FROM ReportAttachments WHERE report_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reportId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReportAttachments a = new ReportAttachments();
                a.setAttachmentId(rs.getInt("attachment_id"));
                a.setReportId(rs.getInt("report_id"));
                a.setFileName(rs.getString("file_name"));
                a.setFilePath(rs.getString("file_path"));
                a.setUploadedAt(rs.getTimestamp("uploaded_at"));
                list.add(a);
            }
        } catch (Exception e) {
            System.out.println("Error getAttachmentsByReport: " + e.getMessage());
        }
        return list;
    }
}
