package model;

import java.sql.Timestamp;

public class PasswordResets {

    private int reset_id;
    private int user_id;
    private String token;
    private Timestamp expires_at;
    private Timestamp created_at;

    public PasswordResets() {
    }

    public PasswordResets(int reset_id, int user_id, String token, Timestamp expires_at, Timestamp created_at) {
        this.reset_id = reset_id;
        this.user_id = user_id;
        this.token = token;
        this.expires_at = expires_at;
        this.created_at = created_at;
    }

    public int getReset_id() {
        return reset_id;
    }

    public void setReset_id(int reset_id) {
        this.reset_id = reset_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(Timestamp expires_at) {
        this.expires_at = expires_at;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "PasswordResets{" + "reset_id=" + reset_id + ", user_id=" + user_id + ", token=" + token + ", expires_at=" + expires_at + ", created_at=" + created_at + '}';
    }
}


