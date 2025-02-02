package com.example.sharding_demo.flyway;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DataSourceContextHolder extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public DataSourceContextHolder() {

        setCurrentDatasource("datasource1");
    }

    public static void setCurrentDatasource(String datasource) {

        contextHolder.set(datasource);
    }

    public static String getCurrentDatasource() {

        return contextHolder.get();
    }

    public static void clear() {

        contextHolder.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {

        return contextHolder.get();
    }
}
