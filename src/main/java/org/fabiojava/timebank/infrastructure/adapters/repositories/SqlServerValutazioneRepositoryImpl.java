package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.model.Valutazione;
import org.fabiojava.timebank.domain.ports.database.DatabaseConnection;
import org.fabiojava.timebank.domain.ports.repositories.ValutazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SqlServerValutazioneRepositoryImpl implements ValutazioneRepository {
    private final DatabaseConnection databaseConnection;

    @Autowired
    public SqlServerValutazioneRepositoryImpl(DatabaseConnection dbConnection) {
        this.databaseConnection = dbConnection;
    }

    @Override
    public void save(Valutazione valutazione) {
        
    }

    @Override
    public Valutazione findById(Long id) {
        return null;
    }

    @Override
    public List<Valutazione> findAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Valutazione valutazione) {

    }
}
