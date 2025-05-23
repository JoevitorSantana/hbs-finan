package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.dto.ApoiadorDTO;
import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.DoacaoAlimenticia;





import java.util.List;

public interface IDoacaoAlimenticia {
    void save(DoacaoAlimenticia doacaoAlimenticia);
    DoacaoAlimenticia findById(int id);
    List<DoacaoAlimenticia> findAll();
    void delete(int id);
    void update(DoacaoAlimenticia doacaoAlimenticia);
}
