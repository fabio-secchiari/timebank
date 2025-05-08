package org.fabiojava.timebank.domain.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Richiesta {
    private Long idRichiesta;
    private String matricolaRichiedente;
    private Long idAttivita;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int oreRichieste;
    private StatoRichiesta stato;
    private PrioritaRichiesta priorita;
    private String note;
    private LocalDateTime dataInserimento;

    public enum StatoRichiesta {
        CANCELLATA,
        COMPLETATA,
        ASSEGNATA,
        APERTA,
    }

    public enum PrioritaRichiesta {
        URGENTE,
        ALTA,
        NORMALE,
        BASSA,
    }
}
