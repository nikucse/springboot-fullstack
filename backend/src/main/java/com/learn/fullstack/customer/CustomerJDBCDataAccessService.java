package com.learn.fullstack.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
@RequiredArgsConstructor
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;
    @Override
    public List<Customer> getAllCustomers() {
        final String SQL = """
                SELECT id, name, email, password, age, gender FROM customer
                """;
        return jdbcTemplate.query(SQL, customerRowMapper);
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        final String SQL = """
                SELECT id, name, age, email, password, gender FROM customer where id = ?
                """;
        return jdbcTemplate.query(SQL, customerRowMapper,id).stream().findFirst();
    }

    @Override
    public void addCustomer(Customer customer) {
        final var SQL = "INSERT INTO customer(name, email, password, age, gender) VALUES(?,?,?,?,?)";
        jdbcTemplate
                .update(SQL,customer.getName(),customer.getEmail(),customer.getPassword(),customer.getAge(), customer.getGender().name());
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        final String SQL = """
                SELECT count(email) FROM customer where email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(SQL, Integer.class, email);

        System.out.println("Count ======= "+count);
        return count != null && count > 0;
    }

    @Override
    public boolean existsCustomerWithId(Integer id) {
        final String SQL = """
                SELECT count(id)  FROM customer where id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(SQL, Integer.class,id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Integer id) {

        final String SQL = """
                DELETE FROM customer where id = ?
                """;
        jdbcTemplate.update(SQL, id);

    }

    @Override
    public void updateCustomer(Customer customer) {

        if(customer.getName() != null){
            final String SQL = """
                    UPDATE customer SET name = ? WHERE id = ?
                    """;
            int result = jdbcTemplate.update(SQL,customer.getName(),customer.getId());
            System.out.println("update customer name result " + result);
        }
        if(customer.getAge() != null){
            final String SQL = """
                    UPDATE customer SET age = ? WHERE id = ?
                    """;
            int result = jdbcTemplate.update(SQL,customer.getAge(),customer.getId());
            System.out.println("update customer age result " + result);
        }

        if(customer.getEmail() != null){
            final String SQL = """
                    UPDATE customer SET email = ? WHERE id = ?
                    """;
            int result = jdbcTemplate.update(SQL,customer.getEmail(),customer.getId());
            System.out.println("update customer email result " + result);
        }
    }

    @Override
    public Optional<Customer> getUserByEmail(String email) {
        final String SQL = """
                SELECT id, name, age, email, password, gender FROM customer where email = ?
                """;
        return jdbcTemplate.query(SQL, customerRowMapper,email).stream().findFirst();
    }
}
