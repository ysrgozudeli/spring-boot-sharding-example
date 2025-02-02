package com.example.sharding_demo.sharding;

import com.example.sharding_demo.flyway.DataSourceContextHolder;
import com.example.sharding_demo.sharding.request_data.ShopContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import javax.sql.DataSource;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(0)
public class ShardingAspect {

    @Autowired
    private Environment environment;

    @Autowired
    @Qualifier("datasource1")
    private DataSource datasource1;

    // Pointcut for public methods in @Service annotated classes with @Transactional
    @Pointcut("within(@org.springframework.stereotype.Service *) && @within(org.springframework.transaction.annotation.Transactional)")
    public void transactionalServiceMethods() {

    }

    // Advice to switch datasource before execution
    @Before("transactionalServiceMethods() && execution(public * *(..))")
    public void switchDatasource() {

        if (Arrays.asList(environment.getActiveProfiles()).contains("test")) {
            return;
        }

        Long shopId = ShopContext.getShopId(); // Get the shop ID from the context
        if (shopId != null) {
            // Determine the data source name based on the shop ID
            String dataSourceName = findDataSource(shopId);
            if (dataSourceName != null) {
                DataSourceContextHolder.setCurrentDatasource(dataSourceName); // Set the data source
                System.out.println("Datasource switched to: " + dataSourceName);
            } else {
                throw new IllegalStateException("No data source found for shop ID: " + shopId);
            }
        } else {
            throw new IllegalStateException("Shop ID is not set in the context!");
        }
    }

    // Advice to clear the datasource after execution
    @After("transactionalServiceMethods() && execution(public * *(..))")
    public void cleanupDatasource() {

        DataSourceContextHolder.clear();
    }

    private String findDataSource(Long shopId) {

        String sql = """
                SELECT o.datasource
                FROM ORGANIZATION o
                INNER JOIN SHOP s ON s.ORGANIZATION_ID = o.ID
                WHERE s.ID = ?
            """;

        try (Connection connection = datasource1.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, shopId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("datasource");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to query datasource", e);
        }
        return null;
    }
}
