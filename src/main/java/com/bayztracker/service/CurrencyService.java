package com.bayztracker.service;

import com.bayztracker.model.Currency;
import com.bayztracker.repositories.CurrencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Transactional(readOnly = true)
    public List<Currency> getEnabledCurrencies() {
        return currencyRepository.findAllByEnabledIsTrue();
    }
    @Transactional(readOnly = true)
    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }
}
