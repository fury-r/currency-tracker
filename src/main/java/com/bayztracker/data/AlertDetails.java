package com.bayztracker.data;


import com.bayztracker.utils.Status;


public class AlertDetails {


    private Long userId;

    private Long currencyId;

    private Float targetPrice;

    private Status status=Status.NEW;


    public AlertDetails(Long userId, Long currencyId, Float targetPrice, Status status){
        this.userId=userId;
        this.currencyId=currencyId;
        this.status=status;
        this.targetPrice=targetPrice;

    }


    public Long getUserId() {
        return userId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public Float getTargetPrice() {
        return targetPrice;
    }

    public Status getStatus() {
        return status;
    }
}
