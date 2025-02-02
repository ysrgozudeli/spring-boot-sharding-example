package com.example.sharding_demo.sharding.custom_transaction;

import com.example.sharding_demo.flyway.DataSourceContextHolder;
import jakarta.persistence.EntityManagerFactory;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;

public class DynamicTransactionManager extends JpaTransactionManager {

    @Autowired
    private Environment environment;

    public DynamicTransactionManager(EntityManagerFactory emf) {

        super(emf);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {

        String dataSourceName;
        if (Arrays.asList(environment.getActiveProfiles()).contains("test")) {
            dataSourceName = "datasource1";
        } else {
            dataSourceName = DataSourceContextHolder.getCurrentDatasource();
        }

        // Ensure the correct data source is set
        if (dataSourceName == null) {
            throw new IllegalStateException("No datasource set in context!");
        }
        super.doBegin(transaction, definition);
    }
}
