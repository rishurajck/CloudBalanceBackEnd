package com.project.cloudbalance.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import net.snowflake.client.jdbc.SnowflakeBasicDataSource;


@Configuration
public class SnowflakeConfig {

    @Value("${snowflake.account}")
    private String account;

    @Value("${snowflake.username}")
    private String username;

    @Value("${snowflake.password}")
    private String password;

    @Value("${snowflake.warehouse}")
    private String warehouse;

    @Value("${snowflake.database}")
    private String database;

    @Value("${snowflake.schema}")
    private String schema;

    @Bean(name = "snowflakeDataSource")
    public DataSource snowflakeDataSource() {
        SnowflakeBasicDataSource dataSource = new SnowflakeBasicDataSource();
        dataSource.setUrl("jdbc:snowflake://" + account + ".snowflakecomputing.com/?db=" + database + "&schema=" + schema + "&warehouse=" + warehouse);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "snowflakeJdbcTemplate")
    public JdbcTemplate snowflakeJdbcTemplate(@Qualifier("snowflakeDataSource") DataSource snowflakeDataSource) {
        return new JdbcTemplate(snowflakeDataSource);
    }
}
