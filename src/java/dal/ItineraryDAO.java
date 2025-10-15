package dal;

import model.Itinerary;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItineraryDAO extends DBConnect {

    private static final Logger LOGGER = Logger.getLogger(ItineraryDAO.class.getName());

    /**
     * 🟢 Thêm nhiều lịch trình cho 1 chuyến đi (batch insert + transaction)
     */
    public boolean insertAll(List<Itinerary> list) {
        if (list == null || list.isEmpty()) return false;

        String sql = """
            INSERT INTO Itinerary 
                (trip_id, day_number, title, description, start_time, end_time, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())
        """;

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Itinerary it : list) {
                    ps.setInt(1, it.getTripId());
                    ps.setInt(2, it.getDayNumber());
                    ps.setString(3, it.getTitle());
                    ps.setString(4, it.getDescription());
                    ps.setTime(5, it.getStartTime());
                    ps.setTime(6, it.getEndTime());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Lỗi khi insert batch Itinerary", e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 🟢 Lấy toàn bộ lịch trình của 1 chuyến đi
     */
    public List<Itinerary> getItinerariesByTripId(int tripId) {
        List<Itinerary> list = new ArrayList<>();
        String sql = """
            SELECT * FROM Itinerary
            WHERE trip_id = ?
            ORDER BY day_number ASC, start_time ASC
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tripId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Itinerary it = new Itinerary();
                    it.setItineraryId(rs.getInt("itinerary_id"));
                    it.setTripId(rs.getInt("trip_id"));
                    it.setDayNumber(rs.getInt("day_number"));
                    it.setTitle(rs.getString("title"));
                    it.setDescription(rs.getString("description"));
                    it.setStartTime(rs.getTime("start_time"));
                    it.setEndTime(rs.getTime("end_time"));
                    it.setCreatedAt(rs.getTimestamp("created_at"));
                    it.setUpdatedAt(rs.getTimestamp("updated_at"));
                    list.add(it);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Lỗi khi đọc lịch trình trip_id=" + tripId, e);
        }

        return list;
    }

    /**
     * 🟢 Xóa toàn bộ lịch trình của 1 chuyến đi
     */
    public boolean deleteByTrip(int tripId) {
        String sql = "DELETE FROM Itinerary WHERE trip_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tripId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Lỗi khi xóa lịch trình trip_id=" + tripId, e);
            return false;
        }
    }
}
