package com.learn.fullstack.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.learn.fullstack.auth.AuthenticationRequest;
import com.learn.fullstack.auth.AuthenticationResponse;
import com.learn.fullstack.customer.CustomerDto;
import com.learn.fullstack.customer.CustomerRegistrationRequest;
import com.learn.fullstack.customer.Gender;
import com.learn.fullstack.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationIntigrationTest {

        @Autowired
        private WebTestClient webTestClient;
        @Autowired
        private JWTUtil jwtUtil;

        private static final Random RANDOM = new Random();
        private static final String AUTH_PATH = "/api/v1/auth";
        private static final String CUSTOMER_PATH = "/api/v1/customers";

    @Test
    void canLogin() {
        // Given
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID()+"@gmail.com";
        int age = RANDOM.nextInt(1,100);
        Gender gender = age%2 == 0 ? Gender.MALE : Gender.FEMALE;

        String password = "password";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email, password, age, gender
        );

        AuthenticationRequest  authenticationRequest = new AuthenticationRequest(email, password);

        // sean a post request
        webTestClient.post()
                .uri(CUSTOMER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just( request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(AUTH_PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();

        String jwtToken = result.getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        CustomerDto customerDto = Objects.requireNonNull(result.getResponseBody()).customerDto();

        assertThat(jwtUtil.isTokenValid(jwtToken,customerDto.userName())).isTrue();

        assertThat(customerDto.email()).isEqualTo(email);
        assertThat(customerDto.age()).isEqualTo(age);
        assertThat(customerDto.name()).isEqualTo(name);
        assertThat(customerDto.userName()).isEqualTo(email);
        assertThat(customerDto.gender()).isEqualTo(gender);
        assertThat(customerDto.roles().get(0)).isEqualTo("ROLE_USER");
    }
}
