package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Refunds {

    private int refund_id;
    private int payment_id;
    private int user_id;
    private BigDecimal amount;
    private String reason;
    private String status;
    private Timestamp created_at;

    public Refunds() {
    }

    public Refunds(int refund_id, int payment_id, int user_id, BigDecimal amount, String reason, String status, Timestamp created_at) {
        this.refund_id = refund_id;
        this.payment_id = payment_id;
        this.user_id = user_id;
        this.amount = amount;
        this.reason = reason;
        this.status = status;
        this.created_at = created_at;
    }

    public int getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(int refund_id) {
        this.refund_id = refund_id;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
        return "Refunds{" + "refund_id=" + refund_id + ", payment_id=" + payment_id + ", user_id=" + user_id + ", amount=" + amount + ", reason=" + reason + ", status=" + status + ", created_at=" + created_at + '}';
    }
}


