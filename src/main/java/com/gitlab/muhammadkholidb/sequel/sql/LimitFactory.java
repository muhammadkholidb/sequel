package com.gitlab.muhammadkholidb.sequel.sql;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author muhammad
 */
@Slf4j
public class LimitFactory {

    private final String productName;

    public LimitFactory(String productName) {
        log.info("Database product name: {}", productName);
        this.productName = productName.toLowerCase();
    }

    public String getClause(Limit limit) {
        validateLimit(limit.getValue());
        String clause = null;
        if (StringUtils.containsAny(productName, "mysql", "mariadb")) {
            clause = getClauseMySQL(limit);
        } else if (productName.contains("h2")) {
            clause = getClauseH2(limit);
        } else if (productName.contains("postgresql")) {
            clause = getClausePostgreSQL(limit);
        } else {
            throw new UnsupportedOperationException(
                    String.format("Limit implementation for product %s not found", productName));
        }
        return clause;
    }

    private String getClauseMySQL(Limit limit) {
        if (limit.getOffset() == null) {
            return String.format(" LIMIT %s ", limit.getValue());
        }
        return String.format(" LIMIT %s, %s ", limit.getOffset(), limit.getValue());
    }

    private String getClausePostgreSQL(Limit limit) {
        if (limit.getOffset() == null) {
            return String.format(" LIMIT %s ", limit.getValue());
        }
        return String.format(" LIMIT %s OFFSET %s ", limit.getValue(), limit.getOffset());
    }

    private String getClauseH2(Limit limit) {
        return getClausePostgreSQL(limit);
    }

    private void validateLimit(Integer value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Limit should be a positive number");
        }
    }

}
