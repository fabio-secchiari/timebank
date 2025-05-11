package org.fabiojava.timebank.infrastructure.adapters.mapper;

import lombok.NoArgsConstructor;
import org.fabiojava.timebank.domain.model.Richiesta;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class RichiestaMapper {

    public static Richiesta toEntity(ResultSet resultSet) throws SQLException {
        return new Richiesta(
                resultSet.getInt("id_richiesta"),
                resultSet.getString("matricola_richiedente"),
                resultSet.getInt("id_attivita"),
                resultSet.getDate("data_richiesta_inizio"),
                resultSet.getDate("data_richiesta_fine"),
                resultSet.getInt("ore_richieste"),
                Richiesta.StatoRichiesta.valueOf(resultSet.getString("stato")),
                resultSet.getString("note"),
                resultSet.getTimestamp("data_inserimento"),
                Richiesta.PrioritaRichiesta.valueOf(resultSet.getString("priorita"))
        );
    }
}
