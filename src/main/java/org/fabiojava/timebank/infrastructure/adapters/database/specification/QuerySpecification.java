package org.fabiojava.timebank.infrastructure.adapters.database.specification;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class QuerySpecification {
    private List<SelectQuery> queries = new ArrayList<>();

    private List<OrderByClause> orderBy = new ArrayList<>();
    private List<String> postUnionSelectFields = new ArrayList<>();
    private List<WhereClause> postUnionWhereClauses = new ArrayList<>();
    private boolean isUnion = false;
    private Integer offset;
    private Integer limit;

    public QuerySpecification() {
        queries.add(new SelectQuery());
    }

    public QuerySpecification(QuerySpecification src) {
        this.queries = new ArrayList<>();
        for (SelectQuery query : src.queries) {
            SelectQuery newQuery = new SelectQuery();
            newQuery.selectFields.addAll(query.selectFields);
            newQuery.fromTable = query.fromTable;
            // Deep copy dei joins
            for (JoinClause join : query.joins) {
                newQuery.joins.add(new JoinClause(
                        join.table(),
                        join.condition(),
                        join.type(),
                        new ArrayList<>(join.parameters())
                ));
            }
            // Deep copy delle where clauses
            for (WhereClause where : query.whereClauses) {
                newQuery.whereClauses.add(new WhereClause(
                        where.field(),
                        where.operator(),
                        where.value()
                ));
            }
            this.queries.add(newQuery);
        }
        this.orderBy = new ArrayList<>();
        for (OrderByClause order : src.orderBy) {
            this.orderBy.add(new OrderByClause(
                    order.field(),
                    order.ascending()
            ));
        }
        this.postUnionSelectFields = new ArrayList<>(src.postUnionSelectFields);
        this.postUnionWhereClauses = new ArrayList<>();
        for (WhereClause where : src.postUnionWhereClauses) {
            this.postUnionWhereClauses.add(new WhereClause(
                    where.field(),
                    where.operator(),
                    where.value()
            ));
        }
        this.isUnion = src.isUnion;
        this.offset = src.offset;
        this.limit = src.limit;
    }

    @Getter
    public static class SelectQuery {
        private final List<String> selectFields = new ArrayList<>();
        private String fromTable;
        private final List<JoinClause> joins = new ArrayList<>();
        private final List<WhereClause> whereClauses = new ArrayList<>();
    }

    public record JoinClause(String table, String condition, QuerySpecification.JoinClause.JoinType type,
                             List<Object> parameters) {public enum JoinType {
            INNER, LEFT, RIGHT
        }}
    public record WhereClause(String field, String operator, Object value) {}
    public record OrderByClause(String field, boolean ascending) {}

    public QuerySpecification addSelect(String... fields) {
        getCurrentQuery().selectFields.addAll(Arrays.asList(fields));
        return this;
    }

    public QuerySpecification addSelectOnUnion(String... fields) {
        this.postUnionSelectFields.addAll(Arrays.asList(fields));
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

    public QuerySpecification whereOnUnion(String field, String operator, Object value) {
        this.postUnionWhereClauses.add(new WhereClause(field, operator, value));
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

    public QuerySpecification offset(int offset) {
        this.offset = offset;
        return this;
    }


    private SelectQuery getCurrentQuery() {
        return queries.getLast();
    }
}