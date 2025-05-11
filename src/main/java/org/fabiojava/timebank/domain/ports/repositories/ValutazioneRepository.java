package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Valutazione;

import java.util.List;
import java.util.Optional;

public interface ValutazioneRepository {
    Long save(Valutazione valutazione);
    Optional<Valutazione> findById(Integer id);
    List<Valutazione> findByUtente(String username);
    List<Valutazione> findAll();
    void delete(Integer id);
    void update(Valutazione valutazione);
}
