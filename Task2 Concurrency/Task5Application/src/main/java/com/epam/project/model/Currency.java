package com.epam.project.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code; // USD, EUR etc
    private BigDecimal amount;

    public Currency(String code, BigDecimal amount) {
        this.code = code;
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
