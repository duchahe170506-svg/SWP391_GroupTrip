package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Trips;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class TripDAO {

    private Trips mapResultSetToTrip(ResultSet rs) throws SQLException {
        Trips trip = new Trips();
        trip.setTripId(rs.getInt("trip_id"));
        trip.setGroupId(rs.getInt("group_id"));
        trip.setName(rs.getString("name"));
        trip.setLocation(rs.getString("location"));
        trip.setStartDate(rs.getDate("start_date"));
        trip.setEndDate(rs.getDate("end_date"));
        trip.setBudget(rs.getBigDecimal("budget"));
        trip.setStatus(rs.getString("status"));
        trip.setCreatedAt(rs.getTimestamp("created_at"));
        return trip;
    }

    public List<Trips> getAllTrips() {
        List<Trips> list = new ArrayList<>();
        String sql = "SELECT * FROM Trips";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToTrip(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Trips getTripById(int id) {
        String sql = "SELECT * FROM Trips WHERE trip_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToTrip(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addTrip(Trips trip) {
        String sql = "INSERT INTO Trips (group_id, name, location, start_date, end_date, budget, status, created_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trip.getGroupId());
            ps.setString(2, trip.getName());
            ps.setString(3, trip.getLocation());
            ps.setDate(4, trip.getStartDate());
            ps.setDate(5, trip.getEndDate());
            ps.setBigDecimal(6, trip.getBudget() == null ? BigDecimal.ZERO : trip.getBudget());
            ps.setString(7, trip.getStatus() == null ? "Active" : trip.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTrip(Trips trip) {
        String sql = "UPDATE Trips SET group_id=?, name=?, location=?, start_date=?, end_date=?, budget=?, status=? "
                   + "WHERE trip_id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trip.getGroupId());
            ps.setString(2, trip.getName());
            ps.setString(3, trip.getLocation());
            ps.setDate(4, trip.getStartDate());
            ps.setDate(5, trip.getEndDate());
            ps.setBigDecimal(6, trip.getBudget());
            ps.setString(7, trip.getStatus());
            ps.setInt(8, trip.getTripId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTrip(int id) {
        String sql = "DELETE FROM Trips WHERE trip_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
