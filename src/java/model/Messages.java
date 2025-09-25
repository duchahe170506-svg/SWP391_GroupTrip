package model;

import java.sql.Timestamp;

public class Messages {

    private int message_id;
    private int sender_id;
    private int receiver_id;
    private String content;
    private Timestamp created_at;
    private boolean is_read;

    public Messages() {
    }

    public Messages(int message_id, int sender_id, int receiver_id, String content, Timestamp created_at, boolean is_read) {
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.content = content;
        this.created_at = created_at;
        this.is_read = is_read;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    @Override
    public String toString() {
        return "Messages{" + "message_id=" + message_id + ", sender_id=" + sender_id + ", receiver_id=" + receiver_id + ", content=" + content + ", created_at=" + created_at + ", is_read=" + is_read + '}';
    }
}


