package org.fabiojava.timebank.gui.services;

import org.fabiojava.timebank.domain.model.Inserimento;
import org.fabiojava.timebank.domain.model.Utente;
import org.fabiojava.timebank.domain.ports.repositories.UtenteRepository;
import org.fabiojava.timebank.domain.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UtenteServiceImpl implements UtenteService {
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtenteServiceImpl(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registraUtente(String matricola, String username, String password,
                               String nome, String cognome, String email,
                               String indirizzo, String telefono) {
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
                Timestamp.valueOf(LocalDateTime.now()) ,
                0
        );
        utenteRepository.save(nuovoUtente);
    }


    public Optional<Utente> verificaCredenziali(String username, String password) {
        Optional<Utente> utenteOpt = utenteRepository.findByUsername(username);
        if (utenteOpt.isPresent()) {
            Utente utente = utenteOpt.get();
            if (passwordEncoder.matches(password, utente.getPassword())) {
                return utenteOpt;
            }
        }
        return Optional.empty();
    }

    public boolean esisteMatricola(String matricola) {
        return utenteRepository.findByMatricola(matricola).isPresent();
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

    @Override
    public Optional<Utente> findByIdRichiesta(Long id_richiesta) {
        return utenteRepository.findByIdInserimento(id_richiesta, Inserimento.TIPO_INSERIMENTO.RICHIESTA);
    }

    @Override
    public Optional<Utente> findByIdOfferta(Long id_offerta) {
        return utenteRepository.findByIdInserimento(id_offerta, Inserimento.TIPO_INSERIMENTO.OFFERTA);
    }
}