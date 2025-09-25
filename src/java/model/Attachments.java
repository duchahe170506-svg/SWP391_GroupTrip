package model;

import java.sql.Timestamp;

public class Attachments {

    private int attachment_id;
    private Integer message_id;
    private Integer comment_id;
    private String file_path;
    private Timestamp uploaded_at;

    public Attachments() {
    }

    public Attachments(int attachment_id, Integer message_id, Integer comment_id, String file_path, Timestamp uploaded_at) {
        this.attachment_id = attachment_id;
        this.message_id = message_id;
        this.comment_id = comment_id;
        this.file_path = file_path;
        this.uploaded_at = uploaded_at;
    }

    public int getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(int attachment_id) {
        this.attachment_id = attachment_id;
    }

    public Integer getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Integer message_id) {
        this.message_id = message_id;
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public Timestamp getUploaded_at() {
        return uploaded_at;
    }

    public void setUploaded_at(Timestamp uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    @Override
    public String toString() {
        return "Attachments{" + "attachment_id=" + attachment_id + ", message_id=" + message_id + ", comment_id=" + comment_id + ", file_path=" + file_path + ", uploaded_at=" + uploaded_at + '}';
    }
}


