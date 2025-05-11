package org.fabiojava.timebank.infrastructure.adapters.database.builder;

import org.fabiojava.timebank.infrastructure.adapters.database.data.QueryData;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.QuerySpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlServerQueryBuilder {
    public static QueryData buildQuery(QuerySpecification spec) {
        List<Object> allParams = new ArrayList<>();
        List<String> queryStrings = new ArrayList<>();

        for (QuerySpecification.SelectQuery query : spec.getQueries()) {
            StringBuilder sql = new StringBuilder();
            buildSelectClause(query, sql);
            buildFromClause(query, sql);
            buildJoinClauses(query, sql);
            buildWhereClause(query, sql, allParams);
            queryStrings.add(sql.toString());
        }

        String finalQuery = buildFinalQuery(spec, queryStrings);
        return new QueryData(finalQuery, allParams.toArray());
    }

    private static void buildSelectClause(QuerySpecification.SelectQuery query, StringBuilder sql) {
        sql.append("SELECT ");
        if (query.getSelectFields().isEmpty()) {
            sql.append("*");
        } else {
            sql.append(String.join(", ", query.getSelectFields()));
        }
    }

    private static void buildFromClause(QuerySpecification.SelectQuery query, StringBuilder sql) {
        sql.append(" FROM ").append(query.getFromTable());
    }

    private static void buildJoinClauses(QuerySpecification.SelectQuery query, StringBuilder sql) {
        for (QuerySpecification.JoinClause join : query.getJoins()) {
            sql.append(" ").append(join.getType().name())
                    .append(" JOIN ").append(join.getTable())
                    .append(" ON ").append(join.getCondition());
        }
    }

    private static void buildWhereClause(QuerySpecification.SelectQuery query, StringBuilder sql, List<Object> params) {
        if (!query.getWhereClauses().isEmpty()) {
            sql.append(" WHERE ");
            List<String> conditions = query.getWhereClauses().stream()
                    .map(w -> {
                        params.add(w.getValue());
                        return w.getField() + " " + w.getOperator() + " ?";
                    })
                    .collect(Collectors.toList());
            sql.append(String.join(" AND ", conditions));
        }
    }

    public static void buildWhereClause(StringBuilder sql, List<QuerySpecification.WhereClause> whereClauses) {
        if (!whereClauses.isEmpty()) {
            sql.append(" WHERE ");
            List<String> conditions = whereClauses.stream()
                    .map(w -> w.getField() + " " + w.getOperator() + " ?")
                    .collect(Collectors.toList());
            sql.append(String.join(" AND ", conditions));
        }
    }

    private static String buildFinalQuery(QuerySpecification spec, List<String> queryStrings) {
        String combinedQuery;
        if (spec.isUnion()) {
            combinedQuery = String.join(" UNION ", queryStrings);
        } else {
            combinedQuery = queryStrings.getFirst();
        }
        if (spec.getLimit() != null) {
            combinedQuery = "SELECT TOP " + spec.getLimit() + " * FROM (" + combinedQuery + ") AS combined";
        }
        if (!spec.getOrderBy().isEmpty()) {
            combinedQuery += " ORDER BY " + spec.getOrderBy().stream()
                    .map(o -> o.getField() + (o.isAscending() ? " ASC" : " DESC"))
                    .collect(Collectors.joining(", "));
        }
        return combinedQuery;
    }

}
