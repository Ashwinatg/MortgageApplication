package com.hackathon.uob.service;

/**
 * Service class for handling fund transfers.
 * Provides methods to transfer funds between accounts and record the transaction.
 * Utilizes the AccountService to manage account balances and TransactionRepository to save transactions.
 *
 * @author nikhilesh chaurasia
 */
import com.hackathon.uob.entity.Transaction;
import com.hackathon.uob.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FundTransferService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Transfers funds from one account to another.
     *
     * @param fromAccount the account from which funds will be transferred
     * @param toAccount the account to which funds will be transferred
     * @param amount the amount of funds to be transferred
     * @param remark an optional remark for the transfer
     * @return a message indicating the success of the transaction
     * @throws RuntimeException if the from account has insufficient funds
     */
    public String transferFunds(String fromAccount, String toAccount, Double amount, String remark) {
        // Retrieve account balances using AccountService
        Double fromBalance = accountService.getAccountBalance(fromAccount);
        Double toBalance = accountService.getAccountBalance(toAccount);

        // Check if the balance is sufficient
        if (fromBalance < amount) {
            throw new RuntimeException("Insufficient funds in the from account");
        }

        // Update balances
        accountService.updateAccountBalance(fromAccount, fromBalance - amount);
        accountService.updateAccountBalance(toAccount, toBalance + amount);

        // Save the transaction
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setRemark(remark);
        transaction.setCreatedTs(LocalDateTime.now());
        transaction.setUpdatedTs(LocalDateTime.now());
        transaction.setCustomerId(1L);
        transactionRepository.save(transaction);

        return "Transaction successful";
    }
}