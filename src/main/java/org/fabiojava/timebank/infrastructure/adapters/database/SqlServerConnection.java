package org.fabiojava.timebank.infrastructure.adapters.database;

import lombok.NoArgsConstructor;
import org.fabiojava.timebank.domain.exceptions.DatabaseException;
import org.fabiojava.timebank.domain.ports.database.DatabaseConnection;
import org.fabiojava.timebank.domain.ports.mapper.QueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Component
@Primary
@NoArgsConstructor
public class SqlServerConnection implements DatabaseConnection {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SqlServerConnection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean isConnected() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public <T> Optional<T> executeQuery(String query, QueryMapper<T> mapper, Object... params)
            throws DatabaseException {
        try {
            List<T> results = jdbcTemplate.query(query, (rs, rowNum) -> {
                try {
                    return mapper.map(rs);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, params);
            return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
        } catch (Exception e) {
            throw new DatabaseException("Errore nell'esecuzione della query: " + query, e);
        }

    }

    @Override
    public int executeUpdate(String query, Object... params) throws DatabaseException {
        try {
            return jdbcTemplate.update(query, params);
        } catch (Exception e) {
            throw new DatabaseException("Errore nell'esecuzione dell'update: " + query, e);
        }

    }
}