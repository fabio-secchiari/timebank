package org.fabiojava.timebank.infrastructure.adapters.database.builder;

import org.fabiojava.timebank.infrastructure.adapters.database.data.QueryData;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.InsertSpecification;

import java.util.Collections;

public class SqlServerInsertBuilder {
    public static QueryData buildInsert(InsertSpecification spec) {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(spec.getTableName()).append(" (");
        String columns = String.join(", ", spec.getValues().keySet());
        sql.append(columns);

        sql.append(") VALUES (");
        String placeholders = String.join(", ",
                Collections.nCopies(spec.getValues().size(), "?"));
        sql.append(placeholders).append(")");
        Object[] params = spec.getValues().values().toArray();

        return new QueryData(sql.toString(), params);
    }
}
