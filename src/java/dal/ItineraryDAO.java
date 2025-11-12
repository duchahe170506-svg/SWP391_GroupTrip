package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Itinerary;

public class ItineraryDAO {

    public List<Itinerary> getItinerariesByTripId(int tripId) {
        List<Itinerary> itineraries = new ArrayList<>();
        String sql = "SELECT itinerary_id, trip_id, day_number, title, description, start_time, end_time, created_at, updated_at "
                + "FROM itinerary WHERE trip_id = ? ORDER BY day_number ASC, start_time ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                itineraries.add(mapRow(rs));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return itineraries;
    }

    public Itinerary getItineraryById(int itineraryId) {
        String sql = "SELECT itinerary_id, trip_id, day_number, title, description, start_time, end_time, created_at, updated_at "
                + "FROM itinerary WHERE itinerary_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itineraryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean insertItinerary(Itinerary itinerary) {
        String sql = "INSERT INTO itinerary (trip_id, day_number, title, description, start_time, end_time) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itinerary.getTripId());
            ps.setInt(2, itinerary.getDayNumber());
            ps.setString(3, itinerary.getTitle());
            ps.setString(4, itinerary.getDescription());
            ps.setTime(5, itinerary.getStartTime());
            ps.setTime(6, itinerary.getEndTime());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updateItinerary(Itinerary itinerary) {
        String sql = "UPDATE itinerary SET day_number = ?, title = ?, description = ?, start_time = ?, end_time = ? "
                + "WHERE itinerary_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itinerary.getDayNumber());
            ps.setString(2, itinerary.getTitle());
            ps.setString(3, itinerary.getDescription());
            ps.setTime(4, itinerary.getStartTime());
            ps.setTime(5, itinerary.getEndTime());
            ps.setInt(6, itinerary.getItineraryId());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean deleteItinerary(int itineraryId) {
        String sql = "DELETE FROM itinerary WHERE itinerary_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itineraryId);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean insertAll(List<Itinerary> itineraries) {
        if (itineraries == null || itineraries.isEmpty()) {
            return false;
        }
        String sql = "INSERT INTO itinerary (trip_id, day_number, title, description, start_time, end_time) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            for (Itinerary itinerary : itineraries) {
                ps.setInt(1, itinerary.getTripId());
                ps.setInt(2, itinerary.getDayNumber());
                ps.setString(3, itinerary.getTitle());
                ps.setString(4, itinerary.getDescription());
                ps.setTime(5, itinerary.getStartTime());
                ps.setTime(6, itinerary.getEndTime());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
            return true;
        } catch (Exception ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            ex.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ignore) {
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception ignore) {
                }
            }
        }
        return false;
    }

    private Itinerary mapRow(ResultSet rs) throws Exception {
        Itinerary itinerary = new Itinerary();
        itinerary.setItineraryId(rs.getInt("itinerary_id"));
        itinerary.setTripId(rs.getInt("trip_id"));
        itinerary.setDayNumber(rs.getInt("day_number"));
        itinerary.setTitle(rs.getString("title"));
        itinerary.setDescription(rs.getString("description"));
        itinerary.setStartTime(rs.getTime("start_time"));
        itinerary.setEndTime(rs.getTime("end_time"));
        itinerary.setCreatedAt(rs.getTimestamp("created_at"));
        itinerary.setUpdatedAt(rs.getTimestamp("updated_at"));
        return itinerary;
    }
}