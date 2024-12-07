package com.hackathon.uob.repository;

/**
 * Repository interface for managing Account entities.
 * Provides methods to perform CRUD operations and custom queries on Account entities.
 * Extends JpaRepository to leverage Spring Data JPA functionalities.
 *
 * @author nikhilesh chaurasia
 */
import com.hackathon.uob.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Finds a list of accounts by the customer ID.
     *
     * @param customerId the ID of the customer
     * @return a list of accounts associated with the given customer ID
     */
    List<Account> findByCustomerId(Long customerId);

    /**
     * Finds an account by its ID.
     *
     * @param aLong the ID of the account
     * @return an Optional containing the account if found, or empty if not found
     */
    @Override
    Optional<Account> findById(Long aLong);

    /**
     * Finds an account by its account number.
     *
     * @param accountNumber the account number
     * @return an Optional containing the account if found, or empty if not found
     */
    Optional<Account> findByAccountNumber(String accountNumber);
}