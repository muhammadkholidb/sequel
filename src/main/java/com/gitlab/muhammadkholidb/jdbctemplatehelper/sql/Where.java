package com.gitlab.muhammadkholidb.jdbctemplatehelper.sql;

import java.util.ArrayList;
import java.util.List;

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

    public Where contains(String column, Object value) {
        andContains(column, value);
        return this;
    }

    public Where containsIgnoreCase(String column, Object value) {
        andContainsIgnoreCase(column, value);
        return this;
    }

    public Where startsWith(String column, Object value) {
        andStartsWith(column, value);
        return this;
    }

    public Where startsWithIgnoreCase(String column, Object value) {
        andStartsWithIgnoreCase(column, value);
        return this;
    }

    public Where endsWith(String column, Object value) {
        andEndsWith(column, value);
        return this;
    }

    public Where endsWithIgnoreCase(String column, Object value) {
        andEndsWithIgnoreCase(column, value);
        return this;
    }

    // AND

    public Where andEquals(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS, Operator.AND));
        return this;
    }

    public Where andEqualsIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS_IGNORE_CASE, Operator.AND));
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

    public Where andIn(String column, Object[] values) {
        holders.add(new Holder(column, values, Condition.IN, Operator.AND));
        return this;
    }

    public Where andContains(String column, Object value) {
        andLike(column, "%" + value + "%");
        return this;
    }

    public Where andContainsIgnoreCase(String column, Object value) {
        andLikeIgnoreCase(column, "%" + value + "%");
        return this;
    }

    public Where andStartsWith(String column, Object value) {
        andLike(column, value + "%");
        return this;
    }

    public Where andStartsWithIgnoreCase(String column, Object value) {
        andLikeIgnoreCase(column, value + "%");
        return this;
    }

    public Where andEndsWith(String column, Object value) {
        andLike(column, "%" + value);
        return this;
    }

    public Where andEndsWithIgnoreCase(String column, Object value) {
        andLikeIgnoreCase(column, "%" + value);
        return this;
    }

    public Where and(Where where) {
        holders.add(new Holder(where, Operator.AND));
        return this;
    }

    // OR

    public Where orEquals(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS, Operator.OR));
        return this;
    }

    public Where orEqualsIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS_IGNORE_CASE, Operator.OR));
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

    public Where orIn(String column, Object[] values) {
        holders.add(new Holder(column, values, Condition.IN, Operator.OR));
        return this;
    }

    public Where orContains(String column, Object value) {
        orLike(column, "%" + value + "%");
        return this;
    }

    public Where orContainsIgnoreCase(String column, Object value) {
        orLikeIgnoreCase(column, "%" + value + "%");
        return this;
    }

    public Where orStartsWith(String column, Object value) {
        orLike(column, value + "%");
        return this;
    }

    public Where orStartsWithIgnoreCase(String column, Object value) {
        orLikeIgnoreCase(column, value + "%");
        return this;
    }

    public Where orEndsWith(String column, Object value) {
        orLike(column, "%" + value);
        return this;
    }

    public Where orEndsWithIgnoreCase(String column, Object value) {
        orLikeIgnoreCase(column, "%" + value);
        return this;
    }

    public Where or(Where where) {
        holders.add(new Holder(where, Operator.OR));
        return this;
    }

    public List<Object> getValues() {
        List<Object> values = new ArrayList<>();
        holders.forEach(holder -> values.add(holder.value));
        return values;
    }

    public String getClause() {
        if (holders.isEmpty()) {
            return "";
        }
        String prefix = " WHERE ";
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        for (int i = 0; i < holders.size(); i++) {
            Holder holder = holders.get(i);
            if (i > 0) {
                sb.append(holder.operator);
            }
            switch (holder.condition) {
                case WHERE:
                    sb.append(" (");
                    sb.append(holder.getWhere().getClause().substring(prefix.length())); // remove "WHERE" inside current WHERE
                    sb.append(") ");
                    break;
                case EQUALS:
                    sb.append(" ");
                    sb.append(holder.column);
                    sb.append("=? ");
                    break;
                case EQUALS_IGNORE_CASE:
                    sb.append(" LOWER(");
                    sb.append(holder.column);
                    sb.append(")=LOWER(?) ");
                    break;
                case LIKE:
                    sb.append(" ");
                    sb.append(holder.column);
                    sb.append(" LIKE ? ");
                    break;
                case LIKE_IGNORE_CASE:
                    sb.append(" LOWER(");
                    sb.append(holder.column);
                    sb.append(") LIKE LOWER(?) ");
                    break;
                case IN:
                    sb.append(" ");
                    sb.append(holder.column);
                    sb.append("IN (");
                    int length = ((Object[]) holder.value).length;
                    for (int j = 0; j < length; j++) {
                        sb.append("?");
                        if (j < (length - 1)) {
                            sb.append(",");
                        }
                    }
                    sb.append(") ");
                    break;
            }
        }
        return sb.toString();
    }

    private enum Condition {
        WHERE, EQUALS, EQUALS_IGNORE_CASE, LIKE, LIKE_IGNORE_CASE, IN
    }

    private enum Operator {
        AND, OR
    }

    @Data
    private class Holder {

        private final Where where;
        private final String column;
        private final Object value;
        private final Condition condition;
        private final Operator operator;

        public Holder(Where where, Operator operator) {
            this.where = where;
            this.column = null;
            this.value = null;
            this.condition = Condition.WHERE;
            this.operator = operator;
        }

        public Holder(String column, Object value, Condition condition, Operator operator) {
            this.where = null;
            this.column = column;
            this.value = value;
            this.condition = condition;
            this.operator = operator;
        }
    }

}
