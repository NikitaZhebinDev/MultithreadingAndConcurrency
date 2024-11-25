package com.epam.project.service;

import com.epam.project.dao.AccountDAO;
import com.epam.project.exception.AccountNotFoundException;
import com.epam.project.exception.ExchangeRateNotFoundException;
import com.epam.project.exception.InsufficientFundsException;
import com.epam.project.model.Account;
import com.epam.project.model.Currency;
import com.epam.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class CurrencyExchangeService {

    private static final Logger logger = Logger.getLogger(CurrencyExchangeService.class.getName());

    private final AccountDAO accountDAO = new AccountDAO();
    private final Map<String, ExchangeRate> exchangeRates = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    public void addExchangeRate(String fromCurrency, String toCurrency, BigDecimal rate) {
        exchangeRates.put(fromCurrency + "-" + toCurrency, new ExchangeRate(fromCurrency, toCurrency, rate));
        logger.info("Exchange rate added: " + fromCurrency + " to " + toCurrency + " = " + rate);
    }

    public void exchangeCurrency(String accountId, String fromCurrency, String toCurrency, BigDecimal amount) {
        logger.info("Starting exchange: " + fromCurrency + " -> " + toCurrency + " Amount: " + amount);
        lock.lock(); // thread-safety
        try {
            Account account = accountDAO.loadAccount(accountId);
            if (account == null) {
                logger.warning("Exchange failed: Account not found: " + accountId);
                throw new AccountNotFoundException("Account does not exist.");
            }
            Currency fromCurrencyObj = account.getCurrencies().get(fromCurrency);
            if (fromCurrencyObj == null || fromCurrencyObj.getAmount().compareTo(amount) < 0) {
                logger.warning("Exchange failed: Insufficient funds for " + fromCurrency + " in account: " + accountId);
                throw new InsufficientFundsException("Insufficient funds in the source currency.");
            }

            // fetch exch rate
            ExchangeRate rate = exchangeRates.get(fromCurrency + "-" + toCurrency);
            if (rate == null) {
                logger.warning("Exchange failed: Exchange rate not available for " + fromCurrency + " to " + toCurrency);
                throw new ExchangeRateNotFoundException("Exchange rate not available.");
            }

            // exchange
            BigDecimal exchangeAmount = amount.multiply(rate.getRate());
            fromCurrencyObj.setAmount(fromCurrencyObj.getAmount().subtract(amount));

            Currency toCurrencyObj = account.getCurrencies().get(toCurrency);
            if (toCurrencyObj == null) {
                toCurrencyObj = new Currency(toCurrency, BigDecimal.ZERO);
                account.addCurrency(toCurrencyObj);
            }
            toCurrencyObj.setAmount(toCurrencyObj.getAmount().add(exchangeAmount));

            accountDAO.saveAccount(account);

            logger.info("Exchange successful: " + amount + " " + fromCurrency + " to " + exchangeAmount + " " + toCurrency + " (exchange rate = " + rate.getRate() + ")");
        } catch (Exception e) {
            logger.severe("Error during exchange: " + e.getMessage());
        }finally {
            lock.unlock(); // always unlock to avoid deadlocks
        }
    }
}
