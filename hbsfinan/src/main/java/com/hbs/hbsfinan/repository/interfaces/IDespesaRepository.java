package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.dto.DespesaCreateDTO;
import com.hbs.hbsfinan.model.Despesa;

import java.util.List;

public interface IDespesaRepository {

    void save(DespesaCreateDTO despesa);
    Despesa findById(int id);
    List<Despesa> findAll();
    void delete(int id);
    void update(Despesa despesa);

}
