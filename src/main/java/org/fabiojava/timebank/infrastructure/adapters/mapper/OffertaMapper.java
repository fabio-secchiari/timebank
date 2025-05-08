package org.fabiojava.timebank.infrastructure.adapters.mapper;

import lombok.NoArgsConstructor;
import org.fabiojava.timebank.domain.model.Offerta;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class OffertaMapper {

    public static Offerta toEntity(ResultSet resultSet) throws SQLException {
        return new Offerta(
                resultSet.getLong("id_offerta"),
                resultSet.getString("matricola_offerente"),
                resultSet.getLong("id_attivita"),
                resultSet.getDate("data_disponibilita_inizio").toLocalDate(),
                resultSet.getDate("data_disponibilita_fine").toLocalDate(),
                resultSet.getInt("ore_disponibili"),
                Offerta.StatoOfferta.valueOf(resultSet.getString("stato")),
                resultSet.getString("note"),
                resultSet.getTimestamp("data_inserimento").toLocalDateTime()
        );
    }
}
