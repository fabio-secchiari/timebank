package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Prenotazione;

public interface PrenotazioneRepository {
    void save(Prenotazione prenotazione);
    Prenotazione findById(Long id);
    Prenotazione findAll();
    void delete(Long id);
    void update(Prenotazione prenotazione);
}
