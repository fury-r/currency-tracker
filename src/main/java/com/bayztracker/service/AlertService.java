package com.bayztracker.service;

import com.bayztracker.data.AlertDetails;
import com.bayztracker.model.Alert;
import com.bayztracker.model.Currency;
import com.bayztracker.model.User;
import com.bayztracker.repositories.AlertRepository;
import com.bayztracker.repositories.CurrencyRepository;
import com.bayztracker.repositories.UserRepository;
import com.bayztracker.utils.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    public AlertService(AlertRepository alertRepository,CurrencyRepository currencyRepository,UserRepository userRepository) {
        this.alertRepository = alertRepository;
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;

    }

    /* Create */
    @Transactional()
    public  Alert createAlert(AlertDetails alertDtls){
        Alert alert=new Alert();
        Optional<User> user=this.userRepository.findById(alertDtls.getUserId());
        Optional<Currency> currency=this.currencyRepository.findById(alertDtls.getCurrencyId());
        if(user.isPresent() && currency.isPresent()){
            alert.setUser(user.get());
            alert.setCurrency(currency.get());
            alert.setTargetPrice(alertDtls.getTargetPrice());
            alert.setStatus(Status.NEW);
            return alertRepository.save(alert);
        }

        throw new Error();

    }


    /* Read */
    @Transactional(readOnly = true)
    public List<Alert> getAllAlertsForUser(Long id) {
        return alertRepository.getAllAlertsForUser(id);
    }

    @Transactional(readOnly = true)
    public List<Alert> getAllAlerts( ) {
        return alertRepository.getAllAlerts();
    }


    /* Update */

    @Transactional()
    public  Alert updateAlert(Long id,AlertDetails alertDtls){
        Optional<Alert> findAlert=this.alertRepository.findById(id);
        if(findAlert.isPresent() ){
            Alert alert=findAlert.get();
            alert.setTargetPrice(alertDtls.getTargetPrice());
            alert.setStatus(alertDtls.getStatus());
            return  alertRepository.save(alert);
        }

        return  null;
    }

    @Transactional()
    public  Alert updateAlertStatus(Long id,Status status){
        Optional<Alert> findAlert=this.alertRepository.findById(id);

        if(findAlert.isPresent()){
            Alert alert=findAlert.get();
            String alertStatus=alert.getStatus();
            if (status == Status.CANCELLED && !alertStatus.equals(Status.NEW.toString())) {
                throw new IllegalStateException("Alert cannot be cancelled because it is not in the NEW state. Current status: " + alertStatus);
            }

            if (status == Status.ACKED &&  !alertStatus.equals(Status.TRIGGERED.toString())) {
                throw new IllegalStateException("Alert cannot be acknowledged because it is not in the TRIGGERED state. Current status: " + alertStatus);
            }
                alert.setStatus(status);
                return alertRepository.save(alert);

        }
        throw new IllegalArgumentException("Alert with id " + id + " does not exist.");
    }

    /* Delete */

    @Transactional()
    public  boolean deleteAlert(Long id){
        this.alertRepository.deleteById(id);
        Optional<Alert> findAlert=this.alertRepository.findById(id);
        return true;
    }


    @Transactional()
    public  boolean deleteAlertsForUser(Long userId){
        this.alertRepository.deleteAlertByUserId(userId);
        List<Alert> findAlert=this.alertRepository.getAllAlertsForUser(userId);

        if(findAlert.isEmpty()){
            return  true;
        }

        throw new Error();
    }



    @Transactional(readOnly = true)
    public  List<Alert> getAlertsOnStatus(Status status){
        return  alertRepository.findAlertsOnStatus(status.toString());
    }


    @Transactional()
    public Alert getAlert(Long id){
        Optional<Alert> alert=  alertRepository.findById(id);

        return alert.orElse(null);

    }
}
