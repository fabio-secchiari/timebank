package org.fabiojava.timebank.domain.services;

import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.gui.controllers.AllInsertionController;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InserimentiService {
    Page<RichiestaOffertaDTO> filtraRichiesteOfferteRecenti(AllInsertionController.RichiestaCriteria criteria, String matricolaUtente);
    List<RichiestaOffertaDTO> trovaRichiesteOfferteRecenti(String matricolaUtente);
    List<RichiestaOffertaDTO> trovaRichiesteOfferteRecenti(String matricolaUtente, int limit);
    Page<RichiestaOffertaDTO> filtraOwnRichieste(AllInsertionController.RichiestaCriteria criteria, String matricolaUtente);
    Page<RichiestaOffertaDTO> filtraOwnOfferte(AllInsertionController.RichiestaCriteria criteria, String matricolaUtente);
}
