package com.learn.fullstack;

import com.github.javafaker.Faker;
import com.learn.fullstack.customer.Customer;
import com.learn.fullstack.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            var faker = new Faker();
            Random random  = new Random();

            Customer customer = new Customer(
                    faker.name().fullName(),
                    faker.internet().safeEmailAddress(),
                    random.nextInt(16,99)

            );
            //customerRepository.save(customer);
        };
    }
}
/*
ConfigurableApplicationContext appContext = SpringApplication.run(Main.class,args);
Arrays.stream(appContext.getBeanDefinitionNames()).toList().forEach(System.out::println);
 */