package org.fabiojava.timebank.domain.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Dipartimento {
    private String codice;
    private String nome;
    private String descrizione;
    private String sede;
    private String telefono;
    private String email;
    private String direttore;
    private String web;
}
