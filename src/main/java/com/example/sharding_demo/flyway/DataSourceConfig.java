package com.example.sharding_demo.flyway;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.example.sharding_demo",
    entityManagerFactoryRef = "shardingEntityManager",
    transactionManagerRef = "shardingTransactionManager"
)
public class DataSourceConfig {

    private static final String PACKAGE_SCAN = "com.example.sharding_demo";
    // Injecting property values directly using @Value
    @Value("${spring.shard1.datasource.url}")
    private              String datasource1Url;

    @Value("${spring.shard1.datasource.username}")
    private String datasource1Username;

    @Value("${spring.shard1.datasource.password}")
    private String datasource1Password;

    @Value("${spring.shard2.datasource.url}")
    private String datasource2Url;

    @Value("${spring.shard2.datasource.username}")
    private String datasource2Username;

    @Value("${spring.shard2.datasource.password}")
    private String datasource2Password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    // DataSource for datasource1
    @Bean(name = "datasource1")
    @Primary
    public DataSource dataSource1() {

        return DataSourceBuilder.create()
            .url(datasource1Url)
            .username(datasource1Username)
            .password(datasource1Password)
            .driverClassName(driverClassName)
            .build();
    }

    // DataSource for datasource2
    @Bean(name = "datasource2")
    public DataSource dataSource2() {

        return DataSourceBuilder.create()
            .url(datasource2Url)
            .username(datasource2Username)
            .password(datasource2Password)
            .driverClassName(driverClassName)
            .build();
    }

    // JdbcTemplate for datasource1
    @Bean(name = "jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1(@Qualifier("datasource1") DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }

    // JdbcTemplate for datasource2
    @Bean(name = "jdbcTemplate2")
    public JdbcTemplate jdbcTemplate2(@Qualifier("datasource2") DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "shardedDataSource")
    public DataSource shardedDataSources() {

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("datasource1", dataSource1());
        targetDataSources.put("datasource2", dataSource2());
        targetDataSources.put("default", dataSource1());
        ShardingDataSource shardingDataSource = new ShardingDataSource();
        shardingDataSource.setDefaultTargetDataSource(dataSource1());
        shardingDataSource.setTargetDataSources(targetDataSources);
        return shardingDataSource;
    }

    @Bean(name = "shardingEntityManager")
    public LocalContainerEntityManagerFactoryBean multiEntityManager() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(shardedDataSources());
        em.setPackagesToScan(PACKAGE_SCAN);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
//        em.setJpaProperties(hibernateProperties());
        return em;
    }

    @Bean(name = "shardingTransactionManager")
    public PlatformTransactionManager multiTransactionManager() {

        JpaTransactionManager transactionManager
            = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
            multiEntityManager().getObject());
        return transactionManager;
    }

//    @Primary
//    @Bean(name = "dbSessionFactory")
//    public LocalSessionFactoryBean dbSessionFactory() {
//
//        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
//        sessionFactoryBean.setDataSource(multiRoutingDataSource());
//        sessionFactoryBean.setPackagesToScan(PACKAGE_SCAN);
//        sessionFactoryBean.setHibernateProperties(hibernateProperties());
//        return sessionFactoryBean;
//    }

    private Properties hibernateProperties() {

        Properties properties = new Properties();
        properties.put("hibernate.show_sql", false);
        properties.put("hibernate.format_sql", false);
        return properties;
    }
}
