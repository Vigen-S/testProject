package com.example.demo.web.controllers;

import com.example.demo.web.models.Currency;
import com.example.demo.web.models.ListOfCurrencies;
import com.example.demo.web.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping(path = "/currencies")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<ListOfCurrencies> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping(path = "/{currencyName}")
    public ResponseEntity<Currency> getSpecificCurrency(@PathVariable("currencyName") final String currencyName) {
        return ResponseEntity.ok(currencyService.getSpecificCurrency(currencyName));
    }
}
