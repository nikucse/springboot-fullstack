package com.learn.fullstack;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbstractTestContainers {

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                        postgreSQLContainer.getJdbcUrl(),
                        postgreSQLContainer.getUsername(),
                        postgreSQLContainer.getPassword()
                )
                //.locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        System.out.println("============== Migration Done ======");
    }

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("fullstack-dao-unit-test-db")
                    .withUsername("admin")
                    .withPassword("admin");

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry propertySource) {
        propertySource.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        propertySource.add("spring.datasource.username", postgreSQLContainer::getUsername);
        propertySource.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    private static DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .password(postgreSQLContainer.getPassword())
                .username(postgreSQLContainer.getUsername())
                .build();
    }

    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    protected static final Faker FAKER = new Faker();
}
