package org.fabiojava.timebank.infrastructure.adapters.database.specification;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UpdateSpecification {
    private String tableName;
    private Map<String, Object> values = new LinkedHashMap<>();
    private List<QuerySpecification.WhereClause> whereClauses = new ArrayList<>();

    public UpdateSpecification table(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public UpdateSpecification set(String column, Object value) {
        values.put(column, value);
        return this;
    }

    public UpdateSpecification where(String field, String operator, Object value) {
        whereClauses.add(new QuerySpecification.WhereClause(field, operator, value));
        return this;
    }
}