package org.fabiojava.timebank.domain.model;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Inserimento {
    private Long idAttivita;
    private String note;
    private Timestamp dataInserimento;

    public enum TIPO_INSERIMENTO {
        OFFERTA,
        RICHIESTA
    }
}
