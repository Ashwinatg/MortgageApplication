package com.hackathon.uob.repo;

import com.hackathon.uob.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(Long customerId);
    List<Transaction> findByFromAccountOrToAccount(String fromAccount, String toAccount);
}
