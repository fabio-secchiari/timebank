package org.fabiojava.timebank.domain.model;

import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Richiesta extends Inserimento{
    private Integer idRichiesta;
    private Date dataRichiestaInizio;
    private Date dataRichiestaFine;
    private String matricolaRichiedente;
    private int oreRichieste;
    private StatoRichiesta stato;
    private PrioritaRichiesta priorita;

    public Richiesta(Integer id, String matricolaRichiedente, Integer idAttivita, Date dataInizio, Date dataFine, int oreRichieste, StatoRichiesta stato, String note, Timestamp dataInserimento, PrioritaRichiesta priorita) {
        super(idAttivita, note, dataInserimento);
        this.matricolaRichiedente = matricolaRichiedente;
        this.oreRichieste = oreRichieste;
        this.stato = stato;
        this.priorita = priorita;
        this.dataRichiestaInizio = dataInizio;
        this.dataRichiestaFine = dataFine;
        this.idRichiesta = id;
    }

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
