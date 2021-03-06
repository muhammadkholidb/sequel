package com.gitlab.muhammadkholidb.sequel.utility;

import java.util.List;

import com.github.vertical_blank.sqlformatter.SqlFormatter;

import org.springframework.util.CollectionUtils;

public class SQLUtils {

    private SQLUtils() {
    }

    public static String format(String sql, List<?> params) {
        return CollectionUtils.isEmpty(params) ? SqlFormatter.format(sql) : SqlFormatter.format(sql, params);
    }

    public static String format(String sql) {
        return format(sql, null);
    }

    public static String likeValueContains(String text) {
        return "%" + text + "%";
    }

    public static String likeValueStartsWith(String text) {
        return text + "%";
    }

    public static String likeValueEndsWith(String text) {
        return "%" + text;
    }

}
