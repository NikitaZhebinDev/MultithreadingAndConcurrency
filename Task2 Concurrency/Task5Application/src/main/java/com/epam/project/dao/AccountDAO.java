package com.epam.project.dao;

import com.epam.project.model.Account;

import java.io.*;
import java.util.logging.Logger;

public class AccountDAO {

    private static final Logger logger = Logger.getLogger(AccountDAO.class.getName());

    private static final String FILE_DIRECTORY = "./accounts/";
    private static final String FILE_FORMAT = ".txt";

    public AccountDAO() {
        File directory = new File(FILE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs(); // create directory if it doesnt exist
            logger.info("Created directory: " + FILE_DIRECTORY);
        }
    }

    public AccountDAO(String tempDirectory) {
        File directory = new File(tempDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
            logger.info("Created directory: " + tempDirectory);
        }
    }

    public void saveAccount(Account account) throws IOException {
        File file = new File(FILE_DIRECTORY + account.getAccountId() + FILE_FORMAT);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(account);
            logger.info("Account saved: " + account.getAccountId());
        } catch (IOException e) {
            logger.severe("Failed to save account: " + account.getAccountId() + " Error: " + e.getMessage());
            throw e;
        }
    }

    public Account loadAccount(String accountId) throws IOException, ClassNotFoundException {
        File file = new File(FILE_DIRECTORY + accountId + FILE_FORMAT);
        if (!file.exists()) {
            logger.warning("Account not found: " + accountId);
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            logger.info("Account loaded: " + accountId);
            return (Account) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("Failed to load account: " + accountId + " Error: " + e.getMessage());
            throw e;
        }
    }
}
