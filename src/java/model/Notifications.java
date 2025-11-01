package model;

import java.sql.Timestamp;

public class Notifications {

    private int notificationId;
    private Integer senderId;    
    private int userId;
    private String type;         
    private Integer relatedId;    
    private String message;
    private String status;        
    private Timestamp createdAt;
    
    private String senderName;  
    private Integer relatedRequestId;
    private String requestStatus;
    private String tripName;       
    private String typeLabel;

    public Notifications() {
    }

    public Notifications(int notificationId, Integer senderId, int userId, String type, Integer relatedId, String message, String status, Timestamp createdAt) {
        this.notificationId = notificationId;
        this.senderId = senderId;
        this.userId = userId;
        this.type = type;
        this.relatedId = relatedId;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Integer getRelatedRequestId() {
        return relatedRequestId;
    }

    public void setRelatedRequestId(Integer relatedRequestId) {
        this.relatedRequestId = relatedRequestId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
    
    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    
    
    @Override
    public String toString() {
        return "Notifications{" +
                "notificationId=" + notificationId +
                ", senderId=" + senderId +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", relatedId=" + relatedId +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
