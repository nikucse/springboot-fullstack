package com.learn.fullstack.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(Integer id);
    void addCustomer(Customer customer);
    boolean existsCustomerWithEmail(String email);

    boolean existsCustomerWithId(Integer id);

    void deleteCustomerById(Integer id);

    void updateCustomer(Customer customer);
}
