package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Prenotazione;

import java.util.List;

public interface PrenotazioneRepository {
    void save(Prenotazione prenotazione);
    Prenotazione findById(Long id);
    List<Prenotazione> findByUtente(String username);
    List<Prenotazione> findAll();
    void delete(Long id);
    void update(Prenotazione prenotazione);
}
