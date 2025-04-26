package org.fabiojava.timebank.domain.model;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    private String codice;
    private String nome;
    private String descrizione;
}