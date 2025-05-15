package org.fabiojava.timebank.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fabiojava.timebank.domain.model.Prenotazione;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrenotazioneDTO {
    private Long idPrenotazione;
    private Long idOfferta;
    private Long idRichiesta;
    private Timestamp dataMatching;
    private Integer oreConcordate;
    private Prenotazione.StatoPrenotazione stato;
    private Date dataEsecuzione;
    private String noteFeedback;
    private String matricola;
    private String email;
}
