package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.dto.DoacaoMonetariaCreateDTO;
import com.hbs.hbsfinan.model.DoacaoMonetaria;

import java.util.List;

public interface IDoacaoMonetariaRepository {
    void save(DoacaoMonetariaCreateDTO doacaoMonetaria);

    boolean delete(int id);

    void update(DoacaoMonetaria doacaoMonetaria);

    DoacaoMonetaria findById(int id);

    DoacaoMonetaria findByApoiador(String apoiador);

    List<DoacaoMonetaria> findAll();

    public List<DoacaoMonetaria> findByCaixa(int id);
}
