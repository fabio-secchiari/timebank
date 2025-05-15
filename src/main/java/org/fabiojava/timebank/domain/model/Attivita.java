package org.fabiojava.timebank.domain.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Attivita {
    private Long idAttivita;
    private String nomeAttivita;
    private String descrizione;
    private int durataMinimaOre;
    private int durataMassimaOre;
    private String categoria;

    public enum StatoAttivita {
        DISPONIBILE,
        ASSEGNATA,
        COMPLETATA,
        CANCELLATA
    }
}