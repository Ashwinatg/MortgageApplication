package com.hackathon.uob.services;

import com.hackathon.uob.dto.CustomerAccountSummary;
import com.hackathon.uob.entity.Account;
import com.hackathon.uob.entity.AccountType;
import com.hackathon.uob.entity.Customer;
import com.hackathon.uob.entity.Transaction;
import com.hackathon.uob.exceptions.CustomerAccountNotFoundException;
import com.hackathon.uob.exceptions.CustomerNotFoundException;
import com.hackathon.uob.repo.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static com.hackathon.uob.utils.Constants.MSG_CUSTOMER_ACCOUNT_NOT_FOUND;
import static com.hackathon.uob.utils.Constants.MSG_CUSTOMER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AccountSummaryServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountSummaryServiceImpl accountSummaryService;

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {

        given(customerRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(
                CustomerNotFoundException.class,
                () -> accountSummaryService.getCustomerAccountSummary(anyLong()),
                MSG_CUSTOMER_NOT_FOUND);
    }

    @Test
    void shouldThrowExceptionWhenCustomerAccountNotFound() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Akash");
        customer.setEmail("ap983983@gmail.com");
        customer.setAccounts(Set.of());
        given(customerRepository.findById(anyLong())).willReturn(Optional.of(customer));
        assertThrows(
                CustomerAccountNotFoundException.class,
                () -> accountSummaryService.getCustomerAccountSummary(anyLong()),
                MSG_CUSTOMER_ACCOUNT_NOT_FOUND);
    }

    @Test
    void shouldGetAccountSummaryWhenCustomerAccountFound() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Akash");
        customer.setEmail("ap983983@gmail.com");
        Set<Account> accounts = getAccounts();
        Set<Transaction> transaction = getTransaction();
        accounts.stream().findFirst().ifPresent(account -> account.setTransactions(transaction));
        customer.setAccounts(accounts);

        given(customerRepository.findById(anyLong())).willReturn(Optional.of(customer));

        Object customerAccountSummary = accountSummaryService.getCustomerAccountSummary(anyLong());
        CustomerAccountSummary customerAccountSummaryData = (CustomerAccountSummary) customerAccountSummary;
        assertThat(customerAccountSummaryData.getCustomerId()).isEqualTo(customer.getId());
    }

    private Set<Account> getAccounts() {
        Account savingAccount = new Account();
        savingAccount.setId(1L);
        savingAccount.setAccountNumber("00001111");
        savingAccount.setAccountBalance(100000.0);
        savingAccount.setAccountType(AccountType.SAVING);
        savingAccount.setCreatedTs(LocalDateTime.now());
        savingAccount.setUpdatedTs(LocalDateTime.now().plusHours(3));

        Account mortgageAccount = new Account();
        mortgageAccount.setId(2L);
        mortgageAccount.setAccountNumber("00002222");
        mortgageAccount.setAccountBalance(1000.0);
        mortgageAccount.setAccountType(AccountType.MORTGAGE);
        mortgageAccount.setCreatedTs(LocalDateTime.now());
        mortgageAccount.setUpdatedTs(LocalDateTime.now().plusHours(3));
        return Set.of(savingAccount, mortgageAccount);
    }

    private Set<Transaction> getTransaction() {

        Transaction t1 = new Transaction();
        t1.setId(11L);
        t1.setFromAccount("00001111");
        t1.setToAccount("00002222");
        t1.setAmount(2000.0);
        t1.setRemark("From saving to mortgae");
        t1.setCreatedTs(LocalDateTime.now());
        t1.setUpdatedTs(LocalDateTime.now());

        Transaction t2 = new Transaction();
        t2.setId(11L);
        t2.setFromAccount("00001111");
        t2.setToAccount("00002222");
        t2.setAmount(5000.0);
        t2.setRemark("From saving to mortgae");
        t2.setCreatedTs(LocalDateTime.now());
        t2.setUpdatedTs(LocalDateTime.now());
        return Set.of(t1, t2);
    }
}