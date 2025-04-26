package org.fabiojava.timebank.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Attivita {
    private Long id;
    private String titolo;
    private String descrizione;
    private int orePreviste;
    private StatoAttivita stato;
    private String matricolaCreatore;
    private LocalDateTime dataCreazione;
    private String categoria;

    public enum StatoAttivita {
        DISPONIBILE,
        ASSEGNATA,
        COMPLETATA,
        CANCELLATA
    }
}