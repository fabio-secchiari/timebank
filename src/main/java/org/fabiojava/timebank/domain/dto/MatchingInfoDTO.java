package org.fabiojava.timebank.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MatchingInfoDTO {
    private String matricolaRichiedente;
    private String matricolaOfferente;
    private Long idPrenotazione;
    private Long idOfferta;
    private Long idRichiesta;
}
