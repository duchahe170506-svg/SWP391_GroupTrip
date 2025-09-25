package model;

import java.sql.Timestamp;

public class Stories {

    private int story_id;
    private int user_id;
    private String content;
    private Timestamp created_at;

    public Stories() {
    }

    public Stories(int story_id, int user_id, String content, Timestamp created_at) {
        this.story_id = story_id;
        this.user_id = user_id;
        this.content = content;
        this.created_at = created_at;
    }

    public int getStory_id() {
        return story_id;
    }

    public void setStory_id(int story_id) {
        this.story_id = story_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    @Override
    public String toString() {
        return "Stories{" + "story_id=" + story_id + ", user_id=" + user_id + ", content=" + content + ", created_at=" + created_at + '}';
    }
}


