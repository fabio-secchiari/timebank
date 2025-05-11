package org.fabiojava.timebank.domain.model;

import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Prenotazione {
    private Integer idPrenotazione;
    private Integer idOfferta;
    private Integer idRichiesta;
    private Timestamp dataMatching;
    private Integer oreConcordate;
    private StatoPrenotazione stato;
    private Date dataEsecuzione;
    private String noteFeedback;

    public enum StatoPrenotazione {
        CANCELLATO,
        COMPLETATO,
        IN_CORSO,
        PROGRAMMATO
    }
}
