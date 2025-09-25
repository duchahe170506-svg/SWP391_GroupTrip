package model;

import java.sql.Timestamp;

public class GroupMembers {

    private int group_id;
    private int user_id;
    private String role;
    private Timestamp joined_at;

    public GroupMembers() {
    }

    public GroupMembers(int group_id, int user_id, String role, Timestamp joined_at) {
        this.group_id = group_id;
        this.user_id = user_id;
        this.role = role;
        this.joined_at = joined_at;
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

    public Timestamp getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(Timestamp joined_at) {
        this.joined_at = joined_at;
    }

    @Override
    public String toString() {
        return "GroupMembers{" + "group_id=" + group_id + ", user_id=" + user_id + ", role=" + role + ", joined_at=" + joined_at + '}';
    }
}


