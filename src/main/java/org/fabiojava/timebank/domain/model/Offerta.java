package org.fabiojava.timebank.domain.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Offerta {
    private Long id;
    private String matricolaOfferente;
    private Long idAttivita;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int oreDisponibili;
    private StatoOfferta stato;
    private String note;
    private LocalDateTime dataInserimento;

    public enum StatoOfferta {
        CANCELLATA,
        COMPLETATA,
        PRENOTATA,
        DISPONIBILE,
    }
}