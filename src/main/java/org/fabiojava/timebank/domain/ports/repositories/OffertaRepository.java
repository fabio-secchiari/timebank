package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Offerta;

import java.util.List;
import java.util.Optional;

public interface OffertaRepository {
    Long save(Offerta offerta);
    Optional<Offerta> findById(Integer id);
    List<Offerta> findByUtente(String username);
    List<Offerta> findAll();
    void delete(Integer id);
    void update(Offerta offerta);
}
