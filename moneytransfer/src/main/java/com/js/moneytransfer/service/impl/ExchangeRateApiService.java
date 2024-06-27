package com.js.moneytransfer.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.js.moneytransfer.constants.AppConstants;
import com.js.moneytransfer.exception.ExchangeRateNotFoundException;
import com.js.moneytransfer.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
public class ExchangeRateApiService implements ExchangeRateService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;

    private final Cache<String, CacheEntry> cache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    public ExchangeRateApiService(RestTemplate restTemplate,
                                  @Value("${exchange.rate.api.url}") String apiUrl,
                                  @Value("${default.exchange.rate.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        String cacheKey = fromCurrency + "_" + toCurrency;
        CacheEntry cacheEntry = cache.getIfPresent(cacheKey);

        if (cacheEntry != null && !isExpired(cacheEntry)) {
            return cacheEntry.rate();
        }

        String url = apiUrl.replace("{apiKey}", apiKey)
                .replace("{fromCurrency}", fromCurrency)
                .replace("{toCurrency}", toCurrency);

        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
        if (response != null && "success".equals(response.result())) {
            long expirationTime = response.timeNextUpdateUnix() * 1000L;
            cacheEntry = new CacheEntry(response.conversionRate(), expirationTime);
            cache.put(cacheKey, cacheEntry);
            return response.conversionRate();
        } else {
            throw new ExchangeRateNotFoundException(AppConstants.EXCHANGE_RATE_ERROR);
        }
    }

    private boolean isExpired(CacheEntry entry) {
        return System.currentTimeMillis() > entry.expirationTime();
    }

    public record ExchangeRateResponse(
            String result,
            @JsonProperty("base_code") String baseCode,
            @JsonProperty("target_code") String targetCode,
            @JsonProperty("conversion_rate") BigDecimal conversionRate,
            @JsonProperty("time_next_update_unix") long timeNextUpdateUnix) {
    }

    public record CacheEntry(BigDecimal rate, long expirationTime) {
    }
}
