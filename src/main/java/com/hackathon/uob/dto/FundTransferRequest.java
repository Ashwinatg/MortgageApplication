package com.hackathon.uob.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Data Transfer Object for fund transfer requests.
 * This class contains the details required to perform a fund transfer.
 * It includes the source account, destination account, transfer amount, and an optional remark.
 *
 * @author nikhilesh chaurasia
 */
@Data
@RequiredArgsConstructor
public class FundTransferRequest {

    private String fromAccount;
    private String toAccount;
    private Double amount;
    private String remark;

    /**
     * Constructs a new FundTransferRequest with the specified details.
     *
     * @param fromAccount the account from which funds will be transferred
     * @param toAccount the account to which funds will be transferred
     * @param amount the amount of funds to be transferred
     * @param remark an optional remark for the transfer
     */
    public FundTransferRequest(String fromAccount, String toAccount, Double amount, String remark) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.remark = remark;
    }

    /**
     * Gets the source account.
     *
     * @return the source account
     */
    public String getFromAccount() {
        return fromAccount;
    }

    /**
     * Sets the source account.
     *
     * @param fromAccount the source account
     */
    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    /**
     * Gets the destination account.
     *
     * @return the destination account
     */
    public String getToAccount() {
        return toAccount;
    }

    /**
     * Sets the destination account.
     *
     * @param toAccount the destination account
     */
    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    /**
     * Gets the transfer amount.
     *
     * @return the transfer amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the transfer amount.
     *
     * @param amount the transfer amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Gets the remark for the transfer.
     *
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Sets the remark for the transfer.
     *
     * @param remark the remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}