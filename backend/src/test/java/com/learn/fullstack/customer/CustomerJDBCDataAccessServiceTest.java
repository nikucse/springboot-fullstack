package com.learn.fullstack.customer;

import com.learn.fullstack.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();


    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(), customerRowMapper);
    }

    @Test
    void getAllCustomers() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-"+UUID.randomUUID(),
                "password", FAKER.number().numberBetween(16, 99),
                Gender.MALE
        );

        underTest.addCustomer(customer);

        // When
        List<Customer> customers = underTest.getAllCustomers();

        // Then
        assertThat(customers).isNotEmpty();
    }

    @Test
    void getCustomerById() {
        // Given
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-"+UUID.randomUUID();
        int age =  FAKER.number().numberBetween(16, 99);
        Customer customer = new Customer(
               name,email, "password", age, Gender.MALE
        );
        underTest.addCustomer(customer);

        Integer id = underTest.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // When
        Optional<Customer> foundCustomer = underTest.getCustomerById(id);

        // Then
        assertThat(foundCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(name);
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(age);
            assertThat(c.getGender()).isEqualTo(Gender.MALE);
        });
    }

    @Test
    void willReturnEmptyWhenNoCustomerById() {
        // Given
        int id = -1;
        // When
        var actual = underTest.getCustomerById(id);
        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void addCustomer() {
        // Given
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-"+UUID.randomUUID();
        int age =  FAKER.number().numberBetween(16, 99);
        Customer customer = new Customer(name,email, "password", age, Gender.MALE);
        underTest.addCustomer(customer);

        Integer id = underTest.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // When
        Optional<Customer> actual = underTest.getCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(name);
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(age);
            assertThat(c.getGender()).isEqualTo(Gender.MALE);
        });
    }

    @Test
    void existsCustomerWithEmail() {
        // Given
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-"+UUID.randomUUID();
        int age =  FAKER.number().numberBetween(16, 99);
        Customer customer = new Customer(name,email, "password", age, Gender.MALE);
        underTest.addCustomer(customer);

        // When
        boolean actual = underTest.existsCustomerWithEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithId() {
        // Given
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-"+UUID.randomUUID();
        int age =  FAKER.number().numberBetween(16, 99);
        Customer customer = new Customer(name,email, "password", age, Gender.MALE);
        underTest.addCustomer(customer);

        Integer id = underTest.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // When
        boolean actual = underTest.existsCustomerWithId(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void willReturnFalseWhenNoCustomerWithId() {
        // Given
        int id = -1;
        // When
        boolean actual = underTest.existsCustomerWithId(id);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        // Given
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-"+UUID.randomUUID();
        int age =  FAKER.number().numberBetween(16, 99);
        Customer customer = new Customer(name,email, "password", age, Gender.MALE);
        underTest.addCustomer(customer);

        Integer id = underTest.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // When
        underTest.deleteCustomerById(id);

        // Then
        Optional<Customer> actual = underTest.getCustomerById(id);
        assertThat(actual).isEmpty();
    }

    @Test
    void updateCustomerName() {
        // Given
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-"+UUID.randomUUID();
        int age =  FAKER.number().numberBetween(16, 99);
        Customer customer = new Customer(name,email, "password", age, Gender.MALE);
        underTest.addCustomer(customer);

        Integer id = underTest.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        String newName = FAKER.name().fullName();
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setName(newName);

        // When
        underTest.updateCustomer(updatedCustomer);
        // Then
        Optional<Customer> actual = underTest.getCustomerById(id);
        assertThat(actual).hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(age);
        });
    }

    @Test
    void updateCustomerEmail() {
        // Given
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-"+UUID.randomUUID();
        int age =  FAKER.number().numberBetween(16, 99);
        Customer customer = new Customer(name,email, "password", age, Gender.MALE);
        underTest.addCustomer(customer);

        Integer id = underTest.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        String newEmail = "test_email" + "-" + UUID.randomUUID();
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setEmail(newEmail);

        // When
        underTest.updateCustomer(updatedCustomer);

        // Then
        Optional<Customer> actual = underTest.getCustomerById(id);
        assertThat(actual).hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(name);
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(age);
        });
    }
}