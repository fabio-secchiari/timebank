package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Valutazione;

import java.util.List;
import java.util.Optional;

public interface ValutazioneRepository {
    void save(Valutazione valutazione);
    Optional<Valutazione> findById(Long id);
    List<Valutazione> findByUtente(String username);
    List<Valutazione> findAll();
    void delete(Long id);
    void update(Valutazione valutazione);
}
