package org.fabiojava.timebank.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fabiojava.timebank.domain.model.Offerta;
import org.fabiojava.timebank.domain.model.Richiesta;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class RichiestaOffertaDTO {
    private String tipoInserimento;
    private Long id;
    private Timestamp dataInserimento;
    private Date dataInizio;
    private Date dataFine;
    private String matricolaUtente;
    private String stato;
    private Integer oreDisponibili;
    private String note;
    private Long idAttivita;
    private String nomeAttivita;
    private String categoria;

    public Richiesta toRichiesta(){
        return new Richiesta(id, matricolaUtente, idAttivita, dataInizio, dataFine, oreDisponibili,
                Richiesta.StatoRichiesta.valueOf(stato), note, dataInserimento, Richiesta.PrioritaRichiesta.NORMALE);
    }

    public Offerta toOfferta(){
        return new Offerta(id, matricolaUtente, idAttivita, dataInizio, dataFine, oreDisponibili,
                Offerta.StatoOfferta.valueOf(stato), note, dataInserimento);
    }
}