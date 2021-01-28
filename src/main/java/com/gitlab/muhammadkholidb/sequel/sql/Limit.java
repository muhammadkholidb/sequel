package com.gitlab.muhammadkholidb.sequel.sql;

import lombok.Data;

@Data
public class Limit {
    private int limit;
    private int offset;

    public Limit(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public Limit(int limit) {
        this(limit, 0);
    }
}
