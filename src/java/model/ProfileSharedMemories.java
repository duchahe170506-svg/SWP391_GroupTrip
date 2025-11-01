package model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class ProfileSharedMemories {

    private int shareId;
    private int memoryId;
    private int sharedBy;
    private String privacy;
    private Timestamp sharedAt;

    private String title;
    private String content;
    private String imageUrl;

    private List<ProfileMemoryComments> comments;
    private Map<String, Integer> reactionsCountMap;
    private int reactionsCount;
    private String userReaction;
    
    private String tripName;

    public int getShareId() {
        return shareId;
    }

    public void setShareId(int shareId) {
        this.shareId = shareId;
    }

    public int getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(int memoryId) {
        this.memoryId = memoryId;
    }

    public int getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(int sharedBy) {
        this.sharedBy = sharedBy;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public Timestamp getSharedAt() {
        return sharedAt;
    }

    public void setSharedAt(Timestamp sharedAt) {
        this.sharedAt = sharedAt;
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

    public List<ProfileMemoryComments> getComments() {
        return comments;
    }

    public void setComments(List<ProfileMemoryComments> comments) {
        this.comments = comments;
    }

    public Map<String, Integer> getReactionsCountMap() {
        return reactionsCountMap;
    }

    public void setReactionsCountMap(Map<String, Integer> reactionsCountMap) {
        this.reactionsCountMap = reactionsCountMap;
    }

    public int getReactionsCount() {
        return reactionsCount;
    }

    public void setReactionsCount(int reactionsCount) {
        this.reactionsCount = reactionsCount;
    }

    public String getUserReaction() {
        return userReaction;
    }

    public void setUserReaction(String userReaction) {
        this.userReaction = userReaction;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
    
    
}
