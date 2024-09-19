package com.learn.fullstack.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.learn.fullstack.customer.Customer;
import com.learn.fullstack.customer.CustomerRegistrationRequest;
import com.learn.fullstack.customer.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customers";

    @Test
    void canRegisterACustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID()+"@gmail.com";
        int age = RANDOM.nextInt(1,100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age
        );
        // sean a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just( request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get All customers
        List<Customer> customerList = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        // make sure that customer is there
        Customer expected = new Customer(name,email,age);

        assertThat(customerList)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);

        int id = customerList.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expected.setId(id);

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expected);

        // delete
        webTestClient.delete()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void canDeleteACustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID()+"@gmail.com";
        int age = RANDOM.nextInt(1,100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age
        );
        // sean a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just( request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get All customers
        List<Customer> customerList = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        int id = customerList.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // delete customer
        webTestClient.delete()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateACustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID()+"@gmail.com";
        int age = RANDOM.nextInt(1,100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age
        );
        // sean a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just( request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get All customers
        List<Customer> customerList = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        int id = customerList.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer
        String newName = "Nikul_Kumar";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(newName,null,null);

        webTestClient.put()
                .uri(CUSTOMER_URI+"/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just( updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        Customer customer = new Customer(id,newName,email,age);
        assertThat(updatedCustomer).isEqualTo(customer);

        // delete
        webTestClient.delete()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
