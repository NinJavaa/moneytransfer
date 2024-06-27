package com.js.moneytransfer.service;

import com.js.moneytransfer.exception.ExchangeRateNotFoundException;

import java.math.BigDecimal;

public interface ExchangeRateService {
    BigDecimal getExchangeRate(String fromCurrency, String toCurrency) throws ExchangeRateNotFoundException;
}
