package com.example.sharding_demo;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public DataSource testDataSource() {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(System.getProperty("DB_URL"));
        dataSource.setUsername(System.getProperty("DB_USERNAME"));
        dataSource.setPassword(System.getProperty("DB_PASSWORD"));
        return dataSource;
    }
}