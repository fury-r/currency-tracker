package com.bayztracker.model;


import javax.persistence.*;
import java.util.Date;

@Entity(name = "currencies")
@SequenceGenerator(name = "seq_currency", sequenceName = "seq_currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_currency")
    private long id;

    @Column
    private String name;

    @Column
    private String symbol;

    @Column
    private Float currentPrice;

    @Column
    private Date createdAt;

    @Column
    private Boolean enabled;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
