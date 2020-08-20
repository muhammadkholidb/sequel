package com.gitlab.muhammadkholidb.jdbctemplatehelper.sql;

/**
 *
 * @author muhammad
 */
public class LimitMySQL implements Limit {

    private final int limit;
    private final int offset;

    public LimitMySQL(int limit) {
        this.limit = limit;
        this.offset = 0;
    }

    public LimitMySQL(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public String getClause() {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit should be a positive number");
        }
        return String.format(" LIMIT %s, %s ", offset, limit);
    }

}
