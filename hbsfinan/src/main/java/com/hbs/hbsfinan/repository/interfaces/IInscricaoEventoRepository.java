package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.model.InscricaoEvento;
import java.util.List;

public interface IInscricaoEventoRepository {
    void save(InscricaoEvento inscricaoEvento);
    void update(InscricaoEvento inscricaoEvento);
    boolean delete(Long id);
    InscricaoEvento findById(Long id);
    List<InscricaoEvento> findAll();
}
