package org.fabiojava.timebank.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fabiojava.timebank.domain.model.Inserimento;
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

    public RichiestaOffertaDTO(Richiesta richiesta){
        this.tipoInserimento = Inserimento.TIPO_INSERIMENTO.RICHIESTA.name();
        this.id = richiesta.getIdRichiesta();
        this.dataInserimento = richiesta.getDataInserimento();
        this.dataInizio = richiesta.getDataRichiestaInizio();
        this.dataFine = richiesta.getDataRichiestaFine();
        this.matricolaUtente = richiesta.getMatricolaRichiedente();
        this.stato = richiesta.getStato().name();
        this.oreDisponibili = richiesta.getOreRichieste();
        this.note = richiesta.getNote();
        this.idAttivita = richiesta.getIdAttivita();
        this.nomeAttivita = null;
        this.categoria = null;
    }

    public RichiestaOffertaDTO(Offerta offerta){
        this.tipoInserimento = Inserimento.TIPO_INSERIMENTO.OFFERTA.name();
        this.id = offerta.getIdOfferta();
        this.dataInserimento = offerta.getDataInserimento();
        this.dataInizio = offerta.getDataDisponibilitaInizio();
        this.dataFine = offerta.getDataDisponibilitaFine();
        this.matricolaUtente = offerta.getMatricolaOfferente();
        this.stato = offerta.getStato().name();
        this.oreDisponibili = offerta.getOreDisponibili();
        this.note = offerta.getNote();
        this.idAttivita = offerta.getIdAttivita();
        this.nomeAttivita = null;
        this.categoria = null;
    }
}