package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.model.DoacaoMaterial;
import java.util.List;

public interface IDoacaoMaterial {
    void save(DoacaoMaterial doacaoMaterial);

    DoacaoMaterial findById(int id);

    List<DoacaoMaterial> findAll();

    void delete(int id);
}
