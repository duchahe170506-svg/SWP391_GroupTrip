package model;

import java.util.Date;

public class TripImages {
    private int imageId;
    private int tripId;
    private String imageUrl;
    private Date createdAt;

    // Constructor mặc định
    public TripImages() {}

    // Constructor đầy đủ
    public TripImages(int imageId, int tripId, String imageUrl, Date createdAt) {
        this.imageId = imageId;
        this.tripId = tripId;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    // Getter & Setter
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
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

    @Override
    public String toString() {
        return "TripImages{" +
                "imageId=" + imageId +
                ", tripId=" + tripId +
                ", imageUrl='" + imageUrl + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
