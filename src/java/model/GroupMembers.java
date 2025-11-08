package model;

import java.sql.Timestamp;

public class GroupMembers {

    private int group_id;
    private int user_id;
    private String role;        
    private String status;      
    private Timestamp joined_at;
    private Timestamp removed_at;

    
    private String name;
    private String email;

    public GroupMembers() {
    }

    public GroupMembers(int group_id, int user_id, String role, String status, Timestamp joined_at, Timestamp removed_at) {
        this.group_id = group_id;
        this.user_id = user_id;
        this.role = role;
        this.status = status;
        this.joined_at = joined_at;
        this.removed_at = removed_at;
    }

    public GroupMembers(int group_id, int user_id, String role, String status, Timestamp joined_at, Timestamp removed_at, String name, String email) {
        this.group_id = group_id;
        this.user_id = user_id;
        this.role = role;
        this.status = status;
        this.joined_at = joined_at;
        this.removed_at = removed_at;
        this.name = name;
        this.email = email;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(Timestamp joined_at) {
        this.joined_at = joined_at;
    }

    public Timestamp getRemoved_at() {
        return removed_at;
    }

    public void setRemoved_at(Timestamp removed_at) {
        this.removed_at = removed_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "GroupMembers{" +
                "group_id=" + group_id +
                ", user_id=" + user_id +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", joined_at=" + joined_at +
                ", removed_at=" + removed_at +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
