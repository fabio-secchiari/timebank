package org.fabiojava.timebank.infrastructure.adapters.mapper;

import lombok.NoArgsConstructor;
import org.fabiojava.timebank.domain.model.Richiesta;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class RichiestaMapper {

    public static Richiesta toEntity(ResultSet resultSet) throws SQLException {
        return new Richiesta(
                resultSet.getLong("id_richiesta"),
                resultSet.getString("matricola_richiedente"),
                resultSet.getLong("id_attivita"),
                resultSet.getDate("data_richiesta_inizio").toLocalDate(),
                resultSet.getDate("data_richiesta_fine").toLocalDate(),
                resultSet.getInt("ore_richieste"),
                Richiesta.StatoRichiesta.valueOf(resultSet.getString("stato")),
                Richiesta.PrioritaRichiesta.valueOf(resultSet.getString("priorita")),
                resultSet.getString("note"),
                resultSet.getTimestamp("data_inserimento").toLocalDateTime()
        );
    }
}
