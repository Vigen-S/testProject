package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Configuration
public class RestConfiguration {
    @Bean
    public RestTemplate restTemplate(List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors) {
        return new RestTemplate();
    }
}
