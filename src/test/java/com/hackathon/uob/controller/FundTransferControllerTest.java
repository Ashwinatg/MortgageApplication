package com.hackathon.uob.controller;
/**
 * @author nikhilesh chaurasia
 */
import com.hackathon.uob.dto.FundTransferRequest;
import com.hackathon.uob.service.FundTransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FundTransferControllerTest {

    @Mock
    private FundTransferService fundTransferService;

    @InjectMocks
    private FundTransferController fundTransferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void transferFunds_SuccessfulResponse() {
        FundTransferRequest request = new FundTransferRequest("123", "456", 100.0, "Test transfer");
        when(fundTransferService.transferFunds("123", "456", 100.0, "Test transfer")).thenReturn("Transaction successful");

        ResponseEntity<String> response = fundTransferController.transferFunds(request);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void transferFunds_InsufficientFundsResponse() {
        FundTransferRequest request = new FundTransferRequest("123", "456", 100.0, "Test transfer");
        when(fundTransferService.transferFunds("123", "456", 100.0, "Test transfer")).thenThrow(new RuntimeException("Insufficient funds in the from account"));

        ResponseEntity<String> response = fundTransferController.transferFunds(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Insufficient funds in the from account", response.getBody());
    }


    @Test
    void transferFunds_ZeroAmountResponse() {
        FundTransferRequest request = new FundTransferRequest("123", "456", 0.0, "Test transfer");
        when(fundTransferService.transferFunds("123", "456", 0.0, "Test transfer")).thenThrow(new RuntimeException("Amount must be greater than zero"));

        ResponseEntity<String> response = fundTransferController.transferFunds(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Amount must be greater than zero", response.getBody());
    }

    @Test
    void transferFunds_NullFromAccountResponse() {
        FundTransferRequest request = new FundTransferRequest(null, "456", 100.0, "Test transfer");
        when(fundTransferService.transferFunds(null, "456", 100.0, "Test transfer")).thenThrow(new RuntimeException("From account cannot be null"));

        ResponseEntity<String> response = fundTransferController.transferFunds(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("From account cannot be null", response.getBody());
    }

    @Test
    void transferFunds_NullToAccountResponse() {
        FundTransferRequest request = new FundTransferRequest("123", null, 100.0, "Test transfer");
        when(fundTransferService.transferFunds("123", null, 100.0, "Test transfer")).thenThrow(new RuntimeException("To account cannot be null"));

        ResponseEntity<String> response = fundTransferController.transferFunds(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("To account cannot be null", response.getBody());
    }

    @Test
    void transferFunds_NullRemarkResponse() {
        FundTransferRequest request = new FundTransferRequest("123", "456", 100.0, null);
        when(fundTransferService.transferFunds("123", "456", 100.0, null)).thenReturn("Transaction successful");

        ResponseEntity<String> response = fundTransferController.transferFunds(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Transaction successful", response.getBody());
    }

    @Test
    void transferFunds_EmptyFromAccountResponse() {
        FundTransferRequest request = new FundTransferRequest("", "456", 100.0, "Test transfer");
        when(fundTransferService.transferFunds("", "456", 100.0, "Test transfer")).thenThrow(new RuntimeException("From account cannot be empty"));

        ResponseEntity<String> response = fundTransferController.transferFunds(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("From account cannot be empty", response.getBody());
    }

    @Test
    void transferFunds_EmptyToAccountResponse() {
        FundTransferRequest request = new FundTransferRequest("123", "", 100.0, "Test transfer");
        when(fundTransferService.transferFunds("123", "", 100.0, "Test transfer")).thenThrow(new RuntimeException("To account cannot be empty"));

        ResponseEntity<String> response = fundTransferController.transferFunds(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("To account cannot be empty", response.getBody());
    }
}