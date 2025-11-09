package model;

import java.math.BigDecimal;
import java.util.Date;

public class Trips {

    private int tripId;
    private int groupId;
    private String name;
    private String description;
    private String location;
    private String meeting_point;     // 🆕 Địa điểm tập trung
    private Date startDate;
    private Date endDate;
    private BigDecimal budget;
    private String coverImage;
    private int min_participants;     // 🆕 Số người tối thiểu
    private int max_participants;
    private String tripType;          // "Du lịch nghỉ dưỡng", "Foodtour", ...
    private String status;            // Active / Blocked / Private
    private Date createdAt;

    private String group_name;
    private String leader_name;


    public Trips() {
    }

    public Trips(int tripId, int groupId, String name, String description, String location,
            String meeting_point, Date startDate, Date endDate,
            BigDecimal budget, String coverImage,
            int min_participants, int max_participants,
            String tripType, String status, Date createdAt) {
        this.tripId = tripId;
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.meeting_point = meeting_point;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.coverImage = coverImage;
        this.min_participants = min_participants;
        this.max_participants = max_participants;
        this.tripType = tripType;
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

    public String getMeeting_point() {
        return meeting_point;
    }

    public void setMeeting_point(String meeting_point) {
        this.meeting_point = meeting_point;
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

    public int getMin_participants() {
        return min_participants;
    }

    public void setMin_participants(int min_participants) {
        this.min_participants = min_participants;
    }

    public int getMax_participants() {
        return max_participants;
    }

    public void setMax_participants(int max_participants) {
        this.max_participants = max_participants;
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

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getLeader_name() {
        return leader_name;
    }

    public void setLeader_name(String leader_name) {
        this.leader_name = leader_name;
    }
    
    @Override
    public String toString() {
        return "Trips{"
                + "tripId=" + tripId
                + ", groupId=" + groupId
                + ", name=" + name
                + ", description=" + description
                + ", location=" + location
                + ", meeting_point=" + meeting_point
                + ", startDate=" + startDate
                + ", endDate=" + endDate
                + ", budget=" + budget
                + ", coverImage=" + coverImage
                + ", min_participants=" + min_participants
                + ", max_participants=" + max_participants
                + ", tripType=" + tripType
                + ", status=" + status
                + ", createdAt=" + createdAt
                + '}';
    }
}
