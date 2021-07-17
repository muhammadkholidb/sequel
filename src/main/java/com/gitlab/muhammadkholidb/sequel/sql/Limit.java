package com.gitlab.muhammadkholidb.sequel.sql;

import lombok.Data;

@Data
public class Limit {
    private Integer value;
    private Integer offset;

    public Limit(Integer value, Integer offset) {
        this.value = value;
        this.offset = offset;
    }

    public Limit(Integer value) {
        this(value, null);
    }
}
