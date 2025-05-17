package org.fabiojava.timebank.domain.services;

import org.fabiojava.timebank.domain.dto.MatchingInfoDTO;
import org.fabiojava.timebank.domain.dto.PrenotazioneDTO;
import org.fabiojava.timebank.domain.model.Inserimento;
import org.fabiojava.timebank.domain.model.Offerta;
import org.fabiojava.timebank.domain.model.Richiesta;
import org.fabiojava.timebank.gui.controllers.AllInsertionController;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PrenotazioniService {
    Page<PrenotazioneDTO> filterByInserimento(Long id, AllInsertionController.RichiestaCriteria criteria,
                                              Inserimento.TIPO_INSERIMENTO tipoInserimento);
    Optional<MatchingInfoDTO> getMatchingInfoById(Long id_prenotazione);
    void aggiornaPrenotazione(PrenotazioneDTO prenotazioneDTO);
    Optional<Richiesta> getMatchingRichiestaById(Long id);
    Optional<Offerta> getMatchingOffertaById(Long id);
    List<PrenotazioneDTO> getToDoList(String matricolaUtente);
}
