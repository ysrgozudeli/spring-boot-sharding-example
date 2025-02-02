package com.example.sharding_demo.flyway;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ShardingDataSource extends AbstractRoutingDataSource {
//  REVIEW: asdfasdf

    @Override
    protected Object determineCurrentLookupKey() {

        return DataSourceContextHolder.getCurrentDatasource();
    }
}
