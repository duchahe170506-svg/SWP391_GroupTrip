package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Expenses {

    private int expense_id;
    private int trip_id;
    private int paid_by;
    private BigDecimal amount;
    private String description;
    private Timestamp created_at;

    public Expenses() {
    }

    public Expenses(int expense_id, int trip_id, int paid_by, BigDecimal amount, String description, Timestamp created_at) {
        this.expense_id = expense_id;
        this.trip_id = trip_id;
        this.paid_by = paid_by;
        this.amount = amount;
        this.description = description;
        this.created_at = created_at;
    }

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public int getPaid_by() {
        return paid_by;
    }

    public void setPaid_by(int paid_by) {
        this.paid_by = paid_by;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Expenses{" + "expense_id=" + expense_id + ", trip_id=" + trip_id + ", paid_by=" + paid_by + ", amount=" + amount + ", description=" + description + ", created_at=" + created_at + '}';
    }
}


