package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.model.ItemDoacaoMaterial;
import java.util.List;

public interface IItemDoacaoMaterial {
    void save(ItemDoacaoMaterial itemDoacaoMaterial);

    List<ItemDoacaoMaterial> findByDoacaoProdutoId(long doacaoProdutoId);

    void deleteByDoacaoProdutoId(long doacaoProdutoId);
}
