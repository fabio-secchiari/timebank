package org.fabiojava.timebank.infrastructure.adapters.mapper;

import lombok.NoArgsConstructor;
import org.fabiojava.timebank.domain.model.Valutazione;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class ValutazioneMapper {

    public static Valutazione toEntity(ResultSet resultSet) throws SQLException {
        return new Valutazione(
                resultSet.getInt("id"),
                resultSet.getInt("punteggio"),
                resultSet.getString("commento"),
                resultSet.getTimestamp("data_valutazione"),
                Valutazione.TipoValutatore.valueOf(resultSet.getString("tipo_valutatore")),
                resultSet.getInt("id_prenotazione")
        );
    }
}
