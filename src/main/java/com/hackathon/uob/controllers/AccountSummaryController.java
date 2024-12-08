package com.hackathon.uob.controllers;

import com.hackathon.uob.services.AccountSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/accounts")
public class AccountSummaryController {

    private final AccountSummaryService accountSummaryService;

    public AccountSummaryController(AccountSummaryService accountSummaryService) {
        this.accountSummaryService=accountSummaryService;
    }

    @GetMapping("/summary/{customerId}")
    public ResponseEntity<Object> getCustomerAccountSummary(@PathVariable("customerId") Long customerId) {
        return ResponseEntity.ok(accountSummaryService.getCustomerAccountSummary(customerId));
    }
}
