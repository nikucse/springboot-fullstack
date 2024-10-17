package com.learn.fullstack.auth;

import com.learn.fullstack.customer.CustomerDto;

public record AuthenticationResponse(String token, CustomerDto customerDto) {
}
