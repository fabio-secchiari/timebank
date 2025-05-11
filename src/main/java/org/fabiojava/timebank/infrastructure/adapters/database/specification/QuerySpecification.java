package org.fabiojava.timebank.infrastructure.adapters.database.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class QuerySpecification {
    private List<SelectQuery> queries = new ArrayList<>();
    private Integer limit;
    private List<OrderByClause> orderBy = new ArrayList<>();
    private boolean isUnion = false;
    private Integer offset;

    public QuerySpecification() {
        queries.add(new SelectQuery());
    }

    @Getter
    public static class SelectQuery {
        private List<String> selectFields = new ArrayList<>();
        private String fromTable;
        private List<JoinClause> joins = new ArrayList<>();
        private List<WhereClause> whereClauses = new ArrayList<>();
    }

    @Getter
    @AllArgsConstructor
    public static class JoinClause {
        private final String table;
        private final String condition;
        private final JoinType type;
        private final List<Object> parameters;

        public enum JoinType {
            INNER, LEFT, RIGHT
        }
    }

    @Getter
    @AllArgsConstructor
    public static class WhereClause {
        private final String field;
        private final String operator;
        private final Object value;
    }

    @Getter
    @AllArgsConstructor
    public static class OrderByClause {
        private final String field;
        private final boolean ascending;
    }

    public QuerySpecification addSelect(String... fields) {
        getCurrentQuery().selectFields.addAll(Arrays.asList(fields));
        return this;
    }

    public QuerySpecification from(String table) {
        getCurrentQuery().fromTable = table;
        return this;
    }

    public QuerySpecification join(String table, String condition, JoinClause.JoinType type, Object... parameters) {
        getCurrentQuery().joins.add(new JoinClause(table, condition, type,
                parameters != null ? Arrays.asList(parameters) : new ArrayList<>()));
        return this;
    }

    public QuerySpecification where(String field, String operator, Object value) {
        getCurrentQuery().whereClauses.add(new WhereClause(field, operator, value));
        return this;
    }

    public QuerySpecification orderBy(String field, boolean ascending) {
        this.orderBy.add(new OrderByClause(field, ascending));
        return this;
    }

    public QuerySpecification union() {
        this.isUnion = true;
        this.queries.add(new SelectQuery());
        return this;
    }

    public QuerySpecification limit(int limit) {
        this.limit = limit;
        return this;
    }

    private SelectQuery getCurrentQuery() {
        return queries.getLast();
    }
}