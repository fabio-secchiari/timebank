package org.fabiojava.timebank.domain.ports.database;

import org.fabiojava.timebank.domain.exceptions.DatabaseException;
import org.fabiojava.timebank.domain.ports.mapper.QueryMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface DatabaseConnection {
    boolean isConnected();
    <T> List<T> executeQuery(String query, QueryMapper<T> mapper, Object... params) throws DatabaseException;
    Map<String, Object> executeInsert(String query, Object... params) throws DatabaseException;
    int executeUpdate(String query, Object... params) throws DatabaseException;
}

