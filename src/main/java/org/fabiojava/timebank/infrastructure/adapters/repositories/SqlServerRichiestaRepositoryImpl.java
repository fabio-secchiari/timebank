package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.model.Richiesta;
import org.fabiojava.timebank.domain.ports.database.InsertPort;
import org.fabiojava.timebank.domain.ports.database.QueryPort;
import org.fabiojava.timebank.domain.ports.repositories.RichiestaRepository;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.DeleteSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.InsertSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.QuerySpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.UpdateSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.fabiojava.timebank.gui.services.InserimentiServiceImpl.LIMIT_DEFAULT;

@Repository
public class SqlServerRichiestaRepositoryImpl implements RichiestaRepository {
    private final QueryPort queryPort;
    private final InsertPort insertPort;

    @Autowired
    public SqlServerRichiestaRepositoryImpl(QueryPort queryPort, InsertPort insertPort) {
        this.queryPort = queryPort;
        this.insertPort = insertPort;
    }

    @Override
    public Long save(Richiesta richiesta) {
        InsertSpecification spec = new InsertSpecification();
        spec.into("richieste")
            .value("matricola_richiedente", richiesta.getMatricolaRichiedente())
                .value("id_attivita", richiesta.getIdAttivita())
                .value("data_richiesta_inizio", richiesta.getDataRichiestaInizio())
                .value("data_richiesta_fine", richiesta.getDataRichiestaFine())
                .value("ore_richieste", richiesta.getOreRichieste())
                .value("stato", richiesta.getStato().name())
                .value("priorita", richiesta.getPriorita().name())
                .value("note", richiesta.getNote())
                .value("data_inserimento", richiesta.getDataInserimento());
        return insertPort.execute(spec, Richiesta.class).getGeneratedId("id_richiesta");
    }

    @Override
    public Optional<Richiesta> findById(Integer id) {
        QuerySpecification spec = new QuerySpecification();
        spec.from("richieste")
                .where("id_richiesta", "=", id);
        return queryPort.executeSingle(spec, Richiesta.class);
    }

    @Override
    public List<Richiesta> findByUtente(String matricola) {
        return findByUtente(matricola, LIMIT_DEFAULT);
    }

    @Override
    public List<Richiesta> findByUtente(String matricola, int limit) {
        QuerySpecification spec = new QuerySpecification();
        spec.from("richieste")
                .where("matricola_richiedente", "=", matricola)
                .limit(limit);
        return queryPort.execute(spec, Richiesta.class);
    }

    @Override
    public List<Richiesta> findAll() {
        QuerySpecification spec = new QuerySpecification();
        spec.from("richieste");
        return queryPort.execute(spec, Richiesta.class);
    }

    @Override
    public void delete(Integer id) {
        DeleteSpecification spec = new DeleteSpecification();
        spec.from("richieste")
            .where("id_richiesta", "=", id);
        insertPort.delete(spec, Richiesta.class);
    }

    @Override
    public void update(Richiesta richiesta) {
        UpdateSpecification spec = new UpdateSpecification();
        spec.set("data_richiesta_inizio", richiesta.getDataRichiestaInizio())
                .set("data_richiesta_fine", richiesta.getDataRichiestaFine())
                .set("ore_richieste", richiesta.getOreRichieste())
                .set("stato", richiesta.getStato().name())
                .set("priorita", richiesta.getPriorita().name())
                .set("note", richiesta.getNote())
            .where("id_richiesta", "=", richiesta.getIdRichiesta());
        insertPort.update(spec, Richiesta.class);
    }
}
