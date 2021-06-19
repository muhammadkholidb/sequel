package com.gitlab.muhammadkholidb.sequel.sql;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 *
 * @author muhammad
 */
public class Where {

    private static final String PREFIX = " WHERE ";

    private final List<Holder> holders;

    public Where() {
        this.holders = new ArrayList<>();
    }

    public Where equals(String column, Object value) {
        return andEquals(column, value);
    }

    public Where notEquals(String column, Object value) {
        return andNotEquals(column, value);
    }

    public Where equalsIgnoreCase(String column, Object value) {
        return andEqualsIgnoreCase(column, value);
    }

    public Where notEqualsIgnoreCase(String column, Object value) {
        return andNotEqualsIgnoreCase(column, value);
    }

    public Where like(String column, Object value) {
        return andLike(column, value);
    }

    public Where notLike(String column, Object value) {
        return andNotLike(column, value);
    }

    public Where likeIgnoreCase(String column, Object value) {
        return andLikeIgnoreCase(column, value);
    }

    public Where notLikeIgnoreCase(String column, Object value) {
        return andNotLikeIgnoreCase(column, value);
    }

    public Where in(String column, List<?> values) {
        return andIn(column, values);
    }

    public Where notIn(String column, List<?> values) {
        return andNotIn(column, values);
    }

    public Where contains(String column, Object value) {
        return andContains(column, value);
    }

    public Where notContains(String column, Object value) {
        return andNotContains(column, value);
    }

    public Where containsIgnoreCase(String column, Object value) {
        return andContainsIgnoreCase(column, value);
    }

    public Where notContainsIgnoreCase(String column, Object value) {
        return andNotContainsIgnoreCase(column, value);
    }

    public Where startsWith(String column, Object value) {
        return andStartsWith(column, value);
    }

    public Where notStartsWith(String column, Object value) {
        return andNotStartsWith(column, value);
    }

    public Where startsWithIgnoreCase(String column, Object value) {
        return andStartsWithIgnoreCase(column, value);
    }

    public Where notStartsWithIgnoreCase(String column, Object value) {
        return andNotStartsWithIgnoreCase(column, value);
    }

    public Where endsWith(String column, Object value) {
        return andEndsWith(column, value);
    }

    public Where notEndsWith(String column, Object value) {
        return andNotEndsWith(column, value);
    }

    public Where endsWithIgnoreCase(String column, Object value) {
        return andEndsWithIgnoreCase(column, value);
    }

    public Where notEndsWithIgnoreCase(String column, Object value) {
        return andNotEndsWithIgnoreCase(column, value);
    }

    public Where greaterThan(String column, Object value) {
        return andGreaterThan(column, value);
    }

    public Where greaterThanOrEqual(String column, Object value) {
        return andGreaterThanOrEqual(column, value);
    }

    public Where lowerThan(String column, Object value) {
        return andLowerThan(column, value);
    }

    public Where lowerThanOrEqual(String column, Object value) {
        return andLowerThanOrEqual(column, value);
    }

    public Where isNull(String column) {
        return andIsNull(column);
    }

    public Where isNotNull(String column) {
        return andIsNotNull(column);
    }

    // AND

    public Where andEquals(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS, Operator.AND));
        return this;
    }

    public Where andNotEquals(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS, Operator.AND, true));
        return this;
    }

    public Where andEqualsIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS_IGNORE_CASE, Operator.AND));
        return this;
    }

    public Where andNotEqualsIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS_IGNORE_CASE, Operator.AND, true));
        return this;
    }

    public Where andLike(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LIKE, Operator.AND));
        return this;
    }

    public Where andNotLike(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LIKE, Operator.AND, true));
        return this;
    }

    public Where andLikeIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LIKE_IGNORE_CASE, Operator.AND));
        return this;
    }

    public Where andNotLikeIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LIKE_IGNORE_CASE, Operator.AND, true));
        return this;
    }

    public Where andIn(String column, List<?> values) {
        holders.add(new Holder(column, values, Condition.IN, Operator.AND));
        return this;
    }

    public Where andNotIn(String column, List<?> values) {
        holders.add(new Holder(column, values, Condition.IN, Operator.AND, true));
        return this;
    }

    public Where andContains(String column, Object value) {
        andLike(column, "%" + value + "%");
        return this;
    }

    public Where andNotContains(String column, Object value) {
        andNotLike(column, "%" + value + "%");
        return this;
    }

    public Where andContainsIgnoreCase(String column, Object value) {
        andLikeIgnoreCase(column, "%" + value + "%");
        return this;
    }

    public Where andNotContainsIgnoreCase(String column, Object value) {
        andNotLikeIgnoreCase(column, "%" + value + "%");
        return this;
    }

    public Where andStartsWith(String column, Object value) {
        andLike(column, value + "%");
        return this;
    }

    public Where andNotStartsWith(String column, Object value) {
        andNotLike(column, value + "%");
        return this;
    }

    public Where andStartsWithIgnoreCase(String column, Object value) {
        andLikeIgnoreCase(column, value + "%");
        return this;
    }

    public Where andNotStartsWithIgnoreCase(String column, Object value) {
        andNotLikeIgnoreCase(column, value + "%");
        return this;
    }

    public Where andEndsWith(String column, Object value) {
        andLike(column, "%" + value);
        return this;
    }

    public Where andNotEndsWith(String column, Object value) {
        andNotLike(column, "%" + value);
        return this;
    }

    public Where andEndsWithIgnoreCase(String column, Object value) {
        andLikeIgnoreCase(column, "%" + value);
        return this;
    }

    public Where andNotEndsWithIgnoreCase(String column, Object value) {
        andNotLikeIgnoreCase(column, "%" + value);
        return this;
    }

    public Where andGreaterThan(String column, Object value) {
        holders.add(new Holder(column, value, Condition.GREATER_THAN, Operator.AND));
        return this;
    }

    public Where andGreaterThanOrEqual(String column, Object value) {
        holders.add(new Holder(column, value, Condition.GREATER_THAN_OR_EQUAL, Operator.AND));
        return this;
    }

    public Where andLowerThan(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LOWER_THAN, Operator.AND));
        return this;
    }

    public Where andLowerThanOrEqual(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LOWER_THAN_OR_EQUAL, Operator.AND));
        return this;
    }

    public Where andIsNull(String column) {
        holders.add(new Holder(column, null, Condition.NULL, Operator.AND));
        return this;
    }

    public Where andIsNotNull(String column) {
        holders.add(new Holder(column, null, Condition.NULL, Operator.AND, true));
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

    public Where orNotEquals(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS, Operator.OR, true));
        return this;
    }

    public Where orEqualsIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS_IGNORE_CASE, Operator.OR));
        return this;
    }

    public Where orNotEqualsIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.EQUALS_IGNORE_CASE, Operator.OR, true));
        return this;
    }

    public Where orLike(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LIKE, Operator.OR));
        return this;
    }

    public Where orNotLike(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LIKE, Operator.OR, true));
        return this;
    }

    public Where orLikeIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LIKE_IGNORE_CASE, Operator.OR));
        return this;
    }

    public Where orNotLikeIgnoreCase(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LIKE_IGNORE_CASE, Operator.OR, true));
        return this;
    }

    public Where orIn(String column, List<?> values) {
        holders.add(new Holder(column, values, Condition.IN, Operator.OR));
        return this;
    }

    public Where orNotIn(String column, List<?> values) {
        holders.add(new Holder(column, values, Condition.IN, Operator.OR, true));
        return this;
    }

    public Where orContains(String column, Object value) {
        orLike(column, "%" + value + "%");
        return this;
    }

    public Where orNotContains(String column, Object value) {
        orNotLike(column, "%" + value + "%");
        return this;
    }

    public Where orContainsIgnoreCase(String column, Object value) {
        orLikeIgnoreCase(column, "%" + value + "%");
        return this;
    }

    public Where orNotContainsIgnoreCase(String column, Object value) {
        orNotLikeIgnoreCase(column, "%" + value + "%");
        return this;
    }

    public Where orStartsWith(String column, Object value) {
        orLike(column, value + "%");
        return this;
    }

    public Where orNotStartsWith(String column, Object value) {
        orNotLike(column, value + "%");
        return this;
    }

    public Where orStartsWithIgnoreCase(String column, Object value) {
        orLikeIgnoreCase(column, value + "%");
        return this;
    }

    public Where orNotStartsWithIgnoreCase(String column, Object value) {
        orNotLikeIgnoreCase(column, value + "%");
        return this;
    }

    public Where orEndsWith(String column, Object value) {
        orLike(column, "%" + value);
        return this;
    }

    public Where orNotEndsWith(String column, Object value) {
        orNotLike(column, "%" + value);
        return this;
    }

    public Where orEndsWithIgnoreCase(String column, Object value) {
        orLikeIgnoreCase(column, "%" + value);
        return this;
    }

    public Where orNotEndsWithIgnoreCase(String column, Object value) {
        orNotLikeIgnoreCase(column, "%" + value);
        return this;
    }

    public Where orGreaterThan(String column, Object value) {
        holders.add(new Holder(column, value, Condition.GREATER_THAN, Operator.OR));
        return this;
    }

    public Where orGreaterThanOrEqual(String column, Object value) {
        holders.add(new Holder(column, value, Condition.GREATER_THAN_OR_EQUAL, Operator.OR));
        return this;
    }

    public Where orLowerThan(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LOWER_THAN, Operator.OR));
        return this;
    }

    public Where orLowerThanOrEqual(String column, Object value) {
        holders.add(new Holder(column, value, Condition.LOWER_THAN_OR_EQUAL, Operator.OR));
        return this;
    }

    public Where orIsNull(String column) {
        holders.add(new Holder(column, null, Condition.NULL, Operator.OR));
        return this;
    }

    public Where orIsNotNull(String column) {
        holders.add(new Holder(column, null, Condition.NULL, Operator.OR, true));
        return this;
    }

    public Where or(Where where) {
        holders.add(new Holder(where, Operator.OR));
        return this;
    }

    public List<Object> getValues() {
        List<Object> values = new ArrayList<>();
        holders.forEach(holder -> {
            Object value = holder.getValue();
            if (value == null) {
                return;
            }
            if (value instanceof List) {
                values.addAll((List<?>) value);
            } else {
                values.add(value);
            }
        });
        return values;
    }

    public String getClause() {
        if (holders.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX);
        for (int i = 0; i < holders.size(); i++) {
            Holder holder = holders.get(i);
            Operator operator = holder.getOperator();
            Where where = holder.getWhere();
            Condition condition = holder.getCondition();
            String column = holder.getColumn();
            Object value = holder.getValue();
            boolean negated = Boolean.TRUE.equals(holder.getNegated());
            if (i > 0) {
                sb.append(operator);
            }
            switch (condition) {
                case WHERE:
                    sb.append(" (");
                    sb.append(where.getClause().substring(PREFIX.length())); // remove " WHERE " inside current WHERE
                    sb.append(") ");
                    break;
                case EQUALS:
                    sb.append(" ");
                    sb.append(column);
                    sb.append(conditionalString(!negated, "=? ", "<>? "));
                    break;
                case EQUALS_IGNORE_CASE:
                    sb.append(" LOWER(");
                    sb.append(column);
                    sb.append(conditionalString(!negated, ")=LOWER(?) ", ")<>LOWER(?) "));
                    break;
                case LIKE:
                    sb.append(" ");
                    sb.append(column);
                    sb.append(conditionalString(!negated, " LIKE ? ", " NOT LIKE ? "));
                    break;
                case LIKE_IGNORE_CASE:
                    sb.append(" LOWER(");
                    sb.append(column);
                    sb.append(conditionalString(!negated, ") LIKE LOWER(?) ", ") NOT LIKE LOWER(?) "));
                    break;
                case IN:
                    sb.append(" ");
                    sb.append(column);
                    sb.append(conditionalString(!negated, " IN (", " NOT IN ("));
                    int length = ((List<?>) value).size();
                    for (int j = 0; j < length; j++) {
                        sb.append("?");
                        if (j < (length - 1)) {
                            sb.append(",");
                        }
                    }
                    sb.append(") ");
                    break;
                case GREATER_THAN:
                    sb.append(" ");
                    sb.append(column);
                    sb.append(" > ? ");
                    break;
                case GREATER_THAN_OR_EQUAL:
                    sb.append(" ");
                    sb.append(column);
                    sb.append(" >= ? ");
                    break;
                case LOWER_THAN:
                    sb.append(" ");
                    sb.append(column);
                    sb.append(" < ? ");
                    break;
                case LOWER_THAN_OR_EQUAL:
                    sb.append(" ");
                    sb.append(column);
                    sb.append(" <= ? ");
                    break;
                case NULL:
                    sb.append(" ");
                    sb.append(column);
                    sb.append(conditionalString(!negated, " IS NULL ", " IS NOT NULL "));
                    break;
            }
        }
        return sb.toString();
    }

    private String conditionalString(boolean condition, String strIfTrue, String strIfFalse) {
        return condition ? strIfTrue : strIfFalse;
    }

    // @formatter:off
    private enum Condition {
        WHERE, 
        EQUALS, 
        EQUALS_IGNORE_CASE, 
        LIKE, 
        LIKE_IGNORE_CASE, 
        IN, 
        GREATER_THAN, 
        GREATER_THAN_OR_EQUAL, 
        LOWER_THAN, 
        LOWER_THAN_OR_EQUAL,
        NULL
    }
    // @formatter:on

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
        private final Boolean negated;

        public Holder(Where where, Operator operator) {
            this.where = where;
            this.column = null;
            this.value = null;
            this.condition = Condition.WHERE;
            this.operator = operator;
            this.negated = null;
        }

        public Holder(String column, Object value, Condition condition, Operator operator, boolean negated) {
            this.where = null;
            this.column = column;
            this.value = value;
            this.condition = condition;
            this.operator = operator;
            this.negated = negated;
        }

        public Holder(String column, Object value, Condition condition, Operator operator) {
            this(column, value, condition, operator, false);
        }
    }

}
