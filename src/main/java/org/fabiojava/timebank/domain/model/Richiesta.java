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
    private int idAttivita;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int oreRichieste;
    private StatoRichiesta stato;
    private PrioritaRichiesta priorita;
    private String note;
    private LocalDateTime dataInserimento;

    enum StatoRichiesta {
        CANCELLATA,
        COMPLETATA,
        ASSEGNATA,
        APERTA,
    }

    enum PrioritaRichiesta {
        URGENTE,
        ALTA,
        NORMALE,
        BASSA,
    }
}
