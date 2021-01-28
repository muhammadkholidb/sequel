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
        andEquals(column, value);
        return this;
    }

    public Where notEquals(String column, Object value) {
        andNotEquals(column, value);
        return this;
    }

    public Where equalsIgnoreCase(String column, Object value) {
        andEqualsIgnoreCase(column, value);
        return this;
    }

    public Where notEqualsIgnoreCase(String column, Object value) {
        andNotEqualsIgnoreCase(column, value);
        return this;
    }

    public Where like(String column, Object value) {
        andLike(column, value);
        return this;
    }

    public Where notLike(String column, Object value) {
        andNotLike(column, value);
        return this;
    }

    public Where likeIgnoreCase(String column, Object value) {
        andLikeIgnoreCase(column, value);
        return this;
    }

    public Where notLikeIgnoreCase(String column, Object value) {
        andNotLikeIgnoreCase(column, value);
        return this;
    }

    public Where in(String column, List<?> values) {
        andIn(column, values);
        return this;
    }

    public Where notIn(String column, List<?> values) {
        andNotIn(column, values);
        return this;
    }

    public Where contains(String column, Object value) {
        andContains(column, value);
        return this;
    }

    public Where notContains(String column, Object value) {
        andNotContains(column, value);
        return this;
    }

    public Where containsIgnoreCase(String column, Object value) {
        andContainsIgnoreCase(column, value);
        return this;
    }

    public Where notContainsIgnoreCase(String column, Object value) {
        andNotContainsIgnoreCase(column, value);
        return this;
    }

    public Where startsWith(String column, Object value) {
        andStartsWith(column, value);
        return this;
    }

    public Where notStartsWith(String column, Object value) {
        andNotStartsWith(column, value);
        return this;
    }

    public Where startsWithIgnoreCase(String column, Object value) {
        andStartsWithIgnoreCase(column, value);
        return this;
    }

    public Where notStartsWithIgnoreCase(String column, Object value) {
        andNotStartsWithIgnoreCase(column, value);
        return this;
    }

    public Where endsWith(String column, Object value) {
        andEndsWith(column, value);
        return this;
    }

    public Where notEndsWith(String column, Object value) {
        andNotEndsWith(column, value);
        return this;
    }

    public Where endsWithIgnoreCase(String column, Object value) {
        andEndsWithIgnoreCase(column, value);
        return this;
    }

    public Where notEndsWithIgnoreCase(String column, Object value) {
        andNotEndsWithIgnoreCase(column, value);
        return this;
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

    public Where or(Where where) {
        holders.add(new Holder(where, Operator.OR));
        return this;
    }

    public List<Object> getValues() {
        List<Object> values = new ArrayList<>();
        holders.forEach(holder -> {
            Object value = holder.getValue();
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
            }
        }
        return sb.toString();
    }

    private String conditionalString(boolean condition, String strIfTrue, String strIfFalse) {
        return condition ? strIfTrue : strIfFalse;
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
