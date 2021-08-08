package com.gitlab.muhammadkholidb.sequel.repository;

import javax.sql.DataSource;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.junit5.api.DBRider;
import com.gitlab.muhammadkholidb.sequel.config.SequelConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import liquibase.integration.spring.SpringLiquibase;

@DBRider
@DBUnit(caseSensitiveTableNames = true)
@SpringJUnitConfig(RepositoryTestBase.Config.class)
public abstract class RepositoryTestBase {

    @Configuration
    @ComponentScan(basePackageClasses = RepositoryTestBase.class)
    @Import(SequelConfig.class)
    @PropertySource(value = { "classpath:application-test.properties",
            "classpath:application-${spring.profiles.active}.properties" }, ignoreResourceNotFound = true)
    public static class Config {

        @Autowired
        private Environment env;

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName(env.getRequiredProperty("jdbc.driver"));
            ds.setUrl(env.getRequiredProperty("jdbc.url"));
            ds.setUsername(env.getRequiredProperty("jdbc.user"));
            ds.setPassword(env.getRequiredProperty("jdbc.password"));
            return ds;
        }

        @Bean
        public SpringLiquibase springLiquibase(DataSource dataSource) {
            SpringLiquibase springLiquibase = new SpringLiquibase();
            springLiquibase.setDataSource(dataSource);
            springLiquibase.setChangeLog("classpath:liquibase/changelog.xml");
            return springLiquibase;
        }

    }

}
