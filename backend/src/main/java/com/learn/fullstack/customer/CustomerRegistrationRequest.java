package com.learn.fullstack.customer;

public record CustomerRegistrationRequest(String name, String email, String password, int age, Gender gender) {
}
