package com.gitlab.muhammadkholidb.sequel.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.gitlab.muhammadkholidb.sequel.sql.LimitFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class SequelConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTEmplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean
    public LimitFactory limitFactory(DatabaseMetaData databaseMetaData) throws SQLException {
        return new LimitFactory(databaseMetaData.getDatabaseProductName());
    }

    @Bean
    public DatabaseMetaData databaseMetaData(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return conn.getMetaData();
        }
    }

}
