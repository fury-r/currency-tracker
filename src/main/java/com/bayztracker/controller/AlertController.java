
package com.bayztracker.controller;

import com.bayztracker.data.AlertDetails;
import com.bayztracker.exceptions.GlobalExceptionHandler;
import com.bayztracker.model.Alert;
import com.bayztracker.payload.AlertPayload;
import com.bayztracker.service.AlertService;
import com.bayztracker.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alerts")
public class AlertController {
    private final AlertService alertService;
    public AlertController(AlertService alertService){this.alertService=alertService;}
    private static   Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @GetMapping
    public ResponseEntity<List<AlertPayload>> getAllAlerts(){

        List<Alert> alerts = alertService.getAllAlerts();
        List<AlertPayload> payloads = alerts.stream().map(AlertPayload::create).collect(Collectors.toList());
        return ResponseEntity.ok(payloads);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<List<AlertPayload>> getAllAlertsForUser(@PathVariable("user_id") Long userId){

        List<Alert> alerts = alertService.getAllAlertsForUser(userId);
        List<AlertPayload> payloads = alerts.stream().map(AlertPayload::create).collect(Collectors.toList());
        return ResponseEntity.ok(payloads);
    }


    @PostMapping
    public ResponseEntity<?> createAlert(@RequestBody AlertPayload alertPayload){
        if(alertPayload.getCurrency()==null){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("currency is missing");
        }
        if(alertPayload.getUser()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user is missing");

        }

        AlertDetails alert=new AlertDetails(alertPayload.getUser().getId(),alertPayload.getCurrency().getId(),alertPayload.getTargetPrice(), Status.NEW);

        Alert saved=  alertService.createAlert(alert);
        return  ResponseEntity.ok(AlertPayload.create(saved));

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAlertById( @PathVariable("id")Long id, @RequestBody AlertPayload alertPayload){
        if(alertPayload.getCurrency()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("currency is missing");
        }
        if(alertPayload.getUser()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user is missing");

        }

        AlertDetails alert=new AlertDetails(alertPayload.getUser().getId(),alertPayload.getCurrency().getId(),alertPayload.getTargetPrice(), Status.NEW);
        Alert saved=  alertService.updateAlert(id,alert);
        return  ResponseEntity.ok(AlertPayload.create(saved));

    }

    @PutMapping
    public ResponseEntity<?> updateAlert(  @RequestBody AlertPayload alertPayload){
        try {
            if(alertPayload.getCurrency()==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("currency is missing");
            }
            if(alertPayload.getUser()==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user is missing");

            }

            AlertDetails alert=new AlertDetails(alertPayload.getUser().getId(),alertPayload.getCurrency().getId(),alertPayload.getTargetPrice(), Status.NEW);
            Alert saved=  alertService.updateAlert(alertPayload.getId(),alert);
            return  ResponseEntity.ok(AlertPayload.create(saved));
        }catch (Exception e){
            LOG.error(e.toString());
            return  ResponseEntity.badRequest().body("Failed to update,"+e.toString());

        }
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAlert(@PathVariable("id")Long id){
          try{
            this.alertService.deleteAlert(id);
            return  ResponseEntity.ok("Alert deleted");
        }catch (Exception e){
              LOG.error(e.toString());
              return  ResponseEntity.badRequest().body("Failed to delete,"+e.toString());
          }
    }


    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Long id,@RequestBody AlertPayload alertPayload){
        if(Status.existsByName(alertPayload.getStatus().toString())){
            return  ResponseEntity.ok(alertService.updateAlertStatus(id,alertPayload.getStatus()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status");
    }


    @PutMapping("/status/{id}/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Long id,@PathVariable("status") String status){
        if(Status.existsByName(status)){
            return  ResponseEntity.ok(alertService.updateAlertStatus(id,Status.valueOf(status)));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status");
    }

}
