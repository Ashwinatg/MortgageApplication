package com.hackathon.uob.repo;

import com.hackathon.uob.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);
    Customer findByMobile(String mobile);
}
