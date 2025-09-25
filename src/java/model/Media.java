package model;

import java.sql.Timestamp;

public class Media {

    private int media_id;
    private Integer trip_id;
    private Integer story_id;
    private int uploaded_by;
    private String file_path;
    private String media_type;
    private Timestamp uploaded_at;

    public Media() {
    }

    public Media(int media_id, Integer trip_id, Integer story_id, int uploaded_by, String file_path, String media_type, Timestamp uploaded_at) {
        this.media_id = media_id;
        this.trip_id = trip_id;
        this.story_id = story_id;
        this.uploaded_by = uploaded_by;
        this.file_path = file_path;
        this.media_type = media_type;
        this.uploaded_at = uploaded_at;
    }

    public int getMedia_id() {
        return media_id;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }

    public Integer getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Integer trip_id) {
        this.trip_id = trip_id;
    }

    public Integer getStory_id() {
        return story_id;
    }

    public void setStory_id(Integer story_id) {
        this.story_id = story_id;
    }

    public int getUploaded_by() {
        return uploaded_by;
    }

    public void setUploaded_by(int uploaded_by) {
        this.uploaded_by = uploaded_by;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public Timestamp getUploaded_at() {
        return uploaded_at;
    }

    public void setUploaded_at(Timestamp uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    @Override
    public String toString() {
        return "Media{" + "media_id=" + media_id + ", trip_id=" + trip_id + ", story_id=" + story_id + ", uploaded_by=" + uploaded_by + ", file_path=" + file_path + ", media_type=" + media_type + ", uploaded_at=" + uploaded_at + '}';
    }
}


