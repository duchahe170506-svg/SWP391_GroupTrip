package model;

import java.sql.Timestamp;

public class Groups {

    private int group_id;
    private String name;
    private String description;
    private int leader_id;
    private Timestamp created_at;

    public Groups() {
    }

    public Groups(int group_id, String name, String description, int leader_id, Timestamp created_at) {
        this.group_id = group_id;
        this.name = name;
        this.description = description;
        this.leader_id = leader_id;
        this.created_at = created_at;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLeader_id() {
        return leader_id;
    }

    public void setLeader_id(int leader_id) {
        this.leader_id = leader_id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Groups{" + "group_id=" + group_id + ", name=" + name + ", description=" + description + ", leader_id=" + leader_id + ", created_at=" + created_at + '}';
    }
}


