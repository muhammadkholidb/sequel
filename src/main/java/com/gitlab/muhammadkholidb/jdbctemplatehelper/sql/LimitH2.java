package com.gitlab.muhammadkholidb.jdbctemplatehelper.sql;

/**
 *
 * @author muhammad
 */
public class LimitH2 implements Limit {

    private final int limit;
    private final int offset;

    public LimitH2(int limit) {
        this.limit = limit;
        this.offset = 0;
    }

    public LimitH2(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public String getClause() {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit should be a positive number");
        }
        return String.format(" LIMIT %s OFFSET %s ", limit, offset);
    }

}
