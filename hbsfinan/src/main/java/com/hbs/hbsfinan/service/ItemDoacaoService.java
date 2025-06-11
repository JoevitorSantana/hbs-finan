package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.model.ItemDoacao;
import com.hbs.hbsfinan.repository.interfaces.IItemDoacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemDoacaoService {

    private final IItemDoacao itemDoacaoRepository;

    @Autowired
    public ItemDoacaoService(IItemDoacao itemDoacaoRepository) {
        this.itemDoacaoRepository = itemDoacaoRepository;
    }

    // Para salvar, o método do repositório é void, então aqui só chama e retorna void
    public void salvar(ItemDoacao itemDoacao) {
        itemDoacaoRepository.save(itemDoacao);
    }

    // Buscar itens por id da doação
    public List<ItemDoacao> listarPorDoacaoProdutoId(int doacaoProdutoId) {
        return itemDoacaoRepository.findByDoacaoProdutoId(doacaoProdutoId);
    }

    // Deletar itens pela doacaoProdutoId
    public void deletarPorDoacaoProdutoId(int doacaoProdutoId) {
        itemDoacaoRepository.deleteByDoacaoProdutoId(doacaoProdutoId);
    }
}
