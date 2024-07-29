package com.bayztracker.scheduler;

import com.bayztracker.exceptions.GlobalExceptionHandler;
import com.bayztracker.model.Alert;
import com.bayztracker.service.AlertService;
import com.bayztracker.service.CurrencyService;
import com.bayztracker.utils.DateFormatters;
import com.bayztracker.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Component
@Profile("!test")
public class AlertScheduler {

    private  final AlertService alertService;
    private static Logger LOG = LoggerFactory.getLogger(AlertScheduler.class);

    @Autowired
    public  AlertScheduler(AlertService alertService){
        this.alertService=alertService;

    }


    @Scheduled(fixedRate = 30000)
    public void checkIfAlertTrigger(){
        LOG.info("Checking if alerts are triggered");
                List<Alert> alerts=alertService.getAlertsOnStatus(Status.NEW);

        for(Alert alert:alerts){
            Float currentPrice=alert.getCurrency().getCurrentPrice();
            // Assumption: Triggered when  getTargetPrice is less than equals current currency price.
            if(alert.getTargetPrice()<=currentPrice){
                alertService.updateAlertStatus(alert.getId(),Status.TRIGGERED);
                LOG.info(" The target price was reached: Alert triggered for user ID: {} - Currency: {} - Target Price: {} - Current Price: {}", alert.getUser().getId(), alert.getCurrency().getName(), alert.getTargetPrice(), currentPrice);
            }

        }
    }

    // optional method
    // Can push the notification again if not acknowledged
    @Scheduled(fixedRate = 600000*60)
    public void chechIfNotificationAck(){
        List<Alert> alerts=alertService.getAlertsOnStatus(Status.TRIGGERED);

        for(Alert alert:alerts){
            Float currentPrice=alert.getCurrency().getCurrentPrice();
            Duration duration = Duration.between(DateFormatters.convertToLocalDateTime(alert.getUpdatedAt()), LocalDateTime.now());

            if(duration.toHours()>2){
                LOG.info("The target price was reached: Alert Notification for user ID: {} - Currency: {} - Target Price: {} - Current Price: {}", alert.getUser().getId(), alert.getCurrency().getName(), alert.getTargetPrice(), currentPrice);
            }

        }
    }
}
