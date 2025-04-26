package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Attivita;

import java.util.List;

public interface AttivitaRepository {
    void save(Attivita attivita);
    Attivita findById(Long id);
    List<Attivita> findAll();
    void delete(Long id);
    void update(Attivita attivita);
}