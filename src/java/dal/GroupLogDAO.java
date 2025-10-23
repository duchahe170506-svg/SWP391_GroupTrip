package dal;

import model.GroupRoleHistory;
import model.GroupJoinRequests;
import model.MemberRemovals;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupLogDAO extends DBConnect {

    public List<GroupRoleHistory> getRoleHistory(int groupId) throws SQLException {
        List<GroupRoleHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM GroupRoleHistory WHERE group_id = ? ORDER BY changed_at DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GroupRoleHistory h = new GroupRoleHistory();
                h.setHistory_id(rs.getInt("history_id"));
                h.setGroup_id(rs.getInt("group_id"));
                h.setUser_id(rs.getInt("user_id"));
                h.setOld_role(rs.getString("old_role"));
                h.setNew_role(rs.getString("new_role"));
                h.setChanged_by(rs.getInt("changed_by"));
                h.setChanged_at(rs.getTimestamp("changed_at"));
                list.add(h);
            }
        }
        return list;
    }

    public List<GroupJoinRequests> getJoinRequestsHistory(int groupId) throws SQLException {
    List<GroupJoinRequests> list = new ArrayList<>();
    String sql = "SELECT * FROM GroupJoinRequests WHERE group_id = ? AND status IN ('ACCEPTED','REJECTED','INVITED') ORDER BY reviewed_at DESC";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, groupId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            GroupJoinRequests r = new GroupJoinRequests();
            r.setRequest_id(rs.getInt("request_id"));
            r.setGroup_id(rs.getInt("group_id"));
            r.setUser_id(rs.getInt("user_id"));
            r.setStatus(rs.getString("status"));
            r.setRequested_at(rs.getTimestamp("requested_at"));
            r.setReviewed_at(rs.getTimestamp("reviewed_at"));
            int revBy = rs.getInt("reviewed_by");
            if (!rs.wasNull()) r.setReviewed_by(revBy);
            int invBy = rs.getInt("invited_by");
            if (!rs.wasNull()) r.setInvited_by(invBy);
            list.add(r);
        }
    }
    return list;
}


    public List<MemberRemovals> getRemovalHistory(int groupId) throws SQLException {
        List<MemberRemovals> list = new ArrayList<>();
        String sql = "SELECT * FROM MemberRemovals WHERE group_id = ? ORDER BY removed_at DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MemberRemovals m = new MemberRemovals();
                m.setRemoval_id(rs.getInt("removal_id"));
                m.setGroup_id(rs.getInt("group_id"));
                m.setRemoved_user_id(rs.getInt("removed_user_id"));
                m.setRemoved_by(rs.getInt("removed_by"));
                m.setReason(rs.getString("reason"));
                m.setRemoved_at(rs.getTimestamp("removed_at"));
                list.add(m);
            }
        }
        return list;
    }
}
