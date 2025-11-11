package dal;

import model.Trips;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TripDAO {

    /* =========================================================
       CREATE
     * ========================================================= */
    /**
     * Insert trip và trả về ID vừa tạo (>=0) hoặc -1 nếu lỗi
     */
    public int insertTrip(Trips trip) {
        String sql = "INSERT INTO Trips ("
                + "group_id, name, description, location, meeting_point, start_date, end_date, "
                + "budget, cover_image, min_participants, max_participants, trip_type, status"
                + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, trip.getGroupId());
            ps.setString(2, trip.getName());
            ps.setString(3, trip.getDescription());
            ps.setString(4, trip.getLocation());
            ps.setString(5, trip.getMeeting_point());
            setNullableDate(ps, 6, trip.getStartDate());
            setNullableDate(ps, 7, trip.getEndDate());

            if (trip.getBudget() != null) {
                ps.setBigDecimal(8, trip.getBudget());
            } else {
                ps.setNull(8, Types.DECIMAL);
            }

            ps.setString(9, trip.getCoverImage());
            ps.setInt(10, trip.getMin_participants());
            ps.setInt(11, trip.getMax_participants());
            ps.setString(12, trip.getTripType());
            ps.setString(13, trip.getStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    /* =========================================================
       READ
     * ========================================================= */
    public Trips getTripById(int tripId) {
        String sql = "SELECT * FROM Trips WHERE trip_id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tripId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy tất cả chuyến đi active (không filter)
     */
    public List<Trips> getAllTrips() {
        String sql = "SELECT * FROM Trips WHERE status = 'Active' ORDER BY created_at DESC";
        List<Trips> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tìm kiếm theo khoảng ngân sách + ngày đi/khoảng ngày (tham số nào null sẽ
     * bỏ qua)
     */
    public List<Trips> searchTrips(BigDecimal minBudget, BigDecimal maxBudget,
            java.util.Date departOn,
            java.util.Date fromDate, java.util.Date toDate) {

        StringBuilder sql = new StringBuilder("SELECT * FROM Trips WHERE status = 'Active'");
        List<Object> params = new ArrayList<>();

        if (minBudget != null) {
            sql.append(" AND budget >= ?");
            params.add(minBudget);
        }
        if (maxBudget != null) {
            sql.append(" AND budget <= ?");
            params.add(maxBudget);
        }
        if (departOn != null) {
            sql.append(" AND start_date = ?");
            params.add(new java.sql.Date(departOn.getTime()));
        }
        if (fromDate != null) {
            sql.append(" AND start_date >= ?");
            params.add(new java.sql.Date(fromDate.getTime()));
        }
        if (toDate != null) {
            sql.append(" AND end_date <= ?");
            params.add(new java.sql.Date(toDate.getTime()));
        }

        sql.append(" ORDER BY created_at DESC");

        List<Trips> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            for (Object p : params) {
                if (p instanceof BigDecimal) {
                    ps.setBigDecimal(idx++, (BigDecimal) p);
                } else if (p instanceof java.sql.Date) {
                    ps.setDate(idx++, (java.sql.Date) p);
                } else {
                    ps.setObject(idx++, p);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * BỔ SUNG: Tìm kiếm theo ngân sách + ngày + loại chuyến đi
     */
    public List<Trips> searchTrips(BigDecimal minBudget, BigDecimal maxBudget,
            java.util.Date departOn,
            java.util.Date fromDate, java.util.Date toDate,
            String tripType) {

        StringBuilder sql = new StringBuilder("SELECT * FROM Trips WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (minBudget != null) {
            sql.append(" AND budget >= ?");
            params.add(minBudget);
        }
        if (maxBudget != null) {
            sql.append(" AND budget <= ?");
            params.add(maxBudget);
        }
        if (departOn != null) {
            sql.append(" AND start_date = ?");
            params.add(new java.sql.Date(departOn.getTime()));
        }
        if (fromDate != null) {
            sql.append(" AND start_date >= ?");
            params.add(new java.sql.Date(fromDate.getTime()));
        }
        if (toDate != null) {
            sql.append(" AND end_date <= ?");
            params.add(new java.sql.Date(toDate.getTime()));
        }
        if (tripType != null && !tripType.isEmpty()) {
            sql.append(" AND trip_type = ?");
            params.add(tripType);
        }

        sql.append(" ORDER BY created_at DESC");

        List<Trips> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            for (Object p : params) {
                if (p instanceof BigDecimal) {
                    ps.setBigDecimal(idx++, (BigDecimal) p);
                } else if (p instanceof java.sql.Date) {
                    ps.setDate(idx++, (java.sql.Date) p);
                } else {
                    ps.setObject(idx++, p);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* =========================================================
       UPDATE
     * ========================================================= */
    public boolean updateTrip(Trips trip) {
        String sql = "UPDATE Trips SET group_id=?, name=?, description=?, location=?, meeting_point=?, start_date=?, end_date=?,"
                + " budget=?, cover_image=?, min_participants=?, max_participants=?, trip_type=?, status=? WHERE trip_id=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, trip.getGroupId());
            ps.setString(2, trip.getName());
            ps.setString(3, trip.getDescription());
            ps.setString(4, trip.getLocation());
            ps.setString(5, trip.getMeeting_point());
            setNullableDate(ps, 6, trip.getStartDate());
            setNullableDate(ps, 7, trip.getEndDate());
            ps.setBigDecimal(8, trip.getBudget());
            ps.setString(9, trip.getCoverImage());
            ps.setInt(10, trip.getMin_participants());
            ps.setInt(11, trip.getMax_participants());
            ps.setString(12, trip.getTripType());
            ps.setString(13, trip.getStatus());
            ps.setInt(14, trip.getTripId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* =========================================================
       DELETE
     * ========================================================= */
    public boolean deleteTrip(int tripId) {
        String sql = "DELETE FROM Trips WHERE trip_id=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* =========================================================
       EXTRA
     * ========================================================= */
    public int getParticipantCountByTrip(int tripId) {
        String sql = "SELECT COUNT(*) AS cnt "
                + "FROM GroupMembers gm "
                + "JOIN Trips t ON t.group_id = gm.group_id "
                + "WHERE t.trip_id = ? AND gm.role <> 'Leader'";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /* =========================================================
       HELPERS
     * ========================================================= */
    private Trips map(ResultSet rs) throws SQLException {
        Trips trip = new Trips();
        trip.setTripId(rs.getInt("trip_id"));
        trip.setGroupId(rs.getInt("group_id"));
        trip.setName(rs.getString("name"));
        trip.setDescription(rs.getString("description"));
        trip.setLocation(rs.getString("location"));
        trip.setMeeting_point(rs.getString("meeting_point"));
        trip.setStartDate(rs.getDate("start_date"));
        trip.setEndDate(rs.getDate("end_date"));
        trip.setBudget(rs.getBigDecimal("budget"));
        trip.setCoverImage(rs.getString("cover_image"));
        trip.setMin_participants(rs.getInt("min_participants"));
        trip.setMax_participants(rs.getInt("max_participants"));
        trip.setTripType(rs.getString("trip_type"));
        trip.setStatus(rs.getString("status"));
        trip.setCreatedAt(rs.getTimestamp("created_at"));
        return trip;
    }

    private void setNullableDate(PreparedStatement ps, int index, java.util.Date d) throws SQLException {
        if (d == null) {
            ps.setNull(index, Types.DATE);
        } else {
            ps.setDate(index, new java.sql.Date(d.getTime()));
        }
    }

    public List<Trips> getTripsByMember(int userId) {
        String sql = "SELECT t.* FROM Trips t "
                + "JOIN GroupMembers gm ON gm.group_id = t.group_id "
                + "WHERE gm.user_id = ? AND t.status = 'Active'";
        List<Trips> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Trips> getTripsByLeader(int userId) {
        String sql = "SELECT t.* FROM Trips t "
                + "JOIN TravelGroups g ON g.group_id = t.group_id "
                + "WHERE g.leader_id = ? AND t.status != 'Blocked'";
        List<Trips> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean isUserLeaderOfTrip(int userId, int tripId) {
        String sql = """
            SELECT COUNT(*) FROM GroupMembers gm
            JOIN Trips t ON gm.group_id = t.group_id
            WHERE gm.user_id = ? AND t.trip_id = ? AND gm.role = 'Leader'
        """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, tripId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getLeaderIdByTrip(int tripId) {
        String sql = "SELECT leader_id FROM TravelGroups tg "
                + "JOIN Trips t ON tg.group_id = t.group_id WHERE t.trip_id=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("leader_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int countAllTrips() {
        String sql = "SELECT COUNT(*) FROM Trips";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countTripsCreatedByUser(int userId) {
        String sql = """
        SELECT COUNT(*)
        FROM Trips t
        JOIN TravelGroups g ON t.group_id = g.group_id
        WHERE g.leader_id = ?
    """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<String, Integer> countTripsGroupByUser() {
        String sql = """
        SELECT u.name, COUNT(t.trip_id) AS trip_count
        FROM Trips t
        JOIN TravelGroups g ON t.group_id = g.group_id
        JOIN Users u ON g.leader_id = u.user_id
        GROUP BY u.user_id, u.name
        ORDER BY trip_count DESC
    """;

        Map<String, Integer> data = new LinkedHashMap<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("name"), rs.getInt("trip_count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Trips getOverlappingTrip(int userId, int tripId) {
        String sql = """
        SELECT t2.*
        FROM Trips t2
        JOIN GroupMembers gm2 ON gm2.group_id = t2.group_id
        WHERE gm2.user_id = ?
          AND t2.status = 'Active'
          AND (
              (t2.start_date <= (SELECT end_date FROM Trips WHERE trip_id = ?))
              AND (t2.end_date >= (SELECT start_date FROM Trips WHERE trip_id = ?))
          )
        LIMIT 1;
    """;

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, tripId);
            ps.setInt(3, tripId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Trips t = new Trips();
                t.setTripId(rs.getInt("trip_id"));
                t.setName(rs.getString("name"));
                t.setLocation(rs.getString("location"));
                t.setStartDate(rs.getDate("start_date"));
                t.setEndDate(rs.getDate("end_date"));
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Trips> getTripsWithSearch(String keyword) {
        List<Trips> trips = new ArrayList<>();
        String sql = """
        SELECT t.trip_id, t.name, t.group_id, t.location, t.start_date, t.end_date, t.status,
               u.name AS leader_name
        FROM Trips t
        LEFT JOIN TravelGroups g ON t.group_id = g.group_id
        LEFT JOIN Users u ON g.leader_id = u.user_id
        WHERE t.name LIKE ?
        ORDER BY t.created_at DESC
    """;

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Trips t = new Trips();
                    t.setTripId(rs.getInt("trip_id"));
                    t.setName(rs.getString("name"));
                    t.setGroupId(rs.getInt("group_id"));
                    t.setLocation(rs.getString("location"));
                    t.setStartDate(rs.getDate("start_date"));
                    t.setEndDate(rs.getDate("end_date"));
                    t.setStatus(rs.getString("status"));
                    t.setGroup_name(rs.getString("leader_name"));
                    trips.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trips;
    }

    public void updateStatus(int tripId, String newStatus) {
        String sql = "UPDATE Trips SET status = ? WHERE trip_id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, tripId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countTrips() {
        String sql = "SELECT COUNT(*) FROM trips";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countTripsByMonth(int month, int year) {
        String sql = "SELECT COUNT(*) FROM trips WHERE MONTH(created_at) = ? AND YEAR(created_at) = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<Integer, Integer> countTripsByMonthThisYear() {
        Map<Integer, Integer> result = new LinkedHashMap<>();

        for (int m = 1; m <= 12; m++) {
            result.put(m, 0);
        }

        String sql = "SELECT MONTH(start_date) AS month, COUNT(*) AS total "
                + "FROM Trips "
                + "WHERE YEAR(start_date) = YEAR(CURDATE()) "
                + "GROUP BY MONTH(start_date)";

        try (Connection conn = new DBConnect().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int month = rs.getInt("month");
                int total = rs.getInt("total");
                result.put(month, total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    
    public BigDecimal getBudgetByTripId(int tripId) throws SQLException {
        String sql = "SELECT budget FROM Trips WHERE trip_id=?";
        try (Connection conn = new DBConnect().getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("budget") != null ? rs.getBigDecimal("budget") : BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    public boolean hasUserRequested(int groupId, int userId) {
        String sql = "SELECT COUNT(*) FROM GroupJoinRequests WHERE group_id=? AND user_id=? AND status='PENDING'";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
