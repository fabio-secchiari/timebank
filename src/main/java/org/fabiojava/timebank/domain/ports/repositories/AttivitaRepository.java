package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Attivita;

import java.util.List;
import java.util.Optional;

public interface AttivitaRepository {
    void save(Attivita attivita);
    Optional<Attivita> findById(Long id);
    Optional<Attivita> findByNome(String nome);
    List<Attivita> findAll();
    void delete(Long id);
    void update(Attivita attivita);
}