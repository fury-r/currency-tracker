
package com.bayztracker.controller;

import com.bayztracker.data.AlertDetails;
import com.bayztracker.exceptions.GlobalExceptionHandler;
import com.bayztracker.model.Alert;
import com.bayztracker.model.User;
import com.bayztracker.payload.AlertPayload;
import com.bayztracker.service.AlertService;
import com.bayztracker.service.UserService;
import com.bayztracker.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alerts")
public class AlertController {
    private final AlertService alertService;
    private final UserService userService;
    private static   Logger LOG = LoggerFactory.getLogger(AlertController.class);

    public AlertController(AlertService alertService,UserService userService){
        this.alertService=alertService;
        this.userService=userService;

    }
    
    // Gets all the Alerts
    @GetMapping
    public ResponseEntity<List<AlertPayload>> getAllAlerts(){
        // Stringent check can be added as such feature should be only allowed for ROLE ADMIN
        // Other users should be only be able to see their own alerts
        List<Alert> alerts = alertService.getAllAlerts();
        List<AlertPayload> payloads = alerts.stream().map(AlertPayload::create).collect(Collectors.toList());

        return ResponseEntity.ok(payloads);
    }

    // gets all alerts for a user
    // user_id has been added to the url because no jwt auth
    // Can be modified to get alerts from the auth
    // Route name  can be kept as /user
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getAllAlertsForUser(@PathVariable("user_id") Long userId){
        User user=userService.getUser(userId);

        if(user==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User id is invalid");
        }

        List<Alert> alerts = alertService.getAllAlertsForUser(userId);
        List<AlertPayload> payloads = alerts.stream().map(AlertPayload::create).collect(Collectors.toList());

        return ResponseEntity.ok(payloads);
    }

    // get alert based on alert_id
    // This too can be improved by using auth so that users are validated to allow the users to see only their alerts
    @GetMapping("/{alert_id}")
    public ResponseEntity<?> getAlert(@PathVariable("alert_id") Long alertId){

        Alert alert = alertService.getAlert(alertId);

        if(alert!=null){
            return ResponseEntity.ok(AlertPayload.create(alert));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alert id is invalid");

    }

    // Alert creations
    @PostMapping
    public ResponseEntity<?> createAlert(@RequestBody AlertPayload alertPayload){

        if( alertPayload.getCurrency()==null || alertPayload.getCurrency().getId()==0){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("currency is missing");
        }

        // check can be removed if we have jwt auth then we can just get the id from the auth
        if( alertPayload.getUser()==null || alertPayload.getUser().getId()==0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user is missing");

        }

        AlertDetails alert=new AlertDetails(alertPayload.getUser().getId(),alertPayload.getCurrency().getId(),alertPayload.getTargetPrice(), Status.NEW);
        Alert saved=  alertService.createAlert(alert);
        return  ResponseEntity.ok(AlertPayload.create(saved));

    }


    // updating alert
    // can be secured so that users only update the alerts only created by them
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAlertById( @PathVariable("id")Long id, @RequestBody AlertPayload alertPayload){
        if(alertPayload.getCurrency()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("currency is missing");
        }
        if(alertPayload.getUser()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user is missing");

        }

        AlertDetails alert=new AlertDetails(alertPayload.getUser().getId(),alertPayload.getCurrency().getId(),alertPayload.getTargetPrice(), alertPayload.getStatus());
        Alert saved=  alertService.updateAlert(id,alert);
        return  ResponseEntity.ok(AlertPayload.create(saved));

    }

    // update alert by supplying everythin in body
    @PutMapping
    public ResponseEntity<?> updateAlert(@RequestBody AlertPayload alertPayload){
        try {
            if(alertPayload.getCurrency()==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("currency is missing");
            }
            if(alertPayload.getUser()==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user is missing");

            }

            AlertDetails alert=new AlertDetails(alertPayload.getUser().getId(),alertPayload.getCurrency().getId(),alertPayload.getTargetPrice(), Status.NEW);
            Alert saved=  alertService.updateAlert(alertPayload.getId(),alert);
            if(saved==null){
                return  ResponseEntity.badRequest().body("Alert does not exist.Failed to update.");
            }
            return  ResponseEntity.ok(AlertPayload.create(saved));
        }catch (Exception e){
            LOG.error(e.toString());
            return  ResponseEntity.badRequest().body("Failed to update");

        }
    }

    // update alert status
    // can be secured so that users only update the alerts only created by them
    @PutMapping("/status/{alert_id}")
    public ResponseEntity<?> updateStatus(@PathVariable("alert_id") Long id,@RequestBody AlertPayload alertPayload){
        if(Status.existsByName(alertPayload.getStatus().toString())){
            try{
                Alert alert=alertService.updateAlertStatus(id,alertPayload.getStatus());
                if(alert==null){
                    return ResponseEntity.badRequest().body("Id is invalid or event status id cannot be updated");
                }

                return  ResponseEntity.ok(alert);

            }catch (Exception e){
                LOG.error(e.toString());
                return  ResponseEntity.badRequest().body("Failed to update,"+e.toString());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status");
    }



    // delete alert via alert_id
    // can be secured so that users only delete the alerts only created by them
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAlert(@PathVariable("id")Long id){
        try{

            this.alertService.deleteAlert(id);
            return  ResponseEntity.ok("Alert deleted");

            }catch (EmptyResultDataAccessException e){
              return  ResponseEntity.notFound().build();
           }
          catch (Exception e){
              LOG.error(e.toString());
              return  ResponseEntity.badRequest().body("Failed to delete");
          }
    }

    // delete all alerts for user
    // can be secured so that users only delete the alerts only created by them.
    // should only accessible to admin
    @DeleteMapping("/user/{user_id}")
    public ResponseEntity<?> deleteAlertsForUser(@PathVariable("user_id")Long id){
        try{

            this.alertService.deleteAlertsForUser(id);
            return  ResponseEntity.ok("Alerts deleted");

        }catch (EmptyResultDataAccessException e){
            return  ResponseEntity.notFound().build();
        }
        catch (Exception e){
            LOG.error(e.toString());
            return  ResponseEntity.badRequest().body("Failed to delete");
        }
    }
}
