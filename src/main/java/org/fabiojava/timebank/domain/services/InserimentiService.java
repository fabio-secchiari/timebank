package org.fabiojava.timebank.domain.services;

import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.gui.controllers.AllInsertionController;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InserimentiService {
    public Page<RichiestaOffertaDTO> cercaRichieste(AllInsertionController.RichiestaCriteria criteria, String matricolaUtente);
    public List<RichiestaOffertaDTO> trovaRichiesteOfferteRecenti(String matricolaUtente);
}
