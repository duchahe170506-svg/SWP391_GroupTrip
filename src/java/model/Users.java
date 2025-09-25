package model;

import java.sql.Timestamp;

public class Users {

    private int user_id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String status;
    private Timestamp created_at;

    public Users() {
    }

    public Users(int user_id, String name, String email, String password, String role, String status, Timestamp created_at) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.created_at = created_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Users{" + "user_id=" + user_id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role + ", status=" + status + ", created_at=" + created_at + '}';
    }
}
