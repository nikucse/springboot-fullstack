package com.learn.fullstack.auth;

import com.learn.fullstack.customer.Customer;
import com.learn.fullstack.customer.CustomerDto;
import com.learn.fullstack.customer.CustomerDtoMapper;
import com.learn.fullstack.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomerDtoMapper customerDtoMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationResponse login(AuthenticationRequest request ){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        Customer customer = (Customer) authentication.getPrincipal();
        CustomerDto customerDto = customerDtoMapper.apply(customer);
        String token = jwtUtil.issueToken(customerDto.userName(), customerDto.roles());
        return new AuthenticationResponse(token,customerDto);

    }
}
