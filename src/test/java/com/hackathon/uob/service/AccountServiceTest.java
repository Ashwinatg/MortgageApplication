package com.hackathon.uob.service;

import com.hackathon.uob.entity.Account;
import com.hackathon.uob.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAccountByNumber_returnsAccount_whenAccountExists() {
        String accountNumber = "12345";
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        Account result = accountService.getAccountByNumber(accountNumber);

        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
    }

    @Test
    void getAccountByNumber_throwsException_whenAccountDoesNotExist() {
        String accountNumber = "12345";
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.getAccountByNumber(accountNumber);
        });

        assertEquals("Account not found for account number: 12345", exception.getMessage());
    }

    @Test
    void getAccountBalance_returnsBalance_whenAccountExists() {
        String accountNumber = "12345";
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountBalance(1000.0);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        Double balance = accountService.getAccountBalance(accountNumber);

        assertNotNull(balance);
        assertEquals(1000.0, balance);
    }

    @Test
    void updateAccountBalance_updatesBalance_whenAccountExists() {
        String accountNumber = "12345";
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountBalance(1000.0);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        accountService.updateAccountBalance(accountNumber, 2000.0);

        verify(accountRepository, times(1)).save(account);
        assertEquals(2000.0, account.getAccountBalance());
    }
}