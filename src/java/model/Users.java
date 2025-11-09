package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Users {

    private int user_id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private Date date_of_birth;
    private String gender;
    private String avatar;
    private String address;
    private String role;
    private String status;

    private int reportCount;
    private int createdTripCount;
    private int joinedTripCount;

    public Users() {
    }

    public Users(int user_id, String name, String email, String phone, String password, Date date_of_birth,
            String gender, String avatar, String address, String role, String status) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.avatar = avatar;
        this.address = address;
        this.role = role;
        this.status = status;
    }

    public Users(int user_id, String name, String email, String password, String role, String status, Timestamp created_at) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public int getCreatedTripCount() {
        return createdTripCount;
    }

    public void setCreatedTripCount(int createdTripCount) {
        this.createdTripCount = createdTripCount;
    }

    public int getJoinedTripCount() {
        return joinedTripCount;
    }

    public void setJoinedTripCount(int joinedTripCount) {
        this.joinedTripCount = joinedTripCount;
    }
    
    

}
