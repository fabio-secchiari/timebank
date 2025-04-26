package org.fabiojava.timebank.domain.model;

import lombok.*;
import org.fabiojava.timebank.domain.ports.repositories.UtenteRepository;
import org.fabiojava.timebank.infrastructure.adapters.repositories.SqlServerUtenteRepositoryImpl;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Utente {
    private String matricola;
    private String username;
    private String password;
    private String nome;
    private String cognome;
    private String email;
    private String indirizzo;
    private String telefono;
    private LocalDateTime dataRegistrazione;
    private int oreTotali;

    public Utente(String matricola, String username, String password, String nome, String cognome, String email, String indirizzo, String telefono, LocalDateTime dataRegistrazione, int oreTotali){
        this.matricola = matricola;
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.dataRegistrazione = dataRegistrazione;
        this.oreTotali = oreTotali;
    }

    public void aggiungiOre(int ore) {
        this.oreTotali += ore;
    }

    public void sottraiOre(int ore) {
        this.oreTotali -= ore;
    }
}