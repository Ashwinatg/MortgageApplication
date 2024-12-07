package com.hackathon.uob.service;

/**
 * Service class for managing Account operations.
 * Provides methods to fetch account details, get account balance, and update account balance.
 * Utilizes the AccountRepository to interact with the database.
 *
 * @author nikhilesh chaurasia
 */
import com.hackathon.uob.entity.Account;
import com.hackathon.uob.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Fetches an Account by its account number.
     *
     * @param accountNumber the account number
     * @return the Account associated with the given account number
     * @throws RuntimeException if the account is not found
     */
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found for account number: " + accountNumber));
    }

    /**
     * Fetches the balance of an Account by its account number.
     *
     * @param accountNumber the account number
     * @return the balance of the Account
     */
    public Double getAccountBalance(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        return account.getAccountBalance();
    }

    /**
     * Updates the balance of an Account.
     *
     * @param accountNumber the account number
     * @param newBalance the new balance to be set
     */
    public void updateAccountBalance(String accountNumber, Double newBalance) {
        Account account = getAccountByNumber(accountNumber);
        account.setAccountBalance(newBalance);
        accountRepository.save(account);
    }
}