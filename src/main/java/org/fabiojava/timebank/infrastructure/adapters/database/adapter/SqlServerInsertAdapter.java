package org.fabiojava.timebank.infrastructure.adapters.database.adapter;

import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.exceptions.DatabaseException;
import org.fabiojava.timebank.domain.ports.database.InsertPort;
import org.fabiojava.timebank.infrastructure.adapters.database.builder.SqlServerDeleteBuilder;
import org.fabiojava.timebank.infrastructure.adapters.database.builder.SqlServerInsertBuilder;
import org.fabiojava.timebank.infrastructure.adapters.database.builder.SqlServerUpdateBuilder;
import org.fabiojava.timebank.infrastructure.adapters.database.data.InsertResult;
import org.fabiojava.timebank.infrastructure.adapters.database.data.QueryData;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.DeleteSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.InsertSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.UpdateSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Log
@Component
public class SqlServerInsertAdapter implements InsertPort {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SqlServerInsertAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override // entityType è qui per implementazioni/migliorie future
    public <T> InsertResult execute(InsertSpecification spec, Class<T> entityType) {
        QueryData queryData = SqlServerInsertBuilder.buildInsert(spec);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int affectedRows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    queryData.getQuery(),
                    Statement.RETURN_GENERATED_KEYS
            );
            for (int i = 0; i < queryData.getParameters().length; i++) {
                ps.setObject(i + 1, queryData.getParameters()[i]);
            }
            return ps;
        }, keyHolder);

        Map<String, Object> generatedKeys = new HashMap<>();
        if (keyHolder.getKeys() != null) {
            generatedKeys.putAll(keyHolder.getKeys());
        }

        return new InsertResult(generatedKeys, affectedRows);
    }

    @Override // entityType è qui per implementazioni/migliorie future
    public <T> void delete(DeleteSpecification spec, Class<T> entityType) {
        QueryData queryData = SqlServerDeleteBuilder.buildDelete(spec);
        try {
            jdbcTemplate.update(queryData.getQuery(), queryData.getParameters());
        } catch (Exception e) {
            log.severe("Errore nell'esecuzione della query: " + e.getMessage());
            throw new DatabaseException("Errore nell'esecuzione della query", e);
        }
    }

    @Override // entityType è qui per implementazioni/migliorie future
    public <T> void update(UpdateSpecification spec, Class<T> entityType) {
        QueryData queryData = SqlServerUpdateBuilder.buildUpdate(spec);
        try {
            jdbcTemplate.update(queryData.getQuery(), queryData.getParameters());
        } catch (Exception e) {
            log.severe("Errore nell'esecuzione della query: " + e.getMessage());
            throw new DatabaseException("Errore nell'esecuzione della query", e);
        }
    }
}
