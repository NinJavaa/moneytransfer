package com.js.moneytransfer.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.js.moneytransfer.constants.AppConstants;
import com.js.moneytransfer.exception.ExchangeRateNotFoundException;
import com.js.moneytransfer.exception.UnsupportedCurrencyException;
import com.js.moneytransfer.service.ExchangeRateService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExchangeRateApiService implements ExchangeRateService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;

    private final Cache<String, CacheEntry> cache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    private final Set<String> supportedCurrencies = new HashSet<>();

    public ExchangeRateApiService(RestTemplate restTemplate,
                                  @Value("${exchange.rate.api.url}") String apiUrl,
                                  @Value("${default.exchange.rate.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    @PostConstruct
    public void fetchSupportedCurrencies() {
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/codes";
        SupportedCurrenciesResponse response = restTemplate.getForObject(url, SupportedCurrenciesResponse.class);
        if (response != null && "success".equals(response.result())) {
            for (String[] code : response.supportedCodes()) {
                supportedCurrencies.add(code[0]);
            }
        } else {
            throw new UnsupportedCurrencyException("Unable to fetch supported currencies from exchange rate API");
        }
    }

    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        Set<String> unsupportedCurrencies = getUnsupportedCurrencies(fromCurrency, toCurrency);
        if (!unsupportedCurrencies.isEmpty()) {
            throw new UnsupportedCurrencyException("Unsupported currencies: " + unsupportedCurrencies);
        }

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

    private Set<String> getUnsupportedCurrencies(String... currencies) {
        return Stream.of(currencies)
                .filter(currency -> !supportedCurrencies.contains(currency))
                .collect(Collectors.toSet());
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

    public record SupportedCurrenciesResponse(
            String result,
            String documentation,
            String termsOfUse,
            @JsonProperty("supported_codes") String[][] supportedCodes) {
    }
}
