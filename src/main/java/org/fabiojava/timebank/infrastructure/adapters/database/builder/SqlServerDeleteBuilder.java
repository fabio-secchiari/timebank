package org.fabiojava.timebank.infrastructure.adapters.database.builder;

import org.fabiojava.timebank.infrastructure.adapters.database.data.QueryData;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.DeleteSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.QuerySpecification;

import java.util.ArrayList;
import java.util.List;

import static org.fabiojava.timebank.infrastructure.adapters.database.builder.SqlServerQueryBuilder.buildWhereClause;

public class SqlServerDeleteBuilder {
    public static String createDelete(DeleteSpecification spec) {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(spec.getFromTable());
        buildWhereClause(sql, spec.getWhereClauses());
        return sql.toString();
    }

    private static Object[] extractParams(DeleteSpecification spec) {
        List<Object> params = new ArrayList<>();
        spec.getWhereClauses().stream()
                .map(QuerySpecification.WhereClause::getValue)
                .forEach(params::add);
        return params.toArray();
    }

    public static QueryData buildDelete(DeleteSpecification spec) {
        String query = createDelete(spec);
        Object[] params = extractParams(spec);
        return new QueryData(query, params);
    }
}
