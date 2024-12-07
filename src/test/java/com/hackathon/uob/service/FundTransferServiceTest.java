package com.hackathon.uob.service;
/**
 * @author nikhilesh chaurasia
 */
import com.hackathon.uob.entity.Transaction;
import com.hackathon.uob.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FundTransferServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private FundTransferService fundTransferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void transferFunds_SuccessfulTransaction() {
        String fromAccount = "123";
        String toAccount = "456";
        Double amount = 100.0;
        String remark = "Test transfer";

        when(accountService.getAccountBalance(fromAccount)).thenReturn(200.0);
        when(accountService.getAccountBalance(toAccount)).thenReturn(300.0);

        String result = fundTransferService.transferFunds(fromAccount, toAccount, amount, remark);

        assertEquals("Transaction successful", result);
        verify(accountService).updateAccountBalance(fromAccount, 100.0);
        verify(accountService).updateAccountBalance(toAccount, 400.0);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void transferFunds_InsufficientFunds() {
        String fromAccount = "123";
        String toAccount = "456";
        Double amount = 100.0;
        String remark = "Test transfer";

        when(accountService.getAccountBalance(fromAccount)).thenReturn(50.0);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                fundTransferService.transferFunds(fromAccount, toAccount, amount, remark));

        assertEquals("Insufficient funds in the from account", exception.getMessage());
        verify(accountService, never()).updateAccountBalance(anyString(), anyDouble());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}