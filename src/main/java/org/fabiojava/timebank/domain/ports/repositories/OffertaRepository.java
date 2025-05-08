package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Offerta;

import java.util.List;
import java.util.Optional;

public interface OffertaRepository {
    void save(Offerta offerta);
    Optional<Offerta> findById(Long id);
    List<Offerta> findByUtente(String username);
    List<Offerta> findAll();
    void delete(Long id);
    void update(Offerta offerta);
}
