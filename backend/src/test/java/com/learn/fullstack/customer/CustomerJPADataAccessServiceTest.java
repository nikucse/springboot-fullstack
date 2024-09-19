package com.learn.fullstack.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;

    @Mock
    private CustomerRepository repository;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }


    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();

        // Then
        verify(repository).findAll();
    }

    @Test
    void getCustomerById() {
        // Given
        int id = 2;

        // When
        underTest.getCustomerById(id);

        // Then
        verify(repository).findById(id);
    }

    @Test
    void addCustomer() {
        // Given
        Customer customer = new Customer(1,"John Doe", "n9g0I@example.com", 30);

        // When
        underTest.addCustomer(customer);

        // Then
        verify(repository).save(customer);
    }

    @Test
    void existsCustomerWithEmail() {
        // Given
        String email = "nik@gmail.com" ;

        // When
        underTest.existsCustomerWithEmail(email);

        // Then
        verify(repository).existsCustomerByEmail(email);
    }

    @Test
    void existsCustomerWithId() {
        // Given
        int id = 1 ;

        // When
        underTest.existsCustomerWithId(id);

        // Then
        verify(repository).existsById(id);
    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 1 ;

        // When
        underTest.deleteCustomerById(id);

        // Then
        verify(repository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(1,"Nikul","nikul@gmail.com",32);
        // When
        underTest.updateCustomer(customer);

        // Then
        verify(repository).save(customer);
    }
}