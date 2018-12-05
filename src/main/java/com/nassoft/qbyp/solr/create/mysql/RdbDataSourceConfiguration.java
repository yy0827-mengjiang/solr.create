package com.nassoft.qbyp.solr.create.mysql;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class RdbDataSourceConfiguration {

    @Bean(name = "transactionDataSource", destroyMethod="close")
    @Qualifier("transactionDataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource.transaction-rdb")
    public DataSource transactionDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "transactionJdbcTemplate")
    @Primary
    public JdbcTemplate transactionJdbcTemplate(
            @Qualifier("transactionDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
