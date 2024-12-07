package com.hackathon.uob.services;

import com.hackathon.uob.entity.Account;
import com.hackathon.uob.entity.Customer;
import com.hackathon.uob.exceptions.CustomerAccountNotFoundException;
import com.hackathon.uob.exceptions.CustomerNotFoundException;
import com.hackathon.uob.repo.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.hackathon.uob.utils.Constants.MSG_CUSTOMER_ACCOUNT_NOT_FOUND;
import static com.hackathon.uob.utils.Constants.MSG_CUSTOMER_NOT_FOUND;
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
    void shouldGetAccountSummaryWhenCustomerAccount() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Akash");
        customer.setEmail("ap983983@gmail.com");


        Account account = new Account();
        account.setId();
        customer.setAccounts(Set.of());

        given(customerRepository.findById(anyLong())).willReturn(Optional.of(customer));
        assertThrows(
                CustomerAccountNotFoundException.class,
                () -> accountSummaryService.getCustomerAccountSummary(anyLong()),
                MSG_CUSTOMER_ACCOUNT_NOT_FOUND);
    }
}