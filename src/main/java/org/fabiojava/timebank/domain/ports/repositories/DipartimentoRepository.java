package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Dipartimento;

public interface DipartimentoRepository {
    void save(Dipartimento nome);
    Dipartimento findById(Long id);
    Dipartimento findAll();
    void delete(Long id);
    void update(Dipartimento dipartimento);
}
