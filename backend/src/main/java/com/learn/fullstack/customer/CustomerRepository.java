package com.learn.fullstack.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    boolean existsCustomerByEmail(String email);

    Optional<Customer> getCustomerByEmail(String email);
}
