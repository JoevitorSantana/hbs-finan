package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.model.ItemDoacaoMaterial;
import com.hbs.hbsfinan.repository.interfaces.IItemDoacaoMaterial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemDoacaoMaterialService {

    private final IItemDoacaoMaterial itemDoacaoMaterialRepository;

    @Autowired
    public ItemDoacaoMaterialService(IItemDoacaoMaterial itemDoacaoMaterialRepository) {
        this.itemDoacaoMaterialRepository = itemDoacaoMaterialRepository;
    }

    public void salvar(ItemDoacaoMaterial itemDoacaoMaterial) {
        itemDoacaoMaterialRepository.save(itemDoacaoMaterial);
    }

    public List<ItemDoacaoMaterial> listarPorDoacaoProdutoId(long doacaoProdutoId) {
        return itemDoacaoMaterialRepository.findByDoacaoProdutoId(doacaoProdutoId);
    }

    public void deletarPorDoacaoProdutoId(long doacaoProdutoId) {
        itemDoacaoMaterialRepository.deleteByDoacaoProdutoId(doacaoProdutoId);
    }
}
