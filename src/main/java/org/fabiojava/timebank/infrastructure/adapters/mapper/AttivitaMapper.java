package org.fabiojava.timebank.infrastructure.adapters.mapper;

import lombok.NoArgsConstructor;
import org.fabiojava.timebank.domain.model.Attivita;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class AttivitaMapper {
    public static String toCategory(ResultSet resultSet) throws SQLException {
        return resultSet.getString("categoria");
    }

    public static Attivita toEntity(ResultSet resultSet) throws SQLException {
        return new Attivita(
                resultSet.getInt("id_attivita"),
                resultSet.getString("nome_attivita"),
                resultSet.getString("descrizione"),
                resultSet.getInt("durata_minima_ore"),
                resultSet.getInt("durata_massima_ore"),
                resultSet.getString("categoria")
        );
    }
}
