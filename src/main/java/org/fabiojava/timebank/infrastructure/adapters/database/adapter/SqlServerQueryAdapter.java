package org.fabiojava.timebank.infrastructure.adapters.database.adapter;

import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.exceptions.DatabaseException;
import org.fabiojava.timebank.domain.ports.database.QueryPort;
import org.fabiojava.timebank.infrastructure.adapters.database.builder.SqlServerQueryBuilder;
import org.fabiojava.timebank.infrastructure.adapters.database.data.QueryData;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.QuerySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Log
@Component
public class SqlServerQueryAdapter implements QueryPort {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SqlServerQueryAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            fields.addAll(getAllFields(superClass));
        }
        return fields;
    }

    // mapping più generico possibile
    private <T> T mapResultSet(ResultSet rs, Class<T> resultType) {
        try {
            T instance = resultType.getDeclaredConstructor().newInstance();
            List<Field> fields = getAllFields(resultType);

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = toSnakeCase(field.getName());
                
                try {
                    Object value = rs.getObject(fieldName);
                    if (value != null) {
                        if (field.getType().isEnum()) {
                            value = Enum.valueOf((Class<Enum>) field.getType(), value.toString());
                        }
                        field.set(instance, value);
                    }
                } catch (SQLException e) {
                    log.fine("Campo " + fieldName + " non trovato nel ResultSet");
                }
            }
            
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel mapping del ResultSet", e);
        }
    }

    private Object getValueFromResultSet(ResultSet rs, String fieldName, Class<?> fieldType) throws SQLException {
        Object value = rs.getObject(fieldName);

        if (value == null) {
            return null;
        }

        if (fieldType == LocalDateTime.class && value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        }

        if (fieldType == LocalDate.class && value instanceof Date) {
            return ((Date) value).toLocalDate();
        }

        if (fieldType.isEnum() && value instanceof String) {
            @SuppressWarnings("unchecked")
            Class<? extends Enum> enumType = (Class<? extends Enum>) fieldType;
            return Enum.valueOf(enumType, (String) value);
        }

        if (fieldType == Boolean.class && value instanceof Integer) {
            return ((Integer) value) == 1;
        }

        return value;
    }

    private String toSnakeCase(String camelCase) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return camelCase.replaceAll(regex, replacement).toLowerCase();
    }

    public <T> List<T> execute(QuerySpecification spec, Class<T> resultType) {
        QueryData queryData = SqlServerQueryBuilder.buildQuery(spec);

        try {
            return jdbcTemplate.query(queryData.getQuery(), queryData.getParameters(), (rs, rowNum) -> mapResultSet(rs, resultType));
        } catch (Exception e) {
            log.severe("Errore nell'esecuzione della query: " + e.getMessage());
            throw new RuntimeException("Errore nell'esecuzione della query", e);
        }
    }

    @Override
    public <T> Optional<T> executeSingle(QuerySpecification spec, Class<T> resultType) {
        spec.limit(1);

        try {
            List<T> results = execute(spec, resultType);
            return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (IncorrectResultSizeDataAccessException e) {
            log.warning("Query ha restituito più di un risultato quando ne era atteso uno solo");
            return execute(spec, resultType).stream().findFirst();
        } catch (Exception e) {
            throw new DatabaseException("Errore nell'esecuzione della query singola", e);
        }
    }
}