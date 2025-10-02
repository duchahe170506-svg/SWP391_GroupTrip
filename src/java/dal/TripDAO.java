package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Trips;

public class TripDAO extends DBConnect {

    // ===================== TRIPS =====================

    // Lấy tất cả chuyến đi
    public List<Trips> getAllTrips() {
        List<Trips> list = new ArrayList<>();
        String sql = "SELECT * FROM Trips ORDER BY created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách chuyến đi có filter
    public List<Trips> searchTrips(Double budget, Date start, Date end) {
        List<Trips> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Trips WHERE 1=1");

        if (budget != null) {
            sql.append(" AND budget <= ?");
        }
        if (start != null) {
            sql.append(" AND start_date >= ?");
        }
        if (end != null) {
            sql.append(" AND end_date <= ?");
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (budget != null) ps.setDouble(idx++, budget);
            if (start != null) ps.setDate(idx++, start);
            if (end != null) ps.setDate(idx++, end);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Map từ ResultSet sang đối tượng Trips
    private Trips mapResultSet(ResultSet rs) throws SQLException {
        return new Trips(
                rs.getInt("trip_id"),
                rs.getInt("group_id"),
                rs.getString("name"),
                rs.getString("location"),
                rs.getDate("start_date"),
                rs.getDate("end_date"),
                rs.getBigDecimal("budget"),
                rs.getString("status"),
                rs.getTimestamp("created_at")
        );
    }

    // Thêm chuyến đi mới
    public boolean insertTrip(Trips trip) {
        String sql = "INSERT INTO Trips (group_id, name, location, start_date, end_date, budget, status, created_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, trip.getGroup_id());
            ps.setString(2, trip.getName());
            ps.setString(3, trip.getLocation());
            ps.setDate(4, trip.getStart_date());
            ps.setDate(5, trip.getEnd_date());
            ps.setBigDecimal(6, trip.getBudget());
            ps.setString(7, trip.getStatus());
            ps.setTimestamp(8, trip.getCreated_at());

            return ps.executeUpdate() > 0;  // ✅ trả về true nếu thêm thành công
        } catch (SQLException e) {
            System.err.println("❌ Lỗi insertTrip: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ===================== GROUPS =====================

    // ✅ Hàm mới: tạo Group và trả về group_id tự tăng
    public int createGroupAndGetId(String groupName, String description, int leaderId) {
    String sql = "INSERT INTO TravelGroups (name, description, leader_id) VALUES (?, ?, ?)";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, groupName);
        ps.setString(2, description);       // có thể null nếu không nhập
        ps.setInt(3, leaderId);

        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1); // ✅ trả về group_id vừa tạo
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1; // ❌ nếu thất bại
}
}