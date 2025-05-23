package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.model.DoacaoMonetaria;

import java.util.List;

public interface IDoacaoMonetariaRepository {
    void save(DoacaoMonetaria doacaoMonetaria);

    void delete(int id);

    void update(DoacaoMonetaria doacaoMonetaria);

    DoacaoMonetaria findById(int id);

    DoacaoMonetaria findByApoiador(String apoiador);

    List<DoacaoMonetaria> findAll();
}
