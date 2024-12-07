package com.hackathon.uob.repo;

import com.hackathon.uob.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);
    Customer findByMobile(String mobile);
    Optional<Customer> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);

}
