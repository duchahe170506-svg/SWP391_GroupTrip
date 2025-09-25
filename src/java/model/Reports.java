package model;

import java.sql.Timestamp;

public class Reports {

    private int report_id;
    private int trip_id;
    private int created_by;
    private String file_path;
    private String review;
    private Timestamp created_at;

    public Reports() {
    }

    public Reports(int report_id, int trip_id, int created_by, String file_path, String review, Timestamp created_at) {
        this.report_id = report_id;
        this.trip_id = trip_id;
        this.created_by = created_by;
        this.file_path = file_path;
        this.review = review;
        this.created_at = created_at;
    }

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Reports{" + "report_id=" + report_id + ", trip_id=" + trip_id + ", created_by=" + created_by + ", file_path=" + file_path + ", review=" + review + ", created_at=" + created_at + '}';
    }
}


