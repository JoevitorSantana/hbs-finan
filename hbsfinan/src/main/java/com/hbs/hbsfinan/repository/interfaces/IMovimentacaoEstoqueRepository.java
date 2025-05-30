package com.hbs.hbsfinan.repository.interfaces; // Seguindo sua estrutura

import com.hbs.hbsfinan.model.MovimentacaoEstoque;
import java.util.List;

public interface IMovimentacaoEstoqueRepository {
    void save(MovimentacaoEstoque movimentacao);
    void update(MovimentacaoEstoque movimentacao);
    boolean delete(long id);

    MovimentacaoEstoque findById(long id);
    List<MovimentacaoEstoque> findAll();
}

