package com.gitlab.muhammadkholidb.sequel.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author muhammad
 */
@Slf4j
public class LimitFactory {

    private final String dbProductName;

    public LimitFactory(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            dbProductName = conn.getMetaData().getDatabaseProductName().toLowerCase();
            log.info("Database product name: {}", dbProductName);
        }
    }

    public String getClause(Limit limit) {
        validate(limit.getLimit());
        String clause = null;
        if (StringUtils.containsAny(dbProductName, "mysql", "mariadb")) {
            clause = getClauseMySQL(limit);
        } else if (dbProductName.contains("h2")) {
            clause = getClauseH2(limit);
        } else if (dbProductName.contains("postgresql")) {
            clause = getClausePostgreSQL(limit);
        } else {
            throw new UnsupportedOperationException(
                    String.format("Limit implementation for product %s not found", dbProductName));
        }
        return clause;
    }

    private String getClauseH2(Limit limit) {
        return String.format(" LIMIT %s OFFSET %s ", limit.getLimit(), limit.getOffset());
    }

    private String getClauseMySQL(Limit limit) {
        return String.format(" LIMIT %s, %s ", limit.getOffset(), limit.getLimit());
    }

    private String getClausePostgreSQL(Limit limit) {
        return String.format(" LIMIT %s OFFSET %s ", limit.getLimit(), limit.getOffset());
    }

    private void validate(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit should be a positive number");
        }
    }

}
