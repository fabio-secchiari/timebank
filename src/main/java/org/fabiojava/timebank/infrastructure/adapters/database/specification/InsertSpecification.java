package org.fabiojava.timebank.infrastructure.adapters.database.specification;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class InsertSpecification {
    private String tableName;
    private Map<String, Object> values = new LinkedHashMap<>();

    public InsertSpecification into(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public InsertSpecification value(String column, Object value) {
        values.put(column, value);
        return this;
    }

    public InsertSpecification values(Map<String, Object> values) {
        this.values.putAll(values);
        return this;
    }
}