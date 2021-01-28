package com.gitlab.muhammadkholidb.sequel.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.gitlab.muhammadkholidb.sequel.sql.LimitFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class SequelConfig {
    
    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTEmplate() {
        return new NamedParameterJdbcTemplate(jdbcTemplate());
    }

    @Bean
    public LimitFactory limitFactory() throws SQLException {
        return new LimitFactory(dataSource);
    }

}
