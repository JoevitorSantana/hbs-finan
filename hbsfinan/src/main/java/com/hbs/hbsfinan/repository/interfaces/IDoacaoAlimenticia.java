package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.model.DoacaoProduto;
import java.util.List;

public interface IDoacaoAlimenticia {

    // Salvar nova doação, incluindo seus itens
    void save(DoacaoProduto doacaoAlimenticia);

    // Buscar doação pelo ID, incluindo itens associados
    DoacaoProduto findById(int id);

    // Listar todas as doações com seus itens
    List<DoacaoProduto> findAll();

    // Deletar doação pelo ID
    void delete(int id);
}