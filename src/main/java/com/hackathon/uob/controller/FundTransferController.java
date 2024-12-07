package com.hackathon.uob.controller;

/**
 * This controller handles fund transfer requests.
 * It provides an endpoint to transfer funds between accounts.
 *
 * @author nikhilesh chaurasia
 */

import com.hackathon.uob.dto.FundTransferRequest;
import com.hackathon.uob.service.FundTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fund-transfer")
public class FundTransferController {

    @Autowired
    private FundTransferService fundTransferService;

    /**
     * Endpoint to transfer funds between accounts.
     *
     * @param request the fund transfer request containing fromAccount, toAccount, amount, and remark
     * @return a ResponseEntity containing the response message and HTTP status
     */
    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@RequestBody FundTransferRequest request) {
        try {
            String response = fundTransferService.transferFunds(request.getFromAccount(), request.getToAccount(),
                    request.getAmount(), request.getRemark());
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /*
    @GetMapping("/test")
    public String testing() {
        return "Hello World";
    }
    */
}