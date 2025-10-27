package model;

import java.util.Date;
import java.util.List;

public class Memories {

    private int memoryId;
    private int userId;
    private int groupId;
    private String title;
    private String content;
    private String imageUrl;
    private Date createdAt;

    private int reactionsCount;
    private List<MemoryComments> comments;
    private List<Users> taggedUsers;
    private String userName;
    private String userReaction;

    public Memories() {
    }

    public Memories(int memoryId, int userId, int groupId, String title, String content, String imageUrl, Date createdAt) {
        this.memoryId = memoryId;
        this.userId = userId;
        this.groupId = groupId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
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

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getReactionsCount() {
        return reactionsCount;
    }

    public void setReactionsCount(int reactionsCount) {
        this.reactionsCount = reactionsCount;
    }

    public List<MemoryComments> getComments() {
        return comments;
    }

    public void setComments(List<MemoryComments> comments) {
        this.comments = comments;
    }

    public List<Users> getTaggedUsers() {
        return taggedUsers;
    }

    public void setTaggedUsers(List<Users> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserReaction() {
        return userReaction;
    }

    public void setUserReaction(String userReaction) {
        this.userReaction = userReaction;
    }

    
}