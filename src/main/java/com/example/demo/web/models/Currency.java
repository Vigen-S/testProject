package com.example.demo.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Currency(@JsonProperty("currency") String currencyName, @JsonProperty("value") Double currencyValue) {
}
