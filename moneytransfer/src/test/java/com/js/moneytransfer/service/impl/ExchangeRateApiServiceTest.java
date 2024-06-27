package com.js.moneytransfer.service.impl;

import com.js.moneytransfer.exception.ExchangeRateNotFoundException;
import com.js.moneytransfer.service.impl.ExchangeRateApiService.ExchangeRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class ExchangeRateApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Value("${exchange.rate.api.url}")
    private String apiUrl;

    @Value("${default.exchange.rate.api.key}")
    private String apiKey;

    private ExchangeRateApiService exchangeRateService;

    @BeforeEach
    void setUp() {
        exchangeRateService = new ExchangeRateApiService(restTemplate, apiUrl, apiKey);
    }

    @Test
    void getExchangeRate_ShouldReturnExchangeRateSuccessfully() {
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal expectedRate = new BigDecimal("0.85");

        String url = apiUrl.replace("{apiKey}", apiKey)
                .replace("{fromCurrency}", fromCurrency)
                .replace("{toCurrency}", toCurrency);

        ExchangeRateResponse response = new ExchangeRateResponse(
                "success",
                fromCurrency,
                toCurrency,
                expectedRate,
                System.currentTimeMillis() / 1000L + 3600
        );

        when(restTemplate.getForObject(url, ExchangeRateResponse.class)).thenReturn(response);

        BigDecimal result = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);

        assertEquals(expectedRate, result);
    }

    @Test
    void getExchangeRate_ShouldThrowExchangeRateNotFoundException() {
        String fromCurrency = "USD";
        String toCurrency = "EUR";

        String url = apiUrl.replace("{apiKey}", apiKey)
                .replace("{fromCurrency}", fromCurrency)
                .replace("{toCurrency}", toCurrency);

        ExchangeRateResponse response = new ExchangeRateResponse(
                "error",
                fromCurrency,
                toCurrency,
                null,
                0
        );

        when(restTemplate.getForObject(url, ExchangeRateResponse.class)).thenReturn(response);

        assertThrows(ExchangeRateNotFoundException.class, () -> {
            exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        });
    }
}
