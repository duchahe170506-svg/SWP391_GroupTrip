package model;

import java.sql.Timestamp;

public class GroupMembers {

    private int group_id;
    private int user_id;
    private String role;
    private Timestamp joined_at;
    
    private String name;
    private String email;

    public GroupMembers() {
    }

    public GroupMembers(int group_id, int user_id, String role, Timestamp joined_at) {
        this.group_id = group_id;
        this.user_id = user_id;
        this.role = role;
        this.joined_at = joined_at;
    }

    public GroupMembers(int group_id, int user_id, String role, Timestamp joined_at, String name, String email) {
        this.group_id = group_id;
        this.user_id = user_id;
        this.role = role;
        this.joined_at = joined_at;
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

    public Timestamp getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(Timestamp joined_at) {
        this.joined_at = joined_at;
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
        return "GroupMembers{" + "group_id=" + group_id + ", user_id=" + user_id + ", role=" + role + ", joined_at=" + joined_at + '}';
    }
}


