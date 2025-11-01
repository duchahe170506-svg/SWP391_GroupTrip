// Package: model
// File: ProfileMemoryComments.java
package model;

import java.sql.Timestamp;

public class ProfileMemoryComments {
    private int commentId;
    private int shareId;
    private int userId;
    private String content;
    private Timestamp createdAt;

    // Additional field for display (populated if needed)
    private String userName; // Assuming from Users, populated by DAO/Servlet

    // Getters and Setters
    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public int getShareId() { return shareId; }
    public void setShareId(int shareId) { this.shareId = shareId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}