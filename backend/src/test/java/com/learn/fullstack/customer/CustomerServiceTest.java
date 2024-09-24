package com.learn.fullstack.customer;

import com.learn.fullstack.exception.DuplicateResourceException;
import com.learn.fullstack.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();

        // Then
        verify(customerDao).getAllCustomers();
    }

    @Test
    void getCustomerById() {
        // Given
        int id = 1;
        Customer customer = new Customer(1, "John Doe", "n9g0I@example.com", 30);
        Mockito.when(customerDao.getCustomerById(id))
                .thenReturn(Optional.of(customer));
        // When
        Customer actual = underTest.getCustomerById(id);

        // Then
        assertThat(actual).isEqualTo(underTest.getCustomerById(id));

    }
    @Test
    void willThrowExceptionWhenCustomerNotFound() {
        // Given
        int id = 1;
        Mockito.when(customerDao.getCustomerById(id))
                .thenReturn(Optional.empty());
        // Then
        assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {
        // Given
        String email = "n9g0I@example.com";
        Mockito.when(customerDao.existsCustomerWithEmail(email))
                .thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("John Doe", email, 30);

        // When
        underTest.addCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).addCustomer(customerCaptor.capture());
        Customer capturedCustomer = customerCaptor.getValue();
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowExceptionWhenEmailAlreadyExist() {
        // Given
        String email = "n9g0I@example.com";
        Mockito.when(customerDao.existsCustomerWithEmail(email))
                .thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("John Doe", email, 30);

        // When
        assertThatThrownBy(()-> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already exist.");

        // Then
        verify(customerDao,never()).addCustomer(Mockito.any());
    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 1;
        Mockito.when(customerDao.existsCustomerWithId(id))
                .thenReturn(true);

        // When
        underTest.deleteCustomerById(id);

        // Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThrowDeleteExceptionWhenCustomerNotFound() {
        // Given
        int id = 1;
        Mockito.when(customerDao.existsCustomerWithId(id))
                .thenReturn(false);
        // When
        assertThatThrownBy(()-> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));
        // Then
        verify(customerDao,never()).deleteCustomerById(id);
    }

    @Test
    void updateCustomerById() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "John Doe", "n9g0I@example.com", 30);
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest request = new CustomerUpdateRequest("Jon Doe", "nabcI@example.com", 31);
        when(customerDao.existsCustomerWithEmail(request.email())).thenReturn(false);

        // When
        underTest.updateCustomerById(id, request);

        // Then
        verify(customerDao).updateCustomer(customer);
    }
}