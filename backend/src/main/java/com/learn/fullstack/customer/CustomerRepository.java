package com.learn.fullstack.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    boolean existsCustomerByEmail(String email);
}
