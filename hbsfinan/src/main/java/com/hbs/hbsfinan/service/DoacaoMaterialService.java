package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.model.DoacaoMaterial;
import com.hbs.hbsfinan.model.ItemDoacaoMaterial;
import com.hbs.hbsfinan.repository.interfaces.IDoacaoMaterial;
import com.hbs.hbsfinan.repository.interfaces.IItemDoacaoMaterial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoacaoMaterialService {

    private final IDoacaoMaterial doacaoMaterialRepository;
    private final IItemDoacaoMaterial itemDoacaoMaterialRepository;

    @Autowired
    public DoacaoMaterialService(IDoacaoMaterial doacaoMaterialRepository,
                                 IItemDoacaoMaterial itemDoacaoMaterialRepository) {
        this.doacaoMaterialRepository = doacaoMaterialRepository;
        this.itemDoacaoMaterialRepository = itemDoacaoMaterialRepository;
    }

    public List<DoacaoMaterial> listarTodos() {
        return doacaoMaterialRepository.findAll();
    }

    public void salvar(DoacaoMaterial doacaoMaterial) {
        // Salva a doação principal (com ID gerado)
        doacaoMaterialRepository.save(doacaoMaterial);

        // Agora salva os itens da doação, associando a doação
        if (doacaoMaterial.getItens() != null) {
            for (ItemDoacaoMaterial item : doacaoMaterial.getItens()) {
                item.setDoacaoMaterial(doacaoMaterial);
                itemDoacaoMaterialRepository.save(item);
            }
        }
    }

    public void deletar(int id) {
        doacaoMaterialRepository.delete(id);
    }

    public DoacaoMaterial buscarPorId(int id) {
        return doacaoMaterialRepository.findById(id);
    }
}
