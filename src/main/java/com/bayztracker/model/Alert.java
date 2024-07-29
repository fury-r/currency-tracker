package com.bayztracker.model;

import com.bayztracker.utils.Status;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "alerts")
@SequenceGenerator(name = "seq_alert", sequenceName = "seq_alert",allocationSize = 1)
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_alert")
    private  long id;

    @OneToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="currency_id",nullable = false)
    private Currency currency;

    @Column
    private Float targetPrice;

    @Column
    private String status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = String.valueOf(status);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }


    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
