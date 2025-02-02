package com.example.sharding_demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(classes = {ShardingDemoApplication.class})
//@ContextConfiguration(classes = {TestDatabaseConfig.class})
@Import(TestConfig.class)
@AutoConfigureMockMvc
public class BaseIT {

    @ClassRule
    public static final PostgreSQLContainer<CustomPostgresContainer> postgresContainerRule = new CustomPostgresContainer();

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void contextLoads() {

        System.out.println("context loaded");
    }

    protected static String toJsonString(final Object obj) {

        try {
            var mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
