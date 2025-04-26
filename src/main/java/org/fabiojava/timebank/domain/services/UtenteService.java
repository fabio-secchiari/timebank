package org.fabiojava.timebank.domain.services;

import org.fabiojava.timebank.domain.model.Utente;
import org.fabiojava.timebank.domain.ports.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UtenteService {
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void registraUtente(String matricola, String username, String password,
                               String nome, String cognome, String email,
                               String indirizzo, String codDipartimento, String telefono) {
        String encodedPassword = passwordEncoder.encode(password);

        Utente nuovoUtente = new Utente(
                matricola,
                username,
                encodedPassword,  // Password hashata
                nome,
                cognome,
                email,
                indirizzo,
                telefono,
                LocalDateTime.now(),
                0
        );
        utenteRepository.save(nuovoUtente);
    }


    public boolean verificaCredenziali(String username, String password) {
        Optional<Utente> utente = utenteRepository.findByUsername(username);
        return utente.map(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElse(false);
    }

    public boolean esisteMatricola(String matricola) {
        return utenteRepository.findByMatricola(matricola).isPresent();
    }

    public Optional<Utente> trovaUtentePerMatricola(String matricola) {
        return utenteRepository.findByMatricola(matricola);
    }

    public void aggiungiOreUtente(String matricola, int ore) {
        Optional<Utente> utenteOpt = utenteRepository.findByMatricola(matricola);
        if (utenteOpt.isPresent()) {
            Utente utente = utenteOpt.get();
            utente.aggiungiOre(ore);
            utenteRepository.update(utente);
        }
    }

    public void sottraiOreUtente(String matricola, int ore) {
        Optional<Utente> utenteOpt = utenteRepository.findByMatricola(matricola);
        if (utenteOpt.isPresent()) {
            Utente utente = utenteOpt.get();
            utente.sottraiOre(ore);
            utenteRepository.update(utente);
        }
    }

    public void aggiornaUtente(Utente utente) {
        if (esisteMatricola(utente.getMatricola())) {
            utenteRepository.update(utente);
        }
    }

    public void eliminaUtente(String matricola) {
        if (esisteMatricola(matricola)) {
            utenteRepository.delete(matricola);
        }
    }
}