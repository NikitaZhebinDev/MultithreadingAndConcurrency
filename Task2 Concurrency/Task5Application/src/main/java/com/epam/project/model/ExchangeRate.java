package com.epam.project.model;

import java.math.BigDecimal;

public class ExchangeRate {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;

    public ExchangeRate(String fromCurrency, String toCurrency, BigDecimal rate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }
}
