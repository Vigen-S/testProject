package com.example.demo.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ListOfCurrencies(@JsonProperty("date") String validityDate, List<Currency> currencyList) {
}
