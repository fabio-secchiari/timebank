package org.fabiojava.timebank.domain.model;

import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Offerta extends Inserimento{
    private Long idOfferta;
    private Date dataDisponibilitaInizio;
    private Date dataDisponibilitaFine;
    private String matricolaOfferente;
    private int oreDisponibili;
    private StatoOfferta stato;

    public Offerta(Long id, String matricolaOfferente, Long idAttivita, Date dataInizio, Date dataFine, int oreDisponibili, StatoOfferta stato, String note, Timestamp dataInserimento) {
        super(idAttivita, note, dataInserimento);
        this.matricolaOfferente = matricolaOfferente;
        this.oreDisponibili = oreDisponibili;
        this.stato = stato;
        this.dataDisponibilitaInizio = dataInizio;
        this.dataDisponibilitaFine = dataFine;
        this.idOfferta = id;
    }

    public enum StatoOfferta {
        CANCELLATA,
        COMPLETATA,
        PRENOTATA,
        DISPONIBILE,
    }
}