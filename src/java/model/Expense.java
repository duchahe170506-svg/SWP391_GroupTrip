package model;

import java.math.BigDecimal;
import java.util.Date;

public class Expense {
    private int expenseId;
    private int tripId;
    private int paidBy;             
    private BigDecimal amount;
    private String expenseType;    
    private String description;
    private String status;          
    private Date createdAt;
    private Date updatedAt;

 
    private String payerName;
    private String tripName;

   
    public Expense() {
    }

  
    public Expense(int expenseId, int tripId, int paidBy, BigDecimal amount,
                   String expenseType, String description, String status,
                   Date createdAt, Date updatedAt) {
        this.expenseId = expenseId;
        this.tripId = tripId;
        this.paidBy = paidBy;
        this.amount = amount;
        this.expenseType = expenseType;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    
    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(int paidBy) {
        this.paidBy = paidBy;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseId=" + expenseId +
                ", tripId=" + tripId +
                ", paidBy=" + paidBy +
                ", amount=" + amount +
                ", expenseType='" + expenseType + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", payerName='" + payerName + '\'' +
                ", tripName='" + tripName + '\'' +
                '}';
    }
}
