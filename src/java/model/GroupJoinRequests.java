package model;

import java.sql.Timestamp;

public class GroupJoinRequests {
    private int request_id;
    private int group_id;
    private int user_id;
    private String status;
    private Timestamp requested_at;
    private Timestamp reviewed_at;
    private Integer reviewed_by;
    private Integer invited_by;

    public GroupJoinRequests() {
    }

    public GroupJoinRequests(int request_id, int group_id, int user_id, String status,
                             Timestamp requested_at, Timestamp reviewed_at, int reviewed_by, int invited_by) {
        this.request_id = request_id;
        this.group_id = group_id;
        this.user_id = user_id;
        this.status = status;
        this.requested_at = requested_at;
        this.reviewed_at = reviewed_at;
        this.reviewed_by = reviewed_by;
        this.invited_by = invited_by;
    }

    public GroupJoinRequests(int request_id, int group_id, int user_id, String status, Timestamp requested_at, Timestamp reviewed_at) {
        this.request_id = request_id;
        this.group_id = group_id;
        this.user_id = user_id;
        this.status = status;
        this.requested_at = requested_at;
        this.reviewed_at = reviewed_at;
    }

    public GroupJoinRequests(int request_id, int group_id, int user_id, String status, Timestamp requested_at) {
        this.request_id = request_id;
        this.group_id = group_id;
        this.user_id = user_id;
        this.status = status;
        this.requested_at = requested_at;
    }

    public GroupJoinRequests(int request_id, int group_id, int user_id, String status, Timestamp requested_at, Timestamp reviewed_at, int reviewed_by) {
        this.request_id = request_id;
        this.group_id = group_id;
        this.user_id = user_id;
        this.status = status;
        this.requested_at = requested_at;
        this.reviewed_at = reviewed_at;
        this.reviewed_by = reviewed_by;
    }

    

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getRequested_at() {
        return requested_at;
    }

    public void setRequested_at(Timestamp requested_at) {
        this.requested_at = requested_at;
    }

    public Timestamp getReviewed_at() {
        return reviewed_at;
    }

    public void setReviewed_at(Timestamp reviewed_at) {
        this.reviewed_at = reviewed_at;
    }

    public Integer getReviewed_by() {
        return reviewed_by;
    }

    public void setReviewed_by(Integer reviewed_by) {
        this.reviewed_by = reviewed_by;
    }

    public Integer getInvited_by() {
        return invited_by;
    }

    public void setInvited_by(Integer invited_by) {
        this.invited_by = invited_by;
    }

    
}
