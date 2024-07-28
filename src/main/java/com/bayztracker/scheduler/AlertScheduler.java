package com.bayztracker.scheduler;

import com.bayztracker.exceptions.GlobalExceptionHandler;
import com.bayztracker.model.Alert;
import com.bayztracker.service.AlertService;
import com.bayztracker.service.CurrencyService;
import com.bayztracker.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Profile("!test")
public class AlertScheduler {

    private  final AlertService alertService;
    private  final CurrencyService currencyService;
    private static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    public  AlertScheduler(AlertService alertService, CurrencyService currencyService){
        this.alertService=alertService;
        this.currencyService=currencyService;

    }


    @Scheduled(fixedRate = 30000)
    public void checkIfAlertTrigger(){
        LOG.info("Checking if alerts are triggered");
                List<Alert> alerts=alertService.getNewAlerts();

        for(Alert alert:alerts){
                Float currentPrice=alert.getCurrency().getCurrentPrice();
            if(alert.getTargetPrice()<=currentPrice){
                alertService.updateAlertStatus(alert.getId(),Status.TRIGGERED);
                LOG.info("Alert triggered for user ID: {} - Currency: {} - Target Price: {} - Current Price: {}", alert.getUser().getId(), alert.getCurrency().getName(), alert.getTargetPrice(), currentPrice);
            }

        }
    }
}
