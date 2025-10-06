package dal;

import model.TripImages;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripImagesDAO {

    // Thêm 1 ảnh cho trip
    public boolean insertImage(int tripId, String url) {
        String sql = "INSERT INTO TripImages (trip_id, image_url) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            ps.setString(2, url);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách ảnh theo tripId
    public List<TripImages> getImagesByTrip(int tripId) {
        List<TripImages> list = new ArrayList<>();
        String sql = "SELECT * FROM TripImages WHERE trip_id=? ORDER BY created_at";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TripImages img = new TripImages();
                    img.setImageId(rs.getInt("image_id"));
                    img.setTripId(rs.getInt("trip_id"));
                    img.setImageUrl(rs.getString("image_url"));
                    img.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(img);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
