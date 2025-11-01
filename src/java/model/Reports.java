package model;

import java.sql.Timestamp;
import java.util.List;

public class Reports {
    private int report_id;
    private int reporter_id;
    private Integer group_id;
    private Integer reported_user_id;
    private Integer removal_id;
    private String title;
    private String type;
    private String description;
    private String status;
    private String admin_response;
    private Integer reviewed_by;
    private Timestamp created_at;
    private Timestamp updated_at;

    private String reporter_name;
    private String reported_user_name;
    private String group_name;
    private List<String> attachments;

    public Reports() {
    }

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public int getReporter_id() {
        return reporter_id;
    }

    public void setReporter_id(int reporter_id) {
        this.reporter_id = reporter_id;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public Integer getReported_user_id() {
        return reported_user_id;
    }

    public void setReported_user_id(Integer reported_user_id) {
        this.reported_user_id = reported_user_id;
    }

    public Integer getRemoval_id() {
        return removal_id;
    }

    public void setRemoval_id(Integer removal_id) {
        this.removal_id = removal_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdmin_response() {
        return admin_response;
    }

    public void setAdmin_response(String admin_response) {
        this.admin_response = admin_response;
    }

    public Integer getReviewed_by() {
        return reviewed_by;
    }

    public void setReviewed_by(Integer reviewed_by) {
        this.reviewed_by = reviewed_by;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public String getReporter_name() {
        return reporter_name;
    }

    public void setReporter_name(String reporter_name) {
        this.reporter_name = reporter_name;
    }

    public String getReported_user_name() {
        return reported_user_name;
    }

    public void setReported_user_name(String reported_user_name) {
        this.reported_user_name = reported_user_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }
    
}
