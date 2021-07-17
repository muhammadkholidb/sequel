package com.gitlab.muhammadkholidb.sequel.repository;

import javax.sql.DataSource;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.junit5.api.DBRider;
import com.gitlab.muhammadkholidb.sequel.config.SequelConfig;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@DBRider
@DBUnit(caseSensitiveTableNames = true, escapePattern = "\"?\"")
@SpringJUnitConfig({ BaseRepositoryTest.Config.class })
public abstract class BaseRepositoryTest {

    @Configuration
    @ComponentScan(basePackageClasses = BaseRepositoryTest.class)
    @Import(SequelConfig.class)
    @PropertySource({ "classpath:application-test.properties" })
    public static class Config {
        
        @Bean
        public DataSource dataSource() {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE;init=runscript from 'classpath:init.sql'");
            ds.setUser("sa");
            ds.setPassword("sa");
            return ds;
        }

    }

}
