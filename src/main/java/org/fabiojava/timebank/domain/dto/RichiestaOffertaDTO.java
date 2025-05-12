package org.fabiojava.timebank.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RichiestaOffertaDTO {
    private String tipoInserimento;
    private Integer id;
    private LocalDateTime dataInserimento;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private String matricolaUtente;
    private String stato;
    private Integer oreDisponibili;
    private String note;
    private Integer idAttivita;
    private String nomeAttivita;
    private String categoria;
}