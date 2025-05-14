package org.fabiojava.timebank.domain.services;

import org.fabiojava.timebank.domain.dto.PrenotazioneDTO;
import org.fabiojava.timebank.domain.model.Inserimento;
import org.fabiojava.timebank.gui.controllers.AllInsertionController;
import org.springframework.data.domain.Page;

public interface PrenotazioniService {
    Page<PrenotazioneDTO> filterByInserimento(Integer id, AllInsertionController.RichiestaCriteria criteria,
                                              Inserimento.TIPO_INSERIMENTO tipoInserimento);
    void aggiornaPrenotazione(PrenotazioneDTO prenotazioneDTO);
}
