package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.model.Inserimento;
import org.fabiojava.timebank.domain.ports.database.InsertPort;
import org.fabiojava.timebank.domain.ports.database.QueryPort;
import org.fabiojava.timebank.domain.ports.repositories.UtenteRepository;
import org.fabiojava.timebank.domain.model.Utente;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.DeleteSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.InsertSpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.QuerySpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.UpdateSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SqlServerUtenteRepositoryImpl implements UtenteRepository {
    private final QueryPort queryPort;
    private final InsertPort insertPort;

    @Autowired
    public SqlServerUtenteRepositoryImpl(QueryPort queryPort, InsertPort insertPort) {
        this.queryPort = queryPort;
        this.insertPort = insertPort;
    }

    @Override
    public void save(Utente utente) {
        InsertSpecification spec = new InsertSpecification();
        spec.into("utenti")
                .value("matricola", utente.getMatricola())
                .value("nome", utente.getNome())
                .value("email", utente.getEmail())
                .value("username", utente.getUsername())
                .value("password", utente.getPassword())
                .value("cognome", utente.getCognome())
                .value("indirizzo", utente.getIndirizzo())
                .value("telefono", utente.getTelefono())
                .value("data_registrazione", utente.getDataRegistrazione())
                .value("ore_totali", utente.getOreTotali());
        insertPort.execute(spec, Utente.class);
    }

    @Override
    public Optional<Utente> findByMatricola(String matricola) {
        QuerySpecification spec = new QuerySpecification();
        spec.from("utenti")
                .where("matricola", "=", matricola);
        return queryPort.executeSingle(spec, Utente.class);
    }

    @Override
    public Optional<Utente> findByUsername(String username) {
        QuerySpecification spec = new QuerySpecification();
        spec.from("utenti")
                .where("username", "=", username);
        return queryPort.executeSingle(spec, Utente.class);
    }

    @Override
    public Optional<Utente> findByIdInserimento(Long id_inserimento, Inserimento.TIPO_INSERIMENTO tipoInserimento){
        QuerySpecification spec = new QuerySpecification();
        spec.addSelect("u.*");
        if(tipoInserimento == Inserimento.TIPO_INSERIMENTO.RICHIESTA)
            spec.from("richieste r")
                    .join("utenti u", "u.matricola = r.matricola_richiedente", QuerySpecification.JoinClause.JoinType.INNER)
                .where("id_richiesta", "=", id_inserimento);
        else
            spec.from("offerte o")
                    .join("utenti u", "u.matricola = o.matricola_offerente", QuerySpecification.JoinClause.JoinType.INNER)
                .where("id_offerta", "=", id_inserimento);
        return queryPort.executeSingle(spec, Utente.class);
    }

    @Override
    public List<Utente> findAll() {
        QuerySpecification spec = new QuerySpecification();
        spec.from("utenti");
        return queryPort.execute(spec, Utente.class);
    }

    @Override
    public void update(Utente utente) {
        UpdateSpecification spec = new UpdateSpecification();
        spec.table("utenti")
            .set("nome", utente.getNome())
                .set("email", utente.getEmail())
                .set("cognome", utente.getCognome())
                .where("matricola", "=", utente.getMatricola());
    }

    @Override
    public void delete(String matricola) {
        DeleteSpecification spec = new DeleteSpecification();
        spec.from("utenti")
                .where("matricola", "=", matricola);
        insertPort.delete(spec, Utente.class);
    }
}