package model;

import java.sql.Timestamp;

public class GroupRoleHistory {
    private int history_id;
    private int group_id;
    private int user_id;
    private String old_role;
    private String new_role;
    private int changed_by;
    private Timestamp changed_at;

    public GroupRoleHistory() {
    }

    public GroupRoleHistory(int history_id, int group_id, int user_id, String old_role, String new_role, int changed_by, Timestamp changed_at) {
        this.history_id = history_id;
        this.group_id = group_id;
        this.user_id = user_id;
        this.old_role = old_role;
        this.new_role = new_role;
        this.changed_by = changed_by;
        this.changed_at = changed_at;
    }

    public GroupRoleHistory(int group_id, int user_id, String old_role, String new_role, int changed_by) {
        this.group_id = group_id;
        this.user_id = user_id;
        this.old_role = old_role;
        this.new_role = new_role;
        this.changed_by = changed_by;
    }

    public int getHistory_id() {
        return history_id;
    }

    public void setHistory_id(int history_id) {
        this.history_id = history_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getOld_role() {
        return old_role;
    }

    public void setOld_role(String old_role) {
        this.old_role = old_role;
    }

    public String getNew_role() {
        return new_role;
    }

    public void setNew_role(String new_role) {
        this.new_role = new_role;
    }

    public int getChanged_by() {
        return changed_by;
    }

    public void setChanged_by(int changed_by) {
        this.changed_by = changed_by;
    }

    public Timestamp getChanged_at() {
        return changed_at;
    }

    public void setChanged_at(Timestamp changed_at) {
        this.changed_at = changed_at;
    }
}
