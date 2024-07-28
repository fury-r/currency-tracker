package com.bayztracker.repositories;

import com.bayztracker.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert,Long> {

    @Query(value = "SELECT * FROM ALERTS WHERE USER_ID=?1",nativeQuery = true)
    List<Alert> getAllAlertsForUser(Long id);

    @Query(value = "SELECT * FROM ALERTS",nativeQuery = true)
    List<Alert> getAllAlerts( );


    @Query(value = "DELETE  FROM ALERTS where user_id=?id",nativeQuery = true)
    void deleteAlertByUserId( Long id);

    @Query(value = "SELECT * FROM alerts WHERE status='NEW'",nativeQuery = true)
    List<Alert> findAllNewAlerts();
}
