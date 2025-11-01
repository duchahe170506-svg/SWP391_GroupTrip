// Package: model
// File: ProfileMemoryReactions.java
package model;

import java.sql.Timestamp;

public class ProfileMemoryReactions {
    private int shareId;
    private int userId;
    private String type;
    private Timestamp reactedAt;

    // Getters and Setters
    public int getShareId() { return shareId; }
    public void setShareId(int shareId) { this.shareId = shareId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Timestamp getReactedAt() { return reactedAt; }
    public void setReactedAt(Timestamp reactedAt) { this.reactedAt = reactedAt; }
}