package com.bayztracker.payload;

import com.bayztracker.model.Alert;
import com.bayztracker.model.Currency;
import com.bayztracker.model.User;
import com.bayztracker.utils.Status;

import java.util.Date;

public class AlertPayload {

    private long id;
    private User user;
    private Currency currency;
    private Float targetPrice;
    private Status status;
    private Date createdAt;

    public AlertPayload(long id, User user,Currency currency, Float targetPrice, Status status, Date createdAt) {
        this.id = id;
        this.user = user;
        this.currency = currency;
        this.targetPrice = targetPrice;
        this.status=status;
        this.createdAt = createdAt;
    }

    public static AlertPayload create(Alert alert) {
        return new AlertPayload(
                alert.getId(),
                alert.getUser(),
                alert.getCurrency(),
                alert.getTargetPrice(),
                Status.valueOf(alert.getStatus()),
                alert.getCreatedAt()
        );
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Float getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(Float targetPrice) {
        this.targetPrice = targetPrice;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
