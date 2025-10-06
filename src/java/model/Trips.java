package model;

import java.math.BigDecimal;
import java.util.Date;

public class Trips {
    private int tripId;
    private int groupId;
    private String name;
    private String description;
    private String location;
    private Date startDate;
    private Date endDate;
    private BigDecimal budget;
    private String coverImage;
    private String tripType;   // "Du lịch nghỉ dưỡng", "Foodtour", ...
    private String status;     // Active / Blocked
    private Date createdAt;

    // Constructors
    public Trips() {}

    public Trips(int tripId, int groupId, String name, String description, String location,
                 Date startDate, Date endDate, BigDecimal budget, String coverImage,
                 String tripType, String status, Date createdAt) {
        this.tripId = tripId;
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.coverImage = coverImage;
        this.tripType = tripType;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
