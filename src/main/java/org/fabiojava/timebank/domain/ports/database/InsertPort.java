package org.fabiojava.timebank.domain.ports.database;

import org.fabiojava.timebank.infrastructure.adapters.database.data.InsertResult;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.DeleteSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.InsertSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.UpdateSpecification;

public interface InsertPort {
    <T> InsertResult execute(InsertSpecification spec, Class<T> entityType);
    <T> void delete(DeleteSpecification spec, Class<T> entityType);
    <T> void update(UpdateSpecification spec, Class<T> entityType);
}
