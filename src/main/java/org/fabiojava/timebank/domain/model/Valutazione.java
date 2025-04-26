package org.fabiojava.timebank.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Valutazione {
    private Long id;
    private int punteggio;  // da 1 a 5 stelle
    private String commento;
    private LocalDateTime dataValutazione;
    private TipoValutatore tipoValutatore;
    private Long idPrenotazione;

    public enum TipoValutatore {
        RICHIEDENTE,
        OFFERENTE
    }
}