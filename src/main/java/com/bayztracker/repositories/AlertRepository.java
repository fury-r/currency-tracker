package com.bayztracker.repositories;

import com.bayztracker.model.Alert;
import com.bayztracker.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert,Long> {

    @Query(value = "SELECT * FROM ALERTS WHERE user_id=?1",nativeQuery = true)
    List<Alert> getAllAlertsForUser(Long id);

    @Query(value = "SELECT * FROM ALERTS",nativeQuery = true)
    List<Alert> getAllAlerts( );


    @Query(value = "DELETE FROM ALERTS where user_id=?1",nativeQuery = true)
    void deleteAlertByUserId(Long id);

    @Query(value = "SELECT * FROM alerts WHERE status=?1",nativeQuery = true)
    List<Alert> findAlertsOnStatus(String status);
}
