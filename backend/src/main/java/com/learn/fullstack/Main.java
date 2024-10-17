package com.learn.fullstack;

import com.github.javafaker.Faker;
import com.learn.fullstack.customer.Customer;
import com.learn.fullstack.customer.CustomerRepository;
import com.learn.fullstack.customer.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);

    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository , PasswordEncoder passwordEncoder) {
        return args -> {
            var faker = new Faker();
            Random random = new Random();

            for (int i = 0; i < 3; i++) {
            Customer customer = new Customer(
                    faker.name().fullName(),
                    faker.internet().safeEmailAddress(),
                    passwordEncoder.encode(UUID.randomUUID().toString()), random.nextInt(16, 99),
                    Gender.MALE

            );
           //customerRepository.save(customer);

            }
        };
    }
}
/*
ConfigurableApplicationContext appContext = SpringApplication.run(Main.class,args);
Arrays.stream(appContext.getBeanDefinitionNames()).toList().forEach(System.out::println);
 */