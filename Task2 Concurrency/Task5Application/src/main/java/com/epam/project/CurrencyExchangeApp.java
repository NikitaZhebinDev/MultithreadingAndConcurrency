package com.epam.project;

import com.epam.project.dao.AccountDAO;
import com.epam.project.model.Account;
import com.epam.project.model.Currency;
import com.epam.project.service.CurrencyExchangeService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrencyExchangeApp {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        CurrencyExchangeService service = new CurrencyExchangeService();

        // samples
        service.addExchangeRate("USD", "EUR", new BigDecimal("0.85"));
        service.addExchangeRate("EUR", "USD", new BigDecimal("1.18"));
        Account account = new Account("user1");
        account.addCurrency(new Currency("USD", new BigDecimal("1000")));
        account.addCurrency(new Currency("EUR", new BigDecimal("500")));

        AccountDAO accountDAO = new AccountDAO();
        try {
            accountDAO.saveAccount(account);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // simulate several concurrent exchanges
        executorService.submit(() -> {
            try {
                service.exchangeCurrency("user1", "USD", "EUR", new BigDecimal("100"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executorService.submit(() -> {
            try {
                service.exchangeCurrency("user1", "EUR", "USD", new BigDecimal("100"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
    }

}