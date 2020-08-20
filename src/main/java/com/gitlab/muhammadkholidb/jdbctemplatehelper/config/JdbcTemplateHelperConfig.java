package com.gitlab.muhammadkholidb.jdbctemplatehelper.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.gitlab.muhammadkholidb.jdbctemplatehelper.sql.LimitFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcTemplateHelperConfig {
    
    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    public LimitFactory limitFactory() throws SQLException {
        return new LimitFactory(dataSource);
    }

}