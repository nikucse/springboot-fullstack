package com.learn.fullstack.customer;

import com.learn.fullstack.exception.DuplicateResourceException;
import com.learn.fullstack.exception.RequestValidationException;
import com.learn.fullstack.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.getAllCustomers();
    }

    public Customer getCustomerById(Integer id){
        return customerDao.getCustomerById(id)
                .orElseThrow(()-> new ResourceNotFoundException("customer with id [%s] not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest request){

        if(customerDao.existsCustomerWithEmail(request.email())){
            throw new DuplicateResourceException("email already exist.");
        }

        customerDao.addCustomer(new Customer(request.name(), request.email(), request.age()));
    }

    public void deleteCustomerById(Integer customerId) {
        if(!customerDao.existsCustomerWithId(customerId)){
            throw new ResourceNotFoundException("customer with id [%s] not found".formatted(customerId));
        }
        customerDao.deleteCustomerById(customerId);
    }

    public Customer getCustomer(Integer id){
        return customerDao.getCustomerById(id)
                .orElseThrow(()-> new ResourceNotFoundException("customer with id [%s] not found".formatted(id)));
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
                throw new DuplicateResourceException("email already taken");
            }
            customer.setEmail(request.email());
            changes = true;
        }

        if(request.age() != null && !request.age().equals(customer.getAge())){
            customer.setAge(request.age());
            changes = true;
        }

        if(!changes)
            throw new RequestValidationException("no data changes found");

        customerDao.updateCustomer(customer);
    }
}
