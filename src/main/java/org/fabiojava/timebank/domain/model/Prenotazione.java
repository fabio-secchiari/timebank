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
public class Prenotazione {
    private Long idPrenotazione;
    private Long idOfferta;
    private Long idRichiesta;
    private LocalDateTime dataPrenotazione;
    private int oreConcordate;
    private StatoPrenotazione stato;
    private LocalDate dataEsecuzione;
    private Long idValutazioneRichiedente;
    private Long idValutazioneOfferente;
    private String noteFeedback;

    public enum StatoPrenotazione {
        CANCELLATO,
        COMPLETATO,
        IN_CORSO,
        PROGRAMMATO
    }
}
