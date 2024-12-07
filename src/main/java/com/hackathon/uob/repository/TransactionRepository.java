package com.hackathon.uob.repository;

import com.hackathon.uob.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(Long customerId);
    List<Transaction> findByFromAccountOrToAccount(String fromAccount, String toAccount);
}
