package org.fabiojava.timebank.infrastructure.adapters.database.data;

import java.util.Map;
import java.util.Optional;

public class InsertResult {
    Map<String, Object> generatedKeys;
    int affectedRows;

    public InsertResult(Map<String, Object> generatedKeys, int affectedRows) {
        this.generatedKeys = generatedKeys;
        this.affectedRows = affectedRows;
    }

    public Optional<Object> getGeneratedKey(String keyName) {
        return Optional.ofNullable(generatedKeys.get(keyName));
    }

    public <T> T getGeneratedKey(String keyName, Class<T> type) {
        return type.cast(generatedKeys.get(keyName));
    }

    public Long getGeneratedId(String id) {
        return getGeneratedKey(id, Long.class);
    }
}