package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.model.Inserimento;
import org.fabiojava.timebank.domain.model.Prenotazione;
import org.fabiojava.timebank.domain.ports.database.InsertPort;
import org.fabiojava.timebank.domain.ports.database.QueryPort;
import org.fabiojava.timebank.domain.ports.repositories.PrenotazioneRepository;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.DeleteSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.InsertSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.QuerySpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.UpdateSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SqlServerPrenotazioneRepositoryImpl implements PrenotazioneRepository {
    private final QueryPort queryPort;
    private final InsertPort insertPort;

    @Autowired
    public SqlServerPrenotazioneRepositoryImpl(QueryPort queryPort, InsertPort insertPort) {
        this.queryPort = queryPort;
        this.insertPort = insertPort;
    }

    @Override
    public Long save(Prenotazione prenotazione) {
        InsertSpecification spec = new InsertSpecification();
        spec.into("prenotazioni")
                .value("id_offerta", prenotazione.getIdOfferta())
                .value("id_richiesta", prenotazione.getIdRichiesta())
                .value("data_matching", prenotazione.getDataMatching())
                .value("ore_concordate", prenotazione.getOreConcordate())
                .value("stato", prenotazione.getStato().name())
                .value("note_feedback", prenotazione.getNoteFeedback());
        if(prenotazione.getDataEsecuzione() != null)
            spec.value("data_esecuzione", prenotazione.getDataEsecuzione());
        return insertPort.execute(spec, Prenotazione.class).getGeneratedId("GENERATED_KEYS");
    }

    @Override
    public Prenotazione findById(Long id) {
        QuerySpecification spec = new QuerySpecification();
        spec.from("prenotazioni")
                .where("id_prenotazione", "=", id);
        return queryPort.executeSingle(spec, Prenotazione.class).orElse(null);
    }

    @Override
    public List<Prenotazione> findByIdRichiesta(Long id) {
        QuerySpecification spec = new QuerySpecification();
        spec.from("prenotazioni")
                .where("id_richiesta", "=", id);
        return queryPort.execute(spec, Prenotazione.class);
    }

    @Override
    public List<Prenotazione> findByIdOfferta(Long id) {
        QuerySpecification spec = new QuerySpecification();
        spec.from("prenotazioni")
                .where("id_offerta", "=", id);
        return queryPort.execute(spec, Prenotazione.class);
    }

    @Override
    public List<Prenotazione> findByUtente(String matricola, Inserimento.TIPO_INSERIMENTO tipoInserimento) {
        QuerySpecification spec = new QuerySpecification();
        if(tipoInserimento == Inserimento.TIPO_INSERIMENTO.RICHIESTA) {
            spec.from("prenotazioni p")
                    .join("richieste r", "r.id_richiesta = p.id_richiesta", QuerySpecification.JoinClause.JoinType.INNER)
                .where("matricola_richiedente", "=", matricola);
        }else{
            spec.from("prenotazioni p")
                    .join("offerte o", "o.id_offerta = p.id_offerta", QuerySpecification.JoinClause.JoinType.INNER)
                .where("matricola_offerente", "=", matricola);
        }
        return queryPort.execute(spec, Prenotazione.class);
    }

    @Override
    public List<Prenotazione> findAll() {
        QuerySpecification spec = new QuerySpecification();
        spec.from("prenotazioni");
        return queryPort.execute(spec, Prenotazione.class);
    }

    @Override
    public void delete(Long id) {
        DeleteSpecification spec = new DeleteSpecification();
        spec.from("prenotazioni")
                .where("id_prenotazione", "=", id);
        insertPort.delete(spec, Prenotazione.class);
    }

    @Override
    public void update(Prenotazione prenotazione) {
        UpdateSpecification spec = new UpdateSpecification();
        spec.set("stato", prenotazione.getStato().name())
                .set("id_offerta", prenotazione.getIdOfferta())
                .set("id_richiesta", prenotazione.getIdRichiesta())
                .set("data_matching", prenotazione.getDataMatching())
                .set("ore_concordate", prenotazione.getOreConcordate())
                .set("data_esecuzione", prenotazione.getDataEsecuzione())
                .set("note_feedback", prenotazione.getNoteFeedback())
            .table("prenotazioni")
                .where("id_prenotazione", "=", prenotazione.getIdPrenotazione());
        insertPort.update(spec, Prenotazione.class);
    }
}
