package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Richiesta;

import java.util.List;
import java.util.Optional;

public interface RichiestaRepository {
    Long save(Richiesta richiesta);
    Optional<Richiesta> findById(Integer id);
    List<Richiesta> findByUtente(String username);
    List<Richiesta> findByUtente(String username, int limit);
    List<Richiesta> findAll();
    void delete(Integer id);
    void update(Richiesta richiesta);
}
