package com.learn.fullstack.customer;

import java.util.List;

public record CustomerDto(
    Integer id,
    String name,
    String email,
    int age,
    Gender gender,
    List<String> roles,
    String userName
) {
}
