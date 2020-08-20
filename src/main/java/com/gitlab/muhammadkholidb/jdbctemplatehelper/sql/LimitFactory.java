package com.gitlab.muhammadkholidb.jdbctemplatehelper.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

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

    public Limit create(int limit, int offset) {
        if (dbProductName.contains("mysql")) {
            return new LimitMySQL(limit, offset);
        } else if (dbProductName.contains("h2")) {
            return new LimitH2(limit, offset);
        } else if (dbProductName.contains("postgresql")) {
            return new LimitPostgreSQL(limit, offset);
        } else {
            throw new UnsupportedOperationException(String.format("Limit implementation for product %s not found", dbProductName));
        }
    }

    public Limit create(int limit) {
        return create(limit, 0);
    }

}
