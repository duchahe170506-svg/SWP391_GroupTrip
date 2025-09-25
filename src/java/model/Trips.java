package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Trips {

    private int trip_id;
    private int group_id;
    private String name;
    private String location;
    private Date start_date;
    private Date end_date;
    private BigDecimal budget;
    private String status;
    private Timestamp created_at;

    public Trips() {
    }

    public Trips(int trip_id, int group_id, String name, String location, Date start_date, Date end_date, BigDecimal budget, String status, Timestamp created_at) {
        this.trip_id = trip_id;
        this.group_id = group_id;
        this.name = name;
        this.location = location;
        this.start_date = start_date;
        this.end_date = end_date;
        this.budget = budget;
        this.status = status;
        this.created_at = created_at;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
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
        return "Trips{" + "trip_id=" + trip_id + ", group_id=" + group_id + ", name=" + name + ", location=" + location + ", start_date=" + start_date + ", end_date=" + end_date + ", budget=" + budget + ", status=" + status + ", created_at=" + created_at + '}';
    }
}


