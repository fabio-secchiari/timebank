package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Utente;

import java.util.Optional;

public interface UtenteRepository {
    void save(Utente utente);
    Optional<Utente> findByMatricola(String matricola);
    Optional<Utente> findByUsername(String username);
    Optional<Utente> findAll();
    void update(Utente utente);
    void delete(String matricola);
}
