package com.bayztracker.controller;

import com.bayztracker.model.Currency;
import com.bayztracker.payload.CurrencyPayload;
import com.bayztracker.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    
    // get all enabled
    @GetMapping
    public ResponseEntity<List<CurrencyPayload>> getAllEnabled() {
        List<Currency> currencies = currencyService.getEnabledCurrencies();
        List<CurrencyPayload> payloads = currencies.stream().map(CurrencyPayload::create).collect(Collectors.toList());
        return ResponseEntity.ok(payloads);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CurrencyPayload>> getAllCurrencies() {
        List<Currency> currencies = currencyService.getAll();
        List<CurrencyPayload> payloads = currencies.stream().map(CurrencyPayload::create).collect(Collectors.toList());
        return ResponseEntity.ok(payloads);
    }
}
