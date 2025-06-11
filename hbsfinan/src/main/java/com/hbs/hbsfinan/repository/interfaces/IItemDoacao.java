package com.hbs.hbsfinan.repository.interfaces;


import com.hbs.hbsfinan.model.ItemDoacao;

import java.util.List;

public interface IItemDoacao {

    void save(ItemDoacao itemDoacao);

    List<ItemDoacao> findByDoacaoProdutoId(int doacaoProdutoId);

    void deleteByDoacaoProdutoId(int doacaoProdutoId);

}
