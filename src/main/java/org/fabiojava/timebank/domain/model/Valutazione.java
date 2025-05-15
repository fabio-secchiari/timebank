package org.fabiojava.timebank.domain.model;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Valutazione {
    private Long idValutazione;
    private int punteggio;  // da 1 a 5 stelle
    private String commento;
    private Timestamp dataValutazione;
    private TipoValutatore tipoValutatore;
    private Integer idPrenotazione;

    public enum TipoValutatore {
        RICHIEDENTE,
        OFFERENTE
    }
}