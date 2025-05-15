package org.fabiojava.timebank.gui.services;

import org.fabiojava.timebank.domain.dto.CountDTO;
import org.fabiojava.timebank.domain.dto.PrenotazioneDTO;
import org.fabiojava.timebank.domain.model.Inserimento;
import org.fabiojava.timebank.domain.model.Offerta;
import org.fabiojava.timebank.domain.model.Prenotazione;
import org.fabiojava.timebank.domain.model.Richiesta;
import org.fabiojava.timebank.domain.ports.database.InsertPort;
import org.fabiojava.timebank.domain.ports.database.QueryPort;
import org.fabiojava.timebank.domain.ports.repositories.OffertaRepository;
import org.fabiojava.timebank.domain.ports.repositories.PrenotazioneRepository;
import org.fabiojava.timebank.domain.ports.repositories.RichiestaRepository;
import org.fabiojava.timebank.domain.services.PrenotazioniService;
import org.fabiojava.timebank.gui.controllers.AllInsertionController;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.QuerySpecification;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.UpdateSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrenotazioniServiceImpl implements PrenotazioniService {
    private final QueryPort queryPort;
    private final InsertPort insertPort;
    private final RichiestaRepository richiestaRepository;
    private final OffertaRepository offertaRepository;
    private final PrenotazioneRepository prenotazioniRepository;

    @Autowired
    public PrenotazioniServiceImpl(QueryPort queryPort, InsertPort insertPort, RichiestaRepository richiestaRepository, OffertaRepository offertaRepository, PrenotazioneRepository prenotazioniRepository) {
        this.queryPort = queryPort;
        this.insertPort = insertPort;
        this.richiestaRepository = richiestaRepository;
        this.offertaRepository = offertaRepository;
        this.prenotazioniRepository = prenotazioniRepository;
    }

    @Override
    public Page<PrenotazioneDTO> filterByInserimento(Long id, AllInsertionController.RichiestaCriteria criteria,
                                                     Inserimento.TIPO_INSERIMENTO tipoInserimento) {
        QuerySpecification baseSpec = new QuerySpecification();
        baseSpec.from("prenotazioni p");
        if(tipoInserimento == Inserimento.TIPO_INSERIMENTO.RICHIESTA){
            baseSpec.join("richieste r", "r.id_richiesta = p.id_richiesta", QuerySpecification.JoinClause.JoinType.INNER)
                    .join("utenti u", "u.matricola = r.matricola_richiedente", QuerySpecification.JoinClause.JoinType.INNER)
                    .where("r.id_richiesta", "=", id);
        }else{
            baseSpec.join("offerte o", "o.id_offerta = p.id_offerta", QuerySpecification.JoinClause.JoinType.INNER)
                    .join("utenti u", "u.matricola = o.matricola_offerente", QuerySpecification.JoinClause.JoinType.INNER)
                    .where("o.id_offerta", "=", id);
        }
        baseSpec.where("p.stato", "=", Prenotazione.StatoPrenotazione.IN_CORSO.name());

        QuerySpecification countSpec = new QuerySpecification(baseSpec).addSelect("COUNT(*) as total");
        CountDTO totalElements = queryPort.executeSingle(countSpec, CountDTO.class)
                .orElse(new CountDTO(0));

        baseSpec.addSelect("p.*")
                .addSelect("u.matricola")
                .addSelect("u.email");
        baseSpec.orderBy("data_matching", false)
                .offset(criteria.getPagina() * criteria.getDimensionePagina())
                .limit(criteria.getDimensionePagina());

        List<PrenotazioneDTO> content = queryPort.execute(baseSpec, PrenotazioneDTO.class);
        return new PageImpl<>(
                content,
                PageRequest.of(criteria.getPagina(), criteria.getDimensionePagina()),
                totalElements.getTotal()
        );
    }

    @Override
    public void aggiornaPrenotazione(PrenotazioneDTO prenotazioneDTO) {
        UpdateSpecification spec = new UpdateSpecification();
        spec.set("stato", prenotazioneDTO.getStato().name())
                .table("prenotazioni")
            .where("id_prenotazione", "=", prenotazioneDTO.getIdPrenotazione());
        insertPort.update(spec, Prenotazione.class);
        Richiesta richiesta = new Richiesta();
        richiesta.setIdRichiesta(prenotazioneDTO.getIdRichiesta());
        Offerta offerta = new Offerta();
        offerta.setIdOfferta(prenotazioneDTO.getIdOfferta());
        if(prenotazioneDTO.getStato() == Prenotazione.StatoPrenotazione.PROGRAMMATO){
            offerta.setStato(Offerta.StatoOfferta.PRENOTATA);
            richiesta.setStato(Richiesta.StatoRichiesta.ASSEGNATA);
            richiestaRepository.update(richiesta);
            offertaRepository.update(offerta);
        } else if(prenotazioneDTO.getStato() == Prenotazione.StatoPrenotazione.CANCELLATO){
            prenotazioniRepository.delete(prenotazioneDTO.getIdPrenotazione());
            // TODO SEGNALA ALL'UTENTE CHE E' STATA RIFIUTATA
        }
    }
}
