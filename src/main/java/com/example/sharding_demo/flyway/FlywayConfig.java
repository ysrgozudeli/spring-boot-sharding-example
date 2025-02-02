package com.example.sharding_demo.flyway;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flywayForDataSource1(@Qualifier("datasource1") DataSource datasource1) {

        Flyway flyway = Flyway.configure()
            .dataSource(datasource1)
            .locations("classpath:db/migration/shard1") // Separate migrations folder for pg-a
            .load();
        flyway.migrate(); // Apply migrations
        return flyway;
    }

    @Bean
    public Flyway flywayForDataSource2(@Qualifier("datasource2") DataSource datasource2) {

        Flyway flyway = Flyway.configure()
            .dataSource(datasource2)
            .locations("classpath:db/migration/shard2") // Separate migrations folder for pg-b
            .load();
        flyway.migrate(); // Apply migrations
        return flyway;
    }
}
