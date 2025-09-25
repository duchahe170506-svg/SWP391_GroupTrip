package model;

import java.sql.Date;

public class Subscriptions {

    private int subscription_id;
    private int user_id;
    private Date start_date;
    private Date end_date;
    private String status;

    public Subscriptions() {
    }

    public Subscriptions(int subscription_id, int user_id, Date start_date, Date end_date, String status) {
        this.subscription_id = subscription_id;
        this.user_id = user_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
    }

    public int getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(int subscription_id) {
        this.subscription_id = subscription_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Subscriptions{" + "subscription_id=" + subscription_id + ", user_id=" + user_id + ", start_date=" + start_date + ", end_date=" + end_date + ", status=" + status + '}';
    }
}


