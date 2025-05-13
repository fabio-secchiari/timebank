package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Prenotazione;

import java.util.List;

public interface PrenotazioneRepository {
    Long save(Prenotazione prenotazione);
    Prenotazione findById(Integer id);
    List<Prenotazione> findByIdRichiesta(Integer id);
    List<Prenotazione> findByIdOfferta(Integer id);
    List<Prenotazione> findByUtente(String username);
    List<Prenotazione> findAll();
    void delete(Integer id);
    void update(Prenotazione prenotazione);
}
