package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.model.Offerta;
import org.fabiojava.timebank.domain.ports.database.InsertPort;
import org.fabiojava.timebank.domain.ports.database.QueryPort;
import org.fabiojava.timebank.domain.ports.repositories.OffertaRepository;
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
public class SqlServerOffertaRepositoryImpl implements OffertaRepository {
    private final QueryPort queryPort;
    private final InsertPort insertPort;

    @Autowired
    public SqlServerOffertaRepositoryImpl(QueryPort queryPort, InsertPort insertPort) {
        this.queryPort = queryPort;
        this.insertPort = insertPort;
    }

    @Override
    public Long save(Offerta offerta) {
        InsertSpecification spec = new InsertSpecification();
        spec.into("offerte")
            .value("matricola_offerente", offerta.getMatricolaOfferente())
                .value("id_attivita", offerta.getIdAttivita())
                .value("data_disponibilita_inizio", offerta.getDataDisponibilitaInizio())
                .value("data_disponibilita_fine", offerta.getDataDisponibilitaFine())
                .value("ore_disponibili", offerta.getOreDisponibili())
                .value("stato", offerta.getStato().name())
                .value("note", offerta.getNote())
                .value("data_inserimento", offerta.getDataInserimento());
        return insertPort.execute(spec, Offerta.class).getGeneratedId("GENERATED_KEYS");
    }

    @Override
    public Optional<Offerta> findById(Long id) {
        QuerySpecification spec = new QuerySpecification();
        spec.from("offerte")
                .where("id_offerta", "=", id);
        return queryPort.executeSingle(spec, Offerta.class);
    }

    @Override
    public List<Offerta> findByUtente(String matricola) {
        return findByUtente(matricola, LIMIT_DEFAULT);
    }

    @Override
    public List<Offerta> findByUtente(String matricola, int limit) {
        QuerySpecification spec = new QuerySpecification();
        spec.from("offerte")
                .where("matricola_offerente", "=", matricola)
                .limit(limit);
        return queryPort.execute(spec, Offerta.class);
    }

    @Override
    public List<Offerta> findAll() {
        QuerySpecification spec = new QuerySpecification();
        spec.from("offerte");
        return queryPort.execute(spec, Offerta.class);
    }

    @Override
    public void delete(Long id) {
        DeleteSpecification spec = new DeleteSpecification();
        spec.from("offerte")
                .where("id_offerta", "=", id);
        insertPort.delete(spec, Offerta.class);
    }

    @Override
    public void update(Offerta offerta) {
        UpdateSpecification spec = new UpdateSpecification();
        if(offerta.isEmpty()) return;
        spec.table("offerte");
        if(offerta.getDataDisponibilitaInizio() != null) spec.set("data_disponibilita_inizio", offerta.getDataDisponibilitaInizio());
        if(offerta.getDataDisponibilitaFine() != null) spec.set("data_disponibilita_fine", offerta.getDataDisponibilitaFine());
        if(offerta.getOreDisponibili() != -1) spec.set("ore_disponibili", offerta.getOreDisponibili());
        if(offerta.getStato() != null) spec.set("stato", offerta.getStato().name());
        if(offerta.getNote() != null) spec.set("note", offerta.getNote());
        spec.where("id_offerta", "=", offerta.getIdOfferta());
        insertPort.update(spec, Offerta.class);
    }
}
