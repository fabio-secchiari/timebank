package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.model.Valutazione;
import org.fabiojava.timebank.domain.ports.database.InsertPort;
import org.fabiojava.timebank.domain.ports.database.QueryPort;
import org.fabiojava.timebank.domain.ports.repositories.ValutazioneRepository;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.DeleteSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.InsertSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.QuerySpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.UpdateSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SqlServerValutazioneRepositoryImpl implements ValutazioneRepository {
    private final QueryPort queryPort;
    private final InsertPort insertPort;

    @Autowired
    public SqlServerValutazioneRepositoryImpl(QueryPort queryPort, InsertPort insertPort) {
        this.queryPort = queryPort;
        this.insertPort = insertPort;
    }

    @Override
    public Long save(Valutazione valutazione) {
        InsertSpecification spec = new InsertSpecification();
        spec.into("valutazioni")
                .value("punteggio", valutazione.getPunteggio())
                .value("commento", valutazione.getCommento())
                .value("data_valutazione", valutazione.getDataValutazione())
                .value("tipo_valutatore", valutazione.getTipoValutatore().name())
                .value("id_prenotazione", valutazione.getIdPrenotazione());
        return insertPort.execute(spec, Valutazione.class).getGeneratedId("id");
    }

    @Override
    public Optional<Valutazione> findById(Integer id) {
        QuerySpecification spec = new QuerySpecification();
        spec.from("valutazioni")
                .where("id", "=", id);
        return queryPort.executeSingle(spec, Valutazione.class);
    }

    @Override
    public List<Valutazione> findByUtente(String username) {
        QuerySpecification spec = new QuerySpecification();
        spec.addSelect("v.*")
                .from("valutazioni v")
                .join("prenotazioni p", "p.id_prenotazione = v.id_prenotazione", QuerySpecification.JoinClause.JoinType.INNER)
                .join("richieste r", "r.id_richiesta = p.id_richiesta", QuerySpecification.JoinClause.JoinType.LEFT)
                .join("offerte o", "o.id_offerta = p.id_offerta", QuerySpecification.JoinClause.JoinType.LEFT)
            .where("CASE WHEN v.tipo_valutatore = 'RICHIEDENTE' THEN r.matricola_richiedente ELSE o.matricola_offerente END", "=", username);
        return queryPort.execute(spec, Valutazione.class);
    }

    @Override
    public List<Valutazione> findAll() {
        QuerySpecification spec = new QuerySpecification();
        spec.from("valutazioni");
        return queryPort.execute(spec, Valutazione.class);
    }

    @Override
    public void delete(Integer id) {
        DeleteSpecification spec = new DeleteSpecification();
        spec.from("valutazioni")
                .where("id", "=", id);
        insertPort.delete(spec, Valutazione.class);
    }

    // non aggiorno tipo valutatore e id_prenotazione
    @Override
    public void update(Valutazione valutazione) {
        UpdateSpecification spec = new UpdateSpecification();
        spec.set("punteggio", valutazione.getPunteggio())
                .set("commento", valutazione.getCommento())
                .set("data_valutazione", valutazione.getDataValutazione())
            .where("id", "=", valutazione.getId());
        insertPort.update(spec, Valutazione.class);
    }
}
