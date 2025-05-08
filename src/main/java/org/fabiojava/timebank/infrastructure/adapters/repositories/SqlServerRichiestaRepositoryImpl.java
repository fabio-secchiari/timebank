package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.model.Richiesta;
import org.fabiojava.timebank.domain.ports.database.DatabaseConnection;
import org.fabiojava.timebank.domain.ports.repositories.RichiestaRepository;
import org.fabiojava.timebank.infrastructure.adapters.mapper.RichiestaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SqlServerRichiestaRepositoryImpl implements RichiestaRepository {
    private final DatabaseConnection databaseConnection;

    @Autowired
    public SqlServerRichiestaRepositoryImpl(DatabaseConnection dbConnection) {
        this.databaseConnection = dbConnection;
    }

    @Override
    public void save(Richiesta richiesta) {
        String sql = "INSERT INTO richieste (matricola_richiedente, id_attivita, data_richiesta_inizio, " +
                "data_richiesta_fine, ore_richieste, stato, priorita, note, data_inserimento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        databaseConnection.executeUpdate(sql,
                richiesta.getMatricolaRichiedente(),
                richiesta.getIdAttivita(),
                richiesta.getDataInizio(),
                richiesta.getDataFine(),
                richiesta.getOreRichieste(),
                richiesta.getStato().name(),
                richiesta.getPriorita().name(),
                richiesta.getNote(),
                richiesta.getDataInserimento());
    }

    @Override
    public Optional<Richiesta> findById(Long id) {
        String sql = "SELECT * FROM richieste WHERE id_richiesta = ?";
        return databaseConnection.executeQuery(sql, RichiestaMapper::toEntity, id)
                .stream().findFirst();
    }

    @Override
    public List<Richiesta> findByUtente(String matricola) {
        String sql = "SELECT * FROM richieste WHERE matricola_richiedente = ?";
        return databaseConnection.executeQuery(sql, RichiestaMapper::toEntity, matricola)
                .stream().toList();
    }

    @Override
    public List<Richiesta> findAll() {
        String sql = "SELECT * FROM richieste";
        return databaseConnection.executeQuery(sql, RichiestaMapper::toEntity)
                .stream().toList();
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM richieste WHERE id_richiesta = ?";
        databaseConnection.executeUpdate(sql, id);
    }

    @Override
    public void update(Richiesta richiesta) {
        String sql = "UPDATE FROM richieste SET data_richiesta_inizio = ?, data_richiesta_fine = ?, " +
                "ore_richieste = ?, stato = ?, priorita = ?, note = ? WHERE id_richiesta = ?";
        databaseConnection.executeUpdate(sql,
                richiesta.getDataInizio(),
                richiesta.getDataFine(),
                richiesta.getOreRichieste(),
                richiesta.getStato().name(),
                richiesta.getPriorita().name(),
                richiesta.getNote(),
                richiesta.getIdRichiesta());
    }
}
