package com.hackathon.uob.services;

import com.hackathon.uob.dto.AccountSummaryResponse;
import com.hackathon.uob.dto.AccountTransactions;
import com.hackathon.uob.dto.CustomerAccountSummary;
import com.hackathon.uob.entity.Account;
import com.hackathon.uob.entity.Customer;
import com.hackathon.uob.exceptions.CustomerAccountNotFoundException;
import com.hackathon.uob.exceptions.CustomerNotFoundException;
import com.hackathon.uob.repo.AccountRepository;
import com.hackathon.uob.repo.CustomerRepository;
import com.hackathon.uob.repo.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.hackathon.uob.utils.Constants.MSG_CUSTOMER_ACCOUNT_NOT_FOUND;
import static com.hackathon.uob.utils.Constants.MSG_CUSTOMER_NOT_FOUND;

@Service
public class AccountSummaryServiceImpl implements AccountSummaryService {

    private final CustomerRepository customerRepository;

    public AccountSummaryServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public Object getCustomerAccountSummary(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(MSG_CUSTOMER_NOT_FOUND));

        Set<Account> accounts = customer.getAccounts();

        if (accounts.isEmpty()) {
            throw new CustomerAccountNotFoundException(MSG_CUSTOMER_ACCOUNT_NOT_FOUND);
        }

        List<AccountSummaryResponse> accountSummaryList = new ArrayList<>();
        accounts.stream().filter(Objects::nonNull).forEach(account -> {
            AccountSummaryResponse accountSummaryResponse = new AccountSummaryResponse();
            accountSummaryResponse.setAccountNumber(account.getAccountNumber());
            accountSummaryResponse.setAccountType(account.getAccountType());
            accountSummaryResponse.setAvailablebalance(account.getAccountBalance());
            List<AccountTransactions> accountTransactions = new ArrayList<>();
            if (null != account.getTransactions()) {
               account.getTransactions().stream()
                       .filter(Objects::nonNull).forEach(transaction -> {
                           AccountTransactions transactions = new AccountTransactions();
                           transactions.setTransactionId(transaction.getId());
                           transactions.setFromAccount(transaction.getFromAccount());
                           transactions.setToAccount(transaction.getToAccount());
                           transactions.setAmount(transaction.getAmount());
                           transactions.setRemark(transaction.getRemark());
                           transactions.setDate(transaction.getCreatedTs());
                           accountTransactions.add(transactions);
                       });
           }
            accountSummaryResponse.setAccountTransactions(accountTransactions);
            accountSummaryList.add(accountSummaryResponse);
        });

        CustomerAccountSummary customerAccountSummary = new CustomerAccountSummary();
        customerAccountSummary.setCustomerId(customer.getId());
        customerAccountSummary.setCustomerName(customer.getName());
        customerAccountSummary.setAccountsSummary(accountSummaryList);
        return customerAccountSummary;

    }
}
