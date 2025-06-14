package org.fabiojava.timebank.infrastructure.adapters.database.builder;

import org.fabiojava.timebank.infrastructure.adapters.database.data.QueryData;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.UpdateSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.fabiojava.timebank.infrastructure.adapters.database.builder.SqlServerQueryBuilder.buildWhereClause;

public class SqlServerUpdateBuilder {
    public static QueryData buildUpdate(UpdateSpecification spec) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(spec.getTableName());
        sql.append(" SET ");
        String setClause = spec.getValues().keySet().stream()
                .map(column -> column + " = ?")
                .collect(Collectors.joining(", "));
        sql.append(setClause);

        buildWhereClause(sql, spec.getWhereClauses());

        List<Object> params = new ArrayList<>(spec.getValues().values());
        spec.getWhereClauses().forEach(where -> params.add(where.value()));

        return new QueryData(sql.toString(), params.toArray());
    }
}