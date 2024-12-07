package com.hackathon.uob.services;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountSummaryService {

    Object getCustomerAccountSummary(Long customerId);

}
