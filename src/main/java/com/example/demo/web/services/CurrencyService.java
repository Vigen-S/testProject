package com.example.demo.web.services;

import com.example.demo.web.httpclients.HttpClient;
import com.example.demo.web.models.Currency;
import com.example.demo.web.models.ListOfCurrencies;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyService {
    private final HttpClient httpClient;
    public ListOfCurrencies getAllCurrencies(){
        var map = httpClient.getCurrenciesFormInternet();

        var listOfCurrencies = new ArrayList<Currency>();
        for (var entry : map.entrySet()) {
            listOfCurrencies.add(new Currency(entry.getKey(), entry.getValue()));
        }
        return new ListOfCurrencies(LocalDate.now().toString(), listOfCurrencies);
    }

    public Currency getSpecificCurrency(String currency){
        var map = httpClient.getCurrenciesFormInternet();
        return new Currency(currency, map.get(currency));
    }
}
