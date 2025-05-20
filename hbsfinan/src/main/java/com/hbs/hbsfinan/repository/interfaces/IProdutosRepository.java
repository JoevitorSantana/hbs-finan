package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.model.Produtos;

import java.util.List;

public interface IProdutosRepository {
    void save(Produtos produtos);

    Produtos findById(int id);

    List<Produtos> findAll();

    void delete(int id);

    void update(Produtos produtos);
}
