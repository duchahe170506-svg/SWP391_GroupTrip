package model;

import java.sql.Timestamp;

public class Activities {

    private int activity_id;
    private int trip_id;
    private String title;
    private String description;
    private String type;
    private int created_by;
    private Timestamp created_at;

    public Activities() {
    }

    public Activities(int activity_id, int trip_id, String title, String description, String type, int created_by, Timestamp created_at) {
        this.activity_id = activity_id;
        this.trip_id = trip_id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.created_by = created_by;
        this.created_at = created_at;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Activities{" + "activity_id=" + activity_id + ", trip_id=" + trip_id + ", title=" + title + ", description=" + description + ", type=" + type + ", created_by=" + created_by + ", created_at=" + created_at + '}';
    }
}


