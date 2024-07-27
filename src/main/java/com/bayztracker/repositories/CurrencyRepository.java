package com.bayztracker.repositories;

import com.bayztracker.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    public List<Currency> findAllByEnabledIsTrue();
}
