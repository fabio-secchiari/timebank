package org.fabiojava.timebank.domain.ports.database;

import org.fabiojava.timebank.infrastructure.adapters.database.specification.QuerySpecification;

import java.util.List;
import java.util.Optional;

public interface QueryPort {
    <T> List<T> execute(QuerySpecification spec, Class<T> resultType);
    <T> Optional<T> executeSingle(QuerySpecification spec, Class<T> resultType);
}