package model;

import java.sql.Timestamp;

public class MemberRemovals {
    private int removal_id;
    private int group_id;
    private int removed_user_id;
    private int removed_by;
    private String reason;
    private Timestamp removed_at;

    private String removedUserName;
    private String removedByName;

    public MemberRemovals() {
    }

    public MemberRemovals(int removal_id, int group_id, int removed_user_id, int removed_by, String reason, Timestamp removed_at) {
        this.removal_id = removal_id;
        this.group_id = group_id;
        this.removed_user_id = removed_user_id;
        this.removed_by = removed_by;
        this.reason = reason;
        this.removed_at = removed_at;
    }

    public int getRemoval_id() {
        return removal_id;
    }

    public void setRemoval_id(int removal_id) {
        this.removal_id = removal_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getRemoved_user_id() {
        return removed_user_id;
    }

    public void setRemoved_user_id(int removed_user_id) {
        this.removed_user_id = removed_user_id;
    }

    public int getRemoved_by() {
        return removed_by;
    }

    public void setRemoved_by(int removed_by) {
        this.removed_by = removed_by;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getRemoved_at() {
        return removed_at;
    }

    public void setRemoved_at(Timestamp removed_at) {
        this.removed_at = removed_at;
    }

    public String getRemovedUserName() {
        return removedUserName;
    }

    public void setRemovedUserName(String removedUserName) {
        this.removedUserName = removedUserName;
    }

    public String getRemovedByName() {
        return removedByName;
    }

    public void setRemovedByName(String removedByName) {
        this.removedByName = removedByName;
    }
}
