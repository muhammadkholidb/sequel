package com.gitlab.muhammadkholidb.jdbctemplatehelper.sql;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author muhammad
 */
public class Where {

    private final List<Holder> holders;

    public Where() {
        this.holders = new ArrayList<>();
    }

    public Where equals(String column, Object value) {
        andEquals(column, value);
        return this;
    }

    public Where equalsIgnoreCase(String column, Object value) {
        andEqualsIgnoreCase(column, value);
        return this;
    }

    public Where like(String column, Object value) {
        andLike(column, value);
        return this;
    }

    public Where likeIgnoreCase(String column, Object value) {
        andLikeIgnoreCase(column, value);
        return this;
    }

    public Where in(String column, Object[] values) {
        andIn(column, values);
        return this;
    }

    public Where andEquals(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS, Operator.AND));
        return this;
    }

    public Where andEqualsIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS_IGNORE_CASE, Operator.AND));
        return this;
    }

    public Where orEquals(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS, Operator.OR));
        return this;
    }

    public Where orEqualsIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS_IGNORE_CASE, Operator.OR));
        return this;
    }

    public Where andLike(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LIKE, Operator.AND));
        return this;
    }

    public Where andLikeIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LIKE_IGNORE_CASE, Operator.AND));
        return this;
    }

    public Where orLike(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LIKE, Operator.OR));
        return this;
    }

    public Where orLikeIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LIKE_IGNORE_CASE, Operator.OR));
        return this;
    }

    public Where andIn(String column, Object[] values) {
        holders.add(new Holder(column, values, Condition.IN, Operator.AND));
        return this;
    }

    public Where orIn(String column, Object[] values) {
        holders.add(new Holder(column, values, Condition.IN, Operator.OR));
        return this;
    }

    public List<Object> getValues() {
        List<Object> values = new ArrayList<>();
        holders.forEach(holder -> values.add(holder.value));
        return values;
    }

    // It always use 1=1 after WHERE clause to make it easier to append the
    // operators.
    // It will not affect the performance to much
    // https://dba.stackexchange.com/questions/33937/good-bad-or-indifferent-where-1-1
    public String getClause() {
        if (holders.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" WHERE 1=1 ");
        holders.forEach(holder -> {
            sb.append(holder.operator);
            switch (holder.condition) {
            case EQUALS:
                sb.append(" (");
                sb.append(holder.column);
                sb.append("=?) ");
                break;
            case EQUALS_IGNORE_CASE:
                sb.append(" (LOWER(");
                sb.append(holder.column);
                sb.append(")=LOWER(?)) ");
                break;
            case LIKE:
                sb.append(" (");
                sb.append(holder.column);
                sb.append(" LIKE ?) ");
                break;
            case LIKE_IGNORE_CASE:
                sb.append(" (LOWER(");
                sb.append(holder.column);
                sb.append(" ) LIKE LOWER(?)) ");
                break;
            case IN:
                sb.append(" (");
                sb.append(holder.column);
                sb.append("IN (");
                int length = ((Object[]) holder.value).length;
                for (int i = 0; i < length; i++) {
                    sb.append("?");
                    if (i < (length - 1)) {
                        sb.append(",");
                    }
                }
                sb.append(")) ");
                break;
            }
        });
        return sb.toString();
    }

    private enum Condition {
        EQUALS, EQUALS_IGNORE_CASE, LIKE, LIKE_IGNORE_CASE, IN
    }

    private enum Operator {
        AND, OR
    }

    @Data
    @AllArgsConstructor
    private class Holder {

        private final String column;
        private final Object value;
        private final Condition condition;
        private final Operator operator;

    }

}
