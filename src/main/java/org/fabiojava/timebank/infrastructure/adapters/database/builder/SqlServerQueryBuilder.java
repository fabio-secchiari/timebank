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

        spec.getPostUnionWhereClauses().forEach(w -> allParams.add(w.getValue()));

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

    private static String buildOrderByClause(QuerySpecification spec) {
        if (spec.getOrderBy() != null && !spec.getOrderBy().isEmpty()) {
            return " ORDER BY " + spec.getOrderBy().stream()
                    .map(o -> o.getField() + (o.isAscending() ? " ASC" : " DESC"))
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    private static String buildFinalQuery(QuerySpecification spec, List<String> queryStrings) {
        String combinedQuery;
        if (spec.isUnion()) {
            combinedQuery = String.join(" UNION ", queryStrings);

            if (!spec.getPostUnionWhereClauses().isEmpty() || !spec.getPostUnionSelectFields().isEmpty()) {
                StringBuilder selectClause = new StringBuilder("SELECT ");
                if (spec.getPostUnionSelectFields().isEmpty()) {
                    selectClause.append("*");
                } else {
                    selectClause.append(String.join(", ", spec.getPostUnionSelectFields()));
                }

                combinedQuery = selectClause + " FROM (" + combinedQuery + ") AS union_result";

                if (!spec.getPostUnionWhereClauses().isEmpty()) {
                    StringBuilder whereClause = new StringBuilder(" WHERE ");
                    List<String> conditions = spec.getPostUnionWhereClauses().stream()
                            .map(w -> w.getField() + " " + w.getOperator() + " ?")
                            .collect(Collectors.toList());
                    whereClause.append(String.join(" AND ", conditions));
                    combinedQuery += whereClause;
                }
            }
        } else {
            combinedQuery = queryStrings.getFirst();
        }

        if (spec.getOrderBy() != null && !spec.getOrderBy().isEmpty()) {
            combinedQuery += buildOrderByClause(spec);
        }

        if (spec.getOffset() != null || spec.getLimit() != null) {
        StringBuilder paginationClause = new StringBuilder();

            if (spec.getOffset() != null) {
                paginationClause.append(" OFFSET ").append(spec.getOffset()).append(" ROWS");
            } else {
                paginationClause.append(" OFFSET 0 ROWS");
            }

            if (spec.getLimit() != null) {
                paginationClause.append(" FETCH NEXT ").append(spec.getLimit()).append(" ROWS ONLY");
            }

            // Se non c'è già un ORDER BY, ne aggiungiamo uno fittizio per SQL Server
            if (spec.getOrderBy() == null || spec.getOrderBy().isEmpty()) {
                combinedQuery += " ORDER BY (SELECT NULL)";
            }

            combinedQuery += paginationClause;
        }

        return combinedQuery;
    }

}
