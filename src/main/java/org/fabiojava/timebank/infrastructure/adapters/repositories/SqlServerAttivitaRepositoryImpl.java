package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.dto.CategoriaDTO;
import org.fabiojava.timebank.domain.model.Attivita;
import org.fabiojava.timebank.domain.ports.database.InsertPort;
import org.fabiojava.timebank.domain.ports.database.QueryPort;
import org.fabiojava.timebank.domain.ports.repositories.AttivitaRepository;
import org.fabiojava.timebank.infrastructure.adapters.database.data.InsertResult;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.DeleteSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.InsertSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.QuerySpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.UpdateSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SqlServerAttivitaRepositoryImpl implements AttivitaRepository {
    private final QueryPort queryPort;
    private final InsertPort insertPort;

    @Autowired
    public SqlServerAttivitaRepositoryImpl(QueryPort queryPort, InsertPort insertPort) {
        this.queryPort = queryPort;
        this.insertPort = insertPort;
    }

    @Override
    public Long save(Attivita attivita) {
        InsertSpecification spec = new InsertSpecification();
        spec.into("attivita");
        spec.value("nome_attivita", attivita.getNomeAttivita())
            .value("descrizione", attivita.getDescrizione())
            .value("categoria", attivita.getCategoria())
            .value("durata_minima_ore", attivita.getDurataMinimaOre())
            .value("durata_massima_ore", attivita.getDurataMassimaOre());
        InsertResult insertResult = insertPort.execute(spec, Attivita.class);
        return insertResult.getGeneratedId("id_attivita");
    }

    @Override
    public Optional<Attivita> findById(Long id) {
        QuerySpecification spec = new QuerySpecification();
        spec.from("attivita")
            .where("id_attivita", "=", id);
        return queryPort.executeSingle(spec, Attivita.class);
    }

    @Override
    public Optional<Attivita> findByNome(String nome) {
        QuerySpecification spec = new QuerySpecification();
        spec.from("attivita")
            .where("nome_attivita", "=", nome);
        return queryPort.executeSingle(spec, Attivita.class);
    }

    @Override
    public List<Attivita> findAll() {
        QuerySpecification spec = new QuerySpecification();
        spec.from("attivita");
        return queryPort.execute(spec, Attivita.class);
    }

    public List<CategoriaDTO> findAllCategorie(){
        QuerySpecification spec = new QuerySpecification();
        spec.addSelect("DISTINCT categoria")
            .from("attivita");
        return queryPort.execute(spec, CategoriaDTO.class);
    }

    @Override
    public void delete(Long id) {
        DeleteSpecification spec = new DeleteSpecification();
        spec.from("attivita")
            .where("id_attivita", "=", id);
        insertPort.delete(spec, Attivita.class);
    }

    @Override
    public void update(Attivita attivita) {
        UpdateSpecification spec = new UpdateSpecification();
        spec.set("nome_attivita", attivita.getNomeAttivita())
            .set("descrizione", attivita.getDescrizione())
            .set("durata_minima_ore", attivita.getDurataMinimaOre())
            .set("durata_massima_ore", attivita.getDurataMassimaOre())
            .where("id_attivita", "=", attivita.getIdAttivita());
        insertPort.update(spec, Attivita.class);
    }
}
