package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payments {

    private int payment_id;
    private int user_id;
    private Integer group_id;
    private BigDecimal amount;
    private String status;
    private Timestamp created_at;

    public Payments() {
    }

    public Payments(int payment_id, int user_id, Integer group_id, BigDecimal amount, String status, Timestamp created_at) {
        this.payment_id = payment_id;
        this.user_id = user_id;
        this.group_id = group_id;
        this.amount = amount;
        this.status = status;
        this.created_at = created_at;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Payments{" + "payment_id=" + payment_id + ", user_id=" + user_id + ", group_id=" + group_id + ", amount=" + amount + ", status=" + status + ", created_at=" + created_at + '}';
    }
}


