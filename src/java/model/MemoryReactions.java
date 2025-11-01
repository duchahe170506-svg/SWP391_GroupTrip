package model;

import java.util.Date;

public class MemoryReactions {

    private int memoryId;
    private int userId;
    private String type; 
    private Date reactedAt;
    
    private String userReaction;

    public MemoryReactions() {
    }

    public MemoryReactions(int memoryId, int userId, String type, Date reactedAt) {
        this.memoryId = memoryId;
        this.userId = userId;
        this.type = type;
        this.reactedAt = reactedAt;
    }

    public int getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(int memoryId) {
        this.memoryId = memoryId;
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

    public Date getReactedAt() {
        return reactedAt;
    }

    public void setReactedAt(Date reactedAt) {
        this.reactedAt = reactedAt;
    }

    public String getUserReaction() {
        return userReaction;
    }

    public void setUserReaction(String userReaction) {
        this.userReaction = userReaction;
    }
    
    
}
