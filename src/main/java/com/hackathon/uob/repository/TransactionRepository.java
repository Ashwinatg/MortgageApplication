package com.hackathon.uob.repository;

/**
 * Repository interface for managing Transaction entities.
 * Provides methods to perform CRUD operations and custom queries on Transaction entities.
 * Extends JpaRepository to leverage Spring Data JPA functionalities.
 *
 * @author nikhilesh chaurasia
 */
import com.hackathon.uob.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(Long customerId);

    /**
     * Finds a list of transactions by the fromAccount or toAccount.
     *
     * @param fromAccount the account from which funds were transferred
     * @param toAccount the account to which funds were transferred
     * @return a list of transactions associated with the given fromAccount or toAccount
     */
    List<Transaction> findByFromAccountOrToAccount(String fromAccount, String toAccount);
}
