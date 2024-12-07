package com.hackathon.uob.repository;

/**
 * Repository interface for managing Customer entities.
 * Provides methods to perform CRUD operations and custom queries on Customer entities.
 * Extends JpaRepository to leverage Spring Data JPA functionalities.
 *
 * @author nikhilesh chaurasia
 */
import com.hackathon.uob.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

    /**
     * Finds a customer by their mobile number.
     *
     * @param mobile the mobile number of the customer
     * @return the customer with the given mobile number
     */
    Customer findByMobile(String mobile);
}
