package org.fabiojava.timebank.infrastructure.adapters.mapper;

import lombok.NoArgsConstructor;
import org.fabiojava.timebank.domain.model.Valutazione;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class ValutazioneMapper {

    public static Valutazione toEntity(ResultSet resultSet) throws SQLException {
        return new Valutazione(
                resultSet.getLong("id"),
                resultSet.getInt("punteggio"),
                resultSet.getString("commento"),
                resultSet.getTimestamp("data_valutazione").toLocalDateTime(),
                Valutazione.TipoValutatore.valueOf(resultSet.getString("tipo_valutatore")),
                resultSet.getLong("id_prenotazione")
        );
    }
}
