package dal;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
import model.Expense;

public class ExpenseDAO extends DBConnect {

    public List<Expense> getExpensesByTrip(int tripId) throws SQLException {
        String sql = "SELECT e.*, u.name AS payerName, t.name AS tripName "
                + "FROM Expenses e "
                + "JOIN Users u ON e.paid_by = u.user_id "
                + "JOIN Trips t ON e.trip_id = t.trip_id "
                + "WHERE e.trip_id = ? "
                + "ORDER BY e.created_at DESC";

        List<Expense> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Expense e = mapResultSetToExpense(rs);
                    list.add(e);
                }
            }
        }
        return list;
    }

    public boolean addExpense(Expense e) throws SQLException {
        String sql = "INSERT INTO Expenses(trip_id, paid_by, amount, expense_type, description, status, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, e.getTripId());
            ps.setInt(2, e.getPaidBy());
            ps.setBigDecimal(3, e.getAmount());
            ps.setString(4, e.getExpenseType());
            ps.setString(5, e.getDescription());
            ps.setString(6, e.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateExpense(Expense e) throws SQLException {
        String sql = "UPDATE Expenses SET amount=?, paid_by=?, expense_type=?, description=?, status=?, updated_at=NOW() "
                + "WHERE expense_id=?";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, e.getAmount());
            ps.setInt(2, e.getPaidBy());
            ps.setString(3, e.getExpenseType());
            ps.setString(4, e.getDescription());
            ps.setString(5, e.getStatus());
            ps.setInt(6, e.getExpenseId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteExpense(int expenseId) throws SQLException {
        String sql = "DELETE FROM Expenses WHERE expense_id=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, expenseId);
            return ps.executeUpdate() > 0;
        }
    }

    public BigDecimal getTotalExpenseByTrip(int tripId) throws SQLException {
        String sql = "SELECT SUM(amount) AS total FROM Expenses WHERE trip_id=? AND status='Approved' ";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total") != null ? rs.getBigDecimal("total") : BigDecimal.ZERO;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public List<Expense> getAllExpenses() throws SQLException {
        String sql = "SELECT e.*, u.name AS payerName, t.name AS tripName "
                + "FROM Expenses e "
                + "JOIN Users u ON e.paid_by = u.user_id "
                + "JOIN Trips t ON e.trip_id = t.trip_id "
                + "ORDER BY e.created_at DESC";

        List<Expense> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Expense e = mapResultSetToExpense(rs);
                list.add(e);
            }
        }
        return list;
    }

    private Expense mapResultSetToExpense(ResultSet rs) throws SQLException {
        Expense e = new Expense();
        e.setExpenseId(rs.getInt("expense_id"));
        e.setTripId(rs.getInt("trip_id"));
        e.setPaidBy(rs.getInt("paid_by"));
        e.setAmount(rs.getBigDecimal("amount"));
        e.setExpenseType(rs.getString("expense_type"));
        e.setDescription(rs.getString("description"));
        e.setStatus(rs.getString("status"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        e.setUpdatedAt(rs.getTimestamp("updated_at"));
        e.setPayerName(rs.getString("payerName"));
        e.setTripName(rs.getString("tripName"));
        return e;
    }

    public List<Expense> getExpensesByTripId(int tripId) {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT e.*, u.name AS payerName, t.name AS tripName "
                + "FROM Expenses e "
                + "JOIN Users u ON e.paid_by = u.user_id "
                + "JOIN Trips t ON e.trip_id = t.trip_id "
                + "WHERE e.trip_id = ? "
                + "ORDER BY e.created_at DESC";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Expense e = new Expense();
                e.setExpenseId(rs.getInt("expense_id"));
                e.setTripId(rs.getInt("trip_id"));
                e.setPaidBy(rs.getInt("paid_by"));
                e.setAmount(rs.getBigDecimal("amount"));
                e.setExpenseType(rs.getString("expense_type"));
                e.setDescription(rs.getString("description"));
                e.setStatus(rs.getString("status"));
                e.setCreatedAt(rs.getTimestamp("created_at"));
                e.setUpdatedAt(rs.getTimestamp("updated_at"));
                e.setPayerName(rs.getString("payerName"));
                e.setTripName(rs.getString("tripName"));
                list.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public Expense getExpenseById(int expenseId) throws SQLException {
        String sql = "SELECT * FROM Expenses WHERE expense_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, expenseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Expense e = new Expense();
                e.setExpenseId(rs.getInt("expense_id"));
                e.setTripId(rs.getInt("trip_id"));
                e.setPaidBy(rs.getInt("paid_by"));
                e.setAmount(rs.getBigDecimal("amount"));
                e.setExpenseType(rs.getString("expense_type"));
                e.setDescription(rs.getString("description"));
                e.setStatus(rs.getString("status"));
                e.setCreatedAt(rs.getTimestamp("created_at"));
                return e;
            }
        }
        return null;
    }

}
