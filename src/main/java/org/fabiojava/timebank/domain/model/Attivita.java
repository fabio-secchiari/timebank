package org.fabiojava.timebank.domain.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Attivita {
    private Long id;
    private String nome;
    private String descrizione;
    private int durataMinOre;
    private int durataMaxOre;
    private String categoria;

    public enum StatoAttivita {
        DISPONIBILE,
        ASSEGNATA,
        COMPLETATA,
        CANCELLATA
    }
}