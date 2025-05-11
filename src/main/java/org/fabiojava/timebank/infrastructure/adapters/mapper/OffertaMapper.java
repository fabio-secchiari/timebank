package org.fabiojava.timebank.infrastructure.adapters.mapper;

import lombok.NoArgsConstructor;
import org.fabiojava.timebank.domain.model.Offerta;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class OffertaMapper {

    public static Offerta toEntity(ResultSet resultSet) throws SQLException {
        return new Offerta(
                resultSet.getInt("id_offerta"),
                resultSet.getString("matricola_offerente"),
                resultSet.getInt("id_attivita"),
                resultSet.getDate("data_disponibilita_inizio"),
                resultSet.getDate("data_disponibilita_fine"),
                resultSet.getInt("ore_disponibili"),
                Offerta.StatoOfferta.valueOf(resultSet.getString("stato")),
                resultSet.getString("note"),
                resultSet.getTimestamp("data_inserimento")
        );
    }
}
