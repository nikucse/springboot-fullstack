package com.learn.fullstack.customer;

import com.learn.fullstack.exception.DuplicateResourceException;
import com.learn.fullstack.exception.RequestValidationException;
import com.learn.fullstack.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.learn.fullstack.customer.CustomerConstant.*;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDtoMapper customerDtoMapper;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, PasswordEncoder passwordEncoder, CustomerDtoMapper customerDtoMapper) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
        this.customerDtoMapper = customerDtoMapper;
    }

    public List<CustomerDto> getAllCustomers(){
        return customerDao.getAllCustomers()
                .stream()
                .map(customerDtoMapper)
                .collect(Collectors.toList());
    }

    public CustomerDto getCustomerById(Integer id){
        return customerDao.getCustomerById(id)
                .map(customerDtoMapper)
                .orElseThrow(()-> new ResourceNotFoundException(CUSTOMER_NOT_FOUND_MSG.formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest request){

        if(customerDao.existsCustomerWithEmail(request.email())){
            throw new DuplicateResourceException(CUSTOMER_ALREADY_EXIST_MSG);
        }

        customerDao.addCustomer(new Customer(request.name(), request.email(), passwordEncoder.encode(request.password()), request.age(), request.gender()));
    }

    public void deleteCustomerById(Integer customerId) {
        if(!customerDao.existsCustomerWithId(customerId)){
            throw new ResourceNotFoundException(CUSTOMER_NOT_FOUND_MSG.formatted(customerId));
        }
        customerDao.deleteCustomerById(customerId);
    }

    public Customer getCustomer(Integer id){
        return customerDao.getCustomerById(id)
                .orElseThrow(()-> new ResourceNotFoundException(CUSTOMER_NOT_FOUND_MSG.formatted(id)));
    }
    public void updateCustomerById(Integer customerId, CustomerUpdateRequest request) {
        Customer customer = getCustomer(customerId);
        boolean changes = false;

        if(request.name() != null && !request.name().equals(customer.getName())){
            customer.setName(request.name());
            changes = true;
        }
        if(request.email() != null && !request.email().equals(customer.getEmail())){
            if(customerDao.existsCustomerWithEmail(request.email())){
                throw new DuplicateResourceException(CUSTOMER_ALREADY_EXIST_MSG);
            }
            customer.setEmail(request.email());
            changes = true;
        }

        if(request.age() != null && !request.age().equals(customer.getAge())){
            customer.setAge(request.age());
            changes = true;
        }

        if(!changes)
            throw new RequestValidationException(NO_DATA_CHANGES_MSG);

        customerDao.updateCustomer(customer);
    }
}
