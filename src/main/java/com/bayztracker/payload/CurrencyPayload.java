package com.bayztracker.payload;

import com.bayztracker.model.Currency;

import java.util.Date;

public class CurrencyPayload {

    private long id;
    private String name;
    private String symbol;
    private Float currentPrice;
    private Date createdAt;

    public CurrencyPayload(long id, String name, String symbol, Float currentPrice, Date createdAt) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.createdAt = createdAt;
    }

    public static CurrencyPayload create(Currency currency) {
        return new CurrencyPayload(
                currency.getId(),
                currency.getName(),
                currency.getSymbol(),
                currency.getCurrentPrice(),
                currency.getCreatedAt()
        );
    }

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
}
