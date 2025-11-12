/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import com.sun.jdi.connect.spi.Connection;
import java.util.ArrayList;
import java.util.List;
import model.Tasks;
import java.sql.*;

/**
 *
 * @author win
 */
public class TaskDAO {

    public List<Tasks> getAllTasksByTripId(int tripId, String keyword, String status) {
        List<Tasks> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT t.*, u.name AS assigned_user_name "
                + "FROM Tasks t "
                + "LEFT JOIN Users u ON t.assigned_to = u.user_id "
                + "WHERE t.trip_id = ?"
        );

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (");
            sql.append("CAST(t.task_id AS CHAR) LIKE ? OR ");
            sql.append("t.description LIKE ? OR ");
            sql.append("CAST(t.estimated_cost AS CHAR) LIKE ? OR ");
            sql.append("t.status LIKE ? OR ");
            sql.append("u.name LIKE ? OR ");                                    
            sql.append("DATE_FORMAT(t.deadline, '%Y-%m-%d') LIKE ? OR ");
            sql.append("DATE_FORMAT(t.deadline, '%d-%m-%Y') LIKE ? OR ");  // thêm dạng dd-MM-yyyy
            sql.append("DATE_FORMAT(t.deadline, '%d-%m') LIKE ? OR ");    // tìm theo ngày-tháng
            sql.append("DATE_FORMAT(t.deadline, '%m-%Y') LIKE ? OR ");    // tìm theo tháng-năm
            sql.append("DATE_FORMAT(t.deadline, '%Y') LIKE ?");
            sql.append(")");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND t.status = ?");
        }

        try (java.sql.Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            ps.setInt(paramIndex++, tripId);

            if (keyword != null && !keyword.trim().isEmpty()) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, like);
                ps.setString(paramIndex++, like);
                ps.setString(paramIndex++, like);
                ps.setString(paramIndex++, like);
                ps.setString(paramIndex++, like);
                ps.setString(paramIndex++, like);
                ps.setString(paramIndex++, like);
                ps.setString(paramIndex++, like);
                ps.setString(paramIndex++, like);
                ps.setString(paramIndex++, like);
            }

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tasks task = new Tasks();
                    task.setTask_id(rs.getInt("task_id"));
                    task.setTrip_id(rs.getInt("trip_id"));
                    task.setAssigned_to(rs.getObject("assigned_to") != null ? rs.getInt("assigned_to") : null);
                    task.setDescription(rs.getString("description"));
                    task.setDeadline(rs.getDate("deadline"));
                    task.setEstimated_cost(rs.getBigDecimal("estimated_cost"));
                    task.setStatus(rs.getString("status"));
                    try {
                        java.lang.reflect.Method m = task.getClass().getMethod("setAssignedUserName", String.class);
                        m.invoke(task, rs.getString("assigned_user_name"));
                    } catch (Exception ignore) {
                    }
                    list.add(task);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public Tasks getTaskById(int taskId) {
        String sql = "SELECT * FROM Tasks WHERE task_id = ?";
        try (java.sql.Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString());) {
            ps.setInt(1, taskId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Tasks t = new Tasks();
                t.setTask_id(rs.getInt("task_id"));
                t.setTrip_id(rs.getInt("trip_id"));
                t.setAssigned_to(rs.getObject("assigned_to") != null ? rs.getInt("assigned_to") : null);
                t.setDescription(rs.getString("description"));
                t.setDeadline(rs.getDate("deadline"));
                t.setEstimated_cost(rs.getBigDecimal("estimated_cost"));
                t.setStatus(rs.getString("status"));
                return t;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateTask(int taskId, String description, String deadline, String estimatedCost, String status, Integer assigned_to) {
        String sql = "UPDATE Tasks SET description=?, deadline=?, estimated_cost=?, status=?, assigned_to=? WHERE task_id=?";
        try (java.sql.Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString());) {
            ps.setString(1, description);
            ps.setString(2, deadline);
            ps.setString(3, estimatedCost);
            ps.setString(4, status);
            if (assigned_to != null) {
                ps.setInt(5, assigned_to);
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setInt(6, taskId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertTask(int trip_id, String description, String deadline, String estimatedCost, String status, String assigned_to) {
        String sql = "INSERT INTO Tasks (trip_id, description, deadline, estimated_cost, status, assigned_to) VALUES (?, ?, ?, ?, ?, ?)";
        try (java.sql.Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString());) {

            ps.setInt(1, trip_id);
            ps.setString(2, description);
            ps.setString(3, deadline);
            ps.setString(4, estimatedCost);
            ps.setString(5, status);

            if (assigned_to != null && !assigned_to.isEmpty()) {
                ps.setInt(6, Integer.parseInt(assigned_to));
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa task - Không cho phép xóa task đã hoàn thành
     * @param task_id ID của task cần xóa
     * @return true nếu xóa thành công, false nếu thất bại hoặc task đã completed
     */
    public Boolean removeTask(int task_id) {
        // Kiểm tra status trước khi xóa
        String checkSql = "SELECT status FROM tasks WHERE task_id = ?";
        try (java.sql.Connection conn = DBConnect.getConnection(); 
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, task_id);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status");
                if ("Completed".equals(status)) {
                    // Không cho phép xóa task đã hoàn thành
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        // Nếu chưa completed thì cho phép xóa
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        try (java.sql.Connection conn = DBConnect.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, task_id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        TaskDAO dao = new TaskDAO();

        // Danh sách test case
        Object[][] testCases = {
            {1, "", ""}, // 1. Tất cả task của trip 1
            {1, "phở", ""}, // 2. Tìm theo keyword "phở"
            {1, "Hồ Gươm", "Pending"}, // 3. Tìm theo keyword + status cụ thể
            {1, "", "Completed"}, // 4. Chỉ lấy task đã hoàn thành
            {1, "vé", ""}, // 5. Tìm task có từ khóa "vé"
            {1, "hotel", "InProgress"} // 6. Keyword không tiếng Việt
        };

        System.out.println("=== TEST RESULTS FOR getAllTasksByTripId ===");

        for (int i = 0; i < testCases.length; i++) {
            int tripId = (int) testCases[i][0];
            String keyword = (String) testCases[i][1];
            String status = (String) testCases[i][2];

            System.out.println("\n---- Test Case " + (i + 1) + " ----");
            System.out.println("Trip ID: " + tripId + ", Keyword: '" + keyword + "', Status: '" + status + "'");

            List<Tasks> result = dao.getAllTasksByTripId(tripId, keyword, status);

            if (result.isEmpty()) {
                System.out.println("→ No tasks found.");
            } else {
                for (Tasks t : result) {
                    System.out.printf(
                            "ID: %d | Desc: %s | Deadline: %s | Cost: %s | Status: %s%n",
                            t.getTask_id(),
                            t.getDescription(),
                            t.getDeadline(),
                            t.getEstimated_cost(),
                            t.getStatus()
                    );
                }
            }
        }

        System.out.println("\n=== END OF TEST ===");
    }
}
