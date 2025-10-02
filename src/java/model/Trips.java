package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Trips {
    private int tripId;
    private int groupId;
    private String name;
    private String location;
    private Date startDate;
    private Date endDate;
    private BigDecimal budget;
    private String status;
    private Timestamp createdAt;
    

    public Trips() {
    }

    public Trips(int tripId, int groupId, String name, String location, Date startDate, Date endDate, BigDecimal budget, String status, Timestamp createdAt) {
        this.tripId = tripId;
        this.groupId = groupId;
        this.name = name;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
