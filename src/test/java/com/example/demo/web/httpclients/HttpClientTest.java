package com.example.demo.web.httpclients;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpClientTest {

    @Autowired
    HttpClient httpClient;

    @Test
    public void shouldReturnZippedFile() {
        var valuesFromInternet = httpClient.getCurrenciesFormInternet();
        Assertions.assertTrue(!valuesFromInternet.isEmpty());
    }

    //NO TIME TO WRITE MORE TESTS
}