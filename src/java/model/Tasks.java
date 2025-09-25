package model;

import java.math.BigDecimal;
import java.sql.Date;

public class Tasks {

    private int task_id;
    private int trip_id;
    private Integer assigned_to;
    private String description;
    private Date deadline;
    private BigDecimal estimated_cost;
    private String status;

    public Tasks() {
    }

    public Tasks(int task_id, int trip_id, Integer assigned_to, String description, Date deadline, BigDecimal estimated_cost, String status) {
        this.task_id = task_id;
        this.trip_id = trip_id;
        this.assigned_to = assigned_to;
        this.description = description;
        this.deadline = deadline;
        this.estimated_cost = estimated_cost;
        this.status = status;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public Integer getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(Integer assigned_to) {
        this.assigned_to = assigned_to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public BigDecimal getEstimated_cost() {
        return estimated_cost;
    }

    public void setEstimated_cost(BigDecimal estimated_cost) {
        this.estimated_cost = estimated_cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Tasks{" + "task_id=" + task_id + ", trip_id=" + trip_id + ", assigned_to=" + assigned_to + ", description=" + description + ", deadline=" + deadline + ", estimated_cost=" + estimated_cost + ", status=" + status + '}';
    }
}


