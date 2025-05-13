package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.model.Prenotazione;
import org.fabiojava.timebank.domain.ports.repositories.PrenotazioneRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SqlServerPrenotazioneRepositoryImpl implements PrenotazioneRepository {

    @Override
    public Long save(Prenotazione prenotazione) {
        return null;
    }

    @Override
    public Prenotazione findById(Integer id) {
        return null;
    }

    @Override
    public List<Prenotazione> findByIdRichiesta(Integer id) {
        return List.of();
    }

    @Override
    public List<Prenotazione> findByIdOfferta(Integer id) {
        return List.of();
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
    public void delete(Integer id) {

    }

    @Override
    public void update(Prenotazione prenotazione) {

    }
}
