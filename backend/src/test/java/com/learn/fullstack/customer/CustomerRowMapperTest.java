package com.learn.fullstack.customer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.getInt("id")).thenReturn(1);
        Mockito.when(rs.getString("name")).thenReturn("John Doe");
        Mockito.when(rs.getString("email")).thenReturn("n9g0I@example.com");
        Mockito.when(rs.getString("password")).thenReturn("password");
        Mockito.when(rs.getInt("age")).thenReturn(30);
        Mockito.when(rs.getString("gender")).thenReturn("MALE");

        // When
        Customer actual = customerRowMapper.mapRow(rs, 0);

        // Then
        Customer expected = new Customer(1, "John Doe", "n9g0I@example.com", "password", 30, Gender.MALE);
        assertThat(actual).isEqualTo(expected);
    }
}