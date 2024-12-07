package com.hackathon.uob.dto;

import com.hackathon.uob.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class AccountSummaryResponse {

    private String accountNumber;
    private AccountType accountType;
    private Double availablebalance;
    private List<AccountTransactions> accountTransactions;

    public AccountSummaryResponse(String accountNumber, AccountType accountType, Double availablebalance, List<AccountTransactions> accountTransactions) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.availablebalance = availablebalance;
        this.accountTransactions = accountTransactions;
    }

    public AccountSummaryResponse() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Double getAvailablebalance() {
        return availablebalance;
    }

    public void setAvailablebalance(Double availablebalance) {
        this.availablebalance = availablebalance;
    }

    public List<AccountTransactions> getAccountTransactions() {
        return accountTransactions;
    }

    public void setAccountTransactions(List<AccountTransactions> accountTransactions) {
        this.accountTransactions = accountTransactions;
    }

    @Override
    public String toString() {
        return "AccountSummaryResponse{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", availablebalance=" + availablebalance +
                ", accountTransactions=" + accountTransactions +
                '}';
    }
}
