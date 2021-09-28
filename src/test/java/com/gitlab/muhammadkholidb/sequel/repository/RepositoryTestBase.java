package com.gitlab.muhammadkholidb.sequel.repository;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.gitlab.muhammadkholidb.sequel.config.SequelConfig;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import liquibase.integration.spring.SpringLiquibase;

@TestExecutionListeners({ DbUnitTestExecutionListener.class, DependencyInjectionTestExecutionListener.class })
@DbUnitConfiguration(databaseConnection = "databaseConnection")
@SpringJUnitConfig(RepositoryTestBase.Config.class)
public abstract class RepositoryTestBase {

    @Configuration
    @ComponentScan(basePackageClasses = RepositoryTestBase.class)
    @Import(SequelConfig.class)
    @PropertySource(value = {
            "classpath:application-test.properties",
            "classpath:application-${spring.profiles.active}.properties" },
        ignoreResourceNotFound = true)
    public static class Config {

        @Autowired
        private Environment env;

        @Bean
        public IDatabaseConnection databaseConnection(DataSource dataSource, DatabaseMetaData databaseMetaData)
                throws SQLException,
                DatabaseUnitException {
            DatabaseDataSourceConnection databaseConnection = new DatabaseDataSourceConnection(dataSource);
            DatabaseConfig databaseConfig = databaseConnection.getConfig();
            databaseConfig.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
            databaseConfig.setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            databaseConfig.setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN, getEscapePattern(databaseMetaData));
            databaseConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, getDataTypeFactory(databaseMetaData));
            return databaseConnection;
        }

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
        public DatabaseMetaData databaseMetaData(DataSource dataSource) throws SQLException {
            return dataSource.getConnection().getMetaData();
        }

        @Bean
        public SpringLiquibase springLiquibase(DataSource dataSource) {
            SpringLiquibase springLiquibase = new SpringLiquibase();
            springLiquibase.setDataSource(dataSource);
            springLiquibase.setChangeLog("classpath:liquibase/changelog.xml");
            return springLiquibase;
        }

        private String getEscapePattern(DatabaseMetaData databaseMetaData) {
            try {
                String quoteString = databaseMetaData.getIdentifierQuoteString().trim();
                return quoteString + "?" + quoteString;
            } catch (SQLException e) {
                return "";
            }
        }

        private IDataTypeFactory getDataTypeFactory(DatabaseMetaData databaseMetaData) throws SQLException {
            switch (databaseMetaData.getDatabaseProductName().toLowerCase()) {
                case "h2":
                    return new H2DataTypeFactory();
                case "postgresql":
                    return new PostgresqlDataTypeFactory();
                case "mysql":
                case "mariadb":
                    return new MySqlDataTypeFactory();
                default:
                    return new DefaultDataTypeFactory();
            }
        }
    }

}
