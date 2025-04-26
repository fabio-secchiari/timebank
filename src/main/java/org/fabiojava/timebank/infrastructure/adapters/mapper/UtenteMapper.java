package org.fabiojava.timebank.infrastructure.adapters.mapper;

import lombok.NoArgsConstructor;
import org.fabiojava.timebank.domain.model.Utente;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class UtenteMapper {

    public static Utente toEntity(ResultSet resultSet) throws SQLException {
        return new Utente(
                resultSet.getString("matricola"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("nome"),
                resultSet.getString("cognome"),
                resultSet.getString("email"),
                resultSet.getString("indirizzo"),
                resultSet.getString("telefono"),
                resultSet.getTimestamp("data_registrazione").toLocalDateTime(),
                resultSet.getInt("ore_totali")
        );
    }
}
