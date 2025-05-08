package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.model.Prenotazione;
import org.fabiojava.timebank.domain.ports.repositories.PrenotazioneRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SqlServerPrenotazioneRepositoryImpl implements PrenotazioneRepository {

    @Override
    public void save(Prenotazione prenotazione) {

    }

    @Override
    public Prenotazione findById(Long id) {
        return null;
    }

    @Override
    public List<Prenotazione> findByUtente(String username) {
        return null;
    }

    @Override
    public List<Prenotazione> findAll() {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Prenotazione prenotazione) {

    }
}
