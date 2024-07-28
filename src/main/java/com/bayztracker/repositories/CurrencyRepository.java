package com.bayztracker.repositories;

import com.bayztracker.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query(value = "SELECT * FROM currencies WHERE enabled=true",nativeQuery = true)
    List<Currency> findAllByEnabledIsTrue();
}
