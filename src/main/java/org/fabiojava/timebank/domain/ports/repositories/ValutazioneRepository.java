package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Valutazione;

import java.util.List;

public interface ValutazioneRepository {
    void save(Valutazione valutazione);
    Valutazione findById(Long id);
    List<Valutazione> findAll();
    void delete(Long id);
    void update(Valutazione valutazione);
}
