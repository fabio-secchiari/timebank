package org.fabiojava.timebank.domain.services;

import org.fabiojava.timebank.domain.model.Utente;

import java.util.Optional;

public interface UtenteService {
    void registraUtente(String matricola, String username, String password,
                        String nome, String cognome, String email,
                        String indirizzo, String telefono);
    Optional<Utente> verificaCredenziali(String username, String password);
    boolean esisteMatricola(String matricola);
    void aggiungiOreUtente(String matricola, int ore);
    void sottraiOreUtente(String matricola, int ore);
    void aggiornaUtente(Utente utente);
    void eliminaUtente(String matricola);
}
