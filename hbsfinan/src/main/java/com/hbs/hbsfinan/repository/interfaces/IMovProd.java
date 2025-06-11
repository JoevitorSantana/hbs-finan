package com.hbs.hbsfinan.repository.interfaces; // Seguindo sua estrutura


import com.hbs.hbsfinan.model.MovimentacaoProd;

import java.util.List;

public interface IMovProd {
    void save(MovimentacaoProd movimentacao);
    void update(MovimentacaoProd movimentacao);
    boolean delete(long id);

    MovimentacaoProd findById(long id);
    List<MovimentacaoProd> findAll();
}

