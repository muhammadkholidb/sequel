package com.gitlab.muhammadkholidb.sequel.sql;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * @author muhammad
 */
public class Order {

    public static final Direction ASCENDING = Direction.ASCENDING;
    public static final Direction DESCENDING = Direction.DESCENDING;

    private final Map<String, Direction> map;

    public Order() {
        this.map = new LinkedHashMap<>();
    }

    public Order by(String column, Direction direction) {
        map.put(column, direction);
        return this;
    }

    public Order by(String column) {
        return by(column, Direction.ASCENDING);
    }

    public String getClause() {
        return getClause(new UnaryOperator<String>() {
            @Override
            public String apply(String col) {
                return col;
            }
        });
    }

    public String getClause(UnaryOperator<String> fnColumn) {
        if (map != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(" ORDER BY ");
            int i = 0;
            for (Map.Entry<String, Direction> entry : map.entrySet()) {
                sb.append(fnColumn.apply(entry.getKey()));
                sb.append(" ");
                sb.append(entry.getValue().abbr());
                i++;
                if (i < map.size()) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    public enum Direction {
        ASCENDING("ASC"),
        DESCENDING("DESC");

        private final String abbr;

        private Direction(String abbr) {
            this.abbr = abbr;
        }

        public String abbr() {
            return this.abbr;
        }

        public static Direction byAbbr(String abbr) {
            for (Direction direction : values()) {
                if (direction.abbr.equalsIgnoreCase(abbr)) {
                    return direction;
                }
            }
            return null;
        }
    }
}
