package model;

public class ActivityVotes {

    private int activity_id;
    private int user_id;
    private String choice;

    public ActivityVotes() {
    }

    public ActivityVotes(int activity_id, int user_id, String choice) {
        this.activity_id = activity_id;
        this.user_id = user_id;
        this.choice = choice;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    @Override
    public String toString() {
        return "ActivityVotes{" + "activity_id=" + activity_id + ", user_id=" + user_id + ", choice=" + choice + '}';
    }
}


