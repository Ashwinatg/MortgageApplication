package com.hackathon.uob.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class CustomerAccountSummary {

    private Long customerId;
    private String customerName;
    private List<AccountSummaryResponse> accountsSummary;

    public CustomerAccountSummary(Long customerId, String customerName, List<AccountSummaryResponse> accountsSummary) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.accountsSummary = accountsSummary;
    }

    public CustomerAccountSummary() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<AccountSummaryResponse> getAccountsSummary() {
        return accountsSummary;
    }

    public void setAccountsSummary(List<AccountSummaryResponse> accountsSummary) {
        this.accountsSummary = accountsSummary;
    }

    @Override
    public String toString() {
        return "CustomerAccountSummary{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", accountsSummary=" + accountsSummary +
                '}';
    }
}
