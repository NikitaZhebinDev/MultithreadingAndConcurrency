package com.epam.project.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accountId;
    private Map<String, Currency> currencies;

    public Account(String accountId) {
        this.accountId = accountId;
        this.currencies = new HashMap<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public Map<String, Currency> getCurrencies() {
        return currencies;
    }

    public void addCurrency(Currency currency) {
        this.currencies.put(currency.getCode(), currency);
    }

}
