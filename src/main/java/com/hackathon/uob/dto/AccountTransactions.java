package com.hackathon.uob.dto;

import java.time.LocalDateTime;

public class AccountTransactions {

    private Long transactionId;
    private String fromAccount;
    private String toAccount;
    private Double amount;
    private String remark;
    private LocalDateTime date;

    public AccountTransactions() {
    }

    public AccountTransactions(Long transactionId, String fromAccount, String toAccount, Double amount, String remark, LocalDateTime date) {
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.remark = remark;
        this.date = date;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "AccountTransactions{" +
                "transactionId=" + transactionId +
                ", fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", amount=" + amount +
                ", remark='" + remark + '\'' +
                ", date=" + date +
                '}';
    }
}
