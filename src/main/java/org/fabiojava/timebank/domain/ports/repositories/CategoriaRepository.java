package org.fabiojava.timebank.domain.ports.repositories;

import org.fabiojava.timebank.domain.model.Categoria;

import java.util.List;

public interface CategoriaRepository{
    void save(Categoria categoria);
    Categoria findById(Integer id);
    List<Categoria> findAll();
    void delete(Integer id);
    void update(Categoria categoria);
}
